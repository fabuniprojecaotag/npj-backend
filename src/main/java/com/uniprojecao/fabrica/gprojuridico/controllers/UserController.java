package com.uniprojecao.fabrica.gprojuridico.controllers;

import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuários")
public class UserController {
    @Autowired
    private UserService service;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid Usuario data) {
        Map<String, Object> result = service.create(data);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(result.get("id")).toUri();
        return ResponseEntity.created(uri).body(result.get("object"));
    }

    @GetMapping
    public ResponseEntity<List<Object>> findAll(@RequestParam(defaultValue = "20") String limit,
                                                @RequestParam String field,
                                                @RequestParam String filter,
                                                @RequestParam String value) {
        List<Object> usuarios = service.findAll(limit, field, filter, value);
        return ResponseEntity.ok(usuarios);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll(@RequestParam(defaultValue = "20") String limit,
                                       @RequestParam String field,
                                       @RequestParam String filter,
                                       @RequestParam String value) {
        service.deleteAll(limit, field, filter, value);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable String id) {
        Object usuario = service.loadUserByUsername(id);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/me")
    public ResponseEntity<Object> getMe() {
        Object usuario = service.authenticated();
        return ResponseEntity.ok(usuario);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable String id, @RequestBody Map<String, Object> data) {
        service.update(id, data);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
