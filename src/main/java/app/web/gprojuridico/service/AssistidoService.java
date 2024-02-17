package app.web.gprojuridico.service;

import app.web.gprojuridico.exception.ResourceNotFoundException;
import app.web.gprojuridico.model.AssistidoCivil;
import app.web.gprojuridico.model.AssistidoFull;
import app.web.gprojuridico.model.AssistidoTrabalhista;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class AssistidoService {

    CollectionReference collection = FirestoreClient.getFirestore().collection("assistidos");

    public Object insert(Object data) {

        try {
            System.out.println("\nPayload recebido: " + data.toString());
            DocumentReference result = collection.add(data).get();

            System.out.println("\nAssistido adicionado. ID: " + result.getId());

            return findById(result.getId());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Object> findAll() {

        try {
            QuerySnapshot query = collection.get().get();
            List<Object> list = new ArrayList<>();
            for (QueryDocumentSnapshot document : query) {
                Object object = convertSnapshotToCorrespondingModel(document);
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

            return verifySnapshotToFindObjectById(snapshot);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(String id,  Map<String, Object> data) {

        try {
            DocumentReference document = collection.document(id);
            DocumentSnapshot snapshot = document.get().get();
            verifySnapshotToUpdateObject(snapshot, document, data);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String id) {

        try {
            DocumentReference document = collection.document(id);
            DocumentSnapshot snapshot = document.get().get();
            verifySnapshotToDeleteObject(snapshot, document);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private Object verifySnapshotToFindObjectById(DocumentSnapshot snapshot) {
        /*
         * Existe um erro no método .get() do DocumentSnapshot, pois um documento
         * que não existe no Firestore é, de alguma forma, encontrado e retornado
         * com campos null. Por isso, faz-se necessário essa condicional abaixo.
         */
        if (snapshot.exists()) {
            return convertSnapshotToCorrespondingModel(snapshot);
        } else {
            throw new ResourceNotFoundException();
        }
    }

    private void verifySnapshotToUpdateObject(DocumentSnapshot snapshot, DocumentReference document, Map<String, Object> data) {
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

    private void verifySnapshotToDeleteObject(DocumentSnapshot snapshot, DocumentReference document) {
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

    /**
     * Converts the passed snapshot to the corresponding model through the
     * relationship that the person assisted has with the service(s).
     */
    private Object convertSnapshotToCorrespondingModel(DocumentSnapshot snapshot) {

        Boolean dadosFCivil = snapshot.contains("naturalidade") && snapshot.contains("dataNascimento") && snapshot.contains("numDependentes");
        Boolean dadosFTrabalhista = snapshot.contains("ctps") && snapshot.contains("pis") && snapshot.contains("empregadoAtualmente");

        // primeiro, se o snapshot passado possui dados exclusivos de ambas as fichas (civil e trabalhista) para um assistido...
        if (dadosFCivil && dadosFTrabalhista) {
            // então o snapshot é uma instância de AssistidoFull.
            return snapshot.toObject(AssistidoFull.class);
        }
        // ou se o snapshot passado possui dados exclusivos da ficha civil para um assistido...
        else if (dadosFCivil) {
            // então o snapshot é uma instância de AssistidoCivil.
            return snapshot.toObject(AssistidoCivil.class);
        }
        // ou se o snapshot passado possui dados exclusivos da ficha trabalhista para um assistido...
        else if (dadosFTrabalhista) {
            // então o snapshot é uma instância de AssistidoTrabalhista.
            return snapshot.toObject(AssistidoTrabalhista.class);
        } else {
            // senão, caso não haja compatatibilidade do snapshot com as três condicionais acima, lance então uma exceção.
            throw new ResourceNotFoundException();
        }
    }
}

