package com.uniprojecao.fabrica.gprojuridico.controllers;

import com.google.cloud.firestore.Filter;
import com.uniprojecao.fabrica.gprojuridico.dto.body.DeleteBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.body.UpdateBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.models.MedidaJuridica;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.Assistido;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.Atendimento;
import com.uniprojecao.fabrica.gprojuridico.models.processo.Processo;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.repositories.FirestoreRepositoryImpl;
import com.uniprojecao.fabrica.gprojuridico.services.*;
import com.uniprojecao.fabrica.gprojuridico.services.exceptions.InvalidCollectionNameException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.QueryFilterService.getFilter;
import static com.uniprojecao.fabrica.gprojuridico.utils.Constants.*;
import static com.uniprojecao.fabrica.gprojuridico.utils.Utils.convertGenericObjectToClassInstanceWithValidation;

@RestController
@RequestMapping("/api/{collectionName}")
public class FirestoreController {

    @PostMapping
    public ResponseEntity<Object> insert(@PathVariable String collectionName, @RequestBody Object payload)
            throws Exception {

        Object result;
        Object data;

        switch (collectionName) {
            case ASSISTIDOS_COLLECTION:
                data = convertGenericObjectToClassInstanceWithValidation(payload, Assistido.class);
                result = new AssistidoService().insert((Assistido) data);
                break;
            case ATENDIMENTOS_COLLECTION:
                data = convertGenericObjectToClassInstanceWithValidation(payload, Atendimento.class);
                result = new AtendimentoService().insert((Atendimento) data);
                break;
            case MEDIDAS_JURIDICAS_COLLECTION:
                data = convertGenericObjectToClassInstanceWithValidation(payload, MedidaJuridica.class);
                result = new MedidaJuridicaService().insert((MedidaJuridica) data);
                break;
            case PROCESSOS_COLLECTION:
                data = convertGenericObjectToClassInstanceWithValidation(payload, Processo.class);
                result = new ProcessoService().insert((Processo) data);
                break;
            case USUARIOS_COLLECTION:
                data = convertGenericObjectToClassInstanceWithValidation(payload, Usuario.class);
                result = new UsuarioService().insert((Usuario) data);
                break;
            default:
                throw new InvalidCollectionNameException();
        }

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
            @RequestParam(defaultValue = "min") String returnType)
            throws Exception {

        FirestoreRepositoryImpl firestoreRepository = new FirestoreRepositoryImpl(collectionName);

        Filter queryFilter =
                (field != null && value != null) ?
                        getFilter(field, operator, value) :
                        null;

        var docs = firestoreRepository.findAll(startAfter, pageSize, queryFilter, returnType);
        return ResponseEntity.ok(docs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable String collectionName, @PathVariable String id)
            throws Exception {

        Object result = new FirestoreRepositoryImpl(collectionName).findById(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String collectionName, @PathVariable String id, @RequestBody UpdateBodyDTO payload) {

        switch (collectionName) {
            case ASSISTIDOS_COLLECTION:
                new AssistidoService().update(id, (Map<String, Object>) payload.body(), payload.classType());
                break;
            case ATENDIMENTOS_COLLECTION:
                new AtendimentoService().update(id, (Map<String, Object>) payload.body(), payload.classType());
                break;
            case MEDIDAS_JURIDICAS_COLLECTION:
                new MedidaJuridicaService().update(id, (Map<String, Object>) payload.body());
                break;
            case PROCESSOS_COLLECTION:
                new ProcessoService().update(id, (Map<String, Object>) payload.body());
                break;
            case USUARIOS_COLLECTION:
                new UsuarioService().update(id, (Map<String, Object>) payload.body(), payload.classType());
                break;
            default:
                throw new InvalidCollectionNameException();
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@PathVariable String collectionName, @RequestBody DeleteBodyDTO payload) {

        new FirestoreRepositoryImpl(collectionName).delete(payload.ids());
        return ResponseEntity.noContent().build();
    }
}
