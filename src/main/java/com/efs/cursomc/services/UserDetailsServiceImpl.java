package com.efs.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.efs.cursomc.domain.Cliente;
import com.efs.cursomc.repositories.ClienteRepository;
import com.efs.cursomc.security.UserSS;

/**
 * autenticação e geração do token JWT
 * @author edney.siqueira
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	/**
	 * recebe o usuário e retorna o UserDetails
	 */
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Cliente cliente = clienteRepository.findByEmail(email);
		if (cliente == null) {
			throw new UsernameNotFoundException(email);
		}
		
		return new UserSS(cliente.getId(), cliente.getEmail(), cliente.getSenha(), cliente.getPerfis());
	}
}