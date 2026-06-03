package com.project.community.dominio;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UsuarioNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UsuarioNotFoundException(String message) {
		super(message);
	}
	
	public UsuarioNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
