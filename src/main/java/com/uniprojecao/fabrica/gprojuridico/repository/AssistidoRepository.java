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
public class AssistidoRepository extends BaseRepository {

    @Autowired
    public Firestore firestore;

    private static final String collectionName = "assistidos";

    public Assistido save(String customId, Assistido data) {
        super.save(collectionName, customId, data);
        return data;
    }

    public List<AssistidoMinDTO> findAll(@Nonnull Integer limit, @Nullable QueryFilter queryFilter) {
        String[] list = {"nome", "email", "cpf", "quantidade.atendimentos", "quantidade.processos"};
        return super.findAll(collectionName, list, null, limit, queryFilter)
                .stream()
                .map(o -> (AssistidoMinDTO) snapshotToAssistido((DocumentSnapshot) o, true))
                .toList();
    }

    public Assistido findById(String id) {
        var snapshot = (DocumentSnapshot) super.findById(collectionName, null, id);
        return (Assistido) snapshotToAssistido(snapshot, false);
    }

    public void update(String id, Map<String, Object> data) {
        super.update(collectionName, id, data);
    }

    public void delete(String id) {
        super.delete(collectionName, id);
    }

    public void deleteAll(int limit, @Nullable QueryFilter queryFilter) {
        super.deleteAll(collectionName, null, limit, queryFilter);
    }
}
