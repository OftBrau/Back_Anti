package com.restaurante.repository;

import com.restaurante.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    List<Pedido> findByMesaId(Integer mesaId);
    List<Pedido> findByEstado(String estado);
    List<Pedido> findByTipoAndEstado(String tipo, String estado);
}
