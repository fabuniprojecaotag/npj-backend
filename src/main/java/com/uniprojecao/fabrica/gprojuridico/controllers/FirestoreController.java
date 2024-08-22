package com.uniprojecao.fabrica.gprojuridico.controllers;

import com.google.cloud.firestore.Filter;
import com.uniprojecao.fabrica.gprojuridico.dto.DeleteBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.InsertBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.UpdateBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.repositories.FirestoreRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.QueryFilterService.getFilter;

@RestController
@RequestMapping("/documents")
public class FirestoreController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> getDocuments(
            @RequestParam String collection,
            @RequestParam(required = false) String startAfter,
            @RequestParam(required = false) String field,
            @RequestParam(required = false) String operator,
            @RequestParam(required = false) String value,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "min") String returnType
    ) throws Exception {
        // TODO: Adicionar tratamento para quando coleção for Atendimento, ..., e quando o returnType for específico (ex.: forAll...)
        Filter queryFilter = getFilter(field, operator, value);
        var docs = FirestoreRepository.getDocuments(collection, startAfter, pageSize, queryFilter, returnType);
        return ResponseEntity.ok(docs);
    }

    @GetMapping("/{collectionName}/{id}")
    public ResponseEntity<Object> getDocumentById(
            @PathVariable String collectionName,
            @PathVariable String id) throws Exception {
        var result = FirestoreRepository.getDocumentById(collectionName,id);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteDocuments(@RequestBody DeleteBodyDTO payload) {
        FirestoreRepository.deleteDocuments(payload);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Object> insert(@RequestBody InsertBodyDTO payload) throws Exception {
        var result = FirestoreRepository.insertDocument(payload);
        return ResponseEntity.status(201).body(result);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdateBodyDTO payload) throws Exception {
        FirestoreRepository.updateDocument(payload);
        return ResponseEntity.ok().build();
    }
}
