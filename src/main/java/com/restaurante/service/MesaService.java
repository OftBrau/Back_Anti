package com.restaurante.service;

import com.restaurante.model.Mesa;
import com.restaurante.repository.MesaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MesaService {

    private final MesaRepository mesaRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public List<Mesa> listar() {
        return mesaRepository.findAll();
    }

    public Mesa obtener(Integer id) {
        return mesaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada"));
    }

    public Mesa actualizarEstado(Integer id, String estado) {
        Mesa mesa = obtener(id);
        mesa.setEstado(estado);
        Mesa saved = mesaRepository.save(mesa);
        messagingTemplate.convertAndSend("/topic/mesas", saved);
        return saved;
    }

    public Mesa crear() {
        Mesa mesa = Mesa.builder().build();
        Mesa saved = mesaRepository.save(mesa);
        messagingTemplate.convertAndSend("/topic/mesas", saved);
        return saved;
    }

    public void eliminar(Integer id) {
        Mesa mesa = obtener(id);
        mesaRepository.delete(mesa);
        messagingTemplate.convertAndSend("/topic/mesas", "mesa eliminada");
    }
}
