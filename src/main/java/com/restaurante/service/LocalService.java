package com.restaurante.service;

import com.restaurante.model.Local;
import com.restaurante.repository.LocalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocalService {
    private final LocalRepository repository;

    public List<Local> listar() { return repository.findAll(); }

    public Local obtener(Integer id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Local no encontrado"));
    }

    public Local crear(Local l) { return repository.save(l); }

    public Local actualizar(Integer id, Local l) {
        Local existente = obtener(id);
        existente.setNombre(l.getNombre());
        existente.setDireccion(l.getDireccion());
        existente.setTelefono(l.getTelefono());
        return repository.save(existente);
    }

    public void eliminar(Integer id) { repository.deleteById(id); }
}
