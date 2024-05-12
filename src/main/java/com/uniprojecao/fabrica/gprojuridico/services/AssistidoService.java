package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.dto.assistido.AssistidoDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.min.AssistidoMinDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.min.AtendimentoVinculadoAssistidoDTO;
import com.uniprojecao.fabrica.gprojuridico.repository.AssistidoRepository;
import com.uniprojecao.fabrica.gprojuridico.repository.AtendimentoRepository;
import com.uniprojecao.fabrica.gprojuridico.repository.BaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.ASSISTIDOS_COLLECTION;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.ModelMapper.toDto;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.ModelMapper.toEntity;
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

    public List<AtendimentoVinculadoAssistidoDTO> findAllAtendimentos(String id, String limit) {
        var atendimentoRepository = new AtendimentoRepository();
        return atendimentoRepository.findAllToAssistido(parseInt(limit),
                initFilter("envolvidos.assistido.id", "EQUAL", id));
    }

    public AssistidoDTO findById(String id) {
        var assistido = repository.findById(id);
        return (assistido != null) ? toDto(assistido) : null;
    }
}