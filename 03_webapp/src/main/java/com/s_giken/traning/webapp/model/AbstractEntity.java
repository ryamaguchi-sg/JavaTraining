package com.s_giken.traning.webapp.model;

import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;

@MappedSuperclass
public abstract class AbstractEntity implements Persistable<Integer> {
    @Transient
    private boolean isNew = true;

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PrePersist
    @PostLoad
    public void markNotNew() {
        this.isNew = false;
    }
}
