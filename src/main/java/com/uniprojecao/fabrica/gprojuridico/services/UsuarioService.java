package com.uniprojecao.fabrica.gprojuridico.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.uniprojecao.fabrica.gprojuridico.models.autocomplete.UsuarioAutocomplete;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Estagiario;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.dto.min.UsuarioMinDTO;
import com.uniprojecao.fabrica.gprojuridico.repository.BaseRepository;
import com.uniprojecao.fabrica.gprojuridico.repository.UsuarioRepository;
import com.uniprojecao.fabrica.gprojuridico.services.exceptions.UserAlreadyCreatedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.USUARIOS_COLLECTION;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.UsuarioUtils.UserUniqueField;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.filterValidKeys;
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
 * @see Usuario
 * @since 1.0
 */
@Service
public class UsuarioService extends BaseService implements UserDetailsService {

    public UsuarioRepository repository = new UsuarioRepository();
    private static final String collectionName = USUARIOS_COLLECTION;

    public UsuarioService() {
        super(collectionName);
    }

    private void checkIfExists(Usuario data, String id) {
        var result = findById(id);
        if (result != null) {
            String userEmailFound = result.getEmail();
            String userCpfFound = result.getCpf();

            Boolean equalEmails = userEmailFound == data.getEmail();
            Boolean equalCpfs = userCpfFound == data.getCpf();

            if (equalEmails && equalCpfs) {
                throw new UserAlreadyCreatedException(userEmailFound, userCpfFound);
            } else {
                throw new UserAlreadyCreatedException(UserUniqueField.EMAIL, userEmailFound);
            }
        }
    }

    private void defineId(Usuario usuario) {
        String id = (!(usuario instanceof Estagiario estagiario))
                ? usuario.getEmail().replaceAll("@projecao\\.br", "") // Retira o "@projecao.br"
                : estagiario.getMatricula();
        usuario.setId(id);
    }

    private void encryptPassword(Usuario u) {
        u.setSenha(BCrypt.withDefaults().hashToString(12, u.getSenha().toCharArray()));
    }

    private String encryptPassword(String rawPassword) {
        return BCrypt.withDefaults().hashToString(12, rawPassword.toCharArray());
    }

    public List<UsuarioMinDTO> findAll(String limit, String field, String filter, String value) {
        return repository.findAll(parseInt(limit), initFilter(field, filter, value));
    }

    public List<UsuarioAutocomplete> findAllMin(String limit, String field, String filter, String value) {
        return repository.findAllMin(parseInt(limit), initFilter(field, filter, value));
    }

    public Usuario findById(String id) {
        return repository.findById(id);
    }

    public Usuario findMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();
        String id = usuario.getId();
        return findById(id);
    }

    public Usuario insert(Usuario data) {
        defineId(data);
        String id = data.getId();

        checkIfExists(data, id);
        encryptPassword(data);

        BaseRepository.save(collectionName, id, data);
        return data;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return repository.findById(username);
    }

    public void update(String id, Map<String, Object> data, String clazz) {
        var validClazz = switch(clazz) {
            case "Usuario" -> Usuario.class;
            case "Estagiario" -> Estagiario.class;
            default -> throw new IllegalStateException("Unexpected value: " + clazz);
        };

        var filteredData = filterValidKeys(data, validClazz);

        String senhaKey = "senha";
        if (filteredData.containsKey(senhaKey)) {
            var rawPassword = (String) filteredData.get(senhaKey);
            var encryptedPassword = encryptPassword(rawPassword);

            // Dados em mapas Java são imutáveis; logo, uma alternativa é criar um novo map de pares chave-valor
            Map<String, Object> newData = new HashMap<>();
            for (var entry : filteredData.entrySet()) {
                if (entry.getKey() != senhaKey) newData.put(entry.getKey(), entry.getValue());
            }
            newData.put(senhaKey, encryptedPassword);

            // Salva os dados recebidos e com a senha criptografada
            update(id, newData);
        } else {
            update(id, filteredData);
        }
    }
}