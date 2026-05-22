package com.project.community.entidades;

import java.time.LocalDate;


import java.util.HashSet;
import java.util.Set;

import com.project.community.dominio.EventoValidatorException;
import com.project.community.dominio.ParticipanteException;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
//import jakarta.persistence.PrePersist;
//import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper=false, onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor

@Table(name="eventos")
public class Evento extends TimestampEntity {
	
	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Size(max=255)
	private String nombreEvento;
	
	@Size(max=255)	
	private String tipoEvento;
	
	
	private LocalDate fechaEvento;
	private String informacion;
	private String chat;
	private String imagenEvento; 
	@ManyToMany
	@JoinTable(
	    name = "evento_administradores",
	    joinColumns = @JoinColumn(name = "evento_id"),
	    inverseJoinColumns = @JoinColumn(name = "participante_id")
	)
	private Set<Participante> administradores = new HashSet<>();

	@ManyToMany
	@JoinTable(
	    name = "evento_participantes",
	    joinColumns = @JoinColumn(name = "evento_id"),
	    inverseJoinColumns = @JoinColumn(name = "participante_id")
	)
	private Set<Participante> participantesEvento = new HashSet<>();
	private boolean privado;
	private boolean oculto;
	@Max(255)
	@Min(0)
	private int maxNumParticipantes;
	
	 // Domain Driven Design - domain methods
    public boolean addParticipante(Participante participante) {
        if(participantesEvento.add(participante)) {
        	participante.recibirNotificacion(this);
        	return true;
        }
        return false;
    }

    public void removeParticipante(Participante participante) {
        participantesEvento.remove(participante);
        participante.getEventos().remove(this);

       
        administradores.remove(participante);
    }

    public void addAdministrador(Participante participante) {
    	if(!participantesEvento.contains(participante)) {
    	 throw new ParticipanteException("Usuario no encontrado como participante");
    	}
    	administradores.add(participante);
		participante.getEventosAdministrados().add(this);
    
    }

    public void removeAdministrador(Participante participante) {
        administradores.remove(participante);
        participante.getEventosAdministrados().remove(this);
    }
    
    public int countParticipantes() {
    	return this.participantesEvento.size();
    }
   
    public void addCreadorComoAdmin(Participante creador) {
    	if(creador == null) {
    		throw new IllegalArgumentException("Creador de evento nulo");
    	}
    	this.participantesEvento.add(creador);
    	this.administradores.add(creador);
    	
    	creador.getEventos().add(this);
    	creador.getEventosAdministrados().add(this);
    }
    
    public void validarFecha() {
    	if(this.getFechaEvento() != null) {
    		LocalDate hoy = LocalDate.now();
    		if(this.getFechaEvento().isBefore(hoy)) {
    			throw new EventoValidatorException("No puedes asignar una fecha pasada");
    		}
        }
    }
    
    public void actualizarEvento(String nombre, String tipo, LocalDate fecha, String informacion, String chat, boolean privado, boolean oculto, int maxNum) {
    	this.nombreEvento = nombre;
        this.tipoEvento = tipo;
        this.fechaEvento = fecha;
        this.informacion = informacion;
        this.chat = chat;
        this.privado = privado;
        this.oculto = oculto;
        this.maxNumParticipantes = maxNum;
        
        this.validarFecha();
    }
}

