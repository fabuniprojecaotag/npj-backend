package com.uniprojecao.fabrica.gprojuridico.controllers;

import com.uniprojecao.fabrica.gprojuridico.models.Autocomplete.UsuarioAutocomplete;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.dto.min.UsuarioMinDTO;
import com.uniprojecao.fabrica.gprojuridico.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.createUri;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService service;

    @PostMapping
    public ResponseEntity<Usuario> insert(@RequestBody @Valid Usuario data) {
        var result = service.insert(data);
        var id = result.getId();
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

    @GetMapping("/min")
    public ResponseEntity<List<UsuarioAutocomplete>> findAllMin(@RequestParam(defaultValue = "20") String limit,
                                                                @RequestParam(defaultValue = "") String field,
                                                                @RequestParam(defaultValue = "") String filter,
                                                                @RequestParam(defaultValue = "") String value) {
        var usuarios = service.findAllMin(limit, field, filter, value);
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
    public ResponseEntity<Usuario> findById(@PathVariable String id) {
        var usuario = service.findById(id);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/me")
    public ResponseEntity<Usuario> getMe() {
        var usuario = service.findMe();
        return ResponseEntity.ok(usuario);
    }


    @PutMapping("/{id}/{clazz}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Map<String, Object> data,
                                    @PathVariable String clazz) {
        service.update(id, data, clazz);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
