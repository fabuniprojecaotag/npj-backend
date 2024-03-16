package com.uniprojecao.fabrica.gprojuridico.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.uniprojecao.fabrica.gprojuridico.domains.assistido.Assistido;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.dto.min.AssistidoMinDTO;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.AssistidoUtils.snapshotToAssistido;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.filter;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.sleep;

@Repository
public class AssistidoRepository extends BaseRepository {

    @Autowired
    public Firestore firestore;

    private static final String collectionName = "assistidos";

    public Assistido saveWithCustomId(String customId, Assistido data) {
        try {
            firestore.collection(collectionName).document(customId).set(data).get();
            return data;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<AssistidoMinDTO> findAll(@Nonnull Integer limit, @Nullable QueryFilter queryFilter) {
        List<AssistidoMinDTO> list = new ArrayList<>();

        try {
            var result = getDocSnapshots(limit, queryFilter);
            for (QueryDocumentSnapshot document : result) {
                list.add((AssistidoMinDTO) snapshotToAssistido(document, true));
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<QueryDocumentSnapshot> getDocSnapshots(@Nonnull Integer limit, @Nullable QueryFilter queryFilter) {
        ApiFuture<QuerySnapshot> future;

        future = (queryFilter != null) ?
                firestore.collection(collectionName).where(filter(queryFilter)).select("nome", "email", "cpf", "quantidade.atendimentos", "quantidade.processos").limit(limit).get() :
                firestore.collection(collectionName).select("nome", "email", "cpf", "quantidade.atendimentos", "quantidade.processos").limit(limit).get();

        try {
            return future.get().getDocuments();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public Assistido findById(String id) {
        try {
            DocumentReference document = firestore.collection(collectionName).document(id);
            DocumentSnapshot snapshot = document.get().get();
            if (!snapshot.exists()) return null;
            return (Assistido) snapshotToAssistido(snapshot, false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean update(String id, Map<String, Object> data) {
        firestore.collection(collectionName).document(id).update(data);
        return true;
    }

    public Boolean delete(String id) {
        try {
            firestore.collection(collectionName).document(id).delete();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean deleteAll(Integer limit, @Nullable QueryFilter queryFilter) {
        var result = getDocSnapshots(limit, queryFilter);
        for (QueryDocumentSnapshot document : result) {
            document.getReference().delete();
            sleep(200); // Eventualmente será necessário considerar usar programação reativa no sistema, pois a deleção assíncrona deve garantir a deleção em massa por completo.
        }
        return true;
    }
}
