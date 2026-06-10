package com.project.community.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public record UsuarioEntradaDTO (

	 String nombre,
	 String password,
	 String email)
{}
