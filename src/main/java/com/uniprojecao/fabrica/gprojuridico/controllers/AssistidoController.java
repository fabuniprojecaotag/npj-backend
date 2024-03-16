package com.uniprojecao.fabrica.gprojuridico.controllers;

import com.uniprojecao.fabrica.gprojuridico.dto.assistido.AssistidoDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.min.AssistidoMinDTO;
import com.uniprojecao.fabrica.gprojuridico.services.AssistidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/assistidos")
public class AssistidoController {

    @Autowired
    private AssistidoService service;

    @PostMapping
    public ResponseEntity<AssistidoDTO> insert(@RequestBody AssistidoDTO data) {
        var result = service.insert(data);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(result.getCpf()).toUri();
        return ResponseEntity.created(uri).body(result);
    }

    @GetMapping
    public ResponseEntity<List<AssistidoMinDTO>> findAll(@RequestParam(defaultValue = "20") String limit,
                                                @RequestParam(defaultValue = "") String field,
                                                @RequestParam(defaultValue = "") String filter,
                                                @RequestParam(defaultValue = "") String value) {
        List<AssistidoMinDTO> list = service.findAll(limit, field, filter, value);
        return ResponseEntity.ok(list);
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
    public ResponseEntity<AssistidoDTO> findById(@PathVariable String id) {
        AssistidoDTO result = service.findById(id);
        return ResponseEntity.ok(result);
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
