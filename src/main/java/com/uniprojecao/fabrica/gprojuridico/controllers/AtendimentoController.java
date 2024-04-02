package com.uniprojecao.fabrica.gprojuridico.controllers;

import com.uniprojecao.fabrica.gprojuridico.dto.atendimento.AtendimentoDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.min.AtendimentoMinDTO;
import com.uniprojecao.fabrica.gprojuridico.services.AtendimentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.createUri;

@RestController
@RequestMapping("/atendimentos")
public class AtendimentoController {

    @Autowired
    private AtendimentoService service;

    @PostMapping
    public ResponseEntity<AtendimentoDTO> insert(@RequestBody AtendimentoDTO data) {
        var result = service.insert(data);
        var id = result.getId();
        return ResponseEntity.created(createUri(id)).body(data);
    }

    @GetMapping
    public ResponseEntity<List<AtendimentoMinDTO>> findAll(@RequestParam(defaultValue = "20") String limit,
                                                           @RequestParam(defaultValue = "") String field,
                                                           @RequestParam(defaultValue = "") String filter,
                                                           @RequestParam(defaultValue = "") String value) {
        List<AtendimentoMinDTO> list = service.findAll(limit, field, filter, value);
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
    public ResponseEntity<AtendimentoDTO> findById(@PathVariable String id) {
        var result = service.findById(id);
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
