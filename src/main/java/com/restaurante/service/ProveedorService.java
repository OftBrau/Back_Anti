package com.restaurante.service;

import com.restaurante.model.Categoria;
import com.restaurante.model.Proveedor;
import com.restaurante.repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProveedorService {
    private final ProveedorRepository repository;
    private final CategoriaService categoriaService;

    public List<Proveedor> listar() { return repository.findAll(); }

    public Proveedor obtener(Integer id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
    }

    public Proveedor crear(Proveedor p) {
        if (p.getCategoria() != null && p.getCategoria().getId() != null) {
            Categoria categoria = categoriaService.obtener(p.getCategoria().getId());
            p.setCategoria(categoria);
        }
        return repository.save(p);
    }

    public Proveedor actualizar(Integer id, Proveedor p) {
        Proveedor existente = obtener(id);
        existente.setNombre(p.getNombre());
        existente.setContacto(p.getContacto());
        existente.setTelefono(p.getTelefono());
        existente.setDireccion(p.getDireccion());
        if (p.getCategoria() != null && p.getCategoria().getId() != null) {
            Categoria categoria = categoriaService.obtener(p.getCategoria().getId());
            existente.setCategoria(categoria);
        } else {
            existente.setCategoria(null);
        }
        return repository.save(existente);
    }

    public void eliminar(Integer id) { repository.deleteById(id); }
}
