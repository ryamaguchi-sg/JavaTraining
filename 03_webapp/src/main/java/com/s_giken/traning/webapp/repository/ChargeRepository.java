package com.s_giken.traning.webapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.s_giken.traning.webapp.model.Charge;

public interface ChargeRepository extends JpaRepository<Charge, Integer> {
    List<Charge> findByNameLike(String name);
}
