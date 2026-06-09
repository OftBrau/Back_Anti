package com.restaurante.repository;

import com.restaurante.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Integer> {
    List<Venta> findByFechaBetween(LocalDateTime start, LocalDateTime end);
    List<Venta> findByMesaId(Integer mesaId);
}
