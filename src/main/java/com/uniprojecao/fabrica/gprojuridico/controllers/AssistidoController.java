package com.uniprojecao.fabrica.gprojuridico.controllers;

import com.google.cloud.firestore.Filter;
import com.uniprojecao.fabrica.gprojuridico.repositories.FirestoreRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.QueryFilterService.getFilter;
import static com.uniprojecao.fabrica.gprojuridico.utils.Constants.*;

@RestController
@RequestMapping("/assistidos")
public class AssistidoController {

    @GetMapping("/{id}/atendimentos")
    public ResponseEntity<Map<String, Object>> findAllAtendimentos(
            @PathVariable String id,
            @RequestParam(required = false) String startAfter,
            @RequestParam(defaultValue = "10") int pageSize) throws Exception {
        Filter queryFilter = getFilter("envolvidos.assistido.id", "EQUAL", id);
        var docs = FirestoreRepository.getDocuments(ATENDIMENTOS_COLLECTION, startAfter, pageSize, queryFilter, "forAssistido");
        return ResponseEntity.ok(docs);
    }

    @GetMapping("/{id}/processos")
    public ResponseEntity<Map<String, Object>> findAllProcessos(
            @PathVariable String id,
            @RequestParam(required = false) String startAfter,
            @RequestParam(defaultValue = "10") int pageSize) throws Exception {
        Filter queryFilter = getFilter("assistidoId", "EQUAL", id);
        var docs = FirestoreRepository.getDocuments(PROCESSOS_COLLECTION, startAfter, pageSize, queryFilter, "forAssistido");
        return ResponseEntity.ok(docs);
    }
}
