package com.uniprojecao.fabrica.gprojuridico.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.uniprojecao.fabrica.gprojuridico.domains.enums.FilterType;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.services.utils.Utils;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static com.google.cloud.firestore.Query.Direction.DESCENDING;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.filter;

@Repository
@Primary
public class BaseRepository {

    @Autowired
    public Firestore firestore;

    public DocumentReference save(String collectionName, Map<String, Object> data) {

        try {
            return firestore.collection(collectionName).add(data).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DocumentReference save(String collectionName, Object data) {
        try {
            return firestore.collection(collectionName).add(data).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public WriteResult saveWithCustomId(String collectionName, String CustomId, Object data) {

        try {
            return firestore.collection(collectionName).document(CustomId).set(data).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<QueryDocumentSnapshot> findAll(String collectionName, @Nullable Integer limit) {
        if (limit == null) limit = 20;

        try {
            ApiFuture<QuerySnapshot> future = firestore.collection(collectionName).limit(limit).get();
            return future.get().getDocuments();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<QueryDocumentSnapshot> findAll(String collectionName, @Nonnull Integer limit, @Nullable QueryFilter queryFilter) {

        try {
            ApiFuture<QuerySnapshot> future = firestore.collection(collectionName).where(filter(queryFilter)).limit(limit).get();
            return future.get().getDocuments();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DocumentSnapshot findLast(String collectionName) {
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection(collectionName).orderBy("instante", DESCENDING).limit(1).get();
            var list = future.get().getDocuments();
            return list.get(0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DocumentSnapshot findById(String collectionName, String id) {
        try {
            DocumentReference document = firestore.collection(collectionName).document(id);
            DocumentSnapshot snapshot = document.get().get();
            Utils.verifySnapshotIfDocumentExists(snapshot);
            return snapshot;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean update(String collectionName, String id, Map<String, Object> data) {
        try {
            DocumentReference document = firestore.collection(collectionName).document(id);
            DocumentSnapshot snapshot = findById(collectionName, id);
            Utils.verifySnapshotIfDocumentExists(snapshot);
            document.update(data);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean delete(String collectionName, String id) {
        try {
            DocumentReference document = firestore.collection(collectionName).document(id);
            DocumentSnapshot snapshot = document.get().get();
            Utils.verifySnapshotIfDocumentExists(snapshot);
            document.delete();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean deleteAll(String collectionName, Integer limit) {
        List<QueryDocumentSnapshot> result = findAll(collectionName, limit);
        for (QueryDocumentSnapshot document : result) {
            document.getReference().delete();
        }
        return true;
    }

    public Boolean deleteAll(String collectionName, Integer limit, @Nullable QueryFilter queryFilter) {
        List<QueryDocumentSnapshot> result = findAll(collectionName, limit, queryFilter);
        for (QueryDocumentSnapshot document : result) {
            document.getReference().delete();
        }
        return true;
    }
}
