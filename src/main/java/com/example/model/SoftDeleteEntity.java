package com.example.model;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class SoftDeleteEntity extends BaseEntity {

    @Column(name = "deleted_at", nullable = false)
    private boolean deleted = false;
}