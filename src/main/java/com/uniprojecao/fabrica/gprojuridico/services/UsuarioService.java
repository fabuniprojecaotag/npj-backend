package com.uniprojecao.fabrica.gprojuridico.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Estagiario;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.repositories.FirestoreRepository;
import com.uniprojecao.fabrica.gprojuridico.services.exceptions.UserAlreadyCreatedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.utils.Constants.USUARIOS_COLLECTION;
import static com.uniprojecao.fabrica.gprojuridico.utils.Utils.filterValidKeys;

@Service
public class UsuarioService implements UserDetailsService {

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
                throw new RuntimeException("Usuário com o email \"" + userEmailFound + "\" já existe.");
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

    public Usuario findById(String id) throws Exception {
        return (Usuario) FirestoreRepository.getDocumentById(USUARIOS_COLLECTION, id);
    }

    public Usuario findMe() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();
        String id = usuario.getId();
        return findById(id);
    }

    public Usuario insert(Usuario data) throws Exception {
        defineId(data);
        String id = data.getId();

        checkIfExists(data, id);
        encryptPassword(data);

        FirestoreRepository.insert(USUARIOS_COLLECTION, id, data);
        return data;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        try {
            return (UserDetails) FirestoreRepository.getDocumentById(USUARIOS_COLLECTION, username);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void update(String id, Map<String, Object> data, String clazz) {
        var validClazz = switch(clazz) {
            case "Usuario" -> Usuario.class;
            case "Estagiario" -> Estagiario.class;
            default -> throw new IllegalStateException("Unexpected value: " + clazz);
        };

        var filteredData = filterValidKeys(data, validClazz);

        String senhaKey = "senha";
        if (filteredData.containsKey(senhaKey)) {
            var rawPassword = (String) filteredData.get(senhaKey);
            var encryptedPassword = encryptPassword(rawPassword);

            // Dados em mapas Java são imutáveis; logo, uma alternativa é criar um novo map de pares chave-valor
            Map<String, Object> newData = new HashMap<>();
            for (var entry : filteredData.entrySet()) {
                if (entry.getKey() != senhaKey) newData.put(entry.getKey(), entry.getValue());
            }
            newData.put(senhaKey, encryptedPassword);

            // Salva os dados recebidos e com a senha criptografada
            FirestoreRepository.update(USUARIOS_COLLECTION, id, newData);
        } else {
            FirestoreRepository.update(USUARIOS_COLLECTION, id, filteredData);
        }
    }
}