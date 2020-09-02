package com.efs.cursomc.resources.exception;

import java.util.ArrayList;
import java.util.List;

public class ValidationErros extends StandardError {
	private static final long serialVersionUID = 1L;

	private List<FieldMessage> errors = new ArrayList<FieldMessage>();
	
	public ValidationErros(Long timestamp, Integer status, String error, String message, String path) {
		super(timestamp, status, error, message, path);
	}

	public List<FieldMessage> getErrors() {
		return errors;
	}

	public void addError(String fielName, String message) {
		this.getErrors().add(new FieldMessage(fielName, message));
	}
}