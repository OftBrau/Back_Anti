package com.restaurante.controller;

import com.restaurante.model.Envio;
import com.restaurante.service.EnvioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/envios")
@RequiredArgsConstructor
public class EnvioController {

    private final EnvioService envioService;

    @GetMapping
    public ResponseEntity<List<Envio>> listar() {
        return ResponseEntity.ok(envioService.listar());
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<Envio>> pendientes() {
        return ResponseEntity.ok(envioService.pendientes());
    }

    @GetMapping("/entregados/hoy")
    public ResponseEntity<List<Envio>> entregadosHoy() {
        return ResponseEntity.ok(envioService.entregadosHoy());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Envio> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(envioService.obtener(id));
    }

    @PostMapping
    public ResponseEntity<Envio> crear(@RequestBody Envio envio) {
        return ResponseEntity.ok(envioService.crear(envio));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Envio> actualizar(@PathVariable Integer id, @RequestBody Envio envio) {
        return ResponseEntity.ok(envioService.actualizar(id, envio));
    }

    @PatchMapping("/{id}/entregado")
    public ResponseEntity<Envio> marcarEntregado(@PathVariable Integer id) {
        return ResponseEntity.ok(envioService.marcarEntregado(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        envioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
