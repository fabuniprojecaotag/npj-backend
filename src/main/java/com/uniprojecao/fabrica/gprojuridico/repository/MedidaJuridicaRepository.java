package com.uniprojecao.fabrica.gprojuridico.repository;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldPath;
import com.uniprojecao.fabrica.gprojuridico.models.MedidaJuridicaModel;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import jakarta.annotation.Nullable;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.google.cloud.firestore.Query.Direction.DESCENDING;
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

    public DocumentSnapshot findLast() {
        try {
            var list = firestore
                    .collection(collectionName)
                    .orderBy(FieldPath.documentId(), DESCENDING)
                    .limit(1)
                    .get()
                    .get()
                    .getDocuments();
            return (!list.isEmpty()) ? list.get(0) : null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public MedidaJuridicaModel findById(String id) {
        var snapshot = (DocumentSnapshot) findById(collectionName, null, id);
        return snapshotToMedidaJuridica(snapshot);
    }

    private MedidaJuridicaModel snapshotToMedidaJuridica(DocumentSnapshot snapshot) {
        return snapshot != null ? snapshot.toObject(MedidaJuridicaModel.class) : null;
    }
}
