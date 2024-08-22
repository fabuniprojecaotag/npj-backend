package com.uniprojecao.fabrica.gprojuridico.controllers;

import com.uniprojecao.fabrica.gprojuridico.models.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping("/api/usuarios/me")
    public ResponseEntity<Usuario> getMe() throws Exception {
        var usuario = service.findMe();
        return ResponseEntity.ok(usuario);
    }
}
