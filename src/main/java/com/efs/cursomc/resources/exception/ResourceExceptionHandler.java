package com.efs.cursomc.resources.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.efs.cursomc.services.exception.DateIntegrityException;
import com.efs.cursomc.services.exception.ObjectNotFoundException;

//intercepta mensagens
@ControllerAdvice
public class ResourceExceptionHandler {

	//tratador de excecao do ObjectNotFoundException
	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<StandardErros> objectNotFound(ObjectNotFoundException e, HttpServletRequest request) {
		StandardErros err = new StandardErros(HttpStatus.NOT_FOUND.value(), e.getMessage(), System.currentTimeMillis());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}
	
	@ExceptionHandler(DateIntegrityException.class)
	public ResponseEntity<StandardErros> dateIntegrity(DateIntegrityException e, HttpServletRequest request) {
		StandardErros err = new StandardErros(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
}