package com.restaurante.service;

import com.restaurante.model.Categoria;
import com.restaurante.model.Producto;
import com.restaurante.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaService categoriaService;

    public List<Producto> listar() {
        return productoRepository.findByEstado(1);
    }

    public List<Producto> listarPorCategoria(Integer categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId);
    }

    public Producto obtener(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    public Producto crear(Producto producto) {
        if (producto.getCategoria() == null || producto.getCategoria().getId() == null)
            throw new RuntimeException("La categoría es obligatoria");
        Categoria categoria = categoriaService.obtener(producto.getCategoria().getId());
        producto.setCategoria(categoria);
        return productoRepository.save(producto);
    }

    public Producto actualizar(Integer id, Producto producto) {
        Producto p = obtener(id);
        p.setNombre(producto.getNombre());
        p.setPrecio(producto.getPrecio());
        if (producto.getCategoria() != null) {
            p.setCategoria(categoriaService.obtener(producto.getCategoria().getId()));
        }
        p.setEstado(producto.getEstado());
        return productoRepository.save(p);
    }

    public void eliminar(Integer id) {
        Producto p = obtener(id);
        p.setEstado(0);
        productoRepository.save(p);
    }
}
