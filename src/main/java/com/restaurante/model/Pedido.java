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
@Table(name = "Pedidos")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "MesaId")
    private Mesa mesa;

    private LocalDateTime fecha;

    @Builder.Default
    private String estado = "Pendiente";

    @Builder.Default
    private String tipo = "LOCAL";

    private String cliente;
    private String direccion;
    private String telefono;
}
