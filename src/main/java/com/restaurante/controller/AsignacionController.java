package com.restaurante.controller;

import com.restaurante.model.AsignacionCategoriasCocinero;
import com.restaurante.service.AsignacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asignaciones")
@RequiredArgsConstructor
public class AsignacionController {

    private final AsignacionService asignacionService;

    @GetMapping
    public ResponseEntity<List<AsignacionCategoriasCocinero>> listar() {
        return ResponseEntity.ok(asignacionService.listar());
    }

    @GetMapping("/cocinero/{nombre}")
    public ResponseEntity<List<AsignacionCategoriasCocinero>> porCocinero(@PathVariable String nombre) {
        return ResponseEntity.ok(asignacionService.porCocinero(nombre));
    }

    @PostMapping
    public ResponseEntity<AsignacionCategoriasCocinero> crear(@RequestBody AsignacionCategoriasCocinero a) {
        return ResponseEntity.ok(asignacionService.crear(a));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        asignacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
