package com.project.community.dominio;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class ParticipanteException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ParticipanteException(String message) {
		 super (message);
	}
	
	public ParticipanteException(String message, Throwable cause) {
		super(message, cause);
	}
}
