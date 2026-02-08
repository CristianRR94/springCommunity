package com.project.community.entidades;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ErrorResponse {
	private int status;
	private String mensaje;
	private long timestamp;
}
