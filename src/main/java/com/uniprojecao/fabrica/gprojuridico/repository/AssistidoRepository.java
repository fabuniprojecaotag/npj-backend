package com.uniprojecao.fabrica.gprojuridico.repository;

import com.google.cloud.firestore.DocumentSnapshot;
import com.uniprojecao.fabrica.gprojuridico.domains.assistido.Assistido;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.dto.min.AssistidoMinDTO;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.AssistidoUtils.snapshotToAssistido;

@Repository
public class AssistidoRepository extends BaseRepository {

    private static final String COLLECTION_NAME = "assistidos";

    public List<AssistidoMinDTO> findAll(@Nonnull Integer limit, @Nullable QueryFilter queryFilter) {
        String[] columnList = {"nome", "email", "quantidade.atendimentos", "quantidade.processos"};
        return findAll(COLLECTION_NAME, columnList, null, limit, queryFilter)
                .stream()
                .map(o -> (AssistidoMinDTO) snapshotToAssistido((DocumentSnapshot) o, true))
                .toList();
    }

    public Assistido findById(String id) {
        var snapshot = (DocumentSnapshot) findById(COLLECTION_NAME, null, id);
        return (Assistido) snapshotToAssistido(snapshot, false);
    }
}
