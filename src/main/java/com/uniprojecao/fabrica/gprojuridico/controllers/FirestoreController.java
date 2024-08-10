package com.uniprojecao.fabrica.gprojuridico.controllers;

import com.google.cloud.firestore.Filter;
import com.uniprojecao.fabrica.gprojuridico.services.FirestoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
