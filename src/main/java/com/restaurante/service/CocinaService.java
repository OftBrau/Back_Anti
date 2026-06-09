package com.restaurante.service;

import com.restaurante.model.AsignacionCategoriasCocinero;
import com.restaurante.model.DetallePedido;
import com.restaurante.repository.AsignacionCategoriasCocineroRepository;
import com.restaurante.repository.DetallePedidoRepository;
import com.restaurante.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CocinaService {

    private final DetallePedidoRepository detallePedidoRepository;
    private final PedidoRepository pedidoRepository;
    private final AsignacionCategoriasCocineroRepository asignacionRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public List<DetallePedido> pendientesPorCocinero(String cocineroNombre) {
        List<Integer> categoriaIds = asignacionRepository.findByCocineroNombre(cocineroNombre)
                .stream()
                .map(a -> a.getCategoria().getId())
                .collect(Collectors.toList());

        return detallePedidoRepository.findByEstado("Pendiente")
                .stream()
                .filter(d -> categoriaIds.contains(d.getProducto().getCategoria().getId()))
                .collect(Collectors.toList());
    }

    public List<DetallePedido> todosPendientes() {
        return detallePedidoRepository.findByEstado("Pendiente");
    }

    public void marcarListo(Integer detalleId) {
        DetallePedido detalle = detallePedidoRepository.findById(detalleId)
                .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));
        detalle.setEstado("Listo");
        detallePedidoRepository.save(detalle);

        messagingTemplate.convertAndSend("/topic/cocina", detalle);
    }

    public List<DetallePedido> listos() {
        return detallePedidoRepository.findByEstado("Listo");
    }
}
