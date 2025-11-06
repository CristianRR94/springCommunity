package com.project.community.controladores;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.community.entidades.Participante;
import com.project.community.servicios.ParticipanteServiceImpl;

@RestController
@RequestMapping("participante")
@CrossOrigin("http://localhost:4200")
public class ParticipanteController {

	private ParticipanteServiceImpl participanteService;
	
	@GetMapping("{id}")
	public Participante getParticipante(Long id) {
		return participanteService.getParticipante(id);
	}
	
	@GetMapping
	public Iterable<Participante>getparticipantes(){
		return participanteService.getParticipantes();
	}
}
