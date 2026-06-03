package com.project.community.servicios;


import java.util.List;

import com.project.community.DTO.HistorialMensajesDTO;
import com.project.community.DTO.MensajeDTO;

public interface MensajeService {
	public HistorialMensajesDTO guardarMensaje(Long eventoId, MensajeDTO mensaje, String email);
	public List<HistorialMensajesDTO> obtenerHistorial(Long eventoId);
	
}
