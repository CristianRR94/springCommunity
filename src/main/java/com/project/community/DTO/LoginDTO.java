package com.project.community.DTO;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO( 
	@NotBlank
	String nombre,
	@NotBlank
	String password
	) {
}
