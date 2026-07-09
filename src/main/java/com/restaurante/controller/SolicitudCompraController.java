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

    @PostMapping("/{id}/entregar")
    public ResponseEntity<SolicitudCompra> entregar(@PathVariable Integer id) {
        return ResponseEntity.ok(service.marcarEntregado(id));
    }

    @PostMapping("/{id}/pagar")
    public ResponseEntity<SolicitudCompra> pagar(@PathVariable Integer id, @RequestBody Map<String, Object> body) {
        Integer localId = Integer.parseInt(body.get("localId").toString());
        String metodoPago = body.get("metodoPago").toString();
        return ResponseEntity.ok(service.procesarPago(id, localId, metodoPago));
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
