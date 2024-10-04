package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.models.MedidaJuridica;
import org.springframework.stereotype.Service;

import static com.uniprojecao.fabrica.gprojuridico.utils.Constants.MEDIDAS_JURIDICAS_COLLECTION;

@Service
public class MedidaJuridicaService extends GenericService<MedidaJuridica>{
    private static final String PREFIX = "MEDJUR";

    public MedidaJuridicaService() {
        super(MEDIDAS_JURIDICAS_COLLECTION, PREFIX);
    }
}
