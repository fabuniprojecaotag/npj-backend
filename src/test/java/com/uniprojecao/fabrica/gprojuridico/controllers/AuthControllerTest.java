package com.uniprojecao.fabrica.gprojuridico.controllers;

import com.uniprojecao.fabrica.gprojuridico.dto.auth.AuthenticationDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.auth.LoginResponseDTO;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.services.security.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    private final String username = "testuser";
    private final String password = "testpassword";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testVerifyLogin() {
        AuthenticationDTO authRequest = new AuthenticationDTO(username, password);

        // Criar um mock para o Usuario
        Usuario usuario = mock(Usuario.class);
        when(usuario.getId()).thenReturn("1");
        when(usuario.getNome()).thenReturn("Test User");
        when(usuario.getRole()).thenReturn("USER");

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(usuario);
        when(auth.getName()).thenReturn(username);

        // Mockando o retorno do authenticationManager.authenticate()
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        when(authenticationManager.authenticate(authToken)).thenReturn(auth);

        when(tokenService.generateToken(usuario)).thenReturn("mocked-token");

        ResponseEntity<LoginResponseDTO> response = authController.verifyLogin(authRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("mocked-token", Objects.requireNonNull(response.getBody()).access_token());
        verify(authenticationManager).authenticate(authToken);
        verify(tokenService).generateToken(usuario);
    }

}
