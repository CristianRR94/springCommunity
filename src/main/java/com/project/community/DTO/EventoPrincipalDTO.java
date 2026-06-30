package com.project.community.DTO;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record EventoPrincipalDTO (
		 Long id,	
		 String nombreEvento,
		 String tipoEvento,
		 LocalDate fechaEvento,
		 String imagenEvento
		){
	

}
