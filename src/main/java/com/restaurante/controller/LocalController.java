package com.restaurante.controller;

import com.restaurante.model.Local;
import com.restaurante.service.LocalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locales")
@RequiredArgsConstructor
public class LocalController {
    private final LocalService service;

    @GetMapping
    public ResponseEntity<List<Local>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Local> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(service.obtener(id));
    }

    @PostMapping
    public ResponseEntity<Local> crear(@RequestBody Local l) {
        return ResponseEntity.ok(service.crear(l));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Local> actualizar(@PathVariable Integer id, @RequestBody Local l) {
        return ResponseEntity.ok(service.actualizar(id, l));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
