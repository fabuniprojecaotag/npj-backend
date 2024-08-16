package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.models.MedidaJuridicaModel;
import com.uniprojecao.fabrica.gprojuridico.repository.BaseRepository;
import com.uniprojecao.fabrica.gprojuridico.repository.MedidaJuridicaRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.IdUtils.defineId;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.MEDIDAS_JURIDICAS_COLLECTION;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.filterValidKeys;

@Service
public class MedidaJuridicaService extends BaseService {

    MedidaJuridicaRepository repository = new MedidaJuridicaRepository();
    private static final String collectionName = MEDIDAS_JURIDICAS_COLLECTION;

    public MedidaJuridicaService() {
        super(collectionName);
    }

    public MedidaJuridicaModel insert(MedidaJuridicaModel model) {
        model = (MedidaJuridicaModel) defineId(model, collectionName, "MEDJUR");
        BaseRepository.save(collectionName, model.getId(), model);
        return model;
    }

    public void updateData(String id, Map<String, Object> data) {
        var filteredData = filterValidKeys(data, MedidaJuridicaModel.class);

        update(id, filteredData);
    }
}
