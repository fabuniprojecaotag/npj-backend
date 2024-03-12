package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.domains.enums.FilterType;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.repository.AssistidoRepository;
import com.uniprojecao.fabrica.gprojuridico.repository.BaseRepository;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.AssistidoUtils.*;

@Service
public class AssistidoService {

    @Autowired
    AssistidoRepository repository;

    private static final String COLLECTION_NAME = "assistidos";

    public Map<String, Object> insert(Map<String, Object> data) {
        try {
            Map<String, Object> verifiedData = verifyDataToInsertAssistido(data);
            DocumentReference result = repository.save(COLLECTION_NAME, verifiedData);

            String assistidoId = result.getId();
            System.out.println("\nAssistido adicionado. ID: " + assistidoId);
            verifiedData.put("id", assistidoId);

            return verifiedData;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Object> findAll(String limit, String field, String filter, String value) {
        return null;
    }

    public Object findById(String id) {
        DocumentSnapshot snapshot = repository.findById(COLLECTION_NAME, id);
        return verifySnapshotToFindAssistidoById(snapshot);
    }

    public Boolean update(String id,  Map<String, Object> data) {
        return repository.update(COLLECTION_NAME, id, verifyDataToUpdateAssistido(data));
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

