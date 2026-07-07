package com.restaurante.repository;

import com.restaurante.model.ProductoInsumo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoInsumoRepository extends JpaRepository<ProductoInsumo, Integer> {
    List<ProductoInsumo> findByProductoId(Integer productoId);
    void deleteByProductoId(Integer productoId);
}
