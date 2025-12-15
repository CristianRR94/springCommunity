package com.project.community.servicios;

import org.springframework.http.ResponseEntity;

import com.nimbusds.oauth2.sdk.TokenResponse;

public interface AuthService {
	public ResponseEntity<TokenResponse> register();
	public ResponseEntity<TokenResponse> authenticate();
	public ResponseEntity<TokenResponse> refresh();
	
}
