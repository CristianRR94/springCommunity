package com.project.community.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.project.community.DTO.UsuarioEntradaDTO;
import com.project.community.DTO.UsuarioSalidaDTO;
import com.project.community.entidades.Usuario;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
	UsuarioSalidaDTO toSalidaDTO(Usuario usuario);
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "password", ignore = true)
	@Mapping(target = "rol", ignore = true)
	Usuario toUsuarioSalida(UsuarioSalidaDTO usuarioSalidaDTOs);
	List<UsuarioSalidaDTO> toSalidaDTOs(List<Usuario> usuarios);
	List<Usuario> toUsuarioSalidas(List<UsuarioSalidaDTO> usuarioSalidaDTOs);
	
	UsuarioEntradaDTO toEntradaDTO(Usuario usuario);
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "rol", ignore = true)
	Usuario toUsuarioEntrada(UsuarioEntradaDTO usuarioEntradaDTO);
	List<UsuarioEntradaDTO> toEntradaDTOs(List<Usuario> usuarios);
	List<Usuario> toUsuarioEntradas(List<UsuarioEntradaDTO> usuarioEntradaDTO);
}
