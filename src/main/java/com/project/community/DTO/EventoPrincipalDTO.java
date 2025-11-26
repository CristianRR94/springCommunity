package com.project.community.DTO;

import java.time.LocalDate;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoPrincipalDTO {
	private Long id;	
	private String nombreEvento;
	private String tipoEvento;
	private LocalDate fechaEvento;
	private String imagenEvento; 

}
