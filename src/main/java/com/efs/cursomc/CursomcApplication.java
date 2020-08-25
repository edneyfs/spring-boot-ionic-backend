package com.efs.cursomc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.efs.cursomc.domain.Categoria;
import com.efs.cursomc.domain.Cidade;
import com.efs.cursomc.domain.Cliente;
import com.efs.cursomc.domain.Endereco;
import com.efs.cursomc.domain.Estado;
import com.efs.cursomc.domain.Pagamento;
import com.efs.cursomc.domain.PagamentoComBoleto;
import com.efs.cursomc.domain.PagamentoComCartao;
import com.efs.cursomc.domain.Pedido;
import com.efs.cursomc.domain.Produto;
import com.efs.cursomc.domain.enums.EstadoPagamento;
import com.efs.cursomc.domain.enums.TipoCliente;
import com.efs.cursomc.repositories.CategoriaRepository;
import com.efs.cursomc.repositories.CidadeRepository;
import com.efs.cursomc.repositories.ClienteRepository;
import com.efs.cursomc.repositories.EnderecoRepository;
import com.efs.cursomc.repositories.EstadoRepository;
import com.efs.cursomc.repositories.PagamentoRepository;
import com.efs.cursomc.repositories.PedidoRepository;
import com.efs.cursomc.repositories.ProdutoRepository;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner {

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private EstadoRepository estadoRepository;

	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Categoria cat1 = new Categoria("Informática");
		Categoria cat2 = new Categoria("Escritório");

		Produto p1 = new Produto("Computador", 2000.00);
		Produto p2 = new Produto("Impressora", 800.00);
		Produto p3 = new Produto("Mouse", 80.00);
		
		cat1.getProdutos().addAll(Arrays.asList(p1, p2, p3));
		cat2.getProdutos().addAll(Arrays.asList(p2));
		
		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1, cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));

		categoriaRepository.saveAll(Arrays.asList(cat1, cat2));
		produtoRepository.saveAll(Arrays.asList(p1, p2, p3));
		
		//------------------------------------------------------
		Estado estado1 = new Estado("Minas Gerais");
		Estado estado2 = new Estado("São Paulo");
		
		Cidade c1 = new Cidade("Uberlândia", estado1);
		Cidade c2 = new Cidade("São Paulo", estado2);
		Cidade c3 = new Cidade("Campinas", estado2);

		estado1.getCidades().addAll(Arrays.asList(c1));
		estado2.getCidades().addAll(Arrays.asList(c2, c3));
		
		estadoRepository.saveAll(Arrays.asList(estado1, estado2));
		cidadeRepository.saveAll(Arrays.asList(c1, c2, c3));

		//------------------------------------------------------
		Cliente cli1 = new Cliente(null, "Maria Silva", "maria@gmail.com", "190", TipoCliente.PESSOAFISICA);
		cli1.getTelefones().addAll(Arrays.asList("111111", "2222222"));

		Endereco e1 = new Endereco(null, "Rua flores", "123", "nada", "Jardins", "123465", cli1, c1);
		Endereco e2 = new Endereco(null, "Av. Matos", "105", "sala 10", "Centro", "456465", cli1, c2);

		cli1.getEnderecos().addAll(Arrays.asList(e1, e2));
		
		clienteRepository.saveAll(Arrays.asList(cli1));
		enderecoRepository.saveAll(Arrays.asList(e1, e2));
		

		//------------------------------------------------------
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		Pedido ped1 = new Pedido(null, sdf.parse("30/09/2017 10:32"), cli1, e1);
		Pedido ped2 = new Pedido(null, sdf.parse("10/10/2017 19:35"), cli1, e2);
		
		Pagamento pagto1 = new PagamentoComCartao(null, EstadoPagamento.QUITADO, ped1, 6);
		ped1.setPagamento(pagto1);
		
		Pagamento pagto2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ped2, sdf.parse("20/10/2017 00:00"), null);
		ped2.setPagamento(pagto2);
		
		cli1.getPedidos().addAll(Arrays.asList(ped1, ped2));

		pedidoRepository.saveAll(Arrays.asList(ped1, ped2));
		pagamentoRepository.saveAll(Arrays.asList(pagto1, pagto2));
		
	}
}