package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.domains.Autocomplete.AssistidoAutocomplete;
import com.uniprojecao.fabrica.gprojuridico.domains.assistido.AssistidoCivil;
import com.uniprojecao.fabrica.gprojuridico.domains.assistido.AssistidoTrabalhista;
import com.uniprojecao.fabrica.gprojuridico.dto.assistido.AssistidoDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.min.AssistidoMinDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.min.AtendimentoVinculadoAssistidoDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.min.ProcessoVinculado;
import com.uniprojecao.fabrica.gprojuridico.repository.AssistidoRepository;
import com.uniprojecao.fabrica.gprojuridico.repository.AtendimentoRepository;
import com.uniprojecao.fabrica.gprojuridico.repository.BaseRepository;
import com.uniprojecao.fabrica.gprojuridico.repository.ProcessoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.ASSISTIDOS_COLLECTION;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.ModelMapper.toDto;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.ModelMapper.toEntity;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.filterValidKeys;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.initFilter;
import static java.lang.Integer.parseInt;

@Service
public class AssistidoService extends BaseService {

    AssistidoRepository repository = new AssistidoRepository();
    private static final String collectionName = ASSISTIDOS_COLLECTION;

    public AssistidoService() {
        super(collectionName);
    }

    public AssistidoDTO insert(AssistidoDTO dto) {
        BaseRepository.save(collectionName, dto.getCpf(), toEntity(dto));
        return dto;
    }

    public List<AssistidoMinDTO> findAll(String limit, String field, String filter, String value) {
        return repository.findAll(parseInt(limit), initFilter(field, filter, value));
    }

    public List<AssistidoAutocomplete> findAllMin(String limit, String field, String filter, String value) {
        return repository.findAllMin(parseInt(limit), initFilter(field, filter, value));
    }

    public List<AtendimentoVinculadoAssistidoDTO> findAllAtendimentos(String id, String limit) {
        var atendimentoRepository = new AtendimentoRepository();
        return atendimentoRepository.findAllToAssistido(parseInt(limit),
                initFilter("envolvidos.assistido.id", "EQUAL", id));
    }

    public List<ProcessoVinculado> findAllProcessos(String id, String limit) {
        var processoRepository = new ProcessoRepository();
        return processoRepository.findAllToAssistido(parseInt(limit),
                initFilter("assistidoId", "EQUAL", id));
    }

    public AssistidoDTO findById(String id) {
        var assistido = repository.findById(id);
        return (assistido != null) ? toDto(assistido) : null;
    }

    public void update(String id, Map<String, Object> data, String clazz) {
        var validClazz = switch(clazz) {
            case "Trabalhista" -> AssistidoTrabalhista.class;
            case "Civil" -> AssistidoCivil.class;
            default -> throw new IllegalStateException("Unexpected value: " + clazz);
        };

        var filteredData = filterValidKeys(data, validClazz);

        update(id, filteredData);
    }
}