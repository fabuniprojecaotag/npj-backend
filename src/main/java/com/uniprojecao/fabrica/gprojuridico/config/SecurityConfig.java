package com.uniprojecao.fabrica.gprojuridico.config;

import com.uniprojecao.fabrica.gprojuridico.services.security.SecurityFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final SecurityFilter securityFilter;

    private static final String PROFESSOR_ROLE = "PROFESSOR";
    private static final String SECRETARIA_ROLE = "SECRETARIA";
    private static final String COORDENADOR_ROLE = "COORDENADOR";

    public SecurityConfig(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**", "**/**").permitAll() // pre-flight
                        .requestMatchers(HttpMethod.POST, "/auth").permitAll()

                        .requestMatchers(POST, "/usuarios").hasRole(SECRETARIA_ROLE)
                        .requestMatchers(POST, "/processos").hasRole(PROFESSOR_ROLE)
                        .requestMatchers(POST, "/medidas juridicas").hasRole(COORDENADOR_ROLE)

                        .requestMatchers(PUT, "/usuarios/**", "/assistidos/**", "/atendimentos/**", "/processos/**", "/medidas juridicas/**").hasRole(SECRETARIA_ROLE)

                        .requestMatchers(DELETE, "/usuarios", "/assistidos", "/atendimentos", "/processos", "/medidas juridicas").hasRole(COORDENADOR_ROLE)
                        .requestMatchers(DELETE, "/usuarios/**", "/assistidos/**", "/atendimentos/**", "/processos/**", "/medidas juridicas/**").hasRole(COORDENADOR_ROLE)

                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}