package com.restaurante.service;

import com.restaurante.model.Categoria;
import com.restaurante.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public List<Categoria> listar() {
        return categoriaRepository.findByEstado(1);
    }

    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    public Categoria obtener(Integer id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    public Categoria crear(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public Categoria actualizar(Integer id, Categoria categoria) {
        Categoria c = obtener(id);
        c.setNombre(categoria.getNombre());
        c.setEstado(categoria.getEstado());
        return categoriaRepository.save(c);
    }

    public void eliminar(Integer id) {
        Categoria c = obtener(id);
        c.setEstado(0);
        categoriaRepository.save(c);
    }

    public Categoria cambiarEstado(Integer id) {
        Categoria c = obtener(id);
        c.setEstado(c.getEstado() == 1 ? 0 : 1);
        return categoriaRepository.save(c);
    }
}
