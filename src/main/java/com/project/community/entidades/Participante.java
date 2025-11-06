package com.project.community.entidades;

//import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.community.mapper.TimestampEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@EqualsAndHashCode(callSuper=false)
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "participantes")

public class Participante extends TimestampEntity {
	
	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	private String nombreParticipante;
	
	@JsonBackReference("participantesEv")
	@ManyToMany(mappedBy = "participantesEvento")
	@Builder.Default
	private Set<Evento> eventos = new HashSet<>();
	
	@JsonBackReference("administradoresEv")
	@ManyToMany(mappedBy = "administradores")
	@Builder.Default
	private Set<Evento> eventosAdministrados = new HashSet<>();
	
	//private LocalDateTime created_at;
	//private LocalDateTime updated_at;
	
	@OneToOne
	@JoinColumn(name = "usuario_id")
	@JsonIgnoreProperties({"password", "email"})
	private Usuario usuario;
	

}
