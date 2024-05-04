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

/**
 * Baseado no padrão arquitetural camada de serviço, este serviço tem por
 * objetivo apresentar a implementação de regras e lógica de negócio. <p>
 *
 * Este serviço em si trata da interação com o modelo de domínio {@link Usuario},
 * onde as regras de negócios pertinentes a este domínio estão listadas logo
 * abaixo: <p>
 *
 * <h2>Regras de Negócio</h2>
 *
 * <h3>Verificar Usuário</h3>
 * Ao inserir um usuário, deverá ser verificado se este existe no banco de dados.
 * Caso exista, uma exceção deverá ser lançada; senão o fluxo segue-se normalmente. <p>
 *
 * <h3>Gerar Senha Temporária</h3>
 * Caso o DTO recebido não apresente a senha preenchida, uma senha aleatória deverá
 * ser gerada. Além disso, o usuário deve receber, por e-mail, suas credenciais de
 * acesso, contendo: nome de usuário, e-mail e senha temporária. <p>
 *
 * <h3>Gerar Senha Definitiva</h3>
 * Caso seja a 1ª vez que o usuário efetue login no sistema e possua senha temporária
 * ativa, este deverá trocar a senha temporária por uma definitiva, que será informada
 * pelo próprio usuário. <p>
 *
 * <h3>Criptografar Senha</h3>
 * Após o sistema receber o usuário com a senha (definitiva) preenchida, o sistema
 * deverá criptografar a senha para o correto armazenamento desta em algum banco de
 * dados. <p>
 *
 * @author Jonatas Mateus
 * @see UsuarioDTO
 * @since 1.0
 */
@Service
public class UsuarioService extends BaseService implements UserDetailsService {

    private final UsuarioRepository repository;
    private static final String collectionName = USUARIOS_COLLECTION;

    public UsuarioService(UsuarioRepository repository) {
        super(repository, collectionName);
        this.repository = repository;
    }

    public UsuarioDTO insert(UsuarioDTO dto) {
        var id = dto.getEmail();

        verifyIfExistsUserInDatabase(dto, id);
        encryptPassword(dto);

        repository.save(collectionName, id, dtoToUsuario(dto));
        return dto;
    }

    public List<UsuarioMinDTO> findAll(String limit, String field, String filter, String value) {
        return repository.findAll(parseInt(limit), initFilter(field, filter, value));
    }

    public UsuarioDTO findById(String id) {
        return usuarioToDto(repository.findById(id));
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
}
