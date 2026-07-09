package com.restaurante.controller;

import com.restaurante.model.Insumo;
import com.restaurante.service.InsumoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<Insumo>> porCategoria(@PathVariable Integer categoriaId) {
        return ResponseEntity.ok(service.listarPorCategoria(categoriaId));
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Insumo>> porTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(service.listarPorTipo(tipo));
    }

    @GetMapping("/categoria/{categoriaId}/tipo/{tipo}")
    public ResponseEntity<List<Insumo>> porCategoriaYTipo(@PathVariable Integer categoriaId, @PathVariable String tipo) {
        return ResponseEntity.ok(service.listarPorCategoriaYTipo(categoriaId, tipo));
    }

    @GetMapping("/proveedor/{proveedorId}")
    public ResponseEntity<List<Insumo>> porProveedor(@PathVariable Integer proveedorId) {
        return ResponseEntity.ok(service.listarPorProveedor(proveedorId));
    }

    @PostMapping("/asignar-proveedor")
    public ResponseEntity<List<Insumo>> asignarProveedor(@RequestBody Map<String, Object> body) {
        Integer proveedorId = Integer.parseInt(body.get("proveedorId").toString());
        @SuppressWarnings("unchecked")
        List<Integer> insumoIds = ((List<?>) body.get("insumoIds")).stream()
                .map(o -> Integer.parseInt(o.toString())).toList();
        return ResponseEntity.ok(service.asignarProveedor(proveedorId, insumoIds));
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
