package com.uniprojecao.fabrica.gprojuridico.services;

import com.google.cloud.firestore.DocumentSnapshot;
import com.uniprojecao.fabrica.gprojuridico.domains.MedidaJuridica;
import com.uniprojecao.fabrica.gprojuridico.domains.MedidaJuridicaModel;
import com.uniprojecao.fabrica.gprojuridico.repository.BaseRepository;
import com.uniprojecao.fabrica.gprojuridico.repository.MedidaJuridicaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.IdUtils.incrementId;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.MEDIDA_JURIDICA_COLLECTION;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.filterValidKeys;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.initFilter;
import static java.lang.Integer.parseInt;

@Service
public class MedidaJuridicaService extends BaseService {

    MedidaJuridicaRepository repository = new MedidaJuridicaRepository();
    private static final String collectionName = MEDIDA_JURIDICA_COLLECTION;

    public MedidaJuridicaService() {
        super(collectionName);
    }

    public MedidaJuridicaModel insert(MedidaJuridicaModel model) {
        String customId = defineId(model);
        BaseRepository.save(collectionName, customId, model);
        return model;
    }

    public void insertMultipleDemoData() {
        for (var model : MedidaJuridica.values()){
            insert(new MedidaJuridicaModel(null, model.getNormalizedValue(), null, model.getArea()));
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

    private String defineId(MedidaJuridicaModel model) {
        DocumentSnapshot doc = repository.findLast(); // Obtém o último documento
        String id = (doc != null) ? doc.getId() : null; // Armazena o id
        String newId = (id != null) ? incrementId(id) : "MEDJUR00001"; // Incrementa o id
        model.setId(newId);
        return newId;
    }
}
