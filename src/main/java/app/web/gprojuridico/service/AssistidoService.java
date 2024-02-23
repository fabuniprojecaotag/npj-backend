package app.web.gprojuridico.service;

import app.web.gprojuridico.repository.BaseRepository;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static app.web.gprojuridico.service.utils.AssistidoUtils.*;

@Service
public class AssistidoService {

    @Autowired
    BaseRepository repository;

    CollectionReference collection = FirestoreClient.getFirestore().collection("assistidos");

    public Map<String, Object> insert(Map<String, Object> data) {
        try {
            Map<String, Object> verifiedData = verifyDataToInsertAssistido(data);
            DocumentReference result = repository.save(collection, verifiedData);

            String assistidoId = result.getId();
            System.out.println("\nAssistido adicionado. ID: " + assistidoId);
            verifiedData.put("id", assistidoId);

            return verifiedData;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Object> findAll(String limit) {
        List<QueryDocumentSnapshot> result = repository.findAll(collection, Integer.parseInt(limit));
        List<Object> list = new ArrayList<>();

        for (QueryDocumentSnapshot document : result) {
            list.add(convertSnapshotToCorrespondingAssistidoModel(document));
        }

        return list;
    }

    public Object findById(String id) {
        DocumentSnapshot snapshot = repository.findById(collection, id);
        return verifySnapshotToFindAssistidoById(snapshot);
    }

    public Boolean update(String id,  Map<String, Object> data) {
        return repository.update(collection, id, verifyDataToUpdateAssistido(data));
    }

    public Boolean delete(String id) {
        return repository.delete(collection, id);
    }

    public Boolean deleteAll(String limit) {
        return repository.deleteAll(collection, Integer.parseInt(limit));
    }
}

