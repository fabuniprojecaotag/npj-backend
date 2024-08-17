package com.uniprojecao.fabrica.gprojuridico.controllers;

import com.uniprojecao.fabrica.gprojuridico.dto.min.AtendimentoVinculado;
import com.uniprojecao.fabrica.gprojuridico.dto.min.ProcessoVinculado;
import com.uniprojecao.fabrica.gprojuridico.models.autocomplete.AssistidoAutocomplete;
import com.uniprojecao.fabrica.gprojuridico.repository.AssistidoRepository;
import com.uniprojecao.fabrica.gprojuridico.repository.AtendimentoRepository;
import com.uniprojecao.fabrica.gprojuridico.repository.ProcessoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.uniprojecao.fabrica.gprojuridico.services.QueryFilterService.getFilter;
import static java.lang.Integer.parseInt;

@RestController
@RequestMapping("/assistidos")
public class AssistidoController {

    @GetMapping("/autocomplete")
    public ResponseEntity<List<AssistidoAutocomplete>> findAllForAutoComplete(
            @RequestParam(defaultValue = "20") String limit,
            @RequestParam(defaultValue = "") String field,
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "") String value) {
        var list = new AssistidoRepository().findAllForAutoComplete(parseInt(limit), getFilter(field, filter, value));
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}/atendimentos")
    public ResponseEntity<List<AtendimentoVinculado>> findAllAtendimentos(
            @PathVariable String id,
            @RequestParam(defaultValue = "20") String limit) {
        var list = new AtendimentoRepository().findAllToAssistido(parseInt(limit), id);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}/processos")
    public ResponseEntity<List<ProcessoVinculado>> findAllProcessos(
            @PathVariable String id,
            @RequestParam(defaultValue = "20") String limit) {
        var list = new ProcessoRepository().findAllToAssistido(parseInt(limit), id);
        return ResponseEntity.ok(list);
    }
}
