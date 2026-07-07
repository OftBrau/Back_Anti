package com.restaurante.service;

import com.restaurante.dto.ConfirmarPedidoRequest;
import com.restaurante.dto.DisponibilidadResponse;
import com.restaurante.dto.VerificarDisponibilidadResponse;
import com.restaurante.model.DetallePedido;
import com.restaurante.model.Mesa;
import com.restaurante.model.Pedido;
import com.restaurante.model.Producto;
import com.restaurante.repository.DetallePedidoRepository;
import com.restaurante.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final MesaService mesaService;
    private final ProductoService productoServices;
    private final ProductoInsumoService productoInsumoService;
    private final SimpMessagingTemplate messagingTemplate;

    public List<Pedido> listar() {
        return pedidoRepository.findAll();
    }

    public List<Pedido> listarPorMesa(Integer mesaId) {
        return pedidoRepository.findByMesaId(mesaId);
    }

    public List<Pedido> enviosPendientes() {
        return pedidoRepository.findByTipoAndEstado("ENVIO", "EnCocina");
    }

    public Pedido obtener(Integer id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    @Transactional
    public Pedido crear(Integer mesaId) {
        Mesa mesa = mesaService.obtener(mesaId);
        Pedido pedido = Pedido.builder()
                .mesa(mesa)
                .fecha(LocalDateTime.now())
                .estado("Pendiente")
                .build();
        Pedido saved = pedidoRepository.save(pedido);
        messagingTemplate.convertAndSend("/topic/pedidos", saved);
        return saved;
    }

    @Transactional
    public DetallePedido agregarProducto(Integer pedidoId, Integer productoId, Integer cantidad) {
        Pedido pedido = obtener(pedidoId);
        Producto producto = productoServices.obtener(productoId);

        DetallePedido detalle = DetallePedido.builder()
                .pedido(pedido)
                .producto(producto)
                .cantidad(cantidad)
                .precio(producto.getPrecio())
                .estado("Pendiente")
                .horaIngreso(LocalDateTime.now())
                .build();

        DetallePedido saved = detallePedidoRepository.save(detalle);
        messagingTemplate.convertAndSend("/topic/pedidos/" + pedidoId, saved);
        return saved;
    }

    public void eliminarProducto(Integer detalleId) {
        detallePedidoRepository.deleteById(detalleId);
    }

    public List<DetallePedido> detallePorPedido(Integer pedidoId) {
        return detallePedidoRepository.findByPedidoId(pedidoId);
    }

    @Transactional
    public void cerrarPedido(Integer pedidoId) {
        Pedido pedido = obtener(pedidoId);
        pedido.setEstado("Cerrado");
        pedidoRepository.save(pedido);

        Mesa mesa = pedido.getMesa();
        mesa.setEstado("Ocupada");
        mesaService.actualizarEstado(mesa.getId(), "Ocupada");

        messagingTemplate.convertAndSend("/topic/pedidos", pedido);
    }

    @Transactional
    public DisponibilidadResponse verificarDisponibilidad(Integer pedidoId) {
        Pedido pedido = obtener(pedidoId);
        List<DetallePedido> detalles = detallePedidoRepository.findByPedidoId(pedidoId);

        List<DisponibilidadResponse.ItemDisponibilidad> items = new ArrayList<>();
        boolean todosDisponibles = true;

        for (DetallePedido detalle : detalles) {
            boolean disponible = detalle.getProducto().getEstado() == 1;
            detalle.setEstado(disponible ? "Disponible" : "NoDisponible");
            detallePedidoRepository.save(detalle);

            items.add(DisponibilidadResponse.ItemDisponibilidad.builder()
                    .detalleId(detalle.getId())
                    .productoNombre(detalle.getProducto().getNombre())
                    .cantidad(detalle.getCantidad())
                    .disponible(disponible)
                    .build());

            if (!disponible) todosDisponibles = false;
        }

        DisponibilidadResponse response = DisponibilidadResponse.builder()
                .pedidoId(pedidoId)
                .estadoPedido(pedido.getEstado())
                .items(items)
                .todosDisponibles(todosDisponibles)
                .build();

        messagingTemplate.convertAndSend("/topic/pedidos/" + pedidoId, response);
        return response;
    }

    @Transactional
    public Pedido confirmarPedido(Integer pedidoId) {
        Pedido pedido = obtener(pedidoId);
        List<DetallePedido> detalles = detallePedidoRepository.findByPedidoId(pedidoId);

        for (DetallePedido detalle : detalles) {
            if ("Pendiente".equals(detalle.getEstado())) {
                throw new RuntimeException("Hay items sin verificar. Ejecute verificar primero.");
            }
        }

        boolean algunNoDisponible = detalles.stream()
                .anyMatch(d -> "NoDisponible".equals(d.getEstado()));

        if (algunNoDisponible) {
            throw new RuntimeException("Hay items no disponibles. Modifique el pedido primero.");
        }

        pedido.setEstado("Confirmado");
        Pedido saved = pedidoRepository.save(pedido);

        for (DetallePedido detalle : detalles) {
            productoInsumoService.descontarInsumos(detalle.getProducto().getId(), detalle.getCantidad());
        }

        Mesa mesa = pedido.getMesa();
        if (mesa != null) {
            mesa.setEstado("Ocupada");
            mesaService.actualizarEstado(mesa.getId(), "Ocupada");
        }

        messagingTemplate.convertAndSend("/topic/pedidos", saved);
        return saved;
    }

    @Transactional
    public DetallePedido modificarDetalle(Integer detalleId, Integer nuevaCantidad) {
        DetallePedido detalle = detallePedidoRepository.findById(detalleId)
                .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));

        detalle.setCantidad(nuevaCantidad);
        detalle.setEstado("Pendiente");
        DetallePedido saved = detallePedidoRepository.save(detalle);
        messagingTemplate.convertAndSend("/topic/pedidos/" + detalle.getPedido().getId(), saved);
        return saved;
    }

    @Transactional
    public void cancelarPedido(Integer pedidoId) {
        Pedido pedido = obtener(pedidoId);
        pedido.setEstado("Cancelado");
        pedidoRepository.save(pedido);

        Mesa mesa = pedido.getMesa();
        if (mesa != null) {
            mesaService.actualizarEstado(mesa.getId(), "Libre");
        }

        messagingTemplate.convertAndSend("/topic/pedidos", pedido);
    }

    @Transactional
    public void enviarACocina(Integer pedidoId) {
        Pedido pedido = obtener(pedidoId);

        if (!"Confirmado".equals(pedido.getEstado())) {
            throw new RuntimeException("El pedido debe estar confirmado antes de enviar a cocina.");
        }

        List<DetallePedido> detalles = detallePedidoRepository.findByPedidoId(pedidoId);
        for (DetallePedido detalle : detalles) {
            if ("Disponible".equals(detalle.getEstado())) {
                detalle.setEstado("Pendiente");
                detallePedidoRepository.save(detalle);
            }
        }

        pedido.setEstado("EnCocina");
        pedidoRepository.save(pedido);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                messagingTemplate.convertAndSend("/topic/pedidos", pedido);
                messagingTemplate.convertAndSend("/topic/cocina", "nuevo pedido");
            }
        });
    }

    public VerificarDisponibilidadResponse verificarDisponibilidadItems(List<ConfirmarPedidoRequest.ItemRequest> items) {
        List<String> noDisponibles = new ArrayList<>();
        for (ConfirmarPedidoRequest.ItemRequest item : items) {
            Producto producto = productoServices.obtener(item.getProductoId());
            if (producto.getEstado() != 1) {
                noDisponibles.add(producto.getNombre());
            }
        }
        if (noDisponibles.isEmpty()) {
            return VerificarDisponibilidadResponse.builder()
                    .disponible(true)
                    .mensaje("Todos los productos están disponibles")
                    .build();
        }
        return VerificarDisponibilidadResponse.builder()
                .disponible(false)
                .mensaje("No disponible: " + String.join(", ", noDisponibles))
                .build();
    }

    @Transactional
    public Pedido confirmarPedidoCompleto(Integer mesaId, List<ConfirmarPedidoRequest.ItemRequest> items,
                                          String tipo, String cliente, String direccion, String telefono) {
        Mesa mesa = "ENVIO".equals(tipo) ? null : mesaService.obtener(mesaId);
        Pedido pedido = Pedido.builder()
                .mesa(mesa)
                .fecha(LocalDateTime.now())
                .estado("Pendiente")
                .tipo("ENVIO".equals(tipo) ? "ENVIO" : "LOCAL")
                .cliente(cliente)
                .direccion(direccion)
                .telefono(telefono)
                .build();
        pedido = pedidoRepository.save(pedido);
        for (ConfirmarPedidoRequest.ItemRequest item : items) {
            agregarProducto(pedido.getId(), item.getProductoId(), item.getCantidad());
        }
        verificarDisponibilidad(pedido.getId());
        pedido = confirmarPedido(pedido.getId());
        enviarACocina(pedido.getId());
        return pedido;
    }
}
