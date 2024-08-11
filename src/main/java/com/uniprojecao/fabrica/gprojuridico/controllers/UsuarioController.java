package com.uniprojecao.fabrica.gprojuridico.controllers;

import com.uniprojecao.fabrica.gprojuridico.models.autocomplete.UsuarioAutocomplete;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService service;

    @GetMapping("/min")
    public ResponseEntity<List<UsuarioAutocomplete>> findAllMin(@RequestParam(defaultValue = "20") String limit,
                                                                @RequestParam(defaultValue = "") String field,
                                                                @RequestParam(defaultValue = "") String filter,
                                                                @RequestParam(defaultValue = "") String value) {
        var usuarios = service.findAllMin(limit, field, filter, value);
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/me")
    public ResponseEntity<Usuario> getMe() {
        var usuario = service.findMe();
        return ResponseEntity.ok(usuario);
    }
}
