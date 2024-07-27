package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.domains.MedidaJuridica;
import com.uniprojecao.fabrica.gprojuridico.domains.MedidaJuridicaModel;
import com.uniprojecao.fabrica.gprojuridico.repository.BaseRepository;
import com.uniprojecao.fabrica.gprojuridico.repository.MedidaJuridicaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.MEDIDAS_JURIDICAS_COLLECTION;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.filterValidKeys;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.initFilter;
import static java.lang.Integer.parseInt;

@Service
public class MedidaJuridicaService extends BaseService {

    MedidaJuridicaRepository repository = new MedidaJuridicaRepository();
    private static final String collectionName = MEDIDAS_JURIDICAS_COLLECTION;

    public MedidaJuridicaService() {
        super(collectionName);
    }

    public MedidaJuridicaModel insert(MedidaJuridicaModel model) {
        BaseRepository.save(collectionName, model.getNome(), model);
        return model;
    }

    public void insertMultipleDemoData() {
        for (var model : MedidaJuridica.values()){
            BaseRepository.save(collectionName, model.getNormalizedValue(), new MedidaJuridicaModel(model.getNormalizedValue(), null, model.getArea()));
        }
    }

    public List<MedidaJuridicaModel> findAll(String limit, String field, String filter, String value) {
        return repository.findAll(parseInt(limit), initFilter(field, filter, value));
    }

    public MedidaJuridicaModel findById(String id) {
        return repository.findById(id);
    }

    public void updateData(String id, Map<String, Object> data) {
        var filteredData = filterValidKeys(data, MedidaJuridicaModel.class);

        update(id, filteredData);
    }
}
