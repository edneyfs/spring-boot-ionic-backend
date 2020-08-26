package com.efs.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.efs.cursomc.domain.Pedido;
import com.efs.cursomc.repositories.PedidoRepository;
import com.efs.cursomc.services.exception.ObjectNotFoundException;

@Service
public class PedidoServices {

	@Autowired
	private PedidoRepository repo;
	
	public Pedido find(Integer id) {
		Optional<Pedido> op = repo.findById(id);
		return op.orElseThrow(
				() -> new ObjectNotFoundException(
						"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}
}