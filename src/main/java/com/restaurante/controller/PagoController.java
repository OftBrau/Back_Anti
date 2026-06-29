package com.restaurante.controller;

import com.restaurante.dto.ComprobanteResponse;
import com.restaurante.dto.PagoRequest;
import com.restaurante.model.Venta;
import com.restaurante.service.PagoService;
import com.restaurante.service.PdfService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;
    private final PdfService pdfService;

    @PostMapping("/mesa/{mesaId}")
    public ResponseEntity<ComprobanteResponse> pagar(
            @PathVariable Integer mesaId,
            @RequestBody PagoRequest request) {
        Venta venta = pagoService.procesarPago(mesaId, request.getMetodoPago(), request.getMontoRecibido(), request.getComprobante());
        return ResponseEntity.ok(pagoService.generarComprobante(venta.getId()));
    }

    @PostMapping("/pedido/{pedidoId}")
    public ResponseEntity<ComprobanteResponse> pagarPedido(
            @PathVariable Integer pedidoId,
            @RequestBody PagoRequest request) {
        Venta venta = pagoService.procesarPagoPedido(pedidoId, request.getMetodoPago(), request.getMontoRecibido(), request.getComprobante());
        return ResponseEntity.ok(pagoService.generarComprobante(venta.getId()));
    }

    @GetMapping("/{ventaId}/comprobante")
    public ResponseEntity<ComprobanteResponse> comprobante(@PathVariable Integer ventaId) {
        return ResponseEntity.ok(pagoService.generarComprobante(ventaId));
    }

    @GetMapping("/{ventaId}/pdf")
    public ResponseEntity<byte[]> pdf(@PathVariable Integer ventaId, @RequestParam(defaultValue = "oficial") String tipo) {
        ComprobanteResponse comp = pagoService.generarComprobante(ventaId);
        byte[] pdf = pdfService.generarComprobante(comp, tipo);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline().filename("comprobante-" + ventaId + ".pdf").build());
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }

    @GetMapping("/historial")
    public ResponseEntity<List<Venta>> historial() {
        return ResponseEntity.ok(pagoService.historial());
    }
}
