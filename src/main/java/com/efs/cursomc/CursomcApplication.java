package com.efs.cursomc;

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
import com.efs.cursomc.domain.Produto;
import com.efs.cursomc.domain.enums.TipoCliente;
import com.efs.cursomc.repositories.CategoriaRepository;
import com.efs.cursomc.repositories.CidadeRepository;
import com.efs.cursomc.repositories.ClienteRepository;
import com.efs.cursomc.repositories.EnderecoRepository;
import com.efs.cursomc.repositories.EstadoRepository;
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
	}
}