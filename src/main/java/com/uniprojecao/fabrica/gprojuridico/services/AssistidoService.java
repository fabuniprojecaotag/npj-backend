package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.models.assistido.Assistido;
import org.springframework.stereotype.Service;

import static com.uniprojecao.fabrica.gprojuridico.utils.Constants.ASSISTIDOS_COLLECTION;

@Service
public class AssistidoService extends GenericService<Assistido> {
    private static final String PREFIX = "AS";

    public AssistidoService() {
        super(ASSISTIDOS_COLLECTION, PREFIX);
    }
}
