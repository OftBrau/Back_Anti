package com.restaurante.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "Insumos")
public class Insumo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private String unidad;
    @Builder.Default
    private Double stockActual = 0.0;
    @Builder.Default
    private Double stockMinimo = 0.0;
    private String tipo;

    @Builder.Default
    private Double precioCompra = 0.0;

    @ManyToOne
    @JoinColumn(name = "CategoriaId")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "ProveedorId")
    @JsonIgnoreProperties({"categoria"})
    private Proveedor proveedor;

    @Column(length = 1000)
    private String imagen;
}
