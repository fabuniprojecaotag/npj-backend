package com.uniprojecao.fabrica.gprojuridico.repository;

import com.google.cloud.firestore.DocumentSnapshot;
import com.uniprojecao.fabrica.gprojuridico.dto.min.ProcessoVinculado;
import com.uniprojecao.fabrica.gprojuridico.models.processo.Processo;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.QueryFilterService.getFilter;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.PROCESSOS_COLLECTION;

@Repository
@DependsOn("baseRepository")
public class ProcessoRepository extends BaseRepository {

    public Processo insert(Processo data) {
        BaseRepository.insert(PROCESSOS_COLLECTION, data.getNumero(), data);
        return data;
    }

    public List<ProcessoVinculado> findAllToAssistido(Integer limit, String id) {
        String[] columnList = {"vara", "status"};
        return findAll(PROCESSOS_COLLECTION, columnList, null, limit, getFilter("assistidoId", "EQUAL", id))
                .stream()
                .map(o -> snapshotToProcessoVinculado((DocumentSnapshot) o))
                .toList();
    }

    private ProcessoVinculado snapshotToProcessoVinculado(DocumentSnapshot snapshot) {
        return new ProcessoVinculado(
                snapshot.getId(),
                (String) snapshot.get("vara"),
                (String) snapshot.get("status")
        );
    }

    public void update(String id, Map<String, Object> data) {
        BaseRepository.update(PROCESSOS_COLLECTION, id, data);
    }
}
