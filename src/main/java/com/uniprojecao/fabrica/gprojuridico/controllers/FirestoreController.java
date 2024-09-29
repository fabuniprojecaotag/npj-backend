package com.uniprojecao.fabrica.gprojuridico.controllers;

import com.uniprojecao.fabrica.gprojuridico.dto.body.DeleteBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.body.ListBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.body.UpdateBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.Assistido;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.Atendimento;
import com.uniprojecao.fabrica.gprojuridico.repositories.FirestoreRepositoryImpl;
import com.uniprojecao.fabrica.gprojuridico.services.AssistidoService;
import com.uniprojecao.fabrica.gprojuridico.services.AtendimentoService;
import com.uniprojecao.fabrica.gprojuridico.services.exceptions.InvalidCollectionNameException;
import org.springframework.http.ResponseEntity;

import java.util.InvalidPropertiesFormatException;
import java.util.concurrent.ExecutionException;

import static com.uniprojecao.fabrica.gprojuridico.utils.Utils.convertGenericObjectToClassInstanceWithValidation;

public abstract class FirestoreController<T> {

    public ResponseEntity<T> insert(String collectionName, Object payload) throws Exception {
        // Lógica para inserir um objeto na coleção correta
        T result;
        Object data;

        switch (collectionName) {
            case "assistidos":
                data = convertGenericObjectToClassInstanceWithValidation(payload, Assistido.class);
                result = (T) new AssistidoService().insert((Assistido) data);
                break;
            case "atendimentos":
                data = convertGenericObjectToClassInstanceWithValidation(payload, Atendimento.class);
                result = (T) new AtendimentoService().insert((Atendimento) data);
                break;
            // Outros cases para diferentes coleções
            default:
                throw new InvalidCollectionNameException();
        }

        return ResponseEntity.status(201).body(result);
    }

    public ResponseEntity<ListBodyDTO<T>> findAll(String collectionName, String startAfter, int pageSize)
            throws InvalidPropertiesFormatException, ExecutionException, InterruptedException {
        // Lógica para listar todos os objetos da coleção
        FirestoreRepositoryImpl<T> firestoreRepository = new FirestoreRepositoryImpl<>(collectionName);
        var docs = firestoreRepository.findAll(startAfter, pageSize, null, "min");
        return ResponseEntity.ok(docs);
    }

    public ResponseEntity<T> findById(String collectionName, String id)
            throws InvalidPropertiesFormatException, ExecutionException, InterruptedException {
        T result = new FirestoreRepositoryImpl<T>(collectionName).findById(id);
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<Void> update(String collectionName, String id, UpdateBodyDTO<T> payload)
    {
        // Lógica para atualizar um objeto da coleção
        if (collectionName.equals("assistidos")) {
            new AssistidoService().update(id, (UpdateBodyDTO<Assistido>) payload.getBody(), payload.getClassType());
            // Outros cases para diferentes coleções
        } else {
            throw new InvalidCollectionNameException();
        }

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> delete(String collectionName, DeleteBodyDTO payload) {
        // Lógica para deletar um objeto da coleção
        new FirestoreRepositoryImpl<T>(collectionName).delete(payload.ids());
        return ResponseEntity.noContent().build();
    }
}
