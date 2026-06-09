package com.restaurante.controller;

import com.restaurante.model.Mesa;
import com.restaurante.service.MesaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mesas")
@RequiredArgsConstructor
public class MesaController {

    private final MesaService mesaService;

    @GetMapping
    public ResponseEntity<List<Mesa>> listar() {
        return ResponseEntity.ok(mesaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mesa> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(mesaService.obtener(id));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Mesa> actualizarEstado(@PathVariable Integer id, @RequestBody String estado) {
        return ResponseEntity.ok(mesaService.actualizarEstado(id, estado));
    }

    @PostMapping
    public ResponseEntity<Mesa> crear() {
        return ResponseEntity.ok(mesaService.crear());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        mesaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
