package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.domains.processo.Processo;
import com.uniprojecao.fabrica.gprojuridico.repository.BaseRepository;
import com.uniprojecao.fabrica.gprojuridico.repository.ProcessoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.PROCESSOS_COLLECTION;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.initFilter;
import static java.lang.Integer.parseInt;

@Service
public class ProcessoService extends BaseService {

    ProcessoRepository repository = new ProcessoRepository();
    private static final String collectionName = PROCESSOS_COLLECTION;

    public ProcessoService() {
        super(collectionName);
    }

    public Processo insert(Processo data) {
        BaseRepository.save(collectionName, data.getNumero(), data);
        return data;
    }

    public List<Processo> findAll(String limit, String field, String filter, String value) {
        return repository.findAll(parseInt(limit), initFilter(field, filter, value));
    }

    public Processo findByNumero(String numero) {
        return repository.findByNumero(numero);
    }
}
