package com.restaurante.repository;

import com.restaurante.model.Local;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalRepository extends JpaRepository<Local, Integer> {
}
