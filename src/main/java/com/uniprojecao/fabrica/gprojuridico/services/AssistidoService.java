package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.domains.enums.FilterType;
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
    BaseRepository repository;

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
        FilterType filterType = FilterType.valueOf(filter);
        List<QueryDocumentSnapshot> result = repository.findAll(COLLECTION_NAME, Integer.parseInt(limit), field, filterType, value);
        List<Object> list = new ArrayList<>();

        for (QueryDocumentSnapshot document : result) {
            list.add(convertSnapshotToCorrespondingAssistidoModel(document));
        }

        return list;
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
        FilterType filterType = FilterType.valueOf(filter);
        return repository.deleteAll(COLLECTION_NAME, Integer.parseInt(limit), field, filterType, value);
    }
}

