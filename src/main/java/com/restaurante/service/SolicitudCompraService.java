package com.restaurante.service;

import com.restaurante.model.Insumo;
import com.restaurante.model.Local;
import com.restaurante.model.Proveedor;
import com.restaurante.model.SolicitudCompra;
import com.restaurante.model.SolicitudCompraDetalle;
import com.restaurante.repository.SolicitudCompraDetalleRepository;
import com.restaurante.repository.SolicitudCompraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitudCompraService {
    private final SolicitudCompraRepository repository;
    private final SolicitudCompraDetalleRepository detalleRepository;
    private final ProveedorService proveedorService;
    private final InsumoService insumoService;
    private final LocalService localService;

    public List<SolicitudCompra> listar() { return repository.findAll(); }

    public SolicitudCompra obtener(Integer id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
    }

    @Transactional
    public SolicitudCompra crear(SolicitudCompra s) {
        s.setId(null);
        s.setFecha(LocalDateTime.now());
        s.setEstado("Pendiente");
        s.setEstadoPago("PENDIENTE");
        if (s.getProveedor() != null && s.getProveedor().getId() != null) {
            Proveedor proveedor = proveedorService.obtener(s.getProveedor().getId());
            s.setProveedor(proveedor);
        }
        if (s.getItems() != null) {
            for (SolicitudCompraDetalle item : s.getItems()) {
                item.setId(null);
                item.setSolicitudCompra(s);
                if (item.getInsumo() != null && item.getInsumo().getId() != null) {
                    Insumo insumo = insumoService.obtener(item.getInsumo().getId());
                    item.setInsumo(insumo);
                    item.setPrecioUnitario(insumo.getPrecioCompra());
                }
            }
        }
        return repository.save(s);
    }

    @Transactional
    public SolicitudCompra procesarPago(Integer id, Integer localId, String metodoPago) {
        SolicitudCompra s = obtener(id);

        if (!"PENDIENTE".equals(s.getEstadoPago())) {
            throw new RuntimeException("La solicitud ya fue pagada");
        }

        Local local = localService.obtener(localId);
        s.setLocal(local);
        s.setMetodoPago(metodoPago);
        s.setEstadoPago("PAGADO");
        s.setEstado("Completado");

        double total = 0.0;
        for (SolicitudCompraDetalle item : s.getItems()) {
            double subtotal = item.getCantidad() * (item.getPrecioUnitario() != null ? item.getPrecioUnitario() : 0.0);
            total += subtotal;
        }
        s.setTotal(total);

        return repository.save(s);
    }

    @Transactional
    public SolicitudCompra marcarEntregado(Integer id) {
        SolicitudCompra s = obtener(id);
        s.setEstado("Entregado");
        for (SolicitudCompraDetalle item : s.getItems()) {
            Insumo insumo = item.getInsumo();
            if (insumo != null) {
                insumoService.ajustarStock(insumo.getId(), item.getCantidad());
            }
        }
        return repository.save(s);
    }

    public SolicitudCompra actualizarEstado(Integer id, String estado) {
        SolicitudCompra s = obtener(id);
        s.setEstado(estado);
        return repository.save(s);
    }

    public void eliminar(Integer id) { repository.deleteById(id); }
}
