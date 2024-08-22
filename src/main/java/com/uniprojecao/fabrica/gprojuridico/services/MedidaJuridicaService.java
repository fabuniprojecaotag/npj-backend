package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.models.MedidaJuridicaModel;
import com.uniprojecao.fabrica.gprojuridico.repositories.FirestoreRepository;
import org.springframework.stereotype.Service;

import static com.uniprojecao.fabrica.gprojuridico.services.IdService.defineId;
import static com.uniprojecao.fabrica.gprojuridico.utils.Constants.MEDIDAS_JURIDICAS_COLLECTION;

@Service
public class MedidaJuridicaService {

    public MedidaJuridicaModel insert(MedidaJuridicaModel model) {
        model = (MedidaJuridicaModel) defineId(model, MEDIDAS_JURIDICAS_COLLECTION, "MEDJUR");
        FirestoreRepository.insert(MEDIDAS_JURIDICAS_COLLECTION, model.getId(), model);
        return model;
    }
}
