package com.uniprojecao.fabrica.gprojuridico.controllers;

import com.uniprojecao.fabrica.gprojuridico.domains.MedidaJuridicaModel;
import com.uniprojecao.fabrica.gprojuridico.services.MedidaJuridicaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.createUri;

@RestController
@RequestMapping("/medidas-juridicas")
public class MedidaJuridicaController {

    @Autowired
    private MedidaJuridicaService service;

    @PostMapping
    public ResponseEntity<MedidaJuridicaModel> insert(@RequestBody @Valid MedidaJuridicaModel data) {
        var result = service.insert(data);
        var id = result.getId();
        return ResponseEntity.created(createUri(id)).body(result);
    }

    @PostMapping("/demo-data")
    public ResponseEntity<?> insertMultiple() {
        service.insertMultipleDemoData();
        return ResponseEntity.ok("Demo data loaded.");
    }

    @GetMapping
    public ResponseEntity<List<MedidaJuridicaModel>> findAll(@RequestParam(defaultValue = "20") String limit,
                                                             @RequestParam(defaultValue = "") String field,
                                                             @RequestParam(defaultValue = "") String filter,
                                                             @RequestParam(defaultValue = "") String value) {
        var list = service.findAll(limit, field, filter, value);
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
    public ResponseEntity<MedidaJuridicaModel> findById(@PathVariable String id) {
        var result = service.findById(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Map<String, Object> data) {
        service.updateData(id, data);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
