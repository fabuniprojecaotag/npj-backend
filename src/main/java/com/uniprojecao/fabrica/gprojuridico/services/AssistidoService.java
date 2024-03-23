package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.dto.assistido.AssistidoDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.min.AssistidoMinDTO;
import com.uniprojecao.fabrica.gprojuridico.repository.AssistidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.AssistidoUtils.AssistidoToDTO;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.AssistidoUtils.dtoToAssistido;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.initFilter;
import static java.lang.Integer.parseInt;

@Service
public class AssistidoService {

    @Autowired
    AssistidoRepository repository;

    public AssistidoDTO insert(AssistidoDTO data) {
        repository.save(data.getCpf(), dtoToAssistido(data));
        return data;
    }

    public List<AssistidoMinDTO> findAll(String limit, String field, String filter, String value) {
        return repository.findAll(parseInt(limit), initFilter(field, filter, value));
    }

    public AssistidoDTO findById(String id) {
        var result = repository.findById(id);
        return AssistidoToDTO(result);
    }

    public void update(String id, Map<String, Object> data) {
        repository.update(id, data);
    }

    public void delete(String id) {
        repository.delete(id);
    }

    public void deleteAll(String limit, String field, String filter, String value) {
        repository.deleteAll(parseInt(limit), initFilter(field, filter, value));
    }
}
