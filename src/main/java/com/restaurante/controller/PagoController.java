package com.restaurante.controller;

import com.restaurante.dto.ComprobanteResponse;
import com.restaurante.dto.PagoRequest;
import com.restaurante.model.Venta;
import com.restaurante.service.PagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    @PostMapping("/mesa/{mesaId}")
    public ResponseEntity<ComprobanteResponse> pagar(
            @PathVariable Integer mesaId,
            @RequestBody PagoRequest request) {
        Venta venta = pagoService.procesarPago(mesaId, request.getMetodoPago(), request.getMontoRecibido());
        return ResponseEntity.ok(pagoService.generarComprobante(venta.getId()));
    }

    @GetMapping("/{ventaId}/comprobante")
    public ResponseEntity<ComprobanteResponse> comprobante(@PathVariable Integer ventaId) {
        return ResponseEntity.ok(pagoService.generarComprobante(ventaId));
    }
}
