package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.domains.atendimento.Atendimento;
import com.uniprojecao.fabrica.gprojuridico.repository.BaseRepository;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.AtendimentoUtils.convertSnapshotToCorrespondingAtendimentoDTO;

@Service
public class AtendimentoService {

    @Autowired
    BaseRepository repository;

    private static final String COLLECTION_NAME = "atendimentos";

    public Map<String, Object> insert(Atendimento data) {
        DocumentReference result = repository.save(COLLECTION_NAME, data);

        ApiFuture<DocumentSnapshot> future = result.get();
        Object o;

        try {
            o = convertSnapshotToCorrespondingAtendimentoDTO(future.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String documentId = result.getId();
        System.out.println("\nAtendimento adicionado. ID: " + documentId);

        return Map.of(
                "object", o,
                "id", documentId
        );
    }

    public List<Object> findAll(String limit) {
        List<QueryDocumentSnapshot> result = repository.findAll(COLLECTION_NAME, Integer.parseInt(limit));
        List<Object> list = new ArrayList<>();

        for (QueryDocumentSnapshot document : result) {
            list.add(convertSnapshotToCorrespondingAtendimentoDTO(document));
        }

        return list;
    }

    public Object findById(String id) {
        DocumentSnapshot snapshot = repository.findById(COLLECTION_NAME, id);
        return convertSnapshotToCorrespondingAtendimentoDTO(snapshot);
    }

    public Boolean update(String id, Map<String, Object> data) {
        return repository.update(COLLECTION_NAME, id, data);
    }

    public Boolean delete(String id) {
        return repository.delete(COLLECTION_NAME, id);
    }

    public Boolean deleteAll(String limit) {
        return repository.deleteAll(COLLECTION_NAME, Integer.parseInt(limit));
    }
}
