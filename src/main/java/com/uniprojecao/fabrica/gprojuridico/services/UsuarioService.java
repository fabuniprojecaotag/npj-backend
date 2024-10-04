package com.uniprojecao.fabrica.gprojuridico.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.uniprojecao.fabrica.gprojuridico.dto.body.DeleteBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.body.ListBodyDTO;
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

@Service
public class UsuarioService implements UserDetailsService {
    private final FirestoreRepositoryImpl<Usuario> firestoreRepository = new FirestoreRepositoryImpl<>(USUARIOS_COLLECTION);

    public Usuario insert(Usuario usuario) throws ExecutionException, InterruptedException, InvalidPropertiesFormatException {
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

    public void update(String recordId, UpdateBodyDTO<Usuario> data) {
        Map<String, Object> newData = new HashMap<>();
        Usuario usuario = data.getBody();

        // Criptografa a senha se fornecida
        if (usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
            var encryptedPassword = encryptPassword(usuario.getSenha());
            usuario.setSenha(encryptedPassword);
        }

        // Preenche o Map com dados do usuário, excluindo o campo "senha" e campos nulos ou vazios
        for (Field field : Usuario.class.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(usuario);
                // Adiciona ao Map apenas se o valor não for nulo, vazio ou se não for o campo "senha"
                if (value != null && !(value instanceof String && ((String) value).isEmpty()) && !field.getName().equals("senha")) {
                    newData.put(field.getName(), value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace(); // Trate a exceção conforme necessário
            }
        }

        // Atualiza o Firestore com o novo Map, que só contém campos válidos
        firestoreRepository.update(recordId, newData);
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
        String id = (usuario instanceof Estagiario estagiario)
                ? estagiario.getMatricula()
                : usuario.getEmail().replace("@projecao.br", "");
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

    public UserDetails loadUserByUsername(String username) {
        try {
            return new FirestoreRepositoryImpl<Usuario>(USUARIOS_COLLECTION).findById(username);
        } catch (ExecutionException | InvalidPropertiesFormatException | InterruptedException e) {
            throw new UsernameNotFoundException(username);
        }
    }

    public ListBodyDTO<Usuario> listAll(String startAfter, int pageSize) throws InvalidPropertiesFormatException, ExecutionException, InterruptedException {
        return firestoreRepository.findAll(startAfter, pageSize, null, "min");
    }

    public void delete(DeleteBodyDTO deleteBodyDTO) {
        firestoreRepository.delete(deleteBodyDTO.ids());
    }
}
