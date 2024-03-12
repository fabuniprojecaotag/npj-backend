package com.uniprojecao.fabrica.gprojuridico.services;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.uniprojecao.fabrica.gprojuridico.domains.atendimento.Atendimento;
import com.uniprojecao.fabrica.gprojuridico.domains.enums.FilterType;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.repository.AtendimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.AtendimentoUtils.convertSnapshotToCorrespondingAtendimentoDTO;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.AtendimentoUtils.setAndReturnId;

@Service
public class AtendimentoService {

    @Autowired
    AtendimentoRepository repository;

    private static final String COLLECTION_NAME = "atendimentos";

    public Map<String, Object> insert(Atendimento data) {
        DocumentSnapshot doc = repository.findLast(COLLECTION_NAME);
        String id = (String) doc.get("id");
        String customId = doc.exists() ? setAndReturnId(data, id) : setAndReturnId(data, null);
        repository.saveWithCustomId(COLLECTION_NAME, customId, data);

        return Map.of(
                "object", data,
                "id", customId
        );
    }

    public List<Object> findAll(String limit, String field, String filter, String value) {
        return null;
    }

    public Object findById(String id) {
        DocumentSnapshot snapshot = repository.findById(COLLECTION_NAME, id);
        return convertSnapshotToCorrespondingAtendimentoDTO(snapshot, false);
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

    public Boolean deleteAll(String limit, String field, String filter, String value) {
        boolean useQueryParams =
                !(field.isEmpty()) &&
                !(filter.isEmpty()) &&
                !(value.isEmpty());

        QueryFilter queryFilter = (useQueryParams) ? new QueryFilter(field, value, FilterType.valueOf(filter)) : null;
        return repository.deleteAll(COLLECTION_NAME, Integer.parseInt(limit), queryFilter);
    }
}
