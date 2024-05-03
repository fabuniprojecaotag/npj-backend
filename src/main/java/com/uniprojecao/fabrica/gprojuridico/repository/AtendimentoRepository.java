package com.uniprojecao.fabrica.gprojuridico.repository;

import com.google.cloud.firestore.DocumentSnapshot;
import com.uniprojecao.fabrica.gprojuridico.domains.atendimento.Atendimento;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.dto.min.AtendimentoMinDTO;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.AtendimentoUtils.snapshotToAtendimento;

@Repository
public class AtendimentoRepository extends BaseRepository {

    private final String COLLECTION_NAME = "atendimentos";

    public List<AtendimentoMinDTO> findAll(@Nonnull Integer limit, @Nullable QueryFilter queryFilter) {
        String[] columnList = {"area", "status", "envolvidos.assistido"};
        return findAll(COLLECTION_NAME, columnList, null, limit, queryFilter)
                .stream()
                .map(o -> (AtendimentoMinDTO) snapshotToAtendimento((DocumentSnapshot) o, true))
                .toList();
    }

    public DocumentSnapshot findLast() {
        return findLast(COLLECTION_NAME);
    }

    public Atendimento findById(String id) {
        var snapshot = (DocumentSnapshot) findById(COLLECTION_NAME, null, id);
        return (Atendimento) snapshotToAtendimento(snapshot, false);
    }
}
