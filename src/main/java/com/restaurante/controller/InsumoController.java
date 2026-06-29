package com.restaurante.controller;

import com.restaurante.model.Insumo;
import com.restaurante.service.InsumoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/insumos")
@RequiredArgsConstructor
public class InsumoController {
    private final InsumoService service;

    @GetMapping
    public ResponseEntity<List<Insumo>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Insumo> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(service.obtener(id));
    }

    @PostMapping
    public ResponseEntity<Insumo> crear(@RequestBody Insumo i) {
        return ResponseEntity.ok(service.crear(i));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Insumo> actualizar(@PathVariable Integer id, @RequestBody Insumo i) {
        return ResponseEntity.ok(service.actualizar(id, i));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
