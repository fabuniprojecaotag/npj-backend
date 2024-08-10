package com.uniprojecao.fabrica.gprojuridico.services;

import com.google.cloud.firestore.DocumentSnapshot;
import com.uniprojecao.fabrica.gprojuridico.models.autocomplete.AtendimentoAutocomplete;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.Atendimento;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.AtendimentoCivil;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.AtendimentoTrabalhista;
import com.uniprojecao.fabrica.gprojuridico.dto.min.AtendimentoMinDTO;
import com.uniprojecao.fabrica.gprojuridico.repository.AtendimentoRepository;
import com.uniprojecao.fabrica.gprojuridico.repository.BaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.IdUtils.generateId;
import static com.uniprojecao.fabrica.gprojuridico.services.IdUtils.incrementId;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.ATENDIMENTOS_COLLECTION;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.filterValidKeys;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.initFilter;
import static java.lang.Integer.parseInt;

@Service
public class AtendimentoService extends BaseService {

    AtendimentoRepository repository = new AtendimentoRepository();
    private static final String collectionName = ATENDIMENTOS_COLLECTION;

    public AtendimentoService() {
        super(collectionName);
    }

    private String defineId(Atendimento dto) {
        DocumentSnapshot doc = repository.findLast(); // Obtém o último documento
        String id = (doc != null) ? doc.getId() : null; // Armazena o id
        String newId = (id != null) ? incrementId(id) : generateId("ATE"); // Incrementa o id
        dto.setId(newId);
        return newId;
    }

    public Atendimento insert(Atendimento data) {
        String customId = defineId(data);
        BaseRepository.save(collectionName, customId, data);
        return data;
    }

    public List<AtendimentoMinDTO> findAll(String limit, String field, String filter, String value) {
        return repository.findAll(parseInt(limit), initFilter(field, filter, value));
    }

    public List<AtendimentoAutocomplete> findAllMin(String limit, String field, String filter, String value) {
        return repository.findAllMin(parseInt(limit), initFilter(field, filter, value));
    }

    public Atendimento findById(String id) {
        return repository.findById(id);
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
