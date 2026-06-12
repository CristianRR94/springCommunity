package com.project.community.servicios;

import com.project.community.controladores.TokenResponse;
import com.project.community.entidades.Usuario;

public interface LoginService {

	TokenResponse login(Usuario usuario);

	void logout(String token);

}
