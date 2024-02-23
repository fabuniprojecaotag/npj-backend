package app.web.gprojuridico.repository;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;

import java.util.List;
import java.util.Map;

interface CrudRepository {

    DocumentReference save(CollectionReference collection, Map<String, Object> data);

    DocumentReference save(CollectionReference collection, Object data);

    List<QueryDocumentSnapshot> findAll(CollectionReference collection, Integer limit);

    DocumentSnapshot findById(CollectionReference collection, String id);

    Boolean update(CollectionReference collection, String id, Map<String, Object> data);

    Boolean delete(CollectionReference collection, String id);

    Boolean deleteAll(CollectionReference collection, Integer limit);
}
