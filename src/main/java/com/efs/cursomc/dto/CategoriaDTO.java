package com.efs.cursomc.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.efs.cursomc.domain.Categoria;

public class CategoriaDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4546630630682245765L;
	
	private Integer id;
	
	@NotEmpty(message = "Preenchimento obrigatório")
	@Size(min = 5, max = 80, message = "O tamanho deve ser entre 5 e 80 caracteres")
	private String nome;
	
	public CategoriaDTO() {
	}
	
	public CategoriaDTO(Categoria categoria) {
		this.id = categoria.getId();
		this.nome = categoria.getNome();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}