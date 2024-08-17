package com.uniprojecao.fabrica.gprojuridico.repository;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Filter;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.Assistido;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.AssistidoCivil;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.AssistidoTrabalhista;
import com.uniprojecao.fabrica.gprojuridico.models.autocomplete.AssistidoAutocomplete;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.AssistidoUtils.snapshotToAssistido;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.ASSISTIDOS_COLLECTION;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.filterValidKeys;

@Repository
@DependsOn("baseRepository")
public class AssistidoRepository extends BaseRepository {

    private final String[] columnsForAssistidoAutoComplete = {"nome"};

    public Assistido insert(Assistido data) {
        BaseRepository.insert(ASSISTIDOS_COLLECTION, data.getCpf(), data);
        return data;
    }

    public List<AssistidoAutocomplete> findAllForAutoComplete(Integer limit, Filter queryFilter) {

        return findAll(ASSISTIDOS_COLLECTION, columnsForAssistidoAutoComplete, null, limit, queryFilter)
                .stream()
                .map(o -> (AssistidoAutocomplete) snapshotToAssistido((DocumentSnapshot) o, false, true))
                .toList();
    }

    public void update(String id, Map<String, Object> data, String clazz) {
        var validClazz = switch(clazz) {
            case "Trabalhista" -> AssistidoTrabalhista.class;
            case "Civil" -> AssistidoCivil.class;
            default -> throw new IllegalStateException("Unexpected value: " + clazz);
        };

        var filteredData = filterValidKeys(data, validClazz);

        BaseRepository.update(ASSISTIDOS_COLLECTION, id, filteredData);
    }
}
