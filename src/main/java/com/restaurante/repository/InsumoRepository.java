package com.restaurante.repository;

import com.restaurante.model.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InsumoRepository extends JpaRepository<Insumo, Integer> {
    List<Insumo> findByCategoriaId(Integer categoriaId);
    List<Insumo> findByTipo(String tipo);
    List<Insumo> findByCategoriaIdAndTipo(Integer categoriaId, String tipo);
    List<Insumo> findByProveedorId(Integer proveedorId);
}
