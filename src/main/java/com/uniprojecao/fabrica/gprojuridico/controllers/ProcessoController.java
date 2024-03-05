package com.uniprojecao.fabrica.gprojuridico.controllers;

import com.uniprojecao.fabrica.gprojuridico.domains.processo.Processo;
import com.uniprojecao.fabrica.gprojuridico.services.ProcessoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/processos")
public class ProcessoController {

    @Autowired
    private ProcessoService service;

    @PostMapping
    public ResponseEntity<Object> insert(@RequestBody Processo data) {
        Map<String, Object> result = service.insert(data);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(result.get("id")).toUri();
        return ResponseEntity.created(uri).body(result.get("object"));
    }

    @GetMapping
    public ResponseEntity<List<Object>> findAll(@RequestParam(defaultValue = "20") String limit,
                                                @RequestParam String field,
                                                @RequestParam String filter,
                                                @RequestParam String value) {
        List<Object> list = service.findAll(limit, field, filter, value);
        return ResponseEntity.ok(list);
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
        Object object = service.findById(id);
        return ResponseEntity.ok(object);
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
