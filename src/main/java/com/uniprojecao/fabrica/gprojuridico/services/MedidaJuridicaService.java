package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.models.MedidaJuridica;
import com.uniprojecao.fabrica.gprojuridico.repositories.FirestoreRepository;
import org.springframework.stereotype.Service;

import static com.uniprojecao.fabrica.gprojuridico.services.IdService.defineId;
import static com.uniprojecao.fabrica.gprojuridico.utils.Constants.MEDIDAS_JURIDICAS_COLLECTION;

@Service
public class MedidaJuridicaService {

    public MedidaJuridica insert(MedidaJuridica data) throws Exception {
        data = (MedidaJuridica) defineId(data, MEDIDAS_JURIDICAS_COLLECTION, "MEDJUR");
        return (MedidaJuridica) FirestoreRepository.insert(MEDIDAS_JURIDICAS_COLLECTION, data);
    }
}
