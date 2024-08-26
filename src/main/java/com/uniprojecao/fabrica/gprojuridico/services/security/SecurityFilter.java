package com.uniprojecao.fabrica.gprojuridico.services.security;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.services.UsuarioService;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    TokenService tokenService;

    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain)
            throws ServletException, IOException {

        String token = this.recoverToken(request);
        if (token != null) {
            try {
                String usuarioId = tokenService.validateToken(token);
                Usuario usuario = (Usuario) usuarioService.loadUserByUsername(usuarioId);

                var authenticationToken = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } catch (TokenExpiredException e) {
                // Captura e trata especificamente TokenExpiredException
                SecurityContextHolder.clearContext();
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write(e.getMessage());
                return; // Finaliza a execução do filtro
            } catch (RuntimeException e) {
                // Captura outras exceções em tempo de execução
                SecurityContextHolder.clearContext();
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.getWriter().write(e.getMessage());
                return; // Finaliza a execução do filtro
            }
        }

        // Continua o fluxo se não houver exceções
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }
}
