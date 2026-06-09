package com.restaurante.repository;

import com.restaurante.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findByEstado(Integer estado);
    List<Producto> findByCategoriaId(Integer categoriaId);
}
