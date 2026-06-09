package com.restaurante.service;

import com.restaurante.model.AsignacionCategoriasCocinero;
import com.restaurante.repository.AsignacionCategoriasCocineroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AsignacionService {

    private final AsignacionCategoriasCocineroRepository asignacionRepository;

    public List<AsignacionCategoriasCocinero> listar() {
        return asignacionRepository.findAll();
    }

    public List<AsignacionCategoriasCocinero> porCocinero(String nombre) {
        return asignacionRepository.findByCocineroNombre(nombre);
    }

    public AsignacionCategoriasCocinero crear(AsignacionCategoriasCocinero asignacion) {
        return asignacionRepository.save(asignacion);
    }

    public void eliminar(Integer id) {
        asignacionRepository.deleteById(id);
    }
}
