package com.efs.cursomc.services.exception;

public class ObjectNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3263334702583456449L;

	public ObjectNotFoundException(String msg) {
		super(msg);
	}
}