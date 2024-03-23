package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.domains.processo.Processo;
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

    public Processo insert(ProcessoDTO data) {
        return repository.save(dtoToProcesso(data));
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
        repository.update(id, data);
    }

    public void delete(String id) {
        repository.delete(id);
    }

    public void deleteAll(String limit, String field, String filter, String value) {
        repository.deleteAll(parseInt(limit), initFilter(field, filter, value));
    }
}
