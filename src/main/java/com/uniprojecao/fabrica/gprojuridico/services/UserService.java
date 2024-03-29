package com.uniprojecao.fabrica.gprojuridico.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.uniprojecao.fabrica.gprojuridico.domains.enums.FilterType;
import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Estagiario;
import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.dto.min.EstagiarioMinDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.min.UsuarioMinDTO;
import com.uniprojecao.fabrica.gprojuridico.repository.BaseRepository;
import com.uniprojecao.fabrica.gprojuridico.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.convertUsingReflection;

@Service
public class UserService implements UserDetailsService {

    private static final String COLLECTION_NAME = "usuarios";

    @Autowired
    Firestore firestore;

    @Autowired
    UserRepository repository;

    // TODO: finalizar implementação do método abaixo
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
            // TODO: Retirar campo email do map retornado, porque o atributo email é id e é populado automaticamente pelo @DocumentId no modelo
            map.remove("email");
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

    public List<Object> findAll(String limit, String field, String filter, String value) {
        List<QueryDocumentSnapshot> result;
        List<Object> list = new ArrayList<>();
        boolean useQueryParams = !(field.isEmpty()) && !(filter.isEmpty()) && !(value.isEmpty());

        result = (useQueryParams) ?
                repository.findAllMin(COLLECTION_NAME, Integer.parseInt(limit), field, FilterType.valueOf(filter), value) :
                repository.findAllMin(COLLECTION_NAME, Integer.parseInt(limit));

        for (QueryDocumentSnapshot document : result) {
            if (document.contains("matricula")) list.add(document.toObject(EstagiarioMinDTO.class));
            else list.add(document.toObject(UsuarioMinDTO.class));
        }

        return list;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        DocumentSnapshot snapshot = repository.findById(COLLECTION_NAME, username);
        if (snapshot.contains("matricula")) return snapshot.toObject(Estagiario.class);
        else return snapshot.toObject(Usuario.class);
    }

    public Usuario authenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();
        String username = usuario.getEmail();
        return (Usuario) loadUserByUsername(username);
    }

    public Boolean update(String id, Map<String, Object> data) {
        return repository.update(COLLECTION_NAME, id, data);
    }

    public Boolean delete(String id) {
        return repository.delete(COLLECTION_NAME, id);
    }

    public Boolean deleteAll(String limit, String field, String filter, String value) {
        boolean useQueryParams = (field != null) && (filter != null) && (value != null);
        return (useQueryParams) ?
                repository.deleteAll(COLLECTION_NAME, Integer.parseInt(limit), field, FilterType.valueOf(filter), value) :
                repository.deleteAll(COLLECTION_NAME, Integer.parseInt(limit));
    }
}
