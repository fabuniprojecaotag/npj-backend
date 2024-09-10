package com.uniprojecao.fabrica.gprojuridico.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Estagiario;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.repositories.FirestoreRepositoryImpl;
import com.uniprojecao.fabrica.gprojuridico.services.exceptions.UserAlreadyCreatedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.utils.Constants.USUARIOS_COLLECTION;
import static com.uniprojecao.fabrica.gprojuridico.utils.Utils.identifyChildClass;

@Service
public class UsuarioService implements UserDetailsService {

    private final FirestoreRepositoryImpl firestoreRepository = new FirestoreRepositoryImpl(USUARIOS_COLLECTION);

    public Usuario insert(Usuario usuario) throws Exception {
        defineId(usuario);
        String id = usuario.getId();

        checkIfExists(usuario, id);
        encryptPassword(usuario);

        firestoreRepository.insert(id, usuario);
        return usuario;
    }

    public Usuario findById(String id) throws Exception {
        return (Usuario) firestoreRepository.findById(id);
    }

    public void update(String recordId, Map<String, Object> data, String classType) {
        Class<?> clazz = identifyChildClass(Usuario.class.getSimpleName(), classType);

        String senhaKey = "senha";
        if (data.containsKey(senhaKey)) {
            var rawPassword = (String) data.get(senhaKey);
            var encryptedPassword = encryptPassword(rawPassword);
            Map<String, Object> newData = new HashMap<>();

            for (var entry : data.entrySet()) {
                if (entry.getKey() != senhaKey) newData.put(entry.getKey(), entry.getValue());
            }

            newData.put(senhaKey, encryptedPassword);

            // Salva os dados recebidos e com a senha criptografada
            firestoreRepository.update(recordId, newData, clazz);
        } else {
            firestoreRepository.update(recordId, data, clazz);
        }
    }

    private void checkIfExists(Usuario data, String id) throws Exception {
        var result = findById(id);
        if (result != null) {
            String userEmailFound = result.getEmail();
            String userCpfFound = result.getCpf();

            Boolean equalEmails = userEmailFound == data.getEmail();
            Boolean equalCpfs = userCpfFound == data.getCpf();

            if (equalEmails && equalCpfs) {
                throw new UserAlreadyCreatedException(userEmailFound, userCpfFound);
            } else {
                throw new UserAlreadyCreatedException(userEmailFound);
            }
        }
    }

    private void defineId(Usuario usuario) {
        String id = (!(usuario instanceof Estagiario estagiario))
                ? usuario.getEmail().replaceAll("@projecao\\.br", "") // Retira o "@projecao.br"
                : estagiario.getMatricula();
        usuario.setId(id);
    }

    private void encryptPassword(Usuario u) {
        u.setSenha(BCrypt.withDefaults().hashToString(12, u.getSenha().toCharArray()));
    }

    private String encryptPassword(String rawPassword) {
        return BCrypt.withDefaults().hashToString(12, rawPassword.toCharArray());
    }

    public Usuario findMe() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();
        String id = usuario.getId();
        return findById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        try {
            return (UserDetails) new FirestoreRepositoryImpl(USUARIOS_COLLECTION).findById(username);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
