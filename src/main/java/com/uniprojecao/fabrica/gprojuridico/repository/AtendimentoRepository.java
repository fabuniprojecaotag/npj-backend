package com.uniprojecao.fabrica.gprojuridico.repository;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Filter;
import com.uniprojecao.fabrica.gprojuridico.dto.min.AtendimentoMinDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.min.AtendimentoVinculadoAssistidoDTO;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.Atendimento;
import com.uniprojecao.fabrica.gprojuridico.models.autocomplete.AtendimentoAutocomplete;
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

    public List<AtendimentoMinDTO> findAll(@Nonnull Integer limit, @Nullable Filter queryFilter) {
        String[] columnList = {"area", "status", "envolvidos.assistido"};
        return findAll(collectionName, columnList, null, limit, queryFilter)
                .stream()
                .map(o -> (AtendimentoMinDTO) snapshotToAtendimento((DocumentSnapshot) o, true, false, false))
                .toList();
    }

    public List<AtendimentoAutocomplete> findAllMin(@Nonnull Integer limit, @Nullable Filter queryFilter) {
        return findAll(collectionName, null, null, limit, queryFilter)
                .stream()
                .map(o -> (AtendimentoAutocomplete) snapshotToAtendimento((DocumentSnapshot) o, false, true, false))
                .toList();
    }

    public List<AtendimentoVinculadoAssistidoDTO> findAllToAssistido(@Nonnull Integer limit, Filter queryFilter) {
        String[] columnList = {"area", "status", "envolvidos.assistido", "envolvidos.estagiario", "instante"};
        return findAll(collectionName, columnList, null, limit, queryFilter)
                .stream()
                .map(o -> (AtendimentoVinculadoAssistidoDTO) snapshotToAtendimento((DocumentSnapshot) o, false, false, true))
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
        return (Atendimento) snapshotToAtendimento(snapshot, false, false, false);
    }
}
