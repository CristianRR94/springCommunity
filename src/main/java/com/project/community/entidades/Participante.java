package com.project.community.entidades;

//import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


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
	
	@ManyToMany
	@JoinTable(
			name="participante_amigos",
			joinColumns = @JoinColumn(name="participante_id"),
			inverseJoinColumns = @JoinColumn(name= "amigo_id"))
	@Builder.Default
	private Set<Participante> amigos = new HashSet<>();
	
	@OneToOne
	@JoinColumn(name = "usuario_id")
	@JsonIgnoreProperties({"password", "email"})
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Usuario usuario;
	
	public void cambiarNombreParticipante(String nombre) {
		if(nombre == null || nombre.isBlank()) {
			throw new IllegalArgumentException("Nombre vacío");
		}
		if(nombre.length()<6 || nombre.length()>20) {
			throw new IllegalArgumentException("El nombre debe tener entre 6 y 20 letras");
		}
		this.nombreParticipante = nombre;
	}
	
	public Long obtenerIdUsuario() {
		if(this.usuario ==null) {
			throw new IllegalStateException("El participante no tiene usuario asociado");
		}
		return this.usuario.getId();
	}

}
