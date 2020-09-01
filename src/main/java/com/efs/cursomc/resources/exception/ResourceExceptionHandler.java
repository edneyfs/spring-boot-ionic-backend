package com.efs.cursomc.resources.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.efs.cursomc.services.exception.AuthorizationException;
import com.efs.cursomc.services.exception.DateIntegrityException;
import com.efs.cursomc.services.exception.FileException;
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
	
	/**
	 * 
	 * @param e
	 * @param request
	 * @return
	 */
	//tratador de excecao do ObjectNotFoundException
	@ExceptionHandler(AuthorizationException.class)
	public ResponseEntity<StandardErros> authorization(AuthorizationException e, HttpServletRequest request) {
		//FORBIDDEN acesso negado
		StandardErros err = new StandardErros(HttpStatus.FORBIDDEN.value(), e.getMessage(), System.currentTimeMillis());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
	}
	
	/**
	 * 
	 * @param e
	 * @param request
	 * @return
	 */
	//tratador de excecao do ObjectNotFoundException
	@ExceptionHandler(FileException.class)
	public ResponseEntity<StandardErros> file(FileException e, HttpServletRequest request) {
		//FORBIDDEN acesso negado
		StandardErros err = new StandardErros(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
	
	/**
	 * 
	 * @param e
	 * @param request
	 * @return
	 */
	//tratador de excecao do ObjectNotFoundException
	@ExceptionHandler(AmazonServiceException.class)
	public ResponseEntity<StandardErros> amazonService(AmazonServiceException e, HttpServletRequest request) {
		//FORBIDDEN acesso negado
		HttpStatus code = HttpStatus.valueOf(e.getErrorCode());
		StandardErros err = new StandardErros(code.value(), e.getMessage(), System.currentTimeMillis());
		return ResponseEntity.status(code).body(err);
	}
	
	/**
	 * 
	 * @param e
	 * @param request
	 * @return
	 */
	//tratador de excecao do ObjectNotFoundException
	@ExceptionHandler(AmazonClientException.class)
	public ResponseEntity<StandardErros> amazonClient(AmazonClientException e, HttpServletRequest request) {
		//FORBIDDEN acesso negado
		StandardErros err = new StandardErros(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
	
	/**
	 * 
	 * @param e
	 * @param request
	 * @return
	 */
	//tratador de excecao do ObjectNotFoundException
	@ExceptionHandler(AmazonS3Exception.class)
	public ResponseEntity<StandardErros> amazonS3(AmazonS3Exception e, HttpServletRequest request) {
		//FORBIDDEN acesso negado
		StandardErros err = new StandardErros(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
	
	
	
}