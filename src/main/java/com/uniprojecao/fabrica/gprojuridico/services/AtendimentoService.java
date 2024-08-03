package com.uniprojecao.fabrica.gprojuridico.services;

import com.google.cloud.firestore.DocumentSnapshot;
import com.uniprojecao.fabrica.gprojuridico.domains.Autocomplete.AtendimentoAutocomplete;
import com.uniprojecao.fabrica.gprojuridico.domains.atendimento.AtendimentoCivil;
import com.uniprojecao.fabrica.gprojuridico.domains.atendimento.AtendimentoTrabalhista;
import com.uniprojecao.fabrica.gprojuridico.dto.atendimento.AtendimentoDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.min.AtendimentoMinDTO;
import com.uniprojecao.fabrica.gprojuridico.repository.AtendimentoRepository;
import com.uniprojecao.fabrica.gprojuridico.repository.BaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.IdUtils.generateId;
import static com.uniprojecao.fabrica.gprojuridico.services.IdUtils.incrementId;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.ATENDIMENTOS_COLLECTION;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.ManualMapper.toDto;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.ManualMapper.toEntity;
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

    private String defineId(AtendimentoDTO dto) {
        DocumentSnapshot doc = repository.findLast(); // Obtém o último documento
        String id = (doc != null) ? doc.getId() : null; // Armazena o id
        String newId = (id != null) ? incrementId(id) : generateId("ATE"); // Incrementa o id
        dto.setId(newId);
        return newId;
    }

    public AtendimentoDTO insert(AtendimentoDTO dto) {
        String customId = defineId(dto);
        BaseRepository.save(collectionName, customId, toEntity(dto));
        return dto;
    }

    public List<AtendimentoMinDTO> findAll(String limit, String field, String filter, String value) {
        return repository.findAll(parseInt(limit), initFilter(field, filter, value));
    }

    public List<AtendimentoAutocomplete> findAllMin(String limit, String field, String filter, String value) {
        return repository.findAllMin(parseInt(limit), initFilter(field, filter, value));
    }

    public AtendimentoDTO findById(String id) {
        var atendimento = repository.findById(id);
        return (atendimento != null) ? toDto(atendimento) : null;
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
