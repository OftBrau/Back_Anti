package com.restaurante.repository;

import com.restaurante.model.AsignacionCategoriasCocinero;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AsignacionCategoriasCocineroRepository extends JpaRepository<AsignacionCategoriasCocinero, Integer> {
    List<AsignacionCategoriasCocinero> findByCocineroNombre(String cocineroNombre);
}
