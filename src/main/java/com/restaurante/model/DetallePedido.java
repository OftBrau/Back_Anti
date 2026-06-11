package com.restaurante.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "DetallePedido")
public class DetallePedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "PedidoId")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "ProductoId")
    private Producto producto;

    private Integer cantidad;

    private Double precio;

    @Builder.Default
    private String estado = "Pendiente";

    private LocalDateTime horaIngreso;
}
