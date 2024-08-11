package com.uniprojecao.fabrica.gprojuridico.controllers;

import com.uniprojecao.fabrica.gprojuridico.models.autocomplete.AtendimentoAutocomplete;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.Atendimento;
import com.uniprojecao.fabrica.gprojuridico.dto.min.AtendimentoMinDTO;
import com.uniprojecao.fabrica.gprojuridico.services.AtendimentoService;
import jakarta.validation.Valid;
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

    @GetMapping("/min")
    public ResponseEntity<List<AtendimentoAutocomplete>> findAllMin(@RequestParam(defaultValue = "20") String limit,
                                                           @RequestParam(defaultValue = "") String field,
                                                           @RequestParam(defaultValue = "") String filter,
                                                           @RequestParam(defaultValue = "") String value) {
        List<AtendimentoAutocomplete> list = service.findAllMin(limit, field, filter, value);
        return ResponseEntity.ok(list);
    }
}
