package com.restaurante.config;

import com.restaurante.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> {})
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/error", "/api/auth/**", "/ws/**").permitAll()
                .requestMatchers("/api/dashboard/**").hasAnyRole("CONTROL", "MESAS", "CAJERO")
                .requestMatchers(HttpMethod.GET, "/api/pedidos/**").hasAnyRole("MESAS", "CONTROL", "CAJERO")
                .requestMatchers("/api/pedidos/**").hasAnyRole("MESAS", "CONTROL")
                .requestMatchers("/api/cocina/**").hasAnyRole("COCINERO", "CONTROL")
                .requestMatchers("/api/pagos/*/pdf").permitAll()
                .requestMatchers("/api/pagos/**").hasAnyRole("CAJERO", "CONTROL")
                .requestMatchers(HttpMethod.GET, "/api/reportes/**").hasAnyRole("CAJERO", "CONTROL")
                .requestMatchers("/api/reportes/**").hasRole("CONTROL")
                .requestMatchers(HttpMethod.POST, "/api/mesas/**").hasRole("CONTROL")
                .requestMatchers(HttpMethod.DELETE, "/api/mesas/**").hasRole("CONTROL")
                .requestMatchers("/api/asignaciones/**").hasRole("CONTROL")
                .requestMatchers(HttpMethod.POST, "/api/productos/**").hasRole("CONTROL")
                .requestMatchers(HttpMethod.PUT, "/api/productos/**").hasRole("CONTROL")
                .requestMatchers(HttpMethod.DELETE, "/api/productos/**").hasRole("CONTROL")
                .requestMatchers(HttpMethod.POST, "/api/categorias/**").hasRole("CONTROL")
                .requestMatchers(HttpMethod.PUT, "/api/categorias/**").hasRole("CONTROL")
                .requestMatchers(HttpMethod.DELETE, "/api/categorias/**").hasRole("CONTROL")
                .requestMatchers(HttpMethod.PATCH, "/api/categorias/**").hasRole("CONTROL")
                .requestMatchers("/api/insumos/**").hasRole("CONTROL")
                .requestMatchers("/api/proveedores/**").hasRole("CONTROL")
                .requestMatchers("/api/locales/**").hasAnyRole("CONTROL", "REPARTIDOR")
                .requestMatchers("/api/solicitudes-compra/**").hasAnyRole("CONTROL", "REPARTIDOR")
                .requestMatchers("/api/envios/**").hasAnyRole("CONTROL", "REPARTIDOR")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
