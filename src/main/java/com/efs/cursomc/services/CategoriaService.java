package com.efs.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.efs.cursomc.domain.Categoria;
import com.efs.cursomc.repositories.CategoriaRepository;

@Service
public class CategoriaService {

	//será automaticamente instaciada pelo spring
	@Autowired
	private CategoriaRepository repo;
	
	public Categoria buscar(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElse(null);
	}
}