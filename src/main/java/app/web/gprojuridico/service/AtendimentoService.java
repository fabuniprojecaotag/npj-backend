package app.web.gprojuridico.service;

import app.web.gprojuridico.model.Atendimento;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class AtendimentoService {

    CollectionReference collection = FirestoreClient.getFirestore().collection("atendimentos");

    public void insert(Atendimento data) throws ExecutionException, InterruptedException {

        DocumentReference result = collection.add(data).get();
        System.out.println("Atendimento adicionado. ID: " + result.getId());
    }

    public List<Atendimento> findAll() throws ExecutionException, InterruptedException {

            QuerySnapshot query = collection.get().get();
            List<Atendimento> list = new ArrayList<>();

            for (QueryDocumentSnapshot document : query) {
                Atendimento doc = document.toObject(Atendimento.class);
                list.add(doc);
            }

            return list;
    }

    public Atendimento findById(String id) throws ExecutionException, InterruptedException {

        DocumentReference document = collection.document(id);
        DocumentSnapshot snapshot = document.get().get();
        return snapshot.toObject(Atendimento.class);
    }

    public void update(String id, Map<String, Object> data) {

        DocumentReference document = collection.document(id);
        document.update(data);
        System.out.println("Atendimento atualizado. ID: " + id);
    }

    public void delete(String id) {

        DocumentReference document = collection.document(id);
        ApiFuture<WriteResult> result = document.delete();
        System.out.println("Atendimento com a ID " + id + " deletado.");
    }
}
