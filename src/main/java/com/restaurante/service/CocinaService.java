package com.restaurante.service;

import com.restaurante.model.DetallePedido;
import com.restaurante.repository.DetallePedidoRepository;
import com.restaurante.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CocinaService {

    private final DetallePedidoRepository detallePedidoRepository;
    private final PedidoRepository pedidoRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public List<DetallePedido> pendientes() {
        return detallePedidoRepository.findByEstado("Pendiente");
    }

    public List<DetallePedido> listosDelDia() {
        LocalDateTime hoy = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        return detallePedidoRepository.findByEstado("Listo").stream()
                .filter(d -> d.getHoraIngreso() != null && d.getHoraIngreso().isAfter(hoy))
                .toList();
    }

    public void marcarListo(Integer detalleId) {
        DetallePedido detalle = detallePedidoRepository.findById(detalleId)
                .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));
        detalle.setEstado("Listo");
        detallePedidoRepository.save(detalle);

        messagingTemplate.convertAndSend("/topic/cocina", detalle);
        messagingTemplate.convertAndSend("/topic/dashboard", "actualizar");
    }

    public List<DetallePedido> listos() {
        return detallePedidoRepository.findByEstado("Listo");
    }
}
