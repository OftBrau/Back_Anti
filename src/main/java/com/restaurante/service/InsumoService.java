package com.restaurante.service;

import com.restaurante.model.Categoria;
import com.restaurante.model.Insumo;
import com.restaurante.model.Proveedor;
import com.restaurante.repository.InsumoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InsumoService {
    private final InsumoRepository repository;
    private final CategoriaService categoriaService;
    private final ProveedorService proveedorService;

    public List<Insumo> listar() { return repository.findAll(); }

    public Insumo obtener(Integer id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Insumo no encontrado"));
    }

    public List<Insumo> listarPorCategoria(Integer categoriaId) {
        return repository.findByCategoriaId(categoriaId);
    }

    public List<Insumo> listarPorTipo(String tipo) {
        return repository.findByTipo(tipo);
    }

    public List<Insumo> listarPorCategoriaYTipo(Integer categoriaId, String tipo) {
        return repository.findByCategoriaIdAndTipo(categoriaId, tipo);
    }

    public List<Insumo> listarPorProveedor(Integer proveedorId) {
        return repository.findByProveedorId(proveedorId);
    }

    public List<Insumo> asignarProveedor(Integer proveedorId, List<Integer> insumoIds) {
        Proveedor proveedor = proveedorService.obtener(proveedorId);
        List<Insumo> actuales = repository.findByProveedorId(proveedorId);
        for (Insumo ins : actuales) {
            if (!insumoIds.contains(ins.getId())) {
                ins.setProveedor(null);
                repository.save(ins);
            }
        }
        for (Integer id : insumoIds) {
            Insumo ins = obtener(id);
            if (ins.getProveedor() != null && ins.getProveedor().getId().equals(proveedorId)) continue;
            ins.setProveedor(proveedor);
            repository.save(ins);
        }
        return repository.findByProveedorId(proveedorId);
    }

    public Insumo crear(Insumo i) {
        if (i.getCategoria() != null && i.getCategoria().getId() != null) {
            Categoria categoria = categoriaService.obtener(i.getCategoria().getId());
            i.setCategoria(categoria);
        }
        if (i.getProveedor() != null && i.getProveedor().getId() != null) {
            Proveedor proveedor = proveedorService.obtener(i.getProveedor().getId());
            i.setProveedor(proveedor);
        }
        if (i.getTipo() == null) i.setTipo("COMPRA");
        return repository.save(i);
    }

    public Insumo actualizar(Integer id, Insumo i) {
        Insumo existente = obtener(id);
        existente.setNombre(i.getNombre());
        existente.setUnidad(i.getUnidad());
        existente.setStockActual(i.getStockActual());
        existente.setStockMinimo(i.getStockMinimo());
        existente.setTipo(i.getTipo() != null ? i.getTipo() : "COMPRA");
        existente.setPrecioCompra(i.getPrecioCompra());
        existente.setImagen(i.getImagen());
        if (i.getCategoria() != null && i.getCategoria().getId() != null) {
            Categoria categoria = categoriaService.obtener(i.getCategoria().getId());
            existente.setCategoria(categoria);
        } else {
            existente.setCategoria(null);
        }
        if (i.getProveedor() != null && i.getProveedor().getId() != null) {
            Proveedor proveedor = proveedorService.obtener(i.getProveedor().getId());
            existente.setProveedor(proveedor);
        } else {
            existente.setProveedor(null);
        }
        return repository.save(existente);
    }

    public Insumo ajustarStock(Integer id, Double cantidad) {
        Insumo existente = obtener(id);
        existente.setStockActual(existente.getStockActual() + cantidad);
        return repository.save(existente);
    }

    public void eliminar(Integer id) { repository.deleteById(id); }
}
