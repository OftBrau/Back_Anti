package com.restaurante.service;

import com.restaurante.model.SolicitudCompra;
import com.restaurante.repository.SolicitudCompraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitudCompraService {
    private final SolicitudCompraRepository repository;

    public List<SolicitudCompra> listar() { return repository.findAll(); }

    public SolicitudCompra obtener(Integer id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
    }

    public SolicitudCompra crear(SolicitudCompra s) {
        s.setFecha(LocalDateTime.now());
        s.setEstado("Pendiente");
        return repository.save(s);
    }

    public SolicitudCompra actualizarEstado(Integer id, String estado) {
        SolicitudCompra s = obtener(id);
        s.setEstado(estado);
        return repository.save(s);
    }

    public void eliminar(Integer id) { repository.deleteById(id); }
}
