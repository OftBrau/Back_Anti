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
@Table(name = "AsignacionCategoriasCocinero")
public class AsignacionCategoriasCocinero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String cocineroNombre;

    @ManyToOne
    @JoinColumn(name = "CategoriaId")
    private Categoria categoria;
}
