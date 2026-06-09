package com.restaurante.config;

import com.restaurante.model.Usuario;
import com.restaurante.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() == 0) {
            String password = passwordEncoder.encode("1a2b");

            usuarioRepository.save(Usuario.builder().username("Control").password(password).rol("CONTROL").build());
            usuarioRepository.save(Usuario.builder().username("Mesas").password(password).rol("MESAS").build());
            usuarioRepository.save(Usuario.builder().username("Cajero").password(password).rol("CAJERO").build());
            usuarioRepository.save(Usuario.builder().username("Cocinero_1").password(password).rol("COCINERO_1").build());
            usuarioRepository.save(Usuario.builder().username("Cocinero_2").password(password).rol("COCINERO_2").build());

            System.out.println("✅ Usuarios iniciales creados (password: 1a2b)");
        }
    }
}
