package app.web.gprojuridico.service;

import app.web.gprojuridico.model.user.Perfil;
import com.google.api.core.ApiFuture;
import com.google.api.gax.rpc.NotFoundException;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class PerfilService {

    private static final String COLLECTION_NAME = "perfis";

    public List<Perfil> getAll() {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference usersCollection = dbFirestore.collection(COLLECTION_NAME);

        try {
            ApiFuture<QuerySnapshot> query = usersCollection.get();
            QuerySnapshot querySnapshot = query.get();
            List<Perfil> perfilList = new ArrayList<>();

            for (QueryDocumentSnapshot document : querySnapshot) {
                // Convert Firestore document to your User object
                Perfil perfil = document.toObject(Perfil.class);
                perfil.setDocumentId(document.getId());

                // Check if the user is already in the list
                if (!perfilList.contains(perfil)) {
                    perfilList.add(perfil);
                }
            }
            // Assuming you want to return the perfilList as data in your ResponseModel
            return perfilList;
        } catch (InterruptedException | ExecutionException e) {
            // Handle any exceptions
            throw new RuntimeException("Erro ao procurar perfis:", e);
        }
    }

    public Perfil getPerfilById(String perfilId) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference perfilDocRef = dbFirestore.collection(COLLECTION_NAME).document(perfilId);

        try {
            System.out.println("Attempting to fetch perfil for ID: " + perfilId);

            DocumentSnapshot perfilSnapshot = perfilDocRef.get().get();

            Perfil perfil = perfilSnapshot.toObject(Perfil.class);
            perfil.setDocumentId(perfilSnapshot.getId());
            System.out.println("Perfil found: " + perfil);

            return perfil;
        } catch (NotFoundException e) {
            System.out.println("Perfil not found for ID: " + perfilId);
            // Return null or handle the case where the perfil with the given ID doesn't exist
            throw new RuntimeException("Perfil não encontrado para o id Fornecido", e);
        } catch (InterruptedException | ExecutionException e) {
            // Handle any exceptions
            System.out.println("Error fetching perfil: " + e.getMessage());
            throw new RuntimeException("Error fetching perfil", e);
        }
    }
}
