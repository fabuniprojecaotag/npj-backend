package com.uniprojecao.fabrica.gprojuridico.controllers;

import com.google.cloud.firestore.Filter;
import com.uniprojecao.fabrica.gprojuridico.dto.body.DeleteBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.body.UpdateBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.repositories.FirestoreRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.QueryFilterService.getFilter;

@RestController
@RequestMapping("/api")
public class FirestoreController {

    @GetMapping("/{collectionName}")
    public ResponseEntity<Map<String, Object>> getDocuments(
            @PathVariable String collection,
            @RequestParam(required = false) String startAfter,
            @RequestParam(required = false) String field,
            @RequestParam(required = false) String operator,
            @RequestParam(required = false) String value,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "min") String returnType
    ) throws Exception {
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

    @DeleteMapping("/{collectionName}")
    public ResponseEntity<?> deleteDocuments(
            @PathVariable String collectionName,
            @RequestBody DeleteBodyDTO payload) {
        FirestoreRepository.deleteDocuments(collectionName, payload);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{collectionName}")
    public ResponseEntity<Object> insert(
            @PathVariable String collectionName,
            @RequestBody Object payload) throws Exception {
        var result = FirestoreRepository.insertDocument(collectionName, payload);
        return ResponseEntity.status(201).body(result);
    }

    @PutMapping("/{collectionName}")
    public ResponseEntity<?> update(
            @PathVariable String collectionName,
            @RequestBody UpdateBodyDTO payload) {
        FirestoreRepository.updateDocument(
                collectionName,
                (Map<String, Object>) payload.body(),
                payload.id(),
                payload.classType()
        );
        return ResponseEntity.ok().build();
    }
}
