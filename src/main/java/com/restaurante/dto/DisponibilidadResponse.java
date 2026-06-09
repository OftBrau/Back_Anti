package com.restaurante.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisponibilidadResponse {
    private Integer pedidoId;
    private String estadoPedido;
    private List<ItemDisponibilidad> items;
    private boolean todosDisponibles;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ItemDisponibilidad {
        private Integer detalleId;
        private String productoNombre;
        private Integer cantidad;
        private boolean disponible;
    }
}
