package com.efs.cursomc.domain.enums;

public enum Perfil {

	//O framework spring security obriga que a variavél comece por "ROLE_"
	ADMIN(1, "ROLE_ADMIN"), 
	CLIENTE(2, "ROLE_CLIENTE");

	private Integer codigo;
	private String descricao;

	private Perfil(Integer codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public static Perfil toEnum(Integer id) {
		
		if (id == null) {
			return null;
		}
		
		for (Perfil tipoCliente : Perfil.values()) {
			if (tipoCliente.getCodigo().equals(id)) {
				return tipoCliente;
			}
		}

		throw new IllegalArgumentException("id invalido: " + id);
	}
}