package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.dto.ProcessoDTO;
import com.uniprojecao.fabrica.gprojuridico.repository.ProcessoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.ProcessoUtils.dtoToProcesso;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.ProcessoUtils.processoToDto;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.initFilter;
import static java.lang.Integer.parseInt;

@Service
public class ProcessoService {

    @Autowired
    ProcessoRepository repository;
    private final String COLLECTION_NAME = "processos";

    public ProcessoDTO insert(ProcessoDTO dto) {
        repository.save(COLLECTION_NAME, dto.getNumero(), dtoToProcesso(dto));
        return dto;
    }

    public List<ProcessoDTO> findAll(String limit, String field, String filter, String value) {
        return repository.findAll(parseInt(limit), initFilter(field, filter, value))
                .stream()
                .map(e -> processoToDto(e))
                .toList();
    }

    public ProcessoDTO findById(String id) {
        return processoToDto(repository.findById(id));
    }

    public void update(String id, Map<String, Object> data) {
        repository.update(COLLECTION_NAME, id, data);
    }

    public void delete(String id) {
        repository.delete(COLLECTION_NAME, id);
    }

    public void deleteAll(String limit, String field, String filter, String value) {
        repository.deleteAll(COLLECTION_NAME, null, parseInt(limit), initFilter(field, filter, value));
    }
}
