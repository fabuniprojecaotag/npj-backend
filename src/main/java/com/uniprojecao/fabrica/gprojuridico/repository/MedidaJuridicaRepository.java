package com.uniprojecao.fabrica.gprojuridico.repository;

import com.uniprojecao.fabrica.gprojuridico.models.MedidaJuridicaModel;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.MEDIDAS_JURIDICAS_COLLECTION;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.filterValidKeys;

@Repository
@DependsOn("baseRepository")
public class MedidaJuridicaRepository extends BaseRepository {

    public void update(String id, Map<String, Object> data) {
        var filteredData = filterValidKeys(data, MedidaJuridicaModel.class);

        BaseRepository.update(MEDIDAS_JURIDICAS_COLLECTION, id, filteredData);
    }
}
