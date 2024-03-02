package app.web.gprojuridico.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static app.web.gprojuridico.services.utils.Utils.verifySnapshotIfDocumentExists;

@Repository
public class BaseRepository implements CrudRepository {

    @Autowired
    Firestore firestore;

    @Override
    public DocumentReference save(String collectionName, Map<String, Object> data) {

        try {
            return firestore.collection(collectionName).add(data).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DocumentReference save(String collectionName, Object data) {
        try {
            return firestore.collection(collectionName).add(data).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<QueryDocumentSnapshot> findAll(String collectionName, @Nullable Integer limit) {
        if (limit == null) limit = 20;
        try {
            // asynchronously retrieve multiple documents
            ApiFuture<QuerySnapshot> future = firestore.collection(collectionName).limit(limit).get();

            return future.get().getDocuments();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DocumentSnapshot findById(String collectionName, String id) {
        try {
            DocumentReference document = firestore.collection(collectionName).document(id);
            DocumentSnapshot snapshot = document.get().get();
            verifySnapshotIfDocumentExists(snapshot);
            return snapshot;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean update(String collectionName, String id, Map<String, Object> data) {
        try {
            DocumentReference document = firestore.collection(collectionName).document(id);
            DocumentSnapshot snapshot = findById(collectionName, id);
            verifySnapshotIfDocumentExists(snapshot);
            document.update(data);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean delete(String collectionName, String id) {
        try {
            DocumentReference document = firestore.collection(collectionName).document(id);
            DocumentSnapshot snapshot = document.get().get();
            verifySnapshotIfDocumentExists(snapshot);
            document.delete();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean deleteAll(String collectionName, Integer limit) {
        List<QueryDocumentSnapshot> result = findAll(collectionName, limit);
        for (QueryDocumentSnapshot document : result) {
            document.getReference().delete();
        }
        return true;
    }
}
