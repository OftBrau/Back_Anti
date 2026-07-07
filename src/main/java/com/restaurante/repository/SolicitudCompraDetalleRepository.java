package com.restaurante.repository;

import com.restaurante.model.SolicitudCompraDetalle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitudCompraDetalleRepository extends JpaRepository<SolicitudCompraDetalle, Integer> {
    List<SolicitudCompraDetalle> findBySolicitudCompraId(Integer solicitudCompraId);
}
