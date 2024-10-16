package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.models.processo.Processo;
import org.springframework.stereotype.Service;

import static com.uniprojecao.fabrica.gprojuridico.utils.Constants.PROCESSOS_COLLECTION;

@Service
public class ProcessoService extends GenericService<Processo> {
    private static final String PREFIX = "PROC";

    public ProcessoService() {
        super(PROCESSOS_COLLECTION, PREFIX);
    }
}
