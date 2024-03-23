package com.uniprojecao.fabrica.gprojuridico.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.google.cloud.firestore.Query.Direction.DESCENDING;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.filter;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.sleep;

@Repository
@Primary
public class BaseRepository {

    @Autowired
    public static Firestore firestore; // TODO: Conferir se o modificador de acesso deste atributo fica público ou não.

    static Object save(String collectionName, Class<?> type, Object data) {
        try {
            DocumentReference result = firestore.collection(collectionName).add(data).get();
            DocumentSnapshot snapshot = result.get().get();
            return snapshot.toObject(type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static WriteResult save(String collectionName, String CustomId, Object data) {
        try {
            return firestore.collection(collectionName).document(CustomId).set(data).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static List<Object> findAll(String collectionName, @Nullable String[] fields, @Nullable Class<?> type, @Nonnull Integer limit, @Nullable QueryFilter queryFilter) {
        List<Object> list = new ArrayList<>();

        try {
            var result = getDocSnapshots(collectionName, fields, limit, queryFilter);
            for (QueryDocumentSnapshot document : result) {
                if (type != null) list.add(document.toObject(type));
                else list.add(document);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static DocumentSnapshot findLast(String collectionName) {
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection(collectionName).orderBy("instante", DESCENDING).limit(1).get();
            var list = future.get().getDocuments();
            return list.get(0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static Object findById(String collectionName, @Nullable Class<?> type, String id) {
        try {
            DocumentReference document = firestore.collection(collectionName).document(id);
            DocumentSnapshot snapshot = document.get().get();
            if (!snapshot.exists()) return null;
            if (type != null) return snapshot.toObject(type);
            return snapshot;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static Boolean update(String collectionName, String id, Map<String, Object> data) {
        firestore.collection(collectionName).document(id).update(data);
        return true;
    }

    static Boolean delete(String collectionName, String id) {
        firestore.collection(collectionName).document(id).delete();
        return true;
    }

    static Boolean deleteAll(String collectionName, String[] list, Integer limit, @Nullable QueryFilter queryFilter) {
        var result = getDocSnapshots(collectionName, list, limit, queryFilter);
        for (QueryDocumentSnapshot document : result) {
            document.getReference().delete();
            sleep(200); // Eventualmente será necessário considerar usar programação reativa no sistema, pois a deleção assíncrona deve garantir a deleção em massa por completo.
        }
        return true;
    }

    static List<QueryDocumentSnapshot> getDocSnapshots(String collectionName, @Nullable String[] list, @Nonnull Integer limit, @Nullable QueryFilter queryFilter) {
        if (list == null) list = new String[]{"id"};

        ApiFuture<QuerySnapshot> future;

        future = (queryFilter != null) ?
                firestore.collection(collectionName).where(filter(queryFilter)).select(list).limit(limit).get() :
                firestore.collection(collectionName).select(list).limit(limit).get();

        try {
            return future.get().getDocuments();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
