package app.web.gprojuridico.controllers;

import app.web.gprojuridico.domains.usuario.Usuario;
import app.web.gprojuridico.services.UserService;
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
    public ResponseEntity<List<Object>> findAll(@RequestParam(defaultValue = "20") String limit) {
        List<Object> usuarios = service.findAll(limit);
        return ResponseEntity.ok(usuarios);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll(@RequestParam(defaultValue = "20") String limit) {
        service.deleteAll(limit);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable String id) {
        Object usuario = service.loadUserByUsername(id);
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
