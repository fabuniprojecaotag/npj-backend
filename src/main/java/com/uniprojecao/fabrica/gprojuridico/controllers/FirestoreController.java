package com.uniprojecao.fabrica.gprojuridico.controllers;

import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.services.FirestoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.initFilter;

@RestController
@RequestMapping("/documents")
public class FirestoreController {

    @Autowired
    private FirestoreService service;

    @GetMapping
    public Map<String, Object> getDocuments(
            @RequestParam String collection,
            @RequestParam(required = false) String startAfter,
            @RequestParam(defaultValue = "") String field,
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "") String value,
            @RequestParam(defaultValue = "10") int pageSize) throws Exception {
        QueryFilter queryFilter = initFilter(field, filter, value);
        return service.getDocuments(collection, startAfter, pageSize, queryFilter);
    }
}
