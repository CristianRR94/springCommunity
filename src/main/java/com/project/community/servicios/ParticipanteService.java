package com.project.community.servicios;

import java.util.List;
import java.util.Set;

import com.project.community.DTO.ParticipanteAmigoDTO;
import com.project.community.DTO.ParticipanteDTO;
import com.project.community.entidades.Participante;
import com.project.community.entidades.Usuario;

public interface ParticipanteService {

	// =========================================================================
	// Métodos que devuelven DTOs (Para el Controlador / API Externa)
	// =========================================================================
	
	ParticipanteDTO getParticipanteDTO(Long id);

	ParticipanteAmigoDTO getAmigoDTO(Long id);

	List<ParticipanteDTO> getParticipantesDTO();

	Set<ParticipanteAmigoDTO> getAmigosAutenticadoDTO();

	Set<ParticipanteDTO> mostrarListaAmigosDTO(String input);

	// =========================================================================
	// Métodos nativos con Entidades (Para uso interno entre servicios)
	// =========================================================================
	
	Participante getParticipante(Long id);
	
	Participante postParticipante(Participante participante);
	
	List<Participante> getParticipantes();
	
	Participante putParticipante(Participante participante);
	
	void deleteParticipante(Participante participante);
	
	Set<Participante> getAmigos(Long id);
	
	Set<Participante> mostrarListaAmigos(String input, Long miId);

	// =========================================================================
	// Métodos de Lógica de Negocio y Modificaciones
	// =========================================================================
	
	void addAmigo(Long idAmigo);
	
	Participante crearParticipanteNombreUsuario(String nombre, Usuario usuario);
	
	void cambiarParticipanteNombre(String nombre);
	
	void cambiarImagen(String imagen, Long id);
}