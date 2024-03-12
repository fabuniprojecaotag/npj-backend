package com.uniprojecao.fabrica.gprojuridico.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.uniprojecao.fabrica.gprojuridico.domains.enums.FilterType;
import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService implements UserDetailsService {

    private static final String COLLECTION_NAME = "usuarios";

    @Autowired
    UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public Map<String, Object> create(Usuario u) {
        u.setSenha(BCrypt.withDefaults().hashToString(12, u.getSenha().toCharArray()));

        var id = u.getEmail();
        var result = repository.saveWithCustomId(id, u);

        return Map.of(
                "object", result,
                "id", id
        );
    }

    public List<Object> findAll(String limit, String field, String filter, String value) {
        boolean useQueryParams =
                !(field.isEmpty()) &&
                !(filter.isEmpty()) &&
                !(value.isEmpty());

        QueryFilter queryFilter = (useQueryParams) ? new QueryFilter(field, value, FilterType.valueOf(filter)) : null;

        return (List<Object>) repository.findAll(Integer.parseInt(limit), queryFilter, true);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return repository.findById(username);
    }

    public Usuario authenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();
        String username = usuario.getEmail();
        return (Usuario) loadUserByUsername(username);
    }

    public Boolean update(String id, Map<String, Object> data) {
        return repository.update(id, data);
    }

    public Boolean delete(String id) {
        return repository.delete(id);
    }

    public Boolean deleteAll(String limit, String field, String filter, String value) {
        boolean useQueryParams =
                !(field.isEmpty()) &&
                !(filter.isEmpty()) &&
                !(value.isEmpty());

        QueryFilter queryFilter = (useQueryParams) ? new QueryFilter(field, value, FilterType.valueOf(filter)) : null;
        return repository.deleteAll(COLLECTION_NAME, Integer.parseInt(limit), queryFilter);
    }
}
