package com.uniprojecao.fabrica.gprojuridico.controllers;

import com.uniprojecao.fabrica.gprojuridico.models.autocomplete.AssistidoAutocomplete;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.Assistido;
import com.uniprojecao.fabrica.gprojuridico.dto.min.AssistidoMinDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.min.AtendimentoVinculadoAssistidoDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.min.ProcessoVinculado;
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

    @GetMapping("/{id}/processos")
    public ResponseEntity<List<ProcessoVinculado>> findAllProcessos(@PathVariable String id,
                                                                    @RequestParam(defaultValue = "20")
                                                                                      String limit) {
        List<ProcessoVinculado> list = service.findAllProcessos(id, limit);
        return ResponseEntity.ok(list);
    }
}
