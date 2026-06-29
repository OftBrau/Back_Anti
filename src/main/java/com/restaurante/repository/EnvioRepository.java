package com.restaurante.repository;

import com.restaurante.model.Envio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EnvioRepository extends JpaRepository<Envio, Integer> {
    List<Envio> findByEstadoOrderByFechaCreacionAsc(String estado);
    List<Envio> findAllByOrderByFechaCreacionDesc();
    List<Envio> findByEstadoAndFechaEntregaAfterOrderByFechaEntregaDesc(String estado, LocalDateTime inicio);
}
