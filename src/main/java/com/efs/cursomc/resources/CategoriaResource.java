package com.efs.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.efs.cursomc.domain.Categoria;
import com.efs.cursomc.dto.CategoriaDTO;
import com.efs.cursomc.services.CategoriaService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {
	
	@Autowired
	private CategoriaService service;
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@ApiOperation(value="find >> Busca por id") // Muda a descricao do metodo no Swagger
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Categoria> find(@PathVariable Integer id) { // @PathVariable faz a ligação com o parametro value={id}
		Categoria obj = service.find(id);
		//ResponseEntity - add informaçoes de uma comunicaçao rest http
		return ResponseEntity.ok().body(obj);
	}

	/**
	 * 
	 * @return
	 */
	@ApiOperation(value="findAll >> Retorna todas as categoria") // Muda a descricao do metodo no Swagger
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<CategoriaDTO>> findAll() {
		
		List<Categoria> list = service.findAll();
		// percorrer a lista (stream), map (efetua uma operaçõa para cada elemento da lista), (collect) retorna a lista
		List<CategoriaDTO> listDTO = list.stream().map(
				categoria -> new CategoriaDTO(categoria)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDTO);
	}
	
	/**
	 * 
	 */
	@ApiOperation(value="findPage >> Retorna todas as categoria com paginação") // Muda a descricao do metodo no Swagger
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public ResponseEntity<Page<CategoriaDTO>> findPage(
			@RequestParam(value = "page", defaultValue = "0") Integer page, 
			@RequestParam(value = "linesPerPage", defaultValue = "24")  Integer linesPerPage, //padrão 24
			@RequestParam(value = "orderBy", defaultValue = "nome")  String orderBy, 
			@RequestParam(value = "direction", defaultValue = "ASC")  String direction) {
		
		Page<Categoria> list = service.findPage(page, linesPerPage, orderBy, direction);
		
		//Page já é java 8
		Page<CategoriaDTO> listDTO = list.map(
				categoria -> new CategoriaDTO(categoria));
		return ResponseEntity.ok().body(listDTO);
	}
	
	/**
	 * 
	 * @param objDTO
	 * @return
	 */
	@ApiOperation(value="insert >> Insere categoria") // Muda a descricao do metodo no Swagger
	@PreAuthorize("hasAnyRole('ADMIN')") // so deixa passar se logado com usuário de perfil ADMIN (foi necessário SecurityConfig add @EnableGlobalMethodSecurity(prePostEnabled = true))
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody CategoriaDTO objDTO) { // (@RequestBody converte o JSON neste objeto / @Valid vai chamar as validações da classe DTO
		Categoria obj = service.fromDTO(objDTO);
		obj = service.insert(obj);

		//pega a URI para pesquisa este novo objeto (boa pratica)
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	/**
	 * 
	 * @param obj
	 * @param id
	 * @return
	 */
	@ApiOperation(value="update >> Atualiza categoria") // Muda a descricao do metodo no Swagger
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody CategoriaDTO objDTO, @PathVariable Integer id) {
		Categoria obj = service.fromDTO(objDTO);
		obj.setId(id);
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@ApiOperation(value="delete >> Remove categoria") // Muda a descricao do metodo no Swagger
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Não é possível excluir uma categoria que possui produtos"),
			@ApiResponse(code = 404, message = "Código inexistente") }) // definido no global, SwaggerConfig esta como "não encontrado"
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) { // @PathVariable faz a ligação com o parametro value={id}
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}