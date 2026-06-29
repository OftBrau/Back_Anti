package com.restaurante.service;

import com.restaurante.model.DetallePedido;
import com.restaurante.model.Mesa;
import com.restaurante.model.Pedido;
import com.restaurante.model.Venta;
import com.restaurante.repository.DetallePedidoRepository;
import com.restaurante.repository.MesaRepository;
import com.restaurante.repository.PedidoRepository;
import com.restaurante.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final MesaRepository mesaRepository;
    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final VentaRepository ventaRepository;

    public Map<String, Object> obtenerResumen() {
        List<Mesa> mesas = mesaRepository.findAll();
        List<Pedido> pedidosPendientes = pedidoRepository.findByEstado("Pendiente");
        List<DetallePedido> cocinaPendiente = detallePedidoRepository.findByEstado("Pendiente");

        long libres = mesas.stream().filter(m -> "Libre".equals(m.getEstado())).count();
        long ocupadas = mesas.stream().filter(m -> "Ocupado".equals(m.getEstado())).count();

        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        List<Venta> ventasDelMes = ventaRepository.findByFechaBetween(startOfMonth, LocalDateTime.now());
        double ingresosDelMes = ventasDelMes.stream().mapToDouble(v -> v.getTotal() != null ? v.getTotal() : 0.0).sum();

        Map<String, Object> resumen = new HashMap<>();
        resumen.put("totalMesas", mesas.size());
        resumen.put("mesasLibres", libres);
        resumen.put("mesasOcupadas", ocupadas);
        resumen.put("pedidosPendientes", pedidosPendientes.size());
        resumen.put("cocinaPendiente", cocinaPendiente.size());
        resumen.put("ventasDelMes", (long) ventasDelMes.size());
        resumen.put("ingresosDelMes", ingresosDelMes);
        return resumen;
    }
}
