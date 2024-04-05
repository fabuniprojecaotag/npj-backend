package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.dto.min.UsuarioMinDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.usuario.EstagiarioDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.usuario.UsuarioDTO;
import com.uniprojecao.fabrica.gprojuridico.repository.UsuarioRepository;
import com.uniprojecao.fabrica.gprojuridico.services.exceptions.UserAlreadyCreatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.UsuarioUtils.*;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.encryptPassword;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.initFilter;
import static java.lang.Integer.parseInt;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    private final String COLLECTION_NAME = "usuarios";

    public UsuarioDTO insert(UsuarioDTO dto) {
        var id = (!(dto instanceof EstagiarioDTO e)) ? dto.getEmail().replaceAll("@projecao.br", "") : e.getMatricula();
        dto.setId(id);

        verifyIfExistsUserInDatabase(dto, id);
        encryptPassword(dto);

        repository.save(COLLECTION_NAME, id, dtoToUsuario(dto));
        return dto;
    }

    public List<UsuarioMinDTO> findAll(String limit, String field, String filter, String value) {
        return repository.findAll(parseInt(limit), initFilter(field, filter, value));
    }

    public UsuarioDTO findById(String id) {
        var usuario = repository.findById(id);
        return (usuario != null) ? usuarioToDto(usuario) : null;
    }

    private void verifyIfExistsUserInDatabase(UsuarioDTO dto, String id) {
        var foundedUser = findById(id);
        if (foundedUser != null) {
            String userEmail = foundedUser.getEmail();
            String userCpf = foundedUser.getCpf();
            Boolean equalEmails = Objects.equals(userEmail, id);
            Boolean equalCpfs = Objects.equals(userCpf, dto.getCpf());

            if (equalEmails && equalCpfs) {
                throw new UserAlreadyCreatedException(userEmail, userCpf);
            } else {
                throw new UserAlreadyCreatedException(UserUniqueField.EMAIL, userEmail);
            }
        }
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
        repository.update(COLLECTION_NAME, id, data);
    }

    public void delete(String id) {
        repository.delete(COLLECTION_NAME, id);
    }

    public void deleteAll(String limit, String field, String filter, String value) {
        repository.deleteAll(COLLECTION_NAME, null, parseInt(limit), initFilter(field, filter, value));
    }
}
