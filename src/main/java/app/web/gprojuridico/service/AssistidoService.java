package app.web.gprojuridico.service;

import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

import static app.web.gprojuridico.service.utils.AssistidoUtils.*;
import static app.web.gprojuridico.service.utils.Utils.verifySnapshotToDeleteObject;

@Service
public class AssistidoService {

    CollectionReference collection = FirestoreClient.getFirestore().collection("assistidos");

    public Map<String, Object> insert(Map<String, Object> data) {
        try {
            System.out.println("\nPayload recebido: " + data.toString());
            Map<String, Object> verifiedData = verifyDataToInsertAssistido(data);
            DocumentReference result = collection.add(verifiedData).get();
            String assistidoId = result.getId();

            System.out.println("\nAssistido adicionado. ID: " + assistidoId);

            verifiedData.put("id", assistidoId);

            return verifiedData;
        } catch (InterruptedException | ExecutionException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Object> findAll() {
        try {
            QuerySnapshot query = collection.get().get();
            List<Object> list = new ArrayList<>();
            for (QueryDocumentSnapshot document : query) {
                Object object = convertSnapshotToCorrespondingAssistidoModel(document);
                list.add(object);
            }

            return list;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public Object findById(String id) {
        try {
            DocumentReference document = collection.document(id);
            DocumentSnapshot snapshot = document.get().get();

            return verifySnapshotToFindAssistidoById(snapshot);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean update(String id,  Map<String, Object> data) {
        try {
            DocumentReference document = collection.document(id);
            DocumentSnapshot snapshot = document.get().get();
            verifySnapshotToUpdateAssistido(snapshot, document, data);
            return true;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean delete(String id) {

        try {
            DocumentReference document = collection.document(id);
            DocumentSnapshot snapshot = document.get().get();
            verifySnapshotToDeleteObject(snapshot, document);
            return true;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}

