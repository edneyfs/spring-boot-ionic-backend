package com.efs.cursomc.resources.exception;

import java.io.Serializable;

public class FieldMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 771509172174529573L;
	
	private String fielName;
	private String message;
	
	public FieldMessage() {
	}

	public FieldMessage(String fielName, String message) {
		super();
		this.fielName = fielName;
		this.message = message;
	}

	public String getFielName() {
		return fielName;
	}

	public void setFielName(String fielName) {
		this.fielName = fielName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}