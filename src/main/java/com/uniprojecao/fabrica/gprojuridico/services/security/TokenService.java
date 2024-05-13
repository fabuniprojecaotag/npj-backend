package com.uniprojecao.fabrica.gprojuridico.services.security;

import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secretKey;

    public String generateToken(Usuario usuario) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        return JWT.create()
                .withIssuer("NPJ-Api")
                .withSubject(usuario.getId())
                .withClaim("role", usuario.getRole())
                .withClaim("nome", usuario.getNome())
                .withExpiresAt(genExpirationDate())
                .sign(algorithm);

    }

    public String validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        return JWT.require(algorithm)
                .withIssuer("NPJ-Api")
                .build()
                .verify(token)
                .getSubject();
    }
    private Instant genExpirationDate(){
        return LocalDateTime.now().plusHours(6).toInstant(ZoneOffset.of("-03:00"));
    }
}