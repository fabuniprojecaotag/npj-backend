package com.uniprojecao.fabrica.gprojuridico.repository;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.uniprojecao.fabrica.gprojuridico.domains.assistido.Assistido;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.dto.min.AssistidoMinDTO;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.AssistidoUtils.snapshotToAssistido;

@Repository
public class AssistidoRepository {

    private static final String COLLECTION_NAME = "assistidos";

    public Assistido save(String customId, Assistido assistido) {
        BaseRepository.save(COLLECTION_NAME, customId, assistido);
        return assistido;
    }

    public List<AssistidoMinDTO> findAll(@Nonnull Integer limit, @Nullable QueryFilter queryFilter) {
        String[] columnList = {"nome", "email", "cpf", "quantidade.atendimentos", "quantidade.processos"};
        return BaseRepository.findAll(COLLECTION_NAME, columnList, null, limit, queryFilter)
                .stream()
                .map(o -> (AssistidoMinDTO) snapshotToAssistido((DocumentSnapshot) o, true))
                .toList();
    }

    public Assistido findById(String id) {
        var snapshot = (DocumentSnapshot) BaseRepository.findById(COLLECTION_NAME, null, id);
        return (Assistido) snapshotToAssistido(snapshot, false);
    }

    public void update(String id, Map<String, Object> data) {
        BaseRepository.update(COLLECTION_NAME, id, data);
    }

    public void delete(String id) {
        BaseRepository.delete(COLLECTION_NAME, id);
    }

    public void deleteAll(int limit, @Nullable QueryFilter queryFilter) {
        BaseRepository.deleteAll(COLLECTION_NAME, null, limit, queryFilter);
    }
}
