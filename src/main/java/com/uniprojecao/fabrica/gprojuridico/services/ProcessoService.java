package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.dto.ProcessoDTO;
import com.uniprojecao.fabrica.gprojuridico.repository.BaseRepository;
import com.uniprojecao.fabrica.gprojuridico.repository.ProcessoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.PROCESSOS_COLLECTION;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.ModelMapper.toDto;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.ModelMapper.toEntity;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.initFilter;
import static java.lang.Integer.parseInt;

@Service
public class ProcessoService extends BaseService {

    ProcessoRepository repository = new ProcessoRepository();
    private static final String collectionName = PROCESSOS_COLLECTION;

    public ProcessoService() {
        super(collectionName);
    }

    public ProcessoDTO insert(ProcessoDTO dto) {
        BaseRepository.save(collectionName, dto.getNumero(), toEntity(dto));
        return dto;
    }

    public List<ProcessoDTO> findAll(String limit, String field, String filter, String value) {
        return repository.findAll(parseInt(limit), initFilter(field, filter, value))
                .stream()
                .map(processo -> toDto(processo))
                .toList();
    }

    public ProcessoDTO findByNumero(String numero) {
        var processo = repository.findByNumero(numero);
        return (processo != null) ? toDto(processo) : null;
    }
}
