package com.uniprojecao.fabrica.gprojuridico.repository;

import com.google.cloud.firestore.DocumentSnapshot;
import com.uniprojecao.fabrica.gprojuridico.domains.atendimento.Atendimento;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.dto.min.AtendimentoMinDTO;
import com.uniprojecao.fabrica.gprojuridico.projections.AtendimentosDoAssistidoDTO;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.google.cloud.firestore.Query.Direction.DESCENDING;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.AtendimentoUtils.snapshotToAtendimento;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.ATENDIMENTOS_COLLECTION;

@Repository
@DependsOn("baseRepository")
public class AtendimentoRepository extends BaseRepository {

    private final String collectionName = ATENDIMENTOS_COLLECTION;

    public List<AtendimentoMinDTO> findAll(@Nonnull Integer limit, @Nullable QueryFilter queryFilter) {
        String[] columnList = {"area", "status", "envolvidos.assistido"};
        return findAll(collectionName, columnList, null, limit, queryFilter)
                .stream()
                .map(o -> (AtendimentoMinDTO) snapshotToAtendimento((DocumentSnapshot) o, true, false))
                .toList();
    }

    public List<AtendimentosDoAssistidoDTO> findAllToAssistido(@Nonnull Integer limit, QueryFilter queryFilter) {
        String[] columnList = {"area", "status", "envolvidos.assistido", "envolvidos.estagiario", "instante"};
        return findAll(collectionName, columnList, null, limit, queryFilter)
                .stream()
                .map(o -> (AtendimentosDoAssistidoDTO) snapshotToAtendimento((DocumentSnapshot) o, false, true))
                .toList();
    }

    public DocumentSnapshot findLast() {
        try {
            var list = firestore
                    .collection(collectionName)
                    .orderBy("instante", DESCENDING)
                    .limit(1)
                    .get()
                    .get()
                    .getDocuments();
            return (!list.isEmpty()) ? list.get(0) : null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Atendimento findById(String id) {
        var snapshot = (DocumentSnapshot) findById(collectionName, null, id);
        return (Atendimento) snapshotToAtendimento(snapshot, false, false);
    }
}
