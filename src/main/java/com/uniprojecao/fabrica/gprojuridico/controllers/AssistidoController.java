package com.uniprojecao.fabrica.gprojuridico.controllers;

import com.google.cloud.firestore.Filter;
import com.uniprojecao.fabrica.gprojuridico.dto.body.ListBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.vinculados.AtendimentoVinculadoDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.vinculados.ProcessoVinculadoDTO;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.Assistido;
import com.uniprojecao.fabrica.gprojuridico.repositories.FirestoreRepositoryImpl;
import com.uniprojecao.fabrica.gprojuridico.services.AssistidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.InvalidPropertiesFormatException;
import java.util.concurrent.ExecutionException;

import static com.uniprojecao.fabrica.gprojuridico.services.QueryFilterService.getFilter;
import static com.uniprojecao.fabrica.gprojuridico.utils.Constants.ATENDIMENTOS_COLLECTION;
import static com.uniprojecao.fabrica.gprojuridico.utils.Constants.PROCESSOS_COLLECTION;

@RestController
@RequestMapping(value = "/api/assistidos")
public class AssistidoController extends FirestoreController<Assistido> {
    public AssistidoController(AssistidoService service) {
        super(service);
    }

    @GetMapping("/{id}/atendimentos")
    public ResponseEntity<ListBodyDTO<AtendimentoVinculadoDTO>> findAllAtendimentosVinculados(
            @PathVariable String id,
            @RequestParam(required = false) String startAfter,
            @RequestParam(defaultValue = "10") int pageSize)
            throws InvalidPropertiesFormatException, ExecutionException, InterruptedException {

        Filter queryFilter = getFilter("envolvidos.assistido.id", "EQUAL", id);
        var docs = new FirestoreRepositoryImpl<AtendimentoVinculadoDTO>(ATENDIMENTOS_COLLECTION).findAll(startAfter, pageSize, queryFilter, "forAssistido");
        return ResponseEntity.ok(docs);
    }

    @GetMapping("/{id}/processos")
    public ResponseEntity<ListBodyDTO<ProcessoVinculadoDTO>> findAllProcessosVinculados(
            @PathVariable String id,
            @RequestParam(required = false) String startAfter,
            @RequestParam(defaultValue = "10") int pageSize)
            throws InvalidPropertiesFormatException, ExecutionException, InterruptedException {

        Filter queryFilter = getFilter("assistidoId", "EQUAL", id);
        var docs = new FirestoreRepositoryImpl<ProcessoVinculadoDTO>(PROCESSOS_COLLECTION).findAll(startAfter, pageSize, queryFilter, "forAssistido");
        return ResponseEntity.ok(docs);
    }
}
