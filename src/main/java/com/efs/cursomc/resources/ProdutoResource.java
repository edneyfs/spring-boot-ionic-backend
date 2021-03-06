package com.efs.cursomc.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.efs.cursomc.domain.Produto;
import com.efs.cursomc.dto.ProdutoDTO;
import com.efs.cursomc.resources.utils.URL;
import com.efs.cursomc.services.ProdutoService;

@RestController
@RequestMapping(value = "/produtos")
public class ProdutoResource {
	
	@Autowired
	private ProdutoService service;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Produto> find(@PathVariable Integer id) {
		Produto produto = service.find(id);
		return ResponseEntity.ok().body(produto);
	}
	
	/**
	 * 
	 * @param nome
	 * @param categorias vem separado por virgulas (ex: 1,2,3)
	 * @param page
	 * @param linesPerPage
	 * @param orderBy
	 * @param direction
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Page<ProdutoDTO>> findPage(
			@RequestParam(value = "nome", defaultValue = "0") String nome,
			@RequestParam(value = "categorias", defaultValue = "0") String categorias,
			@RequestParam(value = "page", defaultValue = "0") Integer page, 
			@RequestParam(value = "linesPerPage", defaultValue = "24")  Integer linesPerPage, //padrão 24
			@RequestParam(value = "orderBy", defaultValue = "nome")  String orderBy, 
			@RequestParam(value = "direction", defaultValue = "ASC")  String direction) {
		
		String nomeDecode = URL.decodeParam(nome);
		
		List<Integer> categoriasList = URL.decodeIntList(categorias);
		if (categoriasList == null) {
			categoriasList = new ArrayList<>();
		}
		
		Page<Produto> list = service.search(nomeDecode, categoriasList, page, linesPerPage, orderBy, direction);
		
		//Page já é java 8
		Page<ProdutoDTO> listDTO = list.map(produto -> new ProdutoDTO(produto));
		return ResponseEntity.ok().body(listDTO);
	}
}