package com.efs.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.efs.cursomc.domain.Categoria;
import com.efs.cursomc.repositories.CategoriaRepository;
import com.efs.cursomc.services.exception.ObjectNotFoundException;

@Service
public class CategoriaService {

	//instanciado automaticamente pelo spring
	@Autowired
	private CategoriaRepository repo;
	
	public Categoria buscar(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		
		//retorna o objetp ou exception
		return obj.orElseThrow(
				() -> new ObjectNotFoundException(
						"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}

	public Categoria insert(Categoria obj) {
		return repo.save(obj);
	}
}