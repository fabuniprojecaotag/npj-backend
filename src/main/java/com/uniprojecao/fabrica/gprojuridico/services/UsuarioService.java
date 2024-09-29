package com.uniprojecao.fabrica.gprojuridico.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.uniprojecao.fabrica.gprojuridico.dto.body.UpdateBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Estagiario;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.repositories.FirestoreRepositoryImpl;
import com.uniprojecao.fabrica.gprojuridico.services.exceptions.UserAlreadyCreatedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static com.uniprojecao.fabrica.gprojuridico.utils.Constants.USUARIOS_COLLECTION;
import static com.uniprojecao.fabrica.gprojuridico.utils.Utils.identifyChildClass;

@Service
public class UsuarioService implements UserDetailsService {

    private final FirestoreRepositoryImpl<Usuario> firestoreRepository = new FirestoreRepositoryImpl<>(USUARIOS_COLLECTION);

    public Usuario insert(Usuario usuario) throws InvalidPropertiesFormatException, ExecutionException, InterruptedException {
        defineId(usuario);
        String id = usuario.getId();

        checkIfExists(usuario, id);
        encryptPassword(usuario);

        firestoreRepository.insert(id, usuario);
        return usuario;
    }

    public Usuario findById(String id) throws InvalidPropertiesFormatException, ExecutionException, InterruptedException {
        return firestoreRepository.findById(id);
    }

    public void update(String recordId, UpdateBodyDTO<Usuario> data, String classType) {
        Class<?> clazz = identifyChildClass(Usuario.class.getSimpleName(), classType);

        String senhaKey = "senha";
        Map<String, Object> newData = new HashMap<>();

        if (!data.getBody().getPassword().isEmpty()) {
            var rawPassword = data.getBody().getPassword();
            var encryptedPassword = encryptPassword(rawPassword);

            // Adiciona todos os campos do objeto, exceto a senha original
            for (Field field : Usuario.class.getDeclaredFields()) {
                field.setAccessible(true); // Permite acessar campos privados
                try {
                    // Verifica se não é a senha e adiciona ao novo Map
                    if (!field.getName().equals(senhaKey)) {
                        newData.put(field.getName(), field.get(data.getBody()));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace(); // Trate a exceção conforme necessário
                }
            }

            // Adiciona a senha criptografada
            newData.put(senhaKey, encryptedPassword);

            // Salva os dados recebidos e com a senha criptografada
            firestoreRepository.update(recordId, (UpdateBodyDTO<Usuario>) newData, clazz);
        } else {
            // Se não houver senha a ser alterada, usa os dados originais
            for (Field field : Usuario.class.getDeclaredFields()) {
                field.setAccessible(true); // Permite acessar campos privados
                try {
                    newData.put(field.getName(), field.get(data.getBody()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace(); // Trate a exceção conforme necessário
                }
            }

            firestoreRepository.update(recordId, (UpdateBodyDTO) newData, clazz);
        }
    }


    private void checkIfExists(Usuario data, String id) throws InvalidPropertiesFormatException, ExecutionException, InterruptedException {
        var result = findById(id);
        if (result != null) {
            String userEmailFound = result.getEmail();
            String userCpfFound = result.getCpf();

            Boolean equalEmails = Objects.equals(userEmailFound, data.getEmail());
            Boolean equalCpfs = Objects.equals(userCpfFound, data.getCpf());

            if (equalEmails && equalCpfs) {
                throw new UserAlreadyCreatedException(userEmailFound, userCpfFound);
            } else {
                throw new UserAlreadyCreatedException(userEmailFound);
            }
        }
    }

    private void defineId(Usuario usuario) {
        String id = (!(usuario instanceof Estagiario estagiario))
                ? usuario.getEmail().replace("@projecao\\.br", "") // Retira o "@projecao.br"
                : estagiario.getMatricula();
        usuario.setId(id);
    }

    private void encryptPassword(Usuario u) {
        u.setSenha(BCrypt.withDefaults().hashToString(12, u.getSenha().toCharArray()));
    }

    private String encryptPassword(String rawPassword) {
        return BCrypt.withDefaults().hashToString(12, rawPassword.toCharArray());
    }

    public Usuario findMe() throws InvalidPropertiesFormatException, ExecutionException, InterruptedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();
        String id = usuario.getId();
        return findById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        try {
            return new FirestoreRepositoryImpl<Usuario>(USUARIOS_COLLECTION).findById(username);
        } catch (ExecutionException | InvalidPropertiesFormatException | InterruptedException e) {
            throw new UsernameNotFoundException(username);
        }
    }
}
