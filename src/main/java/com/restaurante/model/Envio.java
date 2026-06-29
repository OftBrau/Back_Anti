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
@Table(name = "Envios")
public class Envio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String cliente;
    private String direccion;
    private String telefono;
    private String detalle;

    private Double total;

    @Builder.Default
    private String estado = "Pendiente";

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaEntrega;

    private Double lat;

    private Double lng;
}
