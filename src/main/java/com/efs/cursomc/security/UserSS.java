package com.efs.cursomc.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.efs.cursomc.domain.enums.Perfil;

/**
 * contrato do Spring Security (implements UserDetails)
 * @author edney.siqueira
 *
 * autenticação e geração do token JWT
 * UserSpringSecurity
 */
public class UserSS implements UserDetails {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String email;
	private String senha;
	private Collection<? extends GrantedAuthority> authorities;
	
	public UserSS() {
	}
	
	
	
	public UserSS(Integer id, String email, String senha, Set<Perfil> perfis) {
		super();
		this.id = id;
		this.email = email;
		this.senha = senha;
		this.authorities = perfis.stream().map( x -> new SimpleGrantedAuthority(x.getDescricao())).collect(Collectors.toList());
	}



	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return senha;
	}

	@Override
	public String getUsername() {
		return email;
	}
	
	public Integer getId(){
		return id;
	}

	/**
	 * Por padrão, nossa conta não esta expirada
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * Por padrão, conta não esta bloqueada
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * Por padrão, credencia não esta expirada
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * usuario esta ativo? nosso padrão, sim.
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}
}