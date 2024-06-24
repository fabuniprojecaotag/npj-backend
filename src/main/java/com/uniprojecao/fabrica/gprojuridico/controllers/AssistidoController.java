package com.uniprojecao.fabrica.gprojuridico.controllers;

import com.uniprojecao.fabrica.gprojuridico.domains.Autocomplete.AssistidoAutocomplete;
import com.uniprojecao.fabrica.gprojuridico.dto.assistido.AssistidoDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.min.AssistidoMinDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.min.AtendimentoVinculadoAssistidoDTO;
import com.uniprojecao.fabrica.gprojuridico.services.AssistidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.createUri;

@RestController
@RequestMapping("/assistidos")
public class AssistidoController {

    @Autowired
    private AssistidoService service;

    @PostMapping
    public ResponseEntity<AssistidoDTO> insert(@RequestBody @Valid AssistidoDTO data) {
        var result = service.insert(data);
        var id = result.getCpf();
        return ResponseEntity.created(createUri(id)).body(result);
    }

    @GetMapping
    public ResponseEntity<List<AssistidoMinDTO>> findAll(@RequestParam(defaultValue = "20") String limit,
                                                @RequestParam(defaultValue = "") String field,
                                                @RequestParam(defaultValue = "") String filter,
                                                @RequestParam(defaultValue = "") String value) {
        List<AssistidoMinDTO> list = service.findAll(limit, field, filter, value);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/min")
    public ResponseEntity<List<AssistidoAutocomplete>> findAllMin(@RequestParam(defaultValue = "20") String limit,
                                                         @RequestParam(defaultValue = "") String field,
                                                         @RequestParam(defaultValue = "") String filter,
                                                         @RequestParam(defaultValue = "") String value) {
        List<AssistidoAutocomplete> list = service.findAllMin(limit, field, filter, value);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}/atendimentos")
    public ResponseEntity<List<AtendimentoVinculadoAssistidoDTO>> findAllAtendimentos(@PathVariable String id,
                                                                                      @RequestParam(defaultValue = "20")
                                                                                String limit) {
        List<AtendimentoVinculadoAssistidoDTO> list = service.findAllAtendimentos(id, limit);
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
