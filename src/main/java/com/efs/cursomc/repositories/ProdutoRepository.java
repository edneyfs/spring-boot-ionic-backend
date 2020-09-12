package com.efs.cursomc.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.efs.cursomc.domain.Categoria;
import com.efs.cursomc.domain.Produto;

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
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

	//@Query Defini o JPQL sem precisar criar a classe importar a interface, anotar os parametros do metodo com @Param("") para que estes sejam substituidos no JPQL 
	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT obj FROM Produto obj INNER JOIN obj.categorias cat WHERE obj.nome LIKE %:nome% AND cat IN :categorias")
	Page<Produto> findDistinctByNomeContainingAndCategoriasIn(@Param("nome") String nome, @Param("categorias") List<Categoria> categorias, Pageable pageRequest);

	//	ou

	//usando padrão de nomes do Spring date
	//			     Distinct  por nome  join    and categorias in
//	@Transactional(readOnly = true)
//	Page<Produto> findDistinctByNomeContainingAndCategoriasIn(String nome, List<Categoria> categorias, Pageable pageRequest);
	@Query("SELECT obj FROM Produto obj INNER JOIN obj.categorias cat WHERE cat IN :categorias")
	Page<Produto> findContainingCategoriasIn(List<Categoria> categorias, Pageable pageRequest);
}