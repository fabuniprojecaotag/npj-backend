package app.web.gprojuridico.service;

import app.web.gprojuridico.exception.ResourceNotFoundException;
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

    public void insert(Atendimento data) {

        try {
            DocumentReference result = collection.add(data).get();
            System.out.println("Atendimento adicionado. ID: " + result.getId());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    public List<Atendimento> findAll() {

        try {
            QuerySnapshot query = collection.get().get();
            List<Atendimento> list = new ArrayList<>();
            for (QueryDocumentSnapshot document : query) {
                Atendimento doc = document.toObject(Atendimento.class);
                list.add(doc);
            }

            return list;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public Atendimento findById(String id) {

        try {
            DocumentReference document = collection.document(id);
            DocumentSnapshot snapshot = document.get().get();
            return (Atendimento) verifySnapshot(snapshot);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(String id, Map<String, Object> data) {

        try {
            DocumentReference document = collection.document(id);
            DocumentSnapshot snapshot = document.get().get();
            verifySnapshot(snapshot, document, data);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String id) {

        try {
            DocumentReference document = collection.document(id);
            DocumentSnapshot snapshot = document.get().get();
            verifySnapshot(snapshot, document);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private Object verifySnapshot(DocumentSnapshot snapshot) {
        /*
         * Existe um erro no método .get() do DocumentSnapshot, pois um documento
         * que não existe no Firestore é, de alguma forma, encontrado e retornado
         * com campos null. Por isso, faz-se necessário essa condicional abaixo.
         */
        if (snapshot.exists()) {
            return snapshot.toObject(Atendimento.class);
        } else {
            throw new ResourceNotFoundException();
        }
    }

    private void verifySnapshot(DocumentSnapshot snapshot, DocumentReference document) {
        /*
         * Existe um erro no método .get() do DocumentSnapshot, pois um documento
         * que não existe no Firestore é, de alguma forma, encontrado e retornado
         * com campos null. Por isso, faz-se necessário essa condicional abaixo.
         */
        if (snapshot.exists()) {
            document.delete();
        } else {
            throw new ResourceNotFoundException();
        }
    }

    private void verifySnapshot(DocumentSnapshot snapshot, DocumentReference document, Map<String, Object> data) {
        /*
         * Existe um erro no método .get() do DocumentSnapshot, pois um documento
         * que não existe no Firestore é, de alguma forma, encontrado e retornado
         * com campos null. Por isso, faz-se necessário essa condicional abaixo.
         */
        if (snapshot.exists()) {
            document.update(data);
        } else {
            throw new ResourceNotFoundException();
        }
    }
}
