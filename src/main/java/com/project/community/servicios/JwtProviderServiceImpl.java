package com.project.community.servicios;


import java.util.Date;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.project.community.entidades.Usuario;
import com.project.community.enums.ClaimJwt;
import com.project.community.enums.TipoToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtProviderServiceImpl implements JwtProviderService{

	@Value("${jwt.secret}")
	private String secretKey;
	@Value("${jwt.expiration}")
	private Long jwtExpiration;
	@Value("${jwt.refresh.expiration}")
	private Long refreshExpiration;
	
	private Claims extraerInfo(final String token) {
		return Jwts.parser()
		.verifyWith(getSigninKey())
		.build()
		.parseSignedClaims(token)
		.getPayload();
	}
		
	@Override
	public String extractUsername(final String token) {
		return extraerInfo(token).getSubject();
	}
	
	@Override
	public String generateToken(final Usuario usuario) {

		return buildToken(usuario, jwtExpiration, TipoToken.ACCESS.getValue());
	}

	@Override
	public String generateRefreshToken(final Usuario usuario) {

		return buildToken(usuario, refreshExpiration, TipoToken.REFRESH.getValue());
	}

	
	private String buildToken(final Usuario usuario, final Long expiration, final String tipo) {
		List<String> roles = usuario.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.toList();
		return Jwts.builder()
				.claims(Map.of(
						ClaimJwt.USUARIO_ID.getValue(), usuario.getId(),
						ClaimJwt.NOMBRE.getValue(), usuario.getNombre(),
						ClaimJwt.TIPO_USO.getValue(), tipo,
						ClaimJwt.ROLES.getValue(), roles)
						)
				.id(UUID.randomUUID().toString())
				.subject(usuario.getNombre())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(getSigninKey())
				.compact();
	}
	
	private SecretKey getSigninKey() {
		
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	@Override
	public boolean isTokenValid(final String token, final UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	private boolean isTokenExpired(final String token) {
		return extractExpiration(token).before(new Date());
	}
	
	@Override
	public Date extractExpiration(final String token) {
		return extraerInfo(token).getExpiration();
	}

	@Override
	public String extractType(String token) {
		return extraerInfo(token).get(ClaimJwt.TIPO_USO.getValue(), String.class);
	}
	
	@Override
	public Long extractId(final String token) {
		return extraerInfo(token).get(ClaimJwt.USUARIO_ID.getValue(), Long.class);
	}

}
