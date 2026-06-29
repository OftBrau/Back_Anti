package com.restaurante.controller;

import com.restaurante.model.SolicitudCompra;
import com.restaurante.service.SolicitudCompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/solicitudes-compra")
@RequiredArgsConstructor
public class SolicitudCompraController {
    private final SolicitudCompraService service;

    @GetMapping
    public ResponseEntity<List<SolicitudCompra>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudCompra> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(service.obtener(id));
    }

    @PostMapping
    public ResponseEntity<SolicitudCompra> crear(@RequestBody SolicitudCompra s) {
        return ResponseEntity.ok(service.crear(s));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<SolicitudCompra> cambiarEstado(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(service.actualizarEstado(id, body.get("estado")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
