package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.dto.min.UsuarioMinDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.usuario.UsuarioDTO;
import com.uniprojecao.fabrica.gprojuridico.repository.UsuarioRepository;
import com.uniprojecao.fabrica.gprojuridico.services.exceptions.UserAlreadyCreatedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.UsuarioUtils.*;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.encryptPassword;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.initFilter;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.USUARIOS_COLLECTION;
import static java.lang.Integer.parseInt;

@Service
public class UsuarioService extends BaseService implements UserDetailsService {

    private final UsuarioRepository REPOSITORY;
    private final String COLLECTION_NAME;

    public UsuarioService(UsuarioRepository repository) {
        super(repository, USUARIOS_COLLECTION);
        REPOSITORY = repository;
        COLLECTION_NAME = USUARIOS_COLLECTION;
    }

    public UsuarioDTO insert(UsuarioDTO dto) {
        var id = dto.getEmail();

        verifyIfExistsUserInDatabase(dto, id);
        encryptPassword(dto);

        REPOSITORY.save(COLLECTION_NAME, id, dtoToUsuario(dto));
        return dto;
    }

    public List<UsuarioMinDTO> findAll(String limit, String field, String filter, String value) {
        return REPOSITORY.findAll(parseInt(limit), initFilter(field, filter, value));
    }

    public UsuarioDTO findById(String id) {
        return usuarioToDto(REPOSITORY.findById(id));
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
        return REPOSITORY.findById(username);
    }

    public UsuarioDTO authenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();
        String id = usuario.getEmail();
        return findById(id);
    }
}
