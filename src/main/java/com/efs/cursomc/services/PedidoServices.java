package com.efs.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.efs.cursomc.domain.ItemPedido;
import com.efs.cursomc.domain.PagamentoComBoleto;
import com.efs.cursomc.domain.Pedido;
import com.efs.cursomc.domain.enums.EstadoPagamento;
import com.efs.cursomc.repositories.ItemPedidoRepository;
import com.efs.cursomc.repositories.PagamentoRepository;
import com.efs.cursomc.repositories.PedidoRepository;
import com.efs.cursomc.services.exception.ObjectNotFoundException;

@Service
public class PedidoServices {

	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private ProdutoServices produtoservice;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	

	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	public Pedido find(Integer id) {
		Optional<Pedido> op = repo.findById(id);
		return op.orElseThrow(
				() -> new ObjectNotFoundException(
						"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getSimpleName()));
	}
	
	@Transactional
	public Pedido insert(Pedido pedido) {
		//dados do pedido
		pedido.setId(null);
		pedido.setInstante(new Date());
		
		//pagamento
		pedido.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		pedido.getPagamento().setPedido(pedido);
		
		//deveria vir do client
		if (pedido.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) pedido.getPagamento();
			BoletoService.preencherPagamentoComBoleto(pagto, pedido.getInstante());
		}
		
		pedido = repo.save(pedido);
		pagamentoRepository.save(pedido.getPagamento());
		
		for (ItemPedido ip : pedido.getItens()) {
			ip.setDesconto(0.0);
			ip.setPreco(produtoservice.find(ip.getProduto().getId()).getPreco());
			ip.setPedido(pedido);
		}
		itemPedidoRepository.saveAll(pedido.getItens());
		
		return pedido;
	}
}