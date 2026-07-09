package com.restaurante.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @Builder.Default
    private String estadoPago = "PENDIENTE";
    private String metodoPago;
    private Double total;

    @ManyToOne
    @JoinColumn(name = "ProveedorId")
    private Proveedor proveedor;

    @ManyToOne
    @JoinColumn(name = "LocalId")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Local local;

    @OneToMany(mappedBy = "solicitudCompra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SolicitudCompraDetalle> items;
}
