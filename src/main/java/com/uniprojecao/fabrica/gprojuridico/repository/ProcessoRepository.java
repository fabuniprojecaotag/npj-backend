package com.uniprojecao.fabrica.gprojuridico.repository;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Filter;
import com.uniprojecao.fabrica.gprojuridico.dto.min.ProcessoVinculado;
import com.uniprojecao.fabrica.gprojuridico.models.processo.Processo;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.PROCESSOS_COLLECTION;

@Repository
@DependsOn("baseRepository")
public class ProcessoRepository extends BaseRepository {

    private final String collectionName = PROCESSOS_COLLECTION;
    private final Class<Processo> TYPE = Processo.class;

    public List<Processo> findAll(int limit, @Nullable Filter queryFilter) {
        String[] columnList = new String[]{"numero", "atendimentoId", "nome", "dataDistribuicao", "vara", "forum", "status"};
        return findAll(collectionName, columnList, TYPE, limit, queryFilter)
                .stream()
                .map(o -> (Processo) o)
                .toList();
    }

    public List<ProcessoVinculado> findAllToAssistido(@Nonnull Integer limit, Filter queryFilter) {
        String[] columnList = {"vara", "status"};
        return findAll(collectionName, columnList, null, limit, queryFilter)
                .stream()
                .map(o -> snapshotToProcessoVinculado((DocumentSnapshot) o))
                .toList();
    }

    public Processo findByNumero(String numero) {
        return (Processo) findById(collectionName, TYPE, numero);
    }

    private ProcessoVinculado snapshotToProcessoVinculado(DocumentSnapshot snapshot) {
        return new ProcessoVinculado(
                snapshot.getId(),
                (String) snapshot.get("vara"),
                (String) snapshot.get("status")
        );
    }
}
