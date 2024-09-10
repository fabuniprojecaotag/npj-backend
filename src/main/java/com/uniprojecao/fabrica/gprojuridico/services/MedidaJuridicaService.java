package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.models.MedidaJuridica;
import com.uniprojecao.fabrica.gprojuridico.repositories.FirestoreRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.IdService.defineId;
import static com.uniprojecao.fabrica.gprojuridico.utils.Constants.MEDIDAS_JURIDICAS_COLLECTION;

@Service
public class MedidaJuridicaService {

    private final FirestoreRepositoryImpl firestoreRepository = new FirestoreRepositoryImpl(MEDIDAS_JURIDICAS_COLLECTION);

    public MedidaJuridica insert(MedidaJuridica medidaJuridica) throws Exception {
        var medidaJuridicaWithNewId = (MedidaJuridica) defineId(medidaJuridica, MEDIDAS_JURIDICAS_COLLECTION, "MEDJUR");
        return (MedidaJuridica) firestoreRepository.insert(medidaJuridicaWithNewId.getId(), medidaJuridicaWithNewId);
    }

    public void update(String recordId, Map<String, Object> data) {
        firestoreRepository.update(recordId, data, MedidaJuridicaService.class);
    }
}
