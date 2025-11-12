package com.project.community.entidades;

import java.time.LocalDate;


import java.util.HashSet;
import java.util.Set;
import com.project.community.mapper.TimestampEntity;
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
	@Size(max=20)
	private String nombreEvento;
	
	@Size(max=20)	
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

	
   // @PrePersist
   // protected void onCreate() {
   //     this.createdAt = LocalDateTime.now();
   // }
	//
   // @PreUpdate
   // protected void onUpdate() {
   //     this.updatedAt = LocalDateTime.now();
   // }
	
	 // Domain Driven Design
    public void addParticipante(Participante participante) {
        participantesEvento.add(participante);
        participante.getEventos().add(this);
    }

    public void removeParticipante(Participante participante) {
        participantesEvento.remove(participante);
        participante.getEventos().remove(this);

       
        administradores.remove(participante);
    }

    public boolean addAdministrador(Participante participante) {
    	if(participantesEvento.contains(participante)) {
    		administradores.add(participante);
    		participante.getEventosAdministrados().add(this);	
    		return true;
    	}
    	else return false;
    }

    public void removeAdministrador(Participante participante) {
        administradores.remove(participante);
        participante.getEventosAdministrados().remove(this);
    }
    
   
}

