package com.efs.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.efs.cursomc.domain.Cliente;
import com.efs.cursomc.domain.ItemPedido;
import com.efs.cursomc.domain.PagamentoComBoleto;
import com.efs.cursomc.domain.Pedido;
import com.efs.cursomc.domain.enums.EstadoPagamento;
import com.efs.cursomc.repositories.ItemPedidoRepository;
import com.efs.cursomc.repositories.PagamentoRepository;
import com.efs.cursomc.repositories.PedidoRepository;
import com.efs.cursomc.security.UserSS;
import com.efs.cursomc.services.exception.AuthorizationException;
import com.efs.cursomc.services.exception.ObjectNotFoundException;

@Service
public class PedidoServices {

	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private ProdutoService produtoservice;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;

	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired //vai procurar uma classe com @Configuration e um email com @Bean com o nome emailServices que retorne o objeto definido aqui 
	private EmailService emailService;
	
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
		pedido.setCliente(clienteService.find(pedido.getCliente().getId()));

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
			ip.setProduto(produtoservice.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(pedido);
		}
		itemPedidoRepository.saveAll(pedido.getItens());
		
		emailService.sendOrderConfirmationHtmlEmail(pedido);
		
		return pedido;
	}
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		UserSS user = UserService.authenticated();
		if (user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente =  clienteService.find(user.getId());
		return repo.findByCliente(cliente, pageRequest);
	}
}