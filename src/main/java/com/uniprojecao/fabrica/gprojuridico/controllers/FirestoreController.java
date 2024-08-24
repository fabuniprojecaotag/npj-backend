package com.uniprojecao.fabrica.gprojuridico.controllers;

import com.google.cloud.firestore.Filter;
import com.uniprojecao.fabrica.gprojuridico.dto.body.DeleteBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.body.UpdateBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.models.MedidaJuridica;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.Atendimento;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.repositories.FirestoreRepository;
import com.uniprojecao.fabrica.gprojuridico.services.AtendimentoService;
import com.uniprojecao.fabrica.gprojuridico.services.MedidaJuridicaService;
import com.uniprojecao.fabrica.gprojuridico.services.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.QueryFilterService.getFilter;
import static com.uniprojecao.fabrica.gprojuridico.utils.Constants.*;

@RestController
@RequestMapping("/api/{collectionName}")
public class FirestoreController {

    @PostMapping
    public ResponseEntity<Object> insert(@PathVariable String collectionName, @RequestBody Object payload) throws Exception {

        var result = switch (collectionName) {
            case ATENDIMENTOS_COLLECTION -> new AtendimentoService().insert((Atendimento) payload);
            case MEDIDAS_JURIDICAS_COLLECTION -> new MedidaJuridicaService().insert((MedidaJuridica) payload);
            case USUARIOS_COLLECTION -> new UsuarioService().insert((Usuario) payload);
            default -> FirestoreRepository.insert(collectionName, payload);
        };

        return ResponseEntity.status(201).body(result);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> findAll(
            @PathVariable String collectionName,
            @RequestParam(required = false) String startAfter,
            @RequestParam(required = false) String field,
            @RequestParam(required = false) String operator,
            @RequestParam(required = false) String value,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "min") String returnType
    ) throws Exception {
        Filter queryFilter = getFilter(field, operator, value);
        var docs = FirestoreRepository.findAll(collectionName, startAfter, pageSize, queryFilter, returnType);
        return ResponseEntity.ok(docs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable String collectionName, @PathVariable String id) throws Exception {
        var result = FirestoreRepository.findById(collectionName,id);
        return ResponseEntity.ok(result);
    }

    @PutMapping
    public ResponseEntity<?> update(@PathVariable String collectionName, @RequestBody UpdateBodyDTO payload) {
        if (collectionName == USUARIOS_COLLECTION)
            new UsuarioService().update(payload.id(), (Map<String, Object>) payload.body(), payload.classType());
        else
            FirestoreRepository.update(collectionName, (Map<String, Object>) payload.body(), payload.id(), payload.classType());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@PathVariable String collectionName, @RequestBody DeleteBodyDTO payload) {
        FirestoreRepository.delete(collectionName, payload.ids());
        return ResponseEntity.noContent().build();
    }
}
