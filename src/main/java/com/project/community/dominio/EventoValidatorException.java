package com.project.community.dominio;

import com.project.community.excepciones.ValidatorException;

public class EventoValidatorException extends ValidatorException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EventoValidatorException(String message) {
		super(message);
	}
	
	public EventoValidatorException(String message, Throwable cause) {
		super(message, cause);
	}

}
