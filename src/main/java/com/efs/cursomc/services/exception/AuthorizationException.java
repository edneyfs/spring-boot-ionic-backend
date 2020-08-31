package com.efs.cursomc.services.exception;

public class AuthorizationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1;

	public AuthorizationException(String msg) {
		super(msg);
	}
}