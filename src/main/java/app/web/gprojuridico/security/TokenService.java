package app.web.gprojuridico.security;

import app.web.gprojuridico.model.user.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secretKey;

    public String generateToken(User user) {
        Algorithm algoritmo = Algorithm.HMAC256(secretKey);
        return JWT.create()
                .withSubject(user.getEmail())
                .withIssuer("NPJ-Api")
                .withIssuedAt(new Date())
                .withExpiresAt(genExpirationDate())
                .sign(algoritmo);
    }

    public String validateToken(String token) {
        try{
            Algorithm algoritmo = Algorithm.HMAC256(secretKey);

            return JWT.require(algoritmo)
                    .withIssuer("NPJ-Api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Erro ao validar Token:" + exception);
        }
    }
    private Instant genExpirationDate(){
        return LocalDateTime.now().plusHours(6).toInstant(ZoneOffset.of("-03:00"));
    }
}