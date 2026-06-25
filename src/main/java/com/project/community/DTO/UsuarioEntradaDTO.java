package com.project.community.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


@JsonIgnoreProperties(ignoreUnknown = true)
public record UsuarioEntradaDTO (
	@NotBlank
	@Size(min = 6, max = 20)
	@Column(unique = true)
	String nombre,
	
	@NotBlank
	@Size(min = 8)
	@Pattern(
	        regexp = ".*\\d.*",
	        message = "La contraseña debe contener al menos un número"
	    )
	String password,
	
	@NotBlank
	@Email
	@Column(unique = true)
	String email)
{}
