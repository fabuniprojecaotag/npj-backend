package app.web.gprojuridico.service;

import app.web.gprojuridico.model.Estagiario;
import app.web.gprojuridico.model.Usuario;
import at.favre.lib.crypto.bcrypt.BCrypt;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

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

    @Override
    public UserDetails loadUserByUsername(String username) {
        try {
            return null;
        } catch (Exception e) {
            throw new UsernameNotFoundException(null);
        }
    }
}
