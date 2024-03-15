package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.domains.enums.FilterType;
import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Estagiario;
import com.uniprojecao.fabrica.gprojuridico.domains.usuario.SupervisorMin;
import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.dto.usuario.EstagiarioDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.usuario.UsuarioDTO;
import com.uniprojecao.fabrica.gprojuridico.repository.UserRepository;
import com.uniprojecao.fabrica.gprojuridico.services.exceptions.UserAlreadyCreatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.encryptPassword;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private static final String COLLECTION_NAME = "usuarios";

    private final UserRepository repository;

    public Map<String, Object> insert(UsuarioDTO dto) {
        var dtoEmail = dto.getEmail();

        // Validação: se já existe usuário com o e-mail e CPF fornecidos ou somente com o e-mail fornecido.
        var foundedUser = (Usuario) loadUserByUsername(dtoEmail);
        if (foundedUser != null) {
            String userEmail = foundedUser.getEmail();
            String userCpf = foundedUser.getCpf();
            Boolean equalEmails = Objects.equals(userEmail, dtoEmail);
            Boolean equalCpfs = Objects.equals(userCpf, dto.getCpf());

            if (equalEmails && equalCpfs) {
                throw new UserAlreadyCreatedException(userEmail, userCpf);
            } else {
                throw new UserAlreadyCreatedException(UserUniqueField.EMAIL, userEmail);
            }
        }

        encryptPassword(dto);

        var result = repository.saveWithCustomId(dtoEmail, passDtoToEntity(dto));
        return Map.of(
                "object", result,
                "id", dtoEmail
        );
    }

    public enum UserUniqueField { EMAIL }

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

    public void update(String id, Map<String, Object> data) {
        repository.update(id, data);
    }

    public void delete(String id) {
        repository.delete(id);
    }

    public void deleteAll(String limit, String field, String filter, String value) {
        boolean useQueryParams =
                !(field.isEmpty()) &&
                !(filter.isEmpty()) &&
                !(value.isEmpty());

        QueryFilter queryFilter = (useQueryParams) ? new QueryFilter(field, value, FilterType.valueOf(filter)) : null;
        repository.deleteAll(COLLECTION_NAME, Integer.parseInt(limit), queryFilter);
    }

    private static Usuario passDtoToEntity(UsuarioDTO dto) {
        Usuario usuario = new Usuario();

        usuario.setEmail(dto.getEmail());
        usuario.setNome(dto.getNome());
        usuario.setCpf(dto.getCpf());
        usuario.setUnidadeInstitucional(dto.getUnidadeInstitucional());
        usuario.setSenha(dto.getSenha());
        usuario.setStatus(dto.getStatus());
        usuario.setRole(dto.getRole());

        if (dto instanceof EstagiarioDTO estagiarioDTO) {
            var estagiario = (Estagiario) usuario;
            var supervisorMinDTO = estagiarioDTO.getSupervisor();

            estagiario.setMatricula(estagiarioDTO.getMatricula());
            estagiario.setSemestre(estagiarioDTO.getMatricula());
            estagiario.setSupervisor(new SupervisorMin(supervisorMinDTO.getId(), supervisorMinDTO.getNome()));

            return estagiario;
        }

        return usuario;
    }
}
