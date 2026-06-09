package com.restaurante.service;

import com.restaurante.dto.ComprobanteResponse;
import com.restaurante.model.*;
import com.restaurante.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final MesaService mesaService;

    @Transactional
    public Venta procesarPago(Integer mesaId, String metodoPago, Double montoRecibido) {
        List<Pedido> pedidos = pedidoRepository.findByMesaId(mesaId);
        Mesa mesa = mesaService.obtener(mesaId);

        double total = 0.0;

        Venta venta = Venta.builder()
                .mesa(mesa)
                .fecha(LocalDateTime.now())
                .metodoPago(metodoPago)
                .montoRecibido(montoRecibido)
                .build();
        venta = ventaRepository.save(venta);

        List<DetalleVenta> detalles = new ArrayList<>();

        for (Pedido pedido : pedidos) {
            List<DetallePedido> items = detallePedidoRepository.findByPedidoId(pedido.getId());
            for (DetallePedido item : items) {
                double subtotal = item.getProducto().getPrecio() * item.getCantidad();
                total += subtotal;

                DetalleVenta dv = DetalleVenta.builder()
                        .venta(venta)
                        .producto(item.getProducto())
                        .cantidad(item.getCantidad())
                        .precioUnitario(item.getProducto().getPrecio())
                        .build();
                detalles.add(dv);
            }
            pedido.setEstado("Pagado");
            pedidoRepository.save(pedido);
        }

        if (montoRecibido != null && montoRecibido < total) {
            throw new RuntimeException("Monto recibido insuficiente. Total: " + total + ", recibido: " + montoRecibido);
        }

        detalleVentaRepository.saveAll(detalles);
        venta.setTotal(total);
        if (montoRecibido != null) {
            venta.setVuelto(montoRecibido - total);
        }
        venta = ventaRepository.save(venta);

        mesaService.actualizarEstado(mesaId, "Libre");

        return venta;
    }

    public ComprobanteResponse generarComprobante(Integer ventaId) {
        Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        List<DetalleVenta> detalles = detalleVentaRepository.findByVentaId(ventaId);

        List<ComprobanteResponse.ItemComprobante> items = detalles.stream()
                .map(d -> ComprobanteResponse.ItemComprobante.builder()
                        .producto(d.getProducto().getNombre())
                        .cantidad(d.getCantidad())
                        .precioUnitario(d.getPrecioUnitario())
                        .subtotal(d.getCantidad() * d.getPrecioUnitario())
                        .build())
                .toList();

        return ComprobanteResponse.builder()
                .ventaId(venta.getId())
                .mesaId(venta.getMesa().getId())
                .items(items)
                .total(venta.getTotal())
                .metodoPago(venta.getMetodoPago())
                .montoRecibido(venta.getMontoRecibido())
                .vuelto(venta.getVuelto())
                .fecha(venta.getFecha())
                .build();
    }
}
