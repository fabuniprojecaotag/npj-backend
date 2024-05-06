package com.uniprojecao.fabrica.gprojuridico.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.dto.min.UsuarioMinDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.usuario.EstagiarioDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.usuario.UsuarioDTO;
import com.uniprojecao.fabrica.gprojuridico.repository.UsuarioRepository;
import com.uniprojecao.fabrica.gprojuridico.services.exceptions.UserAlreadyCreatedException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.USUARIOS_COLLECTION;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.UsuarioUtils.UserUniqueField;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.initFilter;
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

    @Autowired
    private ModelMapper modelMapper;

    public UsuarioService(UsuarioRepository repository) {
        super(repository, collectionName);
        this.repository = repository;
    }

    private void checkIfExists(UsuarioDTO dto, String id) {
        var result = findById(id);
        if (result != null) {
            String userEmailFound = result.getEmail();
            String userCpfFound = result.getCpf();

            Boolean equalEmails = userEmailFound == dto.getEmail();
            Boolean equalCpfs = userCpfFound == dto.getCpf();

            if (equalEmails && equalCpfs) {
                throw new UserAlreadyCreatedException(userEmailFound, userCpfFound);
            } else {
                throw new UserAlreadyCreatedException(UserUniqueField.EMAIL, userEmailFound);
            }
        }
    }

    public static void encryptPassword(UsuarioDTO u) {
        u.setSenha(BCrypt.withDefaults().hashToString(12, u.getSenha().toCharArray()));
    }

    public List<UsuarioMinDTO> findAll(String limit, String field, String filter, String value) {
        return repository.findAll(parseInt(limit), initFilter(field, filter, value));
    }

    public UsuarioDTO findById(String id) {
        var usuario = repository.findById(id);
        return (usuario != null) ? toDto(usuario) : null;
    }

    public UsuarioDTO findMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();
        String id = usuario.getEmail();
        return findById(id);
    }

    private String generateId(UsuarioDTO dto) {
        return (!(dto instanceof EstagiarioDTO e)) ? dto.getEmail().replaceAll("@projecao.br", "") : e.getMatricula();
    }

    public UsuarioDTO insert(UsuarioDTO dto) {
        var id = generateId(dto);
        dto.setId(id);

        checkIfExists(dto, id);
        encryptPassword(dto);

        repository.save(collectionName, id, toEntity(dto));
        return dto;
    }


    @Override
    public UserDetails loadUserByUsername(String username) {
        return repository.findById(username);
    }

    private UsuarioDTO toDto(Usuario entity) {
        return modelMapper.map(entity, UsuarioDTO.class);
    }

    public Usuario toEntity(UsuarioDTO dto) {
        return modelMapper.map(dto, Usuario.class);
    }
}
