package com.efs.cursomc.resources.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.efs.cursomc.services.exception.DateIntegrityException;
import com.efs.cursomc.services.exception.ObjectNotFoundException;

//intercepta mensagens
@ControllerAdvice
public class ResourceExceptionHandler {

	/**
	 * 
	 * @param e
	 * @param request
	 * @return
	 */
	//tratador de excecao do ObjectNotFoundException
	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<StandardErros> objectNotFound(ObjectNotFoundException e, HttpServletRequest request) {
		StandardErros err = new StandardErros(HttpStatus.NOT_FOUND.value(), e.getMessage(), System.currentTimeMillis());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}
	
	/**
	 * 
	 * @param e
	 * @param request
	 * @return
	 */
	@ExceptionHandler(DateIntegrityException.class)
	public ResponseEntity<StandardErros> dateIntegrity(DateIntegrityException e, HttpServletRequest request) {
		StandardErros err = new StandardErros(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
	
	/**
	 * 
	 * @param e
	 * @param request
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardErros> validation(MethodArgumentNotValidException e, HttpServletRequest request) {
		ValidationErros err = new ValidationErros(HttpStatus.BAD_REQUEST.value(), "Erro de validação", System.currentTimeMillis());
		
		for (FieldError error : e.getBindingResult().getFieldErrors()) {
			err.addError(error.getField(), error.getDefaultMessage());
		}
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
}