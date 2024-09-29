package com.uniprojecao.fabrica.gprojuridico.controllers;

import com.uniprojecao.fabrica.gprojuridico.models.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.services.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.InvalidPropertiesFormatException;
import java.util.concurrent.ExecutionException;

@RestController
public class UsuarioController {
    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping("/api/usuarios/me")
    public ResponseEntity<Usuario> getMe() throws InvalidPropertiesFormatException, ExecutionException, InterruptedException {
        var usuario = service.findMe();
        return ResponseEntity.ok(usuario);
    }
}
