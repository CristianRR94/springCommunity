package com.project.community.repositorios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.community.entidades.Token;

public interface TokenRepository extends JpaRepository<Token, Long>{

	List<Token> findAllValidIsFalseOrRevokedIsFalseByUsuarioId(Long id);
	List<Token> findAllByUsuarioIdAndExpiredIsFalseAndRevokedIsFalse(Long id);
	Optional<Token> findByToken(String token);
}
