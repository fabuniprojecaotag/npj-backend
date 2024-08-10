package com.uniprojecao.fabrica.gprojuridico.controllers;

import com.uniprojecao.fabrica.gprojuridico.dto.auth.AuthenticationDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.auth.LoginResponseDTO;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.services.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> verifyLogin(@RequestBody @Valid AuthenticationDTO data) {
        UsernamePasswordAuthenticationToken u = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        Authentication auth = authenticationManager.authenticate(u);
        String accessToken = tokenService.generateToken((Usuario) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(accessToken));
    }
}
