package com.restaurante.controller;

import com.restaurante.dto.ConfirmarPedidoRequest;
import com.restaurante.dto.DisponibilidadResponse;
import com.restaurante.dto.VerificarDisponibilidadResponse;
import com.restaurante.model.DetallePedido;
import com.restaurante.model.Pedido;
import com.restaurante.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<List<Pedido>> listar() {
        return ResponseEntity.ok(pedidoService.listar());
    }

    @GetMapping("/mesa/{mesaId}")
    public ResponseEntity<List<Pedido>> porMesa(@PathVariable Integer mesaId) {
        return ResponseEntity.ok(pedidoService.listarPorMesa(mesaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(pedidoService.obtener(id));
    }

    @PostMapping("/mesa/{mesaId}")
    public ResponseEntity<Pedido> crear(@PathVariable Integer mesaId) {
        return ResponseEntity.ok(pedidoService.crear(mesaId));
    }

    @PostMapping("/{pedidoId}/productos")
    public ResponseEntity<DetallePedido> agregarProducto(
            @PathVariable Integer pedidoId,
            @RequestBody Map<String, Integer> body) {
        Integer productoId = body.get("productoId");
        Integer cantidad = body.getOrDefault("cantidad", 1);
        return ResponseEntity.ok(pedidoService.agregarProducto(pedidoId, productoId, cantidad));
    }

    @DeleteMapping("/detalle/{detalleId}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Integer detalleId) {
        pedidoService.eliminarProducto(detalleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{pedidoId}/detalle")
    public ResponseEntity<List<DetallePedido>> detalle(@PathVariable Integer pedidoId) {
        return ResponseEntity.ok(pedidoService.detallePorPedido(pedidoId));
    }

    @PutMapping("/{pedidoId}/cerrar")
    public ResponseEntity<Void> cerrar(@PathVariable Integer pedidoId) {
        pedidoService.cerrarPedido(pedidoId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{pedidoId}/verificar")
    public ResponseEntity<DisponibilidadResponse> verificar(@PathVariable Integer pedidoId) {
        return ResponseEntity.ok(pedidoService.verificarDisponibilidad(pedidoId));
    }

    @PutMapping("/{pedidoId}/confirmar")
    public ResponseEntity<Pedido> confirmar(@PathVariable Integer pedidoId) {
        return ResponseEntity.ok(pedidoService.confirmarPedido(pedidoId));
    }

    @PutMapping("/detalle/{detalleId}")
    public ResponseEntity<DetallePedido> modificarDetalle(
            @PathVariable Integer detalleId,
            @RequestBody Map<String, Integer> body) {
        Integer cantidad = body.get("cantidad");
        return ResponseEntity.ok(pedidoService.modificarDetalle(detalleId, cantidad));
    }

    @PutMapping("/{pedidoId}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Integer pedidoId) {
        pedidoService.cancelarPedido(pedidoId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{pedidoId}/enviar-cocina")
    public ResponseEntity<Void> enviarCocina(@PathVariable Integer pedidoId) {
        pedidoService.enviarACocina(pedidoId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/verificar-disponibilidad")
    public ResponseEntity<VerificarDisponibilidadResponse> verificarDisponibilidad(
            @RequestBody Map<String, List<ConfirmarPedidoRequest.ItemRequest>> body) {
        return ResponseEntity.ok(pedidoService.verificarDisponibilidadItems(body.get("items")));
    }

    @PostMapping("/confirmar")
    public ResponseEntity<Pedido> confirmar(@RequestBody ConfirmarPedidoRequest request) {
        return ResponseEntity.ok(pedidoService.confirmarPedidoCompleto(request.getMesaId(), request.getItems()));
    }
}
