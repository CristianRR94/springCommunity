package com.project.community.servicios;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.project.community.DTO.ParticipanteAmigoDTO;
import com.project.community.DTO.ParticipanteDTO;
import com.project.community.entidades.Participante;
import com.project.community.entidades.Usuario;
import com.project.community.enums.StorageFolder;
import com.project.community.mapper.ParticipanteMapper;
import com.project.community.repositorios.ParticipanteRepository;
import com.project.community.storage.StorageException;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ParticipanteServiceImpl implements ParticipanteService {
	
	private final ParticipanteRepository participanteRepository;
	private final ImageService imageService;
	private final ParticipanteMapper participanteMapper;
	private final AuthDataService authDataService;
	
	private static final String IMAGE_PARTICIPANTES = "usuarios/default.png";
	
	@Override
	
	public ParticipanteDTO getParticipanteDTO(Long id) {
		Participante participante = participanteRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Participante no encontrado"));
		return participanteMapper.toDTO(participante);
	}

	@Override

	public ParticipanteAmigoDTO getAmigoDTO(Long id) {
		Participante participante = participanteRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Participante no encontrado"));
		return participanteMapper.toAmigoDTO(participante);
	}

	@Override
	
	public List<ParticipanteDTO> getParticipantesDTO() {
		List<Participante> participantes = participanteRepository.findAll();
		return participanteMapper.toDTOs(participantes);
	}

	@Override

	public Set<ParticipanteAmigoDTO> getAmigosAutenticadoDTO() {
		Participante participante = authDataService.obtenerParticipanteAutenticado();
		return participanteMapper.toAmigoListDTO(participante.getAmigos());
	}

	@Override
	
	public Set<ParticipanteDTO> mostrarListaAmigosDTO(String input) {
		Participante participante = authDataService.obtenerParticipanteAutenticado();
		Set<Participante> resultados = participanteRepository.buscarAmigos(input, participante.getId());
		return participanteMapper.toSetParticipantesDTO(resultados);
	}

	// =========================================================================
	// Métodos de uso interno o persistencia nativa de entidades
	// =========================================================================

	@Override
	public Participante getParticipante(Long id) {
		return participanteRepository.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Participante postParticipante(Participante participante) {
		return participanteRepository.save(participante);
	}

	@Override
	@Transactional
	public Participante putParticipante(Participante participante) {
		return participanteRepository.save(participante);
	}

	@Override
	@Transactional
	public void deleteParticipante(Participante participante) {
		participanteRepository.delete(participante);
	}

	@Override
	@Transactional
	public void addAmigo(Long idAmigo) {
		Long idParticipante = authDataService.obtenerParticipanteAutenticado().getId();
		Participante p = participanteRepository.findWithAmigosById(idParticipante)
				.orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
		Participante amigo = participanteRepository.findById(idAmigo)
				.orElseThrow(() -> new EntityNotFoundException("Amigo no encontrado"));
		if (idParticipante.equals(idAmigo)) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No puedes ser tu propio amigo");
	    }
		p.agregarAmigo(amigo);
		participanteRepository.save(p);
	}
	
	@Override
	@Transactional
	public Participante crearParticipanteNombreUsuario(String nombre, Usuario usuario) {
		return Participante.builder()
				.nombreParticipante(nombre)
				.usuario(usuario)
				.imagenParticipante(IMAGE_PARTICIPANTES)
				.build();
	}
	
	@Override
	@Transactional
	public void cambiarParticipanteNombre(String nombre) {
		Participante participante = authDataService.obtenerParticipanteAutenticado();
		participante.cambiarNombreParticipante(nombre);
		participanteRepository.save(participante);
	}
	
	@Override
	
	public Set<Participante> getAmigos(Long id){
		Participante participante = participanteRepository.findWithAmigosById(id)
				.orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
		return participante.getAmigos();
	}

	@Transactional
	@Override
	public void cambiarImagen(MultipartFile imagen) {
		if(imagen == null || imagen.isEmpty()) {
			throw new StorageException("Archivo no encontrado");
		}
		Participante participante = authDataService.obtenerParticipanteAutenticado();
		String fotoVieja = participante.getImagenParticipante();
		String archivo = imageService.postImage(imagen, StorageFolder.USUARIOS);
		
		participante.setImagenParticipante(archivo);
		imageService.deleteImage(fotoVieja);
	}

	//sin llamadas
	@Override
	public List<Participante> getParticipantes() {
		List<Participante> participantes = participanteRepository.findAll();
		return participantes;
	}

	//sin llamadas
	@Override
	public Set<Participante> mostrarListaAmigos(String input, Long miId) {
		Participante participante = authDataService.obtenerParticipanteAutenticado();
		Set<Participante> resultados = participanteRepository.buscarAmigos(input, participante.getId());
			
		return resultados;
	}
}