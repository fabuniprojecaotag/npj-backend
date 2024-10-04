package com.uniprojecao.fabrica.gprojuridico.services.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Usuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TokenServiceTest {
    @Autowired
    private TokenService tokenService;
    private Usuario userModel;

    @BeforeEach
    void setUp() {
        userModel = new Usuario();
        userModel.setId("123");
        userModel.setSenha("senha123");
        userModel.setEmail("usuario@gmail.com");
        userModel.setRole("COORDENADOR");
    }

    @Test
    void testGenerateToken() {
        var newToken = tokenService.generateToken(userModel);

        Assertions.assertNotNull(newToken);
    }

    @Test
    void testValidateToken() {
        var newToken = tokenService.generateToken(userModel);
        var validatedToken = tokenService.validateToken(newToken);

        Assertions.assertNotNull(validatedToken);
    }

    @Test
    void testValidateInvalidToken() {
        Exception exception = Assertions.assertThrows(JWTVerificationException.class, () -> tokenService.validateToken("test-123"));

        Assertions.assertTrue(exception.getMessage().contains("Erro ao validar o token"));
    }
}
