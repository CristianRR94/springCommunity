package com.project.community.controller;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.project.community.DTO.EventoDTO;
import com.project.community.DTO.EventoPrincipalDTO;
import com.project.community.config.JwtAuthFilter;
import com.project.community.controladores.EventoController;
import com.project.community.servicios.EventoService;
import com.project.community.storage.StorageProperties;

@WebMvcTest(
		controllers = EventoController.class,
		excludeFilters = @ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE,
		classes = JwtAuthFilter.class)
		)
@AutoConfigureMockMvc(addFilters=false)
public class EventoControllerTest {
	
	@Autowired
	private MockMvc mockmvc;
	

	
	@MockitoBean
	private EventoService eventoService;
	
	@MockitoBean private StorageProperties storageProperties;
	
	//happy path
	
	@Test
	void  getEvento_ShouldReturnEvento_whenIdExists() throws Exception{
		//arrange
		EventoDTO dto = EventoDTO.builder()
		.id(1L)
		.nombreEvento("eventoTest")
		.build();
		
		when(eventoService.getEventoDTO(1L)).thenReturn(dto);
		
		//act & assert
		mockmvc.perform(get("/api/eventos/1")
				.contentType(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(1))
		.andExpect(jsonPath("$.nombreEvento").value("eventoTest"));
	}
	
	@Test
	void getEventosPorParticipanteId_ShouldReturnListOfEventos_WhenIdParticipanteExists() throws Exception{
		//arrange
		EventoPrincipalDTO principalDto = EventoPrincipalDTO.builder()
				.id(1L)
				.nombreEvento("eventoTest")
				.build();
		EventoPrincipalDTO principalDto2 = EventoPrincipalDTO.builder()
				.id(2L)
				.nombreEvento("eventoTest2")
				.build();
		List<EventoPrincipalDTO> listaEventos= new ArrayList<EventoPrincipalDTO>();
		listaEventos.add(principalDto);
		listaEventos.add(principalDto2);
		when(eventoService.getEventosPorParticipantePrincipalDTO()).thenReturn(listaEventos);
		
		//act assert
		mockmvc.perform(get("/api/eventos/mis-eventos")
				.contentType(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].id").value(1))
		.andExpect(jsonPath("$[1].id").value(2));
	
	}

}
