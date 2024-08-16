package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.models.atendimento.Atendimento;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.AtendimentoCivil;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.AtendimentoTrabalhista;
import com.uniprojecao.fabrica.gprojuridico.models.autocomplete.AtendimentoAutocomplete;
import com.uniprojecao.fabrica.gprojuridico.repository.AtendimentoRepository;
import com.uniprojecao.fabrica.gprojuridico.repository.BaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.IdUtils.defineId;
import static com.uniprojecao.fabrica.gprojuridico.services.QueryFilterService.getFilter;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.ATENDIMENTOS_COLLECTION;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.filterValidKeys;
import static java.lang.Integer.parseInt;

@Service
public class AtendimentoService extends BaseService {

    AtendimentoRepository repository = new AtendimentoRepository();
    private static final String collectionName = ATENDIMENTOS_COLLECTION;

    public AtendimentoService() {
        super(collectionName);
    }

    public Atendimento insert(Atendimento data) {
        data = (Atendimento) defineId(data, collectionName, "ATE");
        BaseRepository.save(collectionName, data.getId(), data);
        return data;
    }

    public List<AtendimentoAutocomplete> findAllMin(String limit, String field, String filter, String value) {
        return repository.findAllMin(parseInt(limit), getFilter(field, filter, value));
    }

    public void update(String id, Map<String, Object> data, String clazz) {
        var validClazz = switch(clazz) {
            case "Trabalhista" -> AtendimentoTrabalhista.class;
            case "Civil" -> AtendimentoCivil.class;
            default -> throw new IllegalStateException("Unexpected value: " + clazz);
        };

        var filteredData = filterValidKeys(data, validClazz);

        update(id, filteredData);
    }
}
