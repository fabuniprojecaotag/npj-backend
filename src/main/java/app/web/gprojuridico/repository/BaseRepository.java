package app.web.gprojuridico.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static app.web.gprojuridico.service.utils.Utils.verifySnapshotIfDocumentExists;

@Repository
public class BaseRepository implements CrudRepository {
    @Override
    public DocumentReference save(CollectionReference collection, Map<String, Object> data) {
        try {
            return collection.add(data).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DocumentReference save(CollectionReference collection, Object data) {
        try {
            return collection.add(data).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<QueryDocumentSnapshot> findAll(CollectionReference collection, @Nullable Integer limit) {
        if (limit == null) limit = 20;
        try {
            // asynchronously retrieve multiple documents
            ApiFuture<QuerySnapshot> future = collection.limit(limit).get();

            return future.get().getDocuments();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DocumentSnapshot findById(CollectionReference collection, String id) {
        try {
            DocumentReference document = collection.document(id);
            return document.get().get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean update(CollectionReference collection, String id, Map<String, Object> data) {
        try {
            DocumentReference document = collection.document(id);
            DocumentSnapshot snapshot = findById(collection, id);
            verifySnapshotIfDocumentExists(snapshot);
            document.update(data);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean delete(CollectionReference collection, String id) {
        try {
            DocumentReference document = collection.document(id);
            DocumentSnapshot snapshot = document.get().get();
            verifySnapshotIfDocumentExists(snapshot);
            document.delete();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean deleteAll(CollectionReference collection, Integer limit) {
        List<QueryDocumentSnapshot> result = findAll(collection, limit);
        for (QueryDocumentSnapshot document : result) {
            document.getReference().delete();
        }
        return true;
    }
}
