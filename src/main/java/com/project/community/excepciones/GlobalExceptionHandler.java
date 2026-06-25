package com.project.community.excepciones;

import org.springframework.http.HttpStatus;



import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.project.community.dominio.EventoNotFoundException;
import com.project.community.dominio.ParticipanteException;
import com.project.community.dominio.UsuarioNotFoundException;
import com.project.community.entidades.ErrorResponse;
import com.project.community.storage.StorageException;
import com.project.community.storage.StorageFileNotFoundException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;


@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<ErrorResponse> handleExpiredJwt(ExpiredJwtException ex){
		ErrorResponse error = new ErrorResponse(
				HttpStatus.UNAUTHORIZED.value(),
				"La sesión ha expirado",
				System.currentTimeMillis());
		return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
	}
	@ExceptionHandler({
        SignatureException.class,
        MalformedJwtException.class
    })
	public ResponseEntity<ErrorResponse> handleInvalidToken(Exception ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "Token inválido",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UsernameNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(StorageException.class)
	public ResponseEntity<ErrorResponse> handleInvalidImage(StorageException ex){
		ErrorResponse error = new ErrorResponse(
				HttpStatus.BAD_REQUEST.value(),
				ex.getMessage(),
				System.currentTimeMillis()
				);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUnknownPath(StorageFileNotFoundException ex){
		ErrorResponse error = new ErrorResponse(
				HttpStatus.NOT_FOUND.value(),
				ex.getMessage(),
				System.currentTimeMillis()
				);
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ParticipanteException.class)
	public ResponseEntity<ErrorResponse> handleInvalidParticipante(ParticipanteException ex){
		ErrorResponse error = new ErrorResponse(
				HttpStatus.UNPROCESSABLE_ENTITY.value(),
				ex.getMessage(),
				System.currentTimeMillis()
				);
		return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
	@ExceptionHandler(EventoNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUnknwonEvento(EventoNotFoundException ex){
		ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(),
				ex.getMessage(),
				System.currentTimeMillis());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ValidatorException.class)
	public ResponseEntity<ErrorResponse> handleValidation(ValidatorException ex){
		ErrorResponse error = new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
				ex.getMessage(),
				System.currentTimeMillis());
			return new ResponseEntity<ErrorResponse>(error, HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
	@ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleDtoValidation(org.springframework.web.bind.MethodArgumentNotValidException ex) {
	    // Extraemos el primer mensaje de error que pusiste en las anotaciones del DTO
	    String mensajeError = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
	    
	    ErrorResponse error = new ErrorResponse(
	            HttpStatus.BAD_REQUEST.value(),
	            mensajeError,
	            System.currentTimeMillis()
	    );
	    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(UsuarioNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUnknwonUsuario(UsuarioNotFoundException ex){
		ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(),
				ex.getMessage(),
				System.currentTimeMillis());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
}
