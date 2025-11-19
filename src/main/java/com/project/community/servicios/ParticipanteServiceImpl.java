package com.project.community.servicios;


import org.springframework.stereotype.Service;


import com.project.community.entidades.Participante;
import com.project.community.repositorios.ParticipanteRepository;
@Service
public class ParticipanteServiceImpl implements ParticipanteService{
	
	private final ParticipanteRepository participanteRepository;
	
	public ParticipanteServiceImpl(ParticipanteRepository participanteRepository) {
		this.participanteRepository = participanteRepository;
	}
	@Override
	public Participante getParticipante(Long id) {
	
		return participanteRepository.findById(id).orElse(null);
	}

	@Override
	public Participante postParticipante(Participante participante) {
		
		return participanteRepository.save(participante);
	}

	@Override
	public Iterable<Participante> getParticipantes() {
		
		return participanteRepository.findAll();
	}

	@Override
	public Participante putParticipante(Participante participante) {
		
		return participanteRepository.save(participante);
	}

	@Override
	public void deleteParticipante(Participante participante) {
		
		participanteRepository.delete(participante);
	}
	
	public Participante findParticipanteByUsuario(Long id) {
		return participanteRepository.findByUsuarioId(id);
	}
	
	public Participante crearParticipanteNombre(String nombre) {
		return Participante.builder().nombreParticipante(nombre).build();
	}
	
	public void cambiarParticipanteNombre(String nombre, Long UsuarioId) {
		Participante participante = this.findParticipanteByUsuario(UsuarioId);
		 participante.cambiarNombreParticipante(nombre);
		 participanteRepository.save(participante);
	}

}
