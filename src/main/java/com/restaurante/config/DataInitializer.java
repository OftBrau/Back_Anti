package com.restaurante.config;

import com.restaurante.model.Usuario;
import com.restaurante.model.Envio;
import com.restaurante.model.Rol;
import com.restaurante.repository.UsuarioRepository;
import com.restaurante.repository.EnvioRepository;
import com.restaurante.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final EnvioRepository envioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (rolRepository.count() == 0) {
            List<String> nombres = List.of("CONTROL", "MESAS", "CAJERO", "COCINERO", "REPARTIDOR");
            for (String n : nombres) {
                rolRepository.save(Rol.builder().nombre(n).build());
            }
            System.out.println("✅ Roles creados");
        }

        if (usuarioRepository.count() == 0) {
            String password = passwordEncoder.encode("12345678");

            usuarioRepository.save(Usuario.builder()
                    .username("Control").password(password)
                    .rol(rolRepository.findByNombre("CONTROL").orElseThrow()).build());
            usuarioRepository.save(Usuario.builder()
                    .username("Mesas").password(password)
                    .rol(rolRepository.findByNombre("MESAS").orElseThrow()).build());
            usuarioRepository.save(Usuario.builder()
                    .username("Cajero").password(password)
                    .rol(rolRepository.findByNombre("CAJERO").orElseThrow()).build());
            usuarioRepository.save(Usuario.builder()
                    .username("Cocinero").password(password)
                    .rol(rolRepository.findByNombre("COCINERO").orElseThrow()).build());
            usuarioRepository.save(Usuario.builder()
                    .username("Repartidor").password(password)
                    .rol(rolRepository.findByNombre("REPARTIDOR").orElseThrow()).build());

            System.out.println("✅ Usuarios iniciales creados (password: 12345678)");
        }

        if (envioRepository.count() == 0) {
            envioRepository.save(Envio.builder()
                    .cliente("Juan Pérez")
                    .direccion("Av. Principal 123, Miraflores")
                    .telefono("987 654 321")
                    .detalle("2× Anticucho, 1× Papas Fritas")
                    .total(45.0)
                    .estado("Pendiente")
                    .fechaCreacion(LocalDateTime.now())
                    .build());

            envioRepository.save(Envio.builder()
                    .cliente("María García")
                    .direccion("Jr. Las Flores 456, San Isidro")
                    .telefono("976 543 210")
                    .detalle("3× Anticucho, 2× Chicha Morada")
                    .total(62.0)
                    .estado("Pendiente")
                    .fechaCreacion(LocalDateTime.now())
                    .build());

            envioRepository.save(Envio.builder()
                    .cliente("Carlos López")
                    .direccion("Calle Real 789, Barranco")
                    .telefono("965 432 109")
                    .detalle("1× Anticucho, 1× Causa, 1× Inca Kola")
                    .total(32.0)
                    .estado("Entregado")
                    .fechaCreacion(LocalDateTime.now().minusHours(3))
                    .fechaEntrega(LocalDateTime.now().minusHours(1))
                    .build());

            System.out.println("✅ Envíos de prueba creados");
        }
    }
}
