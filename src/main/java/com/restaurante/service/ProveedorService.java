package com.restaurante.service;

import com.restaurante.model.Proveedor;
import com.restaurante.repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProveedorService {
    private final ProveedorRepository repository;

    public List<Proveedor> listar() { return repository.findAll(); }

    public Proveedor obtener(Integer id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
    }

    public Proveedor crear(Proveedor p) { return repository.save(p); }

    public Proveedor actualizar(Integer id, Proveedor p) {
        Proveedor existente = obtener(id);
        existente.setNombre(p.getNombre());
        existente.setContacto(p.getContacto());
        existente.setTelefono(p.getTelefono());
        existente.setDireccion(p.getDireccion());
        return repository.save(existente);
    }

    public void eliminar(Integer id) { repository.deleteById(id); }
}
