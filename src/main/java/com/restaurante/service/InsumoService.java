package com.restaurante.service;

import com.restaurante.model.Insumo;
import com.restaurante.repository.InsumoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InsumoService {
    private final InsumoRepository repository;

    public List<Insumo> listar() { return repository.findAll(); }

    public Insumo obtener(Integer id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Insumo no encontrado"));
    }

    public Insumo crear(Insumo i) { return repository.save(i); }

    public Insumo actualizar(Integer id, Insumo i) {
        Insumo existente = obtener(id);
        existente.setNombre(i.getNombre());
        existente.setUnidad(i.getUnidad());
        existente.setStockActual(i.getStockActual());
        existente.setStockMinimo(i.getStockMinimo());
        return repository.save(existente);
    }

    public void eliminar(Integer id) { repository.deleteById(id); }
}
