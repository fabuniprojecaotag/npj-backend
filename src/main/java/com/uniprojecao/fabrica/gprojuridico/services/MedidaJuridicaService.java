package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.models.MedidaJuridicaModel;
import com.uniprojecao.fabrica.gprojuridico.repository.BaseRepository;
import org.springframework.stereotype.Service;

import static com.uniprojecao.fabrica.gprojuridico.services.IdService.defineId;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.MEDIDAS_JURIDICAS_COLLECTION;

@Service
public class MedidaJuridicaService {

    public MedidaJuridicaModel insert(MedidaJuridicaModel model) {
        model = (MedidaJuridicaModel) defineId(model, MEDIDAS_JURIDICAS_COLLECTION, "MEDJUR");
        BaseRepository.insert(MEDIDAS_JURIDICAS_COLLECTION, model.getId(), model);
        return model;
    }
}
