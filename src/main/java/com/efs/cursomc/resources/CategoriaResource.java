package com.efs.cursomc.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.efs.cursomc.domain.Categoria;
import com.efs.cursomc.services.CategoriaService;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource implements Resource {
	
	@Autowired
	private CategoriaService service;
	
	
	// @PathVariable faz a ligação com o parametro value={id}
	//ResponseEntity - add informaçoes de uma comunicaçao rest http
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Categoria> find(@PathVariable Integer id) { 
		Categoria obj = service.buscar(id);
		return ResponseEntity.ok().body(obj);
	}
}