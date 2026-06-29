package com.restaurante.repository;

import com.restaurante.model.SolicitudCompra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitudCompraRepository extends JpaRepository<SolicitudCompra, Integer> {
}
