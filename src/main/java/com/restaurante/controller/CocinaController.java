package com.restaurante.controller;

import com.restaurante.model.DetallePedido;
import com.restaurante.service.CocinaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cocina")
@RequiredArgsConstructor
public class CocinaController {

    private final CocinaService cocinaService;

    @GetMapping("/pendientes")
    public ResponseEntity<List<DetallePedido>> pendientes() {
        return ResponseEntity.ok(cocinaService.pendientes());
    }

    @GetMapping("/pendientes/{cocinero}")
    public ResponseEntity<List<DetallePedido>> pendientesPorCocinero(@PathVariable String cocinero) {
        return ResponseEntity.ok(cocinaService.pendientes());
    }

    @PutMapping("/{detalleId}/listo")
    public ResponseEntity<Void> marcarListo(@PathVariable Integer detalleId) {
        cocinaService.marcarListo(detalleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/listos")
    public ResponseEntity<List<DetallePedido>> listos() {
        return ResponseEntity.ok(cocinaService.listos());
    }

    @GetMapping("/listos/dia")
    public ResponseEntity<List<DetallePedido>> listosDelDia() {
        return ResponseEntity.ok(cocinaService.listosDelDia());
    }
}
