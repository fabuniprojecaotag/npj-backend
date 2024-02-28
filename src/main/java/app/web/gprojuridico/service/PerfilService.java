package app.web.gprojuridico.service;

import app.web.gprojuridico.model.Role;
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

    public List<Role> getAll() {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference usersCollection = dbFirestore.collection(COLLECTION_NAME);

        try {
            ApiFuture<QuerySnapshot> query = usersCollection.get();
            QuerySnapshot querySnapshot = query.get();
            List<Role> roleList = new ArrayList<>();

            for (QueryDocumentSnapshot document : querySnapshot) {
                // Convert Firestore document to your User object
                Role role = document.toObject(Role.class);

                // Check if the user is already in the list
                if (!roleList.contains(role)) {
                    roleList.add(role);
                }
            }
            // Assuming you want to return the perfilList as data in your ResponseModel
            return roleList;
        } catch (InterruptedException | ExecutionException e) {
            // Handle any exceptions
            throw new RuntimeException("Erro ao procurar perfis:", e);
        }
    }

    public Role getPerfilById(String perfilId) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference perfilDocRef = dbFirestore.collection(COLLECTION_NAME).document(perfilId);

        try {
            System.out.println("Attempting to fetch perfil for ID: " + perfilId);

            DocumentSnapshot perfilSnapshot = perfilDocRef.get().get();

            Role role = perfilSnapshot.toObject(Role.class);
            System.out.println("Perfil found: " + role);

            return role;
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
