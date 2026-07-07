package com.restaurante.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "SolicitudesCompra")
public class SolicitudCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime fecha;
    @Builder.Default
    private String estado = "Pendiente";
    @ManyToOne
    @JoinColumn(name = "ProveedorId")
    private Proveedor proveedor;

    @OneToMany(mappedBy = "solicitudCompra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SolicitudCompraDetalle> items;
}
