package com.restaurante.service;

import com.restaurante.model.DetallePedido;
import com.restaurante.model.Mesa;
import com.restaurante.model.Pedido;
import com.restaurante.repository.DetallePedidoRepository;
import com.restaurante.repository.MesaRepository;
import com.restaurante.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final MesaRepository mesaRepository;
    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;

    public Map<String, Object> obtenerResumen() {
        List<Mesa> mesas = mesaRepository.findAll();
        List<Pedido> pedidosPendientes = pedidoRepository.findByEstado("Pendiente");
        List<DetallePedido> cocinaPendiente = detallePedidoRepository.findByEstado("Pendiente");

        long libres = mesas.stream().filter(m -> "Libre".equals(m.getEstado())).count();
        long ocupadas = mesas.stream().filter(m -> "Ocupado".equals(m.getEstado())).count();

        Map<String, Object> resumen = new HashMap<>();
        resumen.put("totalMesas", mesas.size());
        resumen.put("mesasLibres", libres);
        resumen.put("mesasOcupadas", ocupadas);
        resumen.put("pedidosPendientes", pedidosPendientes.size());
        resumen.put("cocinaPendiente", cocinaPendiente.size());
        return resumen;
    }
}
