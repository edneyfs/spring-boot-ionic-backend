package com.efs.cursomc.resources.exception;

import java.util.ArrayList;
import java.util.List;

public class ValidationErros extends StandardErros {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<FieldMessage> errors = new ArrayList<FieldMessage>();
	
	public ValidationErros(Integer status, String msg, Long timeStamp) {
		super(status, msg, timeStamp);
	}

	public List<FieldMessage> getErrors() {
		return errors;
	}

	public void addError(String fielName, String message) {
		this.getErrors().add(new FieldMessage(fielName, message));
	}
}