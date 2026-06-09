package com.restaurante.controller;

import com.restaurante.model.Producto;
import com.restaurante.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoServices;

    @GetMapping
    public ResponseEntity<List<Producto>> listar() {
        return ResponseEntity.ok(productoServices.listar());
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<Producto>> porCategoria(@PathVariable Integer categoriaId) {
        return ResponseEntity.ok(productoServices.listarPorCategoria(categoriaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(productoServices.obtener(id));
    }

    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
        return ResponseEntity.ok(productoServices.crear(producto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Integer id, @RequestBody Producto producto) {
        return ResponseEntity.ok(productoServices.actualizar(id, producto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        productoServices.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
