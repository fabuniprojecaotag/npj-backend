package app.web.gprojuridico.service;

import app.web.gprojuridico.exception.EmailAlreadyExistsException;
import app.web.gprojuridico.exception.NameAlreadyExistsException;
import app.web.gprojuridico.model.Estagiario;
import app.web.gprojuridico.model.Usuario;
import at.favre.lib.crypto.bcrypt.BCrypt;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static app.web.gprojuridico.service.utils.Utils.convertUsingReflection;

@Service
public class UserService implements UserDetailsService {

    private static final String COLLECTION_NAME = "usuarios";
    
    @Autowired
    Firestore firestore;

    public Map<String, Object> create(Usuario usuario) {

        // TODO (login): Implementar lógica de verificar se email passado possui @projecao.br ou @projecao.edu.br

//        ApiFuture<QuerySnapshot> emailFuture = firestore.collection(COLLECTION_NAME).whereEqualTo("email", usuario.getEmail()).get();
//
//        try {
//            QuerySnapshot email = emailFuture.get();
//            if (!email.isEmpty()) throw new EmailAlreadyExistsException("Email já cadastrado");
//        } catch (Exception ignored) {}

        String encryptedPassword = BCrypt.withDefaults().hashToString(12, usuario.getPassword().toCharArray());
        usuario.setSenha(encryptedPassword);

        try {
            Boolean useSuperClass = usuario instanceof Estagiario;
            Map<String, Object> map = convertUsingReflection(usuario, useSuperClass);
            String id = usuario.getEmail();
            firestore.collection(COLLECTION_NAME).document(id).set(map);
            return Map.of(
                    "object", map,
                    "id", id
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    public void deleteUserById(String documentId) {
//
//        CollectionReference usersCollection = firestore.collection(COLLECTION_NAME);
//
//        DocumentReference documentReference = usersCollection.document(documentId);
//
//        ApiFuture<WriteResult> writeResult = documentReference.delete();
//
//        try {
//            writeResult.get();
//            System.out.println("Usuário com o ID " + documentId + " deletado com sucesso.");
//        } catch (NotFoundException e) {
//            throw new RuntimeException("Usuário não encontrado: ", e);
//        } catch (InterruptedException | ExecutionException e) {
//            throw new RuntimeException("Erroao deletar o usuário: " + e.getMessage());
//        }
//    }
//
    public Usuario findUserByEmail(String email) throws ExecutionException, InterruptedException {

        CollectionReference usersCollection = firestore.collection(COLLECTION_NAME);

        Usuario foundUsuario = null;

        // Query Firestore to find a user with matching login and password
        List<QueryDocumentSnapshot> matchingUsers;
        try {
            matchingUsers = usersCollection
                    .whereEqualTo("email", email)
                    .get().get().getDocuments();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Erro ao consultar o banco firebase: " + e.getMessage(), e);
        }

        if (!matchingUsers.isEmpty()) {
            // Assuming login and password combination is unique, return the first matching user
            System.out.println("Usuário encontrado no Fb: " +matchingUsers.get(0));

            foundUsuario = matchingUsers.get(0).toObject(Usuario.class);

            // Retrieve perfil data based on perfil_id
            System.out.println("Usuário encontrado objeto: " + foundUsuario);
        }

        if (foundUsuario == null) {
            throw new RuntimeException("Nenhum usuário encontrado com a password e e-mail fornecidos.");
        }

        return foundUsuario;
    }
//
//    public Usuario findUserByEmailAndPassword(AuthenticationDTO user) throws ExecutionException, InterruptedException {
//
//        CollectionReference usersCollection = firestore.collection(COLLECTION_NAME);
//
//        Usuario foundUsuario = null;
//
//        // Extract login and password from the user object
//        String email = user.login();
//        String password = user.password();
//
//        // Query Firestore to find a user with matching login and password
//        List<QueryDocumentSnapshot> matchingUsers;
//        try {
//            matchingUsers = usersCollection
//                    .whereEqualTo("email", email)
//                    .get().get().getDocuments();
//        } catch (InterruptedException | ExecutionException e) {
//            throw new RuntimeException("Erro ao consultar o banco firebase: " + e.getMessage(), e);
//        }
//
//        if (!matchingUsers.isEmpty()) {
//            // Assuming login and password combination is unique, return the first matching user
//            System.out.println(matchingUsers.get(0));
//
//            foundUsuario = matchingUsers.get(0).toObject(Usuario.class);
//            foundUsuario.setDocumentId(matchingUsers.get(0).getId());
//
//            // Retrieve perfil data based on perfil_id
//            System.out.println(foundUsuario);
//        }
//
//        if (foundUsuario == null) {
//            throw new RuntimeException("Nenhum usuário encontrado com a password e e-mail fornecidos.");
//        }
//
//        BCrypt.Result passwordCheck = BCrypt.verifyer().verify(password.toCharArray(), foundUsuario.getPassword());
//        if (passwordCheck.verified) {
//            return foundUsuario;
//        } else {
//            throw new RuntimeException("A Senha do usuário incorreta");
//        }
//    }
//
//    public ArrayList toggleUserStatus(String documentId) {
//
//        try {
//            DocumentReference userDocument = firestore.collection(COLLECTION_NAME).document(documentId);
//            ApiFuture<DocumentSnapshot> documentSnapshot = userDocument.get();
//
//            String currentStatus = documentSnapshot.get().getString("accountNonLocked");
//            String newStatus = "Ativo".equals(currentStatus) ? "Inativo" : "Ativo";
//
//            ApiFuture<WriteResult> updateResult = userDocument.update("accountNonLocked", newStatus);
//            updateResult.get();
//
//            // Include additional data if needed, e.g., updated accountNonLocked
//            return new ArrayList<>();
//        } catch (NotFoundException e) {
//            throw new RuntimeException("Error usuário não encontrado: " + e.getMessage());
//        } catch (Exception e) {
//            throw new RuntimeException("Error enquanto atualiza o status do usuário: " + e.getMessage());
//        }
//    }
//
//    public List<Usuario> getAllUsers() {
//
//        CollectionReference usersCollection = firestore.collection(COLLECTION_NAME);
//        CollectionReference perfisCollection = firestore.collection("perfis");
//
//        try {
//            ApiFuture<QuerySnapshot> query = usersCollection.get();
//            QuerySnapshot querySnapshot = query.get();
//            List<Usuario> usuarioList = new ArrayList<>();
//
//            for (QueryDocumentSnapshot userDocument : querySnapshot) {
//                // Convert Firestore document to your User object
//                Usuario usuario = userDocument.toObject(Usuario.class);
//                usuario.setDocumentId(userDocument.getId());
//
//                // Check if the user is already in the list
//                if (!usuarioList.contains(usuario)) {
//                    usuarioList.add(usuario);
//                }
//            }
//
//            return usuarioList;
//        } catch (InterruptedException | ExecutionException e) {
//            // Handle any exceptions
//            throw new RuntimeException("Erro ao retornar usuarios do fb: " + e.getMessage(), e);
//        }
//    }
//
//    public List<Usuario> getUserByName(String nome) {
//
//        CollectionReference usersCollection = firestore.collection(COLLECTION_NAME);
//
//        try {
//            // Create a query with the filter on the "username" field
//            Query query = usersCollection.whereEqualTo("username", nome);
//
//            ApiFuture<QuerySnapshot> querySnapshot = query.get();
//            List<Usuario> usuarioList = new ArrayList<>();
//
//            for (QueryDocumentSnapshot document : querySnapshot.get().getDocuments()) {
//                // Convert Firestore document to your User object
//                Usuario usuario = document.toObject(Usuario.class);
//                usuario.setDocumentId(document.getId());
//                usuarioList.add(usuario);
//            }
//
//            return usuarioList;
//        } catch (InterruptedException | ExecutionException e) {
//            // Handle any exceptions
//            throw new RuntimeException("Erro ao retornar usuarios do fb: " + e.getMessage(), e);
//        }
//    }
//
//    public Usuario getUserById(String userId) {
//
//        DocumentReference userDocRef = firestore.collection(COLLECTION_NAME).document(userId);
//
//        try {
//            DocumentSnapshot userSnapshot = userDocRef.get().get();
//
//            Usuario usuario = userSnapshot.toObject(Usuario.class);
//            assert usuario != null;
//            usuario.setDocumentId(userSnapshot.getId());
//            return usuario;
//
//        } catch (NotFoundException e) {
//            throw new RuntimeException("Usuário não encontrado: ", e);
//        } catch (InterruptedException | ExecutionException e) {
//            throw new RuntimeException("Erro ao buscar usuário: ", e);
//        }
//    }
//
//    public Usuario updateUser(Usuario updatedUsuario) {
//        DocumentReference userDocRef = firestore.collection(COLLECTION_NAME).document(updatedUsuario.getDocumentId());
//
//        try {
//            DocumentSnapshot userSnapshot = userDocRef.get().get();
//            if (userSnapshot.exists()) {
//                userDocRef.update(
//                        "username", updatedUsuario.getUsername(),
//                        "email", updatedUsuario.getEmailAcademico(),
//                        "semestre", updatedUsuario.getSemestre(),
//                        "password", updatedUsuario.getPassword(),
////                        "perfil", updatedUser.getPerfil(),
//                        "accountNonLocked", updatedUsuario.isAccountNonLocked()
//                ).get();
//
//                // Recupere o usuário atualizado para retornar ao chamador
//                DocumentSnapshot updatedUserSnapshot = userDocRef.get().get();
//                Usuario usuario = updatedUserSnapshot.toObject(Usuario.class);
//
//                return usuario;
//            } else {
//                throw new RuntimeException("Usuário não encontrado para atualização. ID: " + updatedUsuario.getDocumentId());
//            }
//        } catch (NotFoundException e) {
//            throw new RuntimeException("Usuário não encontrado: ", e);
//        } catch (InterruptedException | ExecutionException e) {
//            throw new RuntimeException("Erro ao atualizar usuário: ", e);
//        }
//    }
//
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return findUserByEmail(username);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
