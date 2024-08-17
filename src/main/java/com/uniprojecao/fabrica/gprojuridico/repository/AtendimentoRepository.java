package com.uniprojecao.fabrica.gprojuridico.repository;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Filter;
import com.uniprojecao.fabrica.gprojuridico.dto.min.AtendimentoVinculado;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.AtendimentoCivil;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.AtendimentoTrabalhista;
import com.uniprojecao.fabrica.gprojuridico.models.autocomplete.AtendimentoAutocomplete;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.QueryFilterService.getFilter;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.AtendimentoUtils.snapshotToAtendimento;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.ATENDIMENTOS_COLLECTION;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.filterValidKeys;

@Repository
@DependsOn("baseRepository")
public class AtendimentoRepository extends BaseRepository {

    private final String[] columnsForAtendimentoVinculado = {
            "area",
            "status",
            "envolvidos.assistido",
            "envolvidos.estagiario",
            "instante"
    };

    public List<AtendimentoAutocomplete> findAllMin(Integer limit, Filter queryFilter) {
        return findAll(ATENDIMENTOS_COLLECTION, null, null, limit, queryFilter)
                .stream()
                .map(o -> (AtendimentoAutocomplete) snapshotToAtendimento((DocumentSnapshot) o, false, true, false))
                .toList();
    }

    public List<AtendimentoVinculado> findAllToAssistido(Integer limit, String id) {

        return findAll(ATENDIMENTOS_COLLECTION, columnsForAtendimentoVinculado, null, limit, getFilter("envolvidos.assistido.id", "EQUAL", id))
                .stream()
                .map(o -> (AtendimentoVinculado) snapshotToAtendimento((DocumentSnapshot) o, false, false, true))
                .toList();
    }

    public void update(String id, Map<String, Object> data, String clazz) {
        var validClazz = switch(clazz) {
            case "Trabalhista" -> AtendimentoTrabalhista.class;
            case "Civil" -> AtendimentoCivil.class;
            default -> throw new IllegalStateException("Unexpected value: " + clazz);
        };

        var filteredData = filterValidKeys(data, validClazz);

        BaseRepository.update(ATENDIMENTOS_COLLECTION, id, filteredData);
    }
}
