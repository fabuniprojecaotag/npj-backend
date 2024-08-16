package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.models.processo.Processo;
import com.uniprojecao.fabrica.gprojuridico.repository.BaseRepository;
import org.springframework.stereotype.Service;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.PROCESSOS_COLLECTION;

@Service
public class ProcessoService extends BaseService {

    private static final String collectionName = PROCESSOS_COLLECTION;

    public ProcessoService() {
        super(collectionName);
    }

    public Processo insert(Processo data) {
        BaseRepository.save(collectionName, data.getNumero(), data);
        return data;
    }
}
