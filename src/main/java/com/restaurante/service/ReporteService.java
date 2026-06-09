package com.restaurante.service;

import com.restaurante.model.DetalleVenta;
import com.restaurante.model.Venta;
import com.restaurante.repository.DetalleVentaRepository;
import com.restaurante.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;

    public List<Venta> ventasDelDia() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);
        return ventaRepository.findByFechaBetween(start, end);
    }

    public List<Venta> ventasPorRango(LocalDateTime start, LocalDateTime end) {
        return ventaRepository.findByFechaBetween(start, end);
    }

    public Map<String, Object> resumenDiario() {
        List<Venta> ventas = ventasDelDia();
        double total = ventas.stream().mapToDouble(Venta::getTotal).sum();
        long count = ventas.size();

        Map<String, Object> resumen = new HashMap<>();
        resumen.put("totalVentas", total);
        resumen.put("cantidadVentas", count);
        resumen.put("ventas", ventas);
        return resumen;
    }

    public List<Venta> ventasPorMesa(Integer mesaId) {
        return ventaRepository.findByMesaId(mesaId);
    }
}
