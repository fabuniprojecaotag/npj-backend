package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.dto.min.UsuarioMinDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.usuario.UsuarioDTO;
import com.uniprojecao.fabrica.gprojuridico.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.UsuarioUtils.*;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.encryptPassword;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.initFilter;
import static java.lang.Integer.parseInt;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository repository;

    public UsuarioDTO insert(UsuarioDTO dto) {
        var id = dto.getEmail();

        verifyIfExistsUserInDatabase(dto, id);
        encryptPassword(dto);

        repository.save(id, dtoToUsuario(dto));
        return dto;
    }

    public List<UsuarioMinDTO> findAll(String limit, String field, String filter, String value) {
        return repository.findAll(parseInt(limit), initFilter(field, filter, value));
    }

    public UsuarioDTO findById(String id) {
        return usuarioToDto(repository.findById(id));
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return repository.findById(username);
    }

    public UsuarioDTO authenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();
        String id = usuario.getEmail();
        return findById(id);
    }

    public void update(String id, Map<String, Object> data) {
        repository.update(id, data);
    }

    public void delete(String id) {
        repository.delete(id);
    }

    public void deleteAll(String limit, String field, String filter, String value) {
        repository.deleteAll(parseInt(limit), initFilter(field, filter, value));
    }
}
