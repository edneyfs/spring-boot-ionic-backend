package com.efs.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.efs.cursomc.domain.Cliente;
import com.efs.cursomc.repositories.ClienteRepository;
import com.efs.cursomc.services.exception.ObjectNotFoundException;

@Service
public class ClienteServices {

	@Autowired
	private ClienteRepository repo;
	
	public Cliente find(Integer id) {
		Optional<Cliente> op = repo.findById(id);
		return op.orElseThrow(
				() -> new ObjectNotFoundException(
						"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}
}