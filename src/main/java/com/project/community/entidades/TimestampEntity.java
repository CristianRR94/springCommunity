package com.project.community.entidades;

import java.time.LocalDateTime;


import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

import lombok.Data;

//Class to add times to entities


@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class TimestampEntity {
	  @CreatedDate
	    @Column(name = "created_at", nullable = false, updatable = false)
	    private LocalDateTime createdAt;

	  @LastModifiedDate
	    @Column(name = "updated_at")
	    private LocalDateTime updatedAt;
}
