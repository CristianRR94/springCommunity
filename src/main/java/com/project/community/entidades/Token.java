package com.project.community.entidades;

import com.project.community.enums.TipoToken;


import jakarta.persistence.Column;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tokens")
public class Token {
	
	public enum TokenType{
		BEARER,
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true, length = 1000)
	private String token;
	
	@Builder.Default
	@Enumerated(EnumType.STRING)
	private TokenType tokenType = TokenType.BEARER;
	
	private boolean revoked;
	
	private boolean expired;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_uso")
	private TipoToken tipoUso;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_id")
	@ToString.Exclude
	private Usuario usuario;
	
	
}
