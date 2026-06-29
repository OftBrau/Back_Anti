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
@Table(name = "Productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;

    private Double precio;

    @ManyToOne
    @JoinColumn(name = "CategoriaId")
    private Categoria categoria;

    @Builder.Default
    private Integer estado = 1;

    @Column(length = 1000)
    private String imagen;
}
