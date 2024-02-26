package app.web.gprojuridico.repository;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;

import java.util.List;
import java.util.Map;

interface CrudRepository {

    DocumentReference save(String collection, Map<String, Object> data);

    DocumentReference save(String collection, Object data);

    List<QueryDocumentSnapshot> findAll(String collection, Integer limit);

    DocumentSnapshot findById(String collection, String id);

    Boolean update(String collection, String id, Map<String, Object> data);

    Boolean delete(String collection, String id);

    Boolean deleteAll(String collection, Integer limit);
}
