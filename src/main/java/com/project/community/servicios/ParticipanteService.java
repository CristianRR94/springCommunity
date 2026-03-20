package com.project.community.servicios;



import java.util.List;
import java.util.Set;

import com.project.community.entidades.Participante;
import com.project.community.entidades.Usuario;

public interface ParticipanteService {

	public Participante getParticipante(Long id);
	
	public Participante postParticipante(Participante participante);
	
	public List<Participante> getParticipantes();
	
	public Participante putParticipante(Participante participante);
	
	public void deleteParticipante(Participante participante);
	
	public Participante findParticipanteByUsuario(Long id);
	
	public void addAmigo(Long idParticipante, Long idAmigo);
	
	//methods for the creation of "Participante" based on "Usuario"
	
	//public Participante postNameParticipante(Participante participante, String name);
	
	public Participante crearParticipanteNombreUsuario(String nombre, Usuario usuario);
	
	public void cambiarParticipanteNombre(String nombre, Long usuarioId);
	
	public Set<Participante> getAmigos(Long id);
	
}
