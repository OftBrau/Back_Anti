package com.restaurante.service;

import com.restaurante.model.Envio;
import com.restaurante.model.Venta;
import com.restaurante.repository.EnvioRepository;
import com.restaurante.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnvioService {

    private final EnvioRepository envioRepository;
    private final VentaRepository ventaRepository;

    public List<Envio> listar() {
        return envioRepository.findAllByOrderByFechaCreacionDesc();
    }

    public List<Envio> pendientes() {
        return envioRepository.findByEstadoOrderByFechaCreacionAsc("Pendiente");
    }

    public List<Envio> entregadosHoy() {
        LocalDateTime inicio = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        return envioRepository.findByEstadoAndFechaEntregaAfterOrderByFechaEntregaDesc("Entregado", inicio);
    }

    public Envio obtener(Integer id) {
        return envioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Envio no encontrado"));
    }

    public Envio crear(Envio envio) {
        envio.setEstado("Pendiente");
        envio.setFechaCreacion(LocalDateTime.now());
        return envioRepository.save(envio);
    }

    public Envio actualizar(Integer id, Envio envio) {
        Envio existente = obtener(id);
        existente.setCliente(envio.getCliente());
        existente.setDireccion(envio.getDireccion());
        existente.setTelefono(envio.getTelefono());
        existente.setDetalle(envio.getDetalle());
        existente.setTotal(envio.getTotal());
        return envioRepository.save(existente);
    }

    public Envio marcarEntregado(Integer id) {
        Envio envio = obtener(id);
        envio.setEstado("Entregado");
        envio.setFechaEntrega(LocalDateTime.now());

        Venta venta = Venta.builder()
                .total(envio.getTotal())
                .metodoPago("Delivery")
                .montoRecibido(envio.getTotal())
                .vuelto(0.0)
                .tipo("DELIVERY")
                .fecha(LocalDateTime.now())
                .build();
        ventaRepository.save(venta);

        return envioRepository.save(envio);
    }

    public void eliminar(Integer id) {
        envioRepository.deleteById(id);
    }
}
