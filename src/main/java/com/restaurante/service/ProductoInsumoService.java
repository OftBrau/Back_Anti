package com.restaurante.service;

import com.restaurante.model.Insumo;
import com.restaurante.model.Producto;
import com.restaurante.model.ProductoInsumo;
import com.restaurante.repository.ProductoInsumoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoInsumoService {
    private final ProductoInsumoRepository repository;
    private final ProductoService productoService;
    private final InsumoService insumoService;

    public List<ProductoInsumo> listarPorProducto(Integer productoId) {
        return repository.findByProductoId(productoId);
    }

    @Transactional
    public List<ProductoInsumo> guardarReceta(Integer productoId, List<ProductoInsumo> items) {
        Producto producto = productoService.obtener(productoId);
        repository.deleteByProductoId(productoId);
        for (ProductoInsumo item : items) {
            item.setId(null);
            item.setProducto(producto);
            if (item.getInsumo() != null && item.getInsumo().getId() != null) {
                Insumo insumo = insumoService.obtener(item.getInsumo().getId());
                item.setInsumo(insumo);
            }
        }
        return repository.saveAll(items);
    }

    @Transactional
    public void descontarInsumos(Integer productoId, Integer cantidad) {
        List<ProductoInsumo> receta = repository.findByProductoId(productoId);
        for (ProductoInsumo item : receta) {
            double totalConsumir = item.getCantidad() * cantidad;
            insumoService.ajustarStock(item.getInsumo().getId(), -totalConsumir);
        }
    }

    @Transactional
    public void descontarInsumosPorDetalles(List<Integer> productoIds, List<Integer> cantidades) {
        for (int i = 0; i < productoIds.size(); i++) {
            descontarInsumos(productoIds.get(i), cantidades.get(i));
        }
    }

    public boolean verificarStock(Integer productoId, Integer cantidad) {
        List<ProductoInsumo> receta = repository.findByProductoId(productoId);
        for (ProductoInsumo item : receta) {
            double necesario = item.getCantidad() * cantidad;
            if (item.getInsumo().getStockActual() < necesario) {
                return false;
            }
        }
        return true;
    }
}
