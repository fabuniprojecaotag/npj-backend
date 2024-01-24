package app.web.gprojuridico.service;

import app.web.gprojuridico.model.Assistido;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class AssistidoService {

    private static final String COLLECTION_NAME = "assistidos";

    public List<Assistido> getAllAssistidos() {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference assistidosCollection = dbFirestore.collection(COLLECTION_NAME);

        try {
            ApiFuture<QuerySnapshot> query = assistidosCollection.get();
            QuerySnapshot querySnapshot = query.get();
            List<Assistido> assistidoList = new ArrayList<>();

            for (QueryDocumentSnapshot document : querySnapshot) {
                Assistido assistido = document.toObject(Assistido.class);
                assistido.setDocumentId(document.getId());

                assistidoList.add(assistido);
            }

            return assistidoList;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Erro ao procurar os assistidos: ", e);
        }
    }

    public Assistido getAssistidoById(String assistidoId) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference assistidoDocRef = dbFirestore.collection(COLLECTION_NAME).document(assistidoId);

        try {
            DocumentSnapshot assistidoSnapshot = assistidoDocRef.get().get();

            if (assistidoSnapshot.exists()) {
                Assistido assistido = assistidoSnapshot.toObject(Assistido.class);
                assert assistido != null;
                assistido.setDocumentId(assistidoSnapshot.getId());
                return assistido;
            } else {
                return null; // ou lance uma exceção para indicar que o assistido não foi encontrado
            }
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Erro ao buscar assistido: " + e.getMessage());
            return null;
        }
    }
    public Assistido createAssistido(Assistido assistido) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference assistidosCollection = dbFirestore.collection(COLLECTION_NAME);

        try {
            // Adiciona o assistido ao Firestore
            ApiFuture<DocumentReference> result = assistidosCollection.add(assistido);
            System.out.println("Assistido adicionado com sucesso. ID: " + result.get().getId());

            return assistido;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Erro ao criar assistido: ", e);
        }
    }

    public Assistido updateAssistido(String assistidoId, Assistido assistido) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference assistidoDocRef = dbFirestore.collection(COLLECTION_NAME).document(assistidoId);

        ApiFuture<WriteResult> result = assistidoDocRef.set(assistido);
        System.out.println("Assistido atualizado com sucesso. ID: " + assistidoId);

        return assistido;
    }

    public void deleteAssistido(String assistidoId) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference assistidoDocRef = dbFirestore.collection(COLLECTION_NAME).document(assistidoId);

        ApiFuture<WriteResult> result = assistidoDocRef.delete();

        try {
            result.get();
            System.out.println("Assistido com a ID " + assistidoId + " deletado com sucesso.");
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error ao deletar o assistido: " + e.getMessage());
        }
    }


}

