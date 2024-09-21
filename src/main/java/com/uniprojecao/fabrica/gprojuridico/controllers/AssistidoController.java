package com.uniprojecao.fabrica.gprojuridico.controllers;

import com.google.cloud.firestore.Filter;
import com.uniprojecao.fabrica.gprojuridico.repositories.FirestoreRepositoryImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.uniprojecao.fabrica.gprojuridico.services.QueryFilterService.getFilter;
import static com.uniprojecao.fabrica.gprojuridico.utils.Constants.ATENDIMENTOS_COLLECTION;
import static com.uniprojecao.fabrica.gprojuridico.utils.Constants.PROCESSOS_COLLECTION;

@RestController
@RequestMapping("/api/assistidos")
public class AssistidoController {

    @GetMapping("/{id}/atendimentos")
    public ResponseEntity<Map<String, Object>> findAllAtendimentosVinculados(
            @PathVariable String id,
            @RequestParam(required = false) String startAfter,
            @RequestParam(defaultValue = "10") int pageSize) throws InvalidPropertiesFormatException, ExecutionException, InterruptedException {

        Filter queryFilter = getFilter("envolvidos.assistido.id", "EQUAL", id);
        var docs = new FirestoreRepositoryImpl(ATENDIMENTOS_COLLECTION).findAll(startAfter, pageSize, queryFilter, "forAssistido");
        return ResponseEntity.ok(docs);
    }

    @GetMapping("/{id}/processos")
    public ResponseEntity<Map<String, Object>> findAllProcessosVinculados(
            @PathVariable String id,
            @RequestParam(required = false) String startAfter,
            @RequestParam(defaultValue = "10") int pageSize)
            throws InvalidPropertiesFormatException, ExecutionException, InterruptedException {

        Filter queryFilter = getFilter("assistidoId", "EQUAL", id);
        var docs = new FirestoreRepositoryImpl(PROCESSOS_COLLECTION).findAll(startAfter, pageSize, queryFilter, "forAssistido");
        return ResponseEntity.ok(docs);
    }
}
