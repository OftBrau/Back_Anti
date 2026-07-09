package com.restaurante.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "SolicitudCompraDetalle")
public class SolicitudCompraDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "SolicitudCompraId")
    @JsonIgnore
    private SolicitudCompra solicitudCompra;

    @ManyToOne
    @JoinColumn(name = "InsumoId")
    private Insumo insumo;

    private Double cantidad;

    private Double precioUnitario;
}
