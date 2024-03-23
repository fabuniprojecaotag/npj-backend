package com.uniprojecao.fabrica.gprojuridico.services;

import com.google.cloud.firestore.DocumentSnapshot;
import com.uniprojecao.fabrica.gprojuridico.dto.atendimento.AtendimentoDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.min.AtendimentoMinDTO;
import com.uniprojecao.fabrica.gprojuridico.repository.AtendimentoRepository;
import com.uniprojecao.fabrica.gprojuridico.repository.ProcessoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.AtendimentoUtils.*;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.initFilter;
import static java.lang.Integer.parseInt;

@Service
public class AtendimentoService {

    @Autowired
    AtendimentoRepository repository;

    public AtendimentoDTO insert(AtendimentoDTO data) {
        DocumentSnapshot doc = repository.findLast();
        String id = (String) doc.get("id");
        String customId = doc.exists() ? setAndReturnId(data, id) : setAndReturnId(data, null);
        repository.save(customId, dtoToAtendimento(data));

        data.setId(customId);
        return data;
    }

    public List<AtendimentoMinDTO> findAll(String limit, String field, String filter, String value) {
        return repository.findAll(parseInt(limit), initFilter(field, filter, value));
    }

    public AtendimentoDTO findById(String id) {
        var result = repository.findById(id);
        return atendimentoToDTO(result);
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
