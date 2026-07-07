package com.restaurante.controller;

import com.restaurante.model.ProductoInsumo;
import com.restaurante.service.ProductoInsumoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/productos/{productoId}/insumos")
@RequiredArgsConstructor
public class ProductoInsumoController {
    private final ProductoInsumoService service;

    @GetMapping
    public ResponseEntity<List<ProductoInsumo>> listar(@PathVariable Integer productoId) {
        return ResponseEntity.ok(service.listarPorProducto(productoId));
    }

    @PostMapping
    public ResponseEntity<List<ProductoInsumo>> guardar(@PathVariable Integer productoId, @RequestBody List<ProductoInsumo> items) {
        return ResponseEntity.ok(service.guardarReceta(productoId, items));
    }

    @GetMapping("/verificar-stock")
    public ResponseEntity<Map<String, Object>> verificarStock(@PathVariable Integer productoId, @RequestParam Integer cantidad) {
        boolean disponible = service.verificarStock(productoId, cantidad);
        return ResponseEntity.ok(Map.of("disponible", disponible));
    }
}
