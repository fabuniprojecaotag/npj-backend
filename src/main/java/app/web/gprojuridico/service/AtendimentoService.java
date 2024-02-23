package app.web.gprojuridico.service;

import app.web.gprojuridico.dto.AtendimentoCivilDTO;
import app.web.gprojuridico.dto.AtendimentoTrabalhistaDTO;
import app.web.gprojuridico.exception.ResourceNotFoundException;
import app.web.gprojuridico.model.Atendimento;

import app.web.gprojuridico.repository.BaseRepository;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class AtendimentoService {

    @Autowired
    BaseRepository repository;

    CollectionReference collection = FirestoreClient.getFirestore().collection("atendimentos");

    public Map<String, Object> insert(Atendimento data) {
        DocumentReference result = repository.save(collection, data);

        ApiFuture<DocumentSnapshot> future = result.get();
        Object o;

        try {
            o = convertSnapshotToCorrespondingDTO(future.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String documentId = result.getId();
        System.out.println("\nAtendimento adicionado. ID: " + documentId);

        return Map.of(
                "object", o,
                "id", documentId
        );
    }

    public List<Object> findAll() {

        try {
            QuerySnapshot query = collection.get().get();
            List<Object> list = new ArrayList<>();
            for (QueryDocumentSnapshot document : query) {
                Object object = convertSnapshotToCorrespondingDTO(document);
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

            return verifySnapshot(snapshot);
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
            return convertSnapshotToCorrespondingDTO(snapshot);
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

    /**
     * Converts the passed snapshot to the corresponding DTO through the
     * registered service area
     */
    private Object convertSnapshotToCorrespondingDTO(DocumentSnapshot snapshot) {
        String area = snapshot.getString("area");
        if (Objects.equals(area, "Trabalhista")) {
            return snapshot.toObject(AtendimentoTrabalhistaDTO.class);
        } else if (Objects.equals(area, "Civil") || Objects.equals(area, "Família") || Objects.equals(area, "Penal")) {
            return snapshot.toObject(AtendimentoCivilDTO.class);
        }
        return null;
    }
}
