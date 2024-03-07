package com.uniprojecao.fabrica.gprojuridico.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.uniprojecao.fabrica.gprojuridico.domains.enums.FilterType;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.filter;

@Repository
public class AtendimentoRepository extends BaseRepository {

    public List<QueryDocumentSnapshot> findAllMin(String collectionName, @Nullable Integer limit) {
        if (limit == null) limit = 20;

        try {
            ApiFuture<QuerySnapshot> future = firestore.collection(collectionName)
                    .select("id", "area", "status", "envolvidos").limit(limit).get();
            return future.get().getDocuments();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<QueryDocumentSnapshot> findAllMin(String collectionName, @Nullable Integer limit, @Nonnull String field,
                                                  @Nonnull FilterType filterType , @Nonnull String value) {
        if (limit == null) limit = 20;

        try {
            ApiFuture<QuerySnapshot> future = firestore.collection(collectionName).where(filter(field, filterType, value))
                    .select("id", "tipo", "status", "envolvidos.assistido.nome").limit(limit).get();
            return future.get().getDocuments();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
