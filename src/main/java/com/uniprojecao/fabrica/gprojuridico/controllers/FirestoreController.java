package com.uniprojecao.fabrica.gprojuridico.controllers;

import com.uniprojecao.fabrica.gprojuridico.dto.body.DeleteBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.body.ListBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.body.UpdateBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.models.BaseModel;
import com.uniprojecao.fabrica.gprojuridico.services.GenericService;
import com.uniprojecao.fabrica.gprojuridico.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.InvalidPropertiesFormatException;
import java.util.concurrent.ExecutionException;

public abstract class FirestoreController<T extends BaseModel> {

    private final GenericService<T> genericService;
    private final Utils<T> utils;

    @Autowired
    protected FirestoreController(GenericService<T> genericService) {
        this.genericService = genericService;
        this.utils = new Utils<>();
    }

    @PostMapping
    public ResponseEntity<T> insert(@RequestBody T payload) throws Exception {
        T result;
        T data;

        data = utils.convertGenericObjectToClassInstanceWithValidation(payload, payload.getClass());
        result = genericService.insert(data);

        return ResponseEntity.status(201).body(result);
    }

    @GetMapping
    public ResponseEntity<ListBodyDTO<T>> findAll(
            @RequestParam(required = false) String startAfter,
            @RequestParam(defaultValue = "10") int pageSize)
            throws InvalidPropertiesFormatException, ExecutionException, InterruptedException {
        var docs = genericService.listAll(startAfter, pageSize);
        return ResponseEntity.ok(docs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<T> findById(@PathVariable String id)
            throws InvalidPropertiesFormatException, ExecutionException, InterruptedException {
        T result = genericService.findById(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody UpdateBodyDTO<T> payload) {
        genericService.update(id, payload);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody DeleteBodyDTO payload) {
        genericService.delete(payload);
        return ResponseEntity.noContent().build();
    }
}
