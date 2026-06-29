package com.restaurante.controller;

import com.restaurante.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping("/diario")
    public ResponseEntity<Map<String, Object>> diario() {
        return ResponseEntity.ok(reporteService.resumenDiario());
    }

    @GetMapping("/rango")
    public ResponseEntity<Map<String, Object>> rango(
            @RequestParam String desde,
            @RequestParam String hasta) {
        LocalDateTime start = LocalDateTime.parse(desde);
        LocalDateTime end = LocalDateTime.parse(hasta);
        return ResponseEntity.ok(reporteService.resumenRango(start, end));
    }

    @GetMapping("/mesa/{mesaId}")
    public ResponseEntity<Map<String, Object>> porMesa(@PathVariable Integer mesaId) {
        return ResponseEntity.ok(reporteService.resumenPorMesa(mesaId));
    }
}
