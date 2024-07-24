package com.uniprojecao.fabrica.gprojuridico.repository;

import com.google.cloud.firestore.DocumentSnapshot;
import com.uniprojecao.fabrica.gprojuridico.domains.MedidaJuridicaModel;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import jakarta.annotation.Nullable;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.MEDIDA_JURIDICA_COLLECTION;

@Repository
@DependsOn("baseRepository")
public class MedidaJuridicaRepository extends BaseRepository {

    private final String collectionName = MEDIDA_JURIDICA_COLLECTION;
    private final Class<MedidaJuridicaModel> type = MedidaJuridicaModel.class;

    public List<MedidaJuridicaModel> findAll(int limit, @Nullable QueryFilter queryFilter) {
        String[] columnList = {"area", "descricao"};
        return findAll(collectionName, columnList, type, limit, queryFilter)
                .stream()
                .map(o -> (MedidaJuridicaModel) o)
                .toList();
    }

    public MedidaJuridicaModel findById(String id) {
        var snapshot = (DocumentSnapshot) findById(collectionName, null, id);
        return snapshotToMedidaJuridica(snapshot);
    }

    private MedidaJuridicaModel snapshotToMedidaJuridica(DocumentSnapshot snapshot) {
        return snapshot != null ? snapshot.toObject(MedidaJuridicaModel.class) : null;
    }
}
