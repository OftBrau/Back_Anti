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
public class ConfirmarPedidoRequest {
    private Integer mesaId;
    private List<ItemRequest> items;
    private String tipo;
    private String cliente;
    private String direccion;
    private String telefono;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ItemRequest {
        private Integer productoId;
        private Integer cantidad;
    }
}
