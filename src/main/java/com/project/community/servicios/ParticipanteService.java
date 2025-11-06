package com.project.community.servicios;



import com.project.community.entidades.Participante;

public interface ParticipanteService {

	public Participante getParticipante(Long id);
	
	public Participante postParticipante(Participante participante);
	
	public Iterable<Participante> getParticipantes();
	
	public Participante putParticipante(Participante participante);
	
	public void deleteParticipante(Participante participante);
	
	//methods for the creation of "Participante" based on "Usuario"
	
	//public Participante postNameParticipante(Participante participante, String name);
	
	public Participante crearParticipanteNombre(String nombre);
}
