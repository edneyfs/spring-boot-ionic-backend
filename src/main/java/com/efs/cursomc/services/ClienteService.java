package com.efs.cursomc.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.efs.cursomc.domain.Cidade;
import com.efs.cursomc.domain.Cliente;
import com.efs.cursomc.domain.Endereco;
import com.efs.cursomc.domain.enums.Perfil;
import com.efs.cursomc.domain.enums.TipoCliente;
import com.efs.cursomc.dto.ClienteDTO;
import com.efs.cursomc.dto.ClienteNewDTO;
import com.efs.cursomc.repositories.ClienteRepository;
import com.efs.cursomc.repositories.EnderecoRepository;
import com.efs.cursomc.security.UserSS;
import com.efs.cursomc.services.exception.AuthorizationException;
import com.efs.cursomc.services.exception.DateIntegrityException;
import com.efs.cursomc.services.exception.ObjectNotFoundException;

@Service
public class ClienteService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ClienteService.class);


	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private S3Service s3Service;
	
	@Autowired
	private ImageService imageService;
	
	@Value("${img.prefix.client.profile}")
	private String prefix;

	@Value("${img.profile.size}")
	private Integer size;
	
	public Cliente find(Integer id) {
		
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		Optional<Cliente> op = repo.findById(id);
		return op.orElseThrow(
				() -> new ObjectNotFoundException(
						"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getSimpleName()));
	}
	

	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		return obj; 
	}

	public Cliente update(Cliente obj) {
		Cliente newObj = this.find(obj.getId());
		this.updateData(newObj, obj);
		return repo.save(newObj);
	}

	/**
	 * REGRA: NÃO posso apagar um cliente que possua pedidos, mas posso apagar um cliente que tenha apenas endereços, estes devem ser apagados em cascata.
	 * @param id
	 */
	public void delete(Integer id) {
		this.find(id);
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DateIntegrityException("Não é possivel excluir porque há Pedidos relacionadas");
		}
	}

	public List<Cliente> findAll() {
		return repo.findAll();
	}
	
	public Cliente findByEmail(String email) {
		//pega o usuário da sessao
		UserSS user = UserService.authenticated();
		
		//não eh admin e não o emaiil passado não é igual ao logado
		if (user == null || !user.hasRole(Perfil.ADMIN) && !email.equals(user.getUsername())) {
			throw new AuthorizationException("Acesso negado");
		}

		Cliente obj = repo.findByEmail(email);
		if (obj == null) {
			throw new ObjectNotFoundException("Objeto não encontrado! Id: " + user.getId() + ", Tipo: " + Cliente.class.getName());
		}
		return obj;
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}

	public Cliente fromDTO(ClienteDTO objDTO) {
		return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail(), null, null, null);
	}
	
	public Cliente fromDTO(ClienteNewDTO objDTO) {
		String senhaEncode = bCryptPasswordEncoder.encode(objDTO.getSenha());
		Cliente cliente = new Cliente(null, objDTO.getNome(), objDTO.getEmail(), objDTO.getCpfOuCnpj(), TipoCliente.toEnum(objDTO.getTipoCliente()), senhaEncode);
		Cidade cidade = new Cidade(objDTO.getCidadeId(), null);
		Endereco endereco = new Endereco(null, objDTO.getLogradouro(), objDTO.getNumero(), objDTO.getComplemento(), objDTO.getBairro(), objDTO.getCep(), cliente, cidade);
		cliente.getEnderecos().add(endereco);
		cliente.getTelefones().add(objDTO.getTelefone1());
		
		if (objDTO.getTelefone2() != null) {
			cliente.getTelefones().add(objDTO.getTelefone2());	
		}
		if (objDTO.getTelefone3() != null) {
			cliente.getTelefones().add(objDTO.getTelefone3());	
		}
		
		return cliente;
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
	
	public URI uploadProfilePicture(MultipartFile multipartFile) {
		//usuário logado
		UserSS user = UserService.authenticated();
		if (user == null) {
			throw new AuthorizationException("Acesso negado");
		}

		
		BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
		
		LOGGER.info("ajustando para deixar a foto quadrada");
		jpgImage = imageService.cropSquare(jpgImage);
		
		LOGGER.info("ajustando tamando da imagem (size): " + size);
		jpgImage = imageService.resize(jpgImage, size);
		
		String fileName = prefix + user.getId() + ".jpg";
		LOGGER.info("Enviando ao S3 a imagem: " + fileName);
		
		return s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");
	}
}