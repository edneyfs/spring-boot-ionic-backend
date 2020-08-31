package com.efs.cursomc.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.efs.cursomc.security.UserSS;

/**
 * Serve para retornar o usuário logado
 * @author edney.siqueira
 *
 */
public class UserService {

	/**
	 * retornar o usuário logado
	 * @return
	 */
	public static UserSS authenticated() {
		try {
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			return null;
		}
	}
}