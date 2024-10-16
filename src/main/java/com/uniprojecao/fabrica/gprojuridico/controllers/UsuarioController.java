package com.uniprojecao.fabrica.gprojuridico.controllers;

import com.uniprojecao.fabrica.gprojuridico.dto.body.DeleteBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.body.ListBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.body.UpdateBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.services.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.InvalidPropertiesFormatException;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "/usuarios")
public class UsuarioController {
    private final UsuarioService service;

    UsuarioController(UsuarioService usuarioService) {
        service = usuarioService;
    }

    @PostMapping
    public ResponseEntity<Usuario> insert(@RequestBody Usuario payload) throws InvalidPropertiesFormatException, ExecutionException, InterruptedException {
        Usuario result;
        result = service.insert(payload);

        return ResponseEntity.status(201).body(result);
    }

    @GetMapping
    public ResponseEntity<ListBodyDTO<Usuario>> findAll(
            @RequestParam(required = false) String startAfter,
            @RequestParam(defaultValue = "10") int pageSize)
            throws InvalidPropertiesFormatException, ExecutionException, InterruptedException {
        var docs = service.listAll(startAfter, pageSize);
        return ResponseEntity.ok(docs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> findById(@PathVariable String id)
            throws InvalidPropertiesFormatException, ExecutionException, InterruptedException {
        Usuario result = service.findById(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody UpdateBodyDTO<Usuario> payload) {
        service.update(id, payload);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody DeleteBodyDTO payload) {
        service.delete(payload);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<Usuario> getMe() throws InvalidPropertiesFormatException, ExecutionException, InterruptedException {
        var usuario = service.findMe();
        return ResponseEntity.ok(usuario);
    }
}
