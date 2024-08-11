package com.uniprojecao.fabrica.gprojuridico.controllers;

import com.google.cloud.firestore.Filter;
import com.uniprojecao.fabrica.gprojuridico.dto.DeleteBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.services.FirestoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.QueryFilterService.getFilter;

@RestController
@RequestMapping("/documents")
public class FirestoreController {

    @Autowired
    private FirestoreService service;

    @GetMapping
    public Map<String, Object> getDocuments(
            @RequestParam String collection,
            @RequestParam(required = false) String startAfter,
            @RequestParam(required = false) String field,
            @RequestParam(required = false) String operator,
            @RequestParam(required = false) String value,
            @RequestParam(defaultValue = "10") int pageSize) throws Exception {
        Filter queryFilter = getFilter(field, operator, value);
        return service.getDocuments(collection, startAfter, pageSize, queryFilter);
    }

    @GetMapping("/{collectionName}/{id}")
    public ResponseEntity<Object> getDocumentById(
            @PathVariable String collectionName,
            @PathVariable String id) throws Exception {
        var result = service.getDocumentById(collectionName,id);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteDocuments(@RequestBody DeleteBodyDTO payload) {
        service.deleteDocuments(payload);
        return ResponseEntity.noContent().build();
    }
}
