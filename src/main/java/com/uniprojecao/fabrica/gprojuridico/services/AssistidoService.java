package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.domains.enums.FilterType;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.dto.assistido.AssistidoDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.min.AssistidoMinDTO;
import com.uniprojecao.fabrica.gprojuridico.repository.AssistidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.AssistidoUtils.convertAssistidoToDTO;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.AssistidoUtils.passDtoToEntity;

@Service
public class AssistidoService {

    @Autowired
    AssistidoRepository repository;

    public AssistidoDTO insert(AssistidoDTO data) {
        var result = repository.saveWithCustomId(data.getCpf(), passDtoToEntity(data));
        return convertAssistidoToDTO(result);
    }

    public List<AssistidoMinDTO> findAll(String limit, String field, String filter, String value) {
        boolean useQueryParams =
                !(field.isEmpty()) &&
                        !(filter.isEmpty()) &&
                        !(value.isEmpty());

        QueryFilter queryFilter = (useQueryParams) ? new QueryFilter(field, value, FilterType.valueOf(filter)) : null;

        return repository.findAll(Integer.parseInt(limit), queryFilter);
    }

    public AssistidoDTO findById(String id) {
        var result = repository.findById(id);
        return convertAssistidoToDTO(result);
    }

    public void update(String id, Map<String, Object> data) {
        repository.update(id, data);
    }

    public void delete(String id) {
        repository.delete(id);
    }

    public void deleteAll(String limit, String field, String filter, String value) {
        boolean useQueryParams =
                !(field.isEmpty()) &&
                        !(filter.isEmpty()) &&
                        !(value.isEmpty());

        QueryFilter queryFilter = (useQueryParams) ? new QueryFilter(field, value, FilterType.valueOf(filter)) : null;
        repository.deleteAll(Integer.parseInt(limit), queryFilter);
    }
}
