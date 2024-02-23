package app.web.gprojuridico.service;

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

import static app.web.gprojuridico.service.utils.AtendimentoUtils.convertSnapshotToCorrespondingAtendimentoDTO;

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
            o = convertSnapshotToCorrespondingAtendimentoDTO(future.get());
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

    public List<Object> findAll(String limit) {
        List<QueryDocumentSnapshot> result = repository.findAll(collection, Integer.parseInt(limit));
        List<Object> list = new ArrayList<>();

        for (QueryDocumentSnapshot document : result) {
            list.add(convertSnapshotToCorrespondingAtendimentoDTO(document));
        }

        return list;
    }

    public Object findById(String id) {
        DocumentSnapshot snapshot = repository.findById(collection, id);
        return convertSnapshotToCorrespondingAtendimentoDTO(snapshot);
    }

    public Boolean update(String id, Map<String, Object> data) {
        return repository.update(collection, id, data);
    }

    public Boolean delete(String id) {
        return repository.delete(collection, id);
    }

    public Boolean deleteAll(String limit) {
        return repository.deleteAll(collection, Integer.parseInt(limit));
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
