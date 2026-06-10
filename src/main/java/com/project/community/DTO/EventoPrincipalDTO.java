package com.project.community.DTO;

import java.time.LocalDate;

public record EventoPrincipalDTO (
		 Long id,	
		 String nombreEvento,
		 String tipoEvento,
		 LocalDate fechaEvento,
		 String imagenEvento
		){
	

}
