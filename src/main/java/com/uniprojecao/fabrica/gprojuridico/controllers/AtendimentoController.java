package com.uniprojecao.fabrica.gprojuridico.controllers;

import com.uniprojecao.fabrica.gprojuridico.models.autocomplete.AtendimentoAutocomplete;
import com.uniprojecao.fabrica.gprojuridico.repository.AtendimentoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.uniprojecao.fabrica.gprojuridico.services.QueryFilterService.getFilter;
import static java.lang.Integer.parseInt;

@RestController
@RequestMapping("/atendimentos")
public class AtendimentoController {

    @GetMapping("/autocomplete")
    public ResponseEntity<List<AtendimentoAutocomplete>> findAllForAutocomplete(
            @RequestParam(defaultValue = "20") String limit,
            @RequestParam(defaultValue = "") String field,
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "") String value) {
        var list = new AtendimentoRepository().findAllMin(parseInt(limit), getFilter(field, filter, value));
        return ResponseEntity.ok(list);
    }
}
