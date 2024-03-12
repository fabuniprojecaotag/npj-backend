package com.uniprojecao.fabrica.gprojuridico.services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.uniprojecao.fabrica.gprojuridico.domains.enums.FilterType;
import com.uniprojecao.fabrica.gprojuridico.domains.processo.Processo;
import com.uniprojecao.fabrica.gprojuridico.dto.ProcessoDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.repository.BaseRepository;
import com.uniprojecao.fabrica.gprojuridico.services.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProcessoService {

    @Autowired
    BaseRepository repository;

    private static final String COLLECTION_NAME = "processos";

    public Map<String, Object> insert(Processo data) {
        DocumentReference result = repository.save(COLLECTION_NAME, data);

        ApiFuture<DocumentSnapshot> future = result.get();
        Processo o;

        try {
            DocumentSnapshot snapshot = future.get();
            o = snapshot.toObject(Processo.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String documentId = result.getId();
        System.out.println("\nProcesso adicionado. ID: " + documentId);

        try {
            return Map.of(
                    "object", Utils.convertUsingReflection(o, null),
                    "id", documentId
            );
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Object> findAll(String limit, String field, String filter, String value) {
        return null;
    }

    public Object findById(String id) {
        DocumentSnapshot snapshot = repository.findById(COLLECTION_NAME, id);
        return snapshot.toObject(Processo.class);
    }

    public Boolean update(String id, Map<String, Object> data) {
        return repository.update(COLLECTION_NAME, id, data);
    }

    public Boolean delete(String id) {
        return repository.delete(COLLECTION_NAME, id);
    }

    public Boolean deleteAll(String limit, String field, String filter, String value) {
        boolean useQueryParams =
                !(field.isEmpty()) &&
                !(filter.isEmpty()) &&
                !(value.isEmpty());

        QueryFilter queryFilter = (useQueryParams) ? new QueryFilter(field, value, FilterType.valueOf(filter)) : null;
        return repository.deleteAll(COLLECTION_NAME, Integer.parseInt(limit), queryFilter);
    }
}
