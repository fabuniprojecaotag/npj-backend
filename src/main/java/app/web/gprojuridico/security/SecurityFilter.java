package app.web.gprojuridico.security;

import app.web.gprojuridico.model.user.User;
import app.web.gprojuridico.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    UserService userService;
    @Autowired
    TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("Filtro JWT está sendo chamado");
        var token = this.recoverToken(request);

        if (token != null) {
            try {
                var loginEmail = this.tokenService.validateToken(token);
                User user = userService.findUserByEmail(loginEmail);

                var autenticacao = new UsernamePasswordAuthenticationToken(user, null,user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(autenticacao);
            } catch (RuntimeException e) {
                SecurityContextHolder.clearContext();
                throw e;
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }
}
