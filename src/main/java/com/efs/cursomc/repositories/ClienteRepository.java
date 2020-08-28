package com.efs.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.efs.cursomc.domain.Cliente;

/**
 * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
 * 5.3.2. Query Creation
 * exemplo de metodos automaticos de consulta: ex: fingBi"Email"
 * 
 * 
 * @author edney.siqueira
 *
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

	/**
	 * se retorna o objeto ben da classe + nome do método com o prefixo "findBy"+atributo, o spring date implementa o metodo. basta setar a assinatura
	 */
	@Transactional(readOnly = true) // readOnly = true não precisa ser envolvida em uma transação de banco de dados, desta fora, é executada mais rápido.
	Cliente findByEmail(String email);
	
}