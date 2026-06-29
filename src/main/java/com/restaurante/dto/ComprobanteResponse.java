package com.restaurante.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComprobanteResponse {
    private Integer ventaId;
    private Integer mesaId;
    private List<ItemComprobante> items;
    private Double total;
    private String metodoPago;
    private Double montoRecibido;
    private Double vuelto;
    private String tipo;
    private String comprobante;
    private LocalDateTime fecha;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ItemComprobante {
        private String producto;
        private Integer cantidad;
        private Double precioUnitario;
        private Double subtotal;
    }
}
