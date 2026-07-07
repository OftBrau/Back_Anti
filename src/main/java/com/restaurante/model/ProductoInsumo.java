package com.restaurante.model;

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
@Table(name = "ProductoInsumo")
public class ProductoInsumo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ProductoId")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "InsumoId")
    private Insumo insumo;

    private Double cantidad;
}
