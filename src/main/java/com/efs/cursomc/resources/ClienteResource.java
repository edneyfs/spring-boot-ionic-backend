package com.efs.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.efs.cursomc.domain.Cliente;
import com.efs.cursomc.dto.ClienteDTO;
import com.efs.cursomc.dto.ClienteNewDTO;
import com.efs.cursomc.services.ClienteServices;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {
	
	@Autowired
	private ClienteServices service;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Cliente> find(@PathVariable Integer id) {
		Cliente cliente = service.find(id);
		return ResponseEntity.ok().body(cliente);
	}
	

	/**
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<ClienteDTO>> findAll() {
		
		List<Cliente> list = service.findAll();
		// percorrer a lista (stream), map (efetua uma operaçõa para cada elemento da lista), (collect) retorna a lista
		List<ClienteDTO> listDTO = list.stream().map(
				categoria -> new ClienteDTO(categoria)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDTO);
	}
	
	/**
	 * 
	 */
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public ResponseEntity<Page<ClienteDTO>> findPage(
			@RequestParam(value = "page", defaultValue = "0") Integer page, 
			@RequestParam(value = "linesPerPage", defaultValue = "24")  Integer linesPerPage, //padrão 24
			@RequestParam(value = "orderBy", defaultValue = "nome")  String orderBy, 
			@RequestParam(value = "direction", defaultValue = "ASC")  String direction) {
		
		Page<Cliente> list = service.findPage(page, linesPerPage, orderBy, direction);
		
		//Page já é java 8
		Page<ClienteDTO> listDTO = list.map(
				categoria -> new ClienteDTO(categoria));
		return ResponseEntity.ok().body(listDTO);
	}
	
	/**
	 * 
	 * @param objDTO
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody ClienteNewDTO objDTO) { // (@RequestBody converte o JSON neste objeto / @Valid vai chamar as validações da classe DTO
		Cliente obj = service.fromDTO(objDTO);
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
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody ClienteDTO objDTO, @PathVariable Integer id) {
		objDTO.setId(id);
		Cliente obj = service.fromDTO(objDTO);
		obj.setId(id);
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) { // @PathVariable faz a ligação com o parametro value={id}
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}