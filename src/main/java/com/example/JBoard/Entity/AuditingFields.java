package com.example.JBoard.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class AuditingFields {
    // TODO: AuditorAware() 구현해보기

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /*@LastModifiedDate
    private LocalDateTime modifiedAt;*/

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;



}
