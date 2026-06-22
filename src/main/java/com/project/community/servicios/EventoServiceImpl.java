package com.project.community.servicios;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.project.community.DTO.EventoDTO;
import com.project.community.DTO.EventoPrincipalDTO;
import com.project.community.dominio.EventoNotFoundException;
import com.project.community.dominio.ParticipanteException;
import com.project.community.entidades.Evento;
import com.project.community.entidades.Participante;
import com.project.community.enums.StorageFolder;
import com.project.community.mapper.EventoMapper;
import com.project.community.repositorios.EventoRepository;
import com.project.community.repositorios.ParticipanteRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventoServiceImpl implements EventoService{

    private final ParticipanteRepository participanteRepository;
	private final EventoRepository eventoRepository;
	private final AuthDataService authDataService;
	private final EventoMapper mapper;
	private final ImageService imageService;
	
	private static final String IMAGE_PLACEHOLDER = "eventos/default.png";

	
	@Override
	public Evento getEvento(Long id) {

		Evento evento = eventoRepository.findById(id).orElseThrow(
				() -> new EventoNotFoundException("Evento no encontrado con id: " + id)
				);

		return evento;
	}

	@Override
	public Set<Evento> getEventos() {

		return null;//eventoRepository.findAll();
	}

	@Override
	@Transactional
	public Evento postEvento(EventoDTO dto) {
		Evento evento = mapper.toEvento(dto);
		Participante participante = authDataService.obtenerParticipanteAutenticado();
		evento.validarFecha();
		evento.addCreadorComoAdmin(participante);
		//cambio para invitacion
		if(dto.getParticipantesEvento() != null) {
			dto.getParticipantesEvento().forEach(id -> {
				participanteRepository.findById(id).ifPresent(
						amigo->{
							evento.addParticipante(
									amigo);
						});
			});
		}
		if(evento.getImagenEvento() == null || evento.getImagenEvento().isEmpty()) {
			evento.setImagenEvento(IMAGE_PLACEHOLDER);
		}
		return eventoRepository.save(evento);
	}

	@Override
	@Transactional
	public EventoDTO putEvento(EventoDTO dto, MultipartFile imagen, Long eventoId) {
		Evento eventoActual = eventoRepository.findById(eventoId)
				.orElseThrow(()-> new EventoNotFoundException("Error al detectar el evento actual"));
		Long idParticipante = authDataService.obtenerParticipanteAutenticado().getId();
		if(idParticipante == null || !participanteRepository.existsById(idParticipante)) {
			throw new ParticipanteException("Participante no encontrado");
		}
		
		boolean esAdmin = eventoActual.getAdministradores().stream()
				.anyMatch(admin -> admin.getId().equals(idParticipante));
		if(!esAdmin) {
			throw new ParticipanteException("El participante no tiene permiso para modificar el evento");
		}
		String viejaImagen = eventoActual.getImagenEvento();
		if(imagen != null && !imagen.isEmpty()) {
			String rutaArchivo = imageService.postImage(imagen, StorageFolder.EVENTOS);
			eventoActual.setImagenEvento(rutaArchivo);
			if(viejaImagen != null && !viejaImagen.isEmpty() && !viejaImagen.equals(IMAGE_PLACEHOLDER)) {
				imageService.deleteImage(viejaImagen);
			}
		}
		eventoActual.actualizarEvento(
		        dto.getNombreEvento(),
		        dto.getTipoEvento(),
		        dto.getFechaEvento(),
		        dto.getInformacion(),
		       // dto.getChat(),
		        dto.isPrivado(),
		        dto.isOculto(),
		        dto.getMaxNumParticipantes()
		    );
		dto.setId(eventoId);
		
		Evento eventoModificado = eventoRepository.save(eventoActual);
		return mapper.toDTO(eventoModificado);
	}

	@Override
	@Transactional
	public void deleteEvento(Long eventoId) {
		Long participanteId = authDataService.obtenerParticipanteAutenticado().getId();
		Evento evento = eventoRepository.findById(eventoId).orElseThrow(() -> new EventoNotFoundException("Evento no encontrado"));
		boolean esAdmin = evento.getAdministradores().stream()
				.anyMatch(admin -> admin.getId().equals(participanteId));
		if(!esAdmin) {
			throw new ParticipanteException("El participante no tiene permiso para eliminar el evento");
		}
		eventoRepository.delete(evento);
		//como no eliminamos de la db si no de memoria, va después
		if(evento.getImagenEvento() != null && !evento.getImagenEvento().isEmpty() && !evento.getImagenEvento().equals(IMAGE_PLACEHOLDER)) {
			imageService.deleteImage(evento.getImagenEvento());
		}
		
	}

	@Override
	public Set<Evento> getEventosPorParticipanteId(Long idParticipante) {
		
		return eventoRepository.findByParticipantesEvento_Id(idParticipante);
	}

	@Override
	public EventoDTO getEventoDTO(Long id) {
		Evento evento = eventoRepository.findById(id)
				.orElseThrow(()-> new EventoNotFoundException("No se ha encontrado el evento"));
		return mapper.toDTO(evento);
	}

	@Override
	public List<EventoPrincipalDTO> getEventosPorParticipantePrincipalDTO() {
		Participante participante = authDataService.obtenerParticipanteAutenticado();
		Long participanteId = participante.getId();
		Set<Evento> eventos = eventoRepository.findByParticipantesEvento_Id(participanteId);
		return mapper.toPrincipalDTOList(eventos);
	}

	@Override
	@Transactional
	public EventoDTO createEventoDTO(EventoDTO eventodto, MultipartFile imagen) {
		Participante participante = authDataService.obtenerParticipanteAutenticado();
		Evento evento = mapper.toEvento(eventodto);
		evento.validarFecha();
		evento.addCreadorComoAdmin(participante);
		//cambio para invitacion
		if(eventodto.getParticipantesEvento() != null) {
			eventodto.getParticipantesEvento().forEach(id -> {
				participanteRepository.findById(id).ifPresent(
						amigo->{
							evento.addParticipante(
									amigo);
						});
			});
		}
		if(evento.getImagenEvento() == null || evento.getImagenEvento().isEmpty()) {
			evento.setImagenEvento(IMAGE_PLACEHOLDER);
		}
		if(imagen != null && !imagen.isEmpty()) {
			String archivo = imageService.postImage(imagen, StorageFolder.EVENTOS);
			evento.setImagenEvento(archivo);
		}
		eventoRepository.save(evento);
		return mapper.toDTO(evento);
	}

	@Override
	public List<EventoPrincipalDTO> getEventosPrincipalDTO() {
		List<Evento> eventos = eventoRepository.findAll();
		Set<Evento> eventosSet = Set.copyOf(eventos);
		Participante participante = authDataService.obtenerParticipanteAutenticado();
		Set<Evento> eventosParticipante = participante.getEventos();
		Set<Evento> eventosFiltrados = eventosSet.stream()
		.filter(e -> !e.isOculto())
		.filter(e -> !e.isPrivado())
		.filter(e -> eventosParticipante.contains(e))
		.collect(Collectors.toSet());
		return mapper.toPrincipalDTOList(eventosFiltrados);
	}

	@Override
	public void deleteEventoDTO() {
		// TODO Auto-generated method stub
		
	}

}
