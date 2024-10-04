package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.models.atendimento.Atendimento;
import org.springframework.stereotype.Service;

import static com.uniprojecao.fabrica.gprojuridico.utils.Constants.ATENDIMENTOS_COLLECTION;

@Service
public class AtendimentoService extends GenericService<Atendimento> {
    private static final String PREFIX = "ATE";

    public AtendimentoService() {
        super(ATENDIMENTOS_COLLECTION, PREFIX);
    }
}

