package com.uniprojecao.fabrica.gprojuridico.controllers;

import com.uniprojecao.fabrica.gprojuridico.dto.min.UsuarioMinDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.usuario.UsuarioDTO;
import com.uniprojecao.fabrica.gprojuridico.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.createUri;

@RestController
@RequestMapping("/usuários")
public class UsuarioController {
    @Autowired
    private UsuarioService service;

    @PostMapping
    public ResponseEntity<UsuarioDTO> create(@RequestBody @Valid UsuarioDTO data) {
        var result = service.insert(data);
        var id = result.getEmail();
        return ResponseEntity.created(createUri(id)).body(result);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioMinDTO>> findAll(@RequestParam(defaultValue = "20") String limit,
                                                       @RequestParam(defaultValue = "") String field,
                                                       @RequestParam(defaultValue = "") String filter,
                                                       @RequestParam(defaultValue = "") String value) {
        var usuarios = service.findAll(limit, field, filter, value);
        return ResponseEntity.ok(usuarios);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll(@RequestParam(defaultValue = "20") String limit,
                                       @RequestParam(defaultValue = "") String field,
                                       @RequestParam(defaultValue = "") String filter,
                                       @RequestParam(defaultValue = "") String value) {
        service.deleteAll(limit, field, filter, value);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> findById(@PathVariable String id) {
        var usuario = service.findById(id);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioDTO> getMe() {
        var usuario = service.authenticated();
        return ResponseEntity.ok(usuario);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Map<String, Object> data) {
        service.update(id, data);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
