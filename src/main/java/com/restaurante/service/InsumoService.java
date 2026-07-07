package com.restaurante.service;

import com.restaurante.model.Categoria;
import com.restaurante.model.Insumo;
import com.restaurante.repository.InsumoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InsumoService {
    private final InsumoRepository repository;
    private final CategoriaService categoriaService;

    public List<Insumo> listar() { return repository.findAll(); }

    public Insumo obtener(Integer id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Insumo no encontrado"));
    }

    public List<Insumo> listarPorCategoria(Integer categoriaId) {
        return repository.findByCategoriaId(categoriaId);
    }

    public List<Insumo> listarPorTipo(String tipo) {
        return repository.findByTipo(tipo);
    }

    public List<Insumo> listarPorCategoriaYTipo(Integer categoriaId, String tipo) {
        return repository.findByCategoriaIdAndTipo(categoriaId, tipo);
    }

    public Insumo crear(Insumo i) {
        if (i.getCategoria() != null && i.getCategoria().getId() != null) {
            Categoria categoria = categoriaService.obtener(i.getCategoria().getId());
            i.setCategoria(categoria);
        }
        return repository.save(i);
    }

    public Insumo actualizar(Integer id, Insumo i) {
        Insumo existente = obtener(id);
        existente.setNombre(i.getNombre());
        existente.setUnidad(i.getUnidad());
        existente.setStockActual(i.getStockActual());
        existente.setStockMinimo(i.getStockMinimo());
        existente.setTipo(i.getTipo());
        existente.setImagen(i.getImagen());
        if (i.getCategoria() != null && i.getCategoria().getId() != null) {
            Categoria categoria = categoriaService.obtener(i.getCategoria().getId());
            existente.setCategoria(categoria);
        } else {
            existente.setCategoria(null);
        }
        return repository.save(existente);
    }

    public Insumo ajustarStock(Integer id, Double cantidad) {
        Insumo existente = obtener(id);
        existente.setStockActual(existente.getStockActual() + cantidad);
        return repository.save(existente);
    }

    public void eliminar(Integer id) { repository.deleteById(id); }
}
