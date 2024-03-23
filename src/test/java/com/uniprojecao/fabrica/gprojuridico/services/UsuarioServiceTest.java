package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Estagiario;
import com.uniprojecao.fabrica.gprojuridico.domains.usuario.SupervisorMin;
import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.dto.usuario.EstagiarioDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.usuario.UsuarioDTO;
import com.uniprojecao.fabrica.gprojuridico.services.exceptions.UserAlreadyCreatedException;
import com.uniprojecao.fabrica.gprojuridico.services.utils.Utils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.uniprojecao.fabrica.gprojuridico.data.UsuarioData.seedWithUsuarioDTO;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.encryptPassword;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.validateText;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioService underTest;

    @Nested
    class Validation {
        @Test
        void validateEncryptedUserPassword() {
            var list = seedWithUsuarioDTO();
            int changedPassword = 0;

            for (var dto : list) {
                var rawPassword = dto.getSenha();
                try (MockedStatic<Utils> utilities = mockStatic(Utils.class)) {
                    utilities.when(() -> encryptPassword(dto)).thenCallRealMethod();
                }
                encryptPassword(dto);
                if (dto.getSenha() != rawPassword) changedPassword++;
            }

            assertEquals(4, changedPassword);
        }

        @Test
        @Disabled("validateReceivedUserEmail() está desativado, pois foi aplicado o @Pattern no atributo email de usuário para receber regex.")
        void validateReceivedUserEmail() {
            var emailRegex = "^[0-9]{9}@projecao\\.edu\\.br|[a-z]{3,}\\.[a-z]{3,}@projecao\\.br$";
            var list = List.of(
                    "202102300@projecao.edu.br", // true
                    "100020003@projecao.edu.br", // true
                    "1234567@projecao.edu.br",
                    "202010100@projecao.br",
                    "marcos.santana@projecao.br", // true
                    "marcos.santana@projecao.brs",
                    "marcos.santana@projecao.edu.br"
            );
            int validatedEmails = 0;

            for (var email : list) {
                try (MockedStatic<Utils> utilities = mockStatic(Utils.class)) {
                    utilities.when(() -> validateText(emailRegex, email)).thenCallRealMethod();
                }
                Boolean result = validateText(emailRegex, email);
                if (result) validatedEmails++;
            }

            assertEquals(3, validatedEmails);
        }
    }

    @Nested
    class Exception {
        @Test
        void throwExceptionWhenBothDTOEmailAndCpfAreEqual() {
            var list = seedWithUsuarioDTO();
            var dto = list.get(0);
            var usuario = passDtoToEntity(dto);

            when(underTest.insert(dto)).thenCallRealMethod();
            when(underTest.loadUserByUsername(dto.getEmail())).then(invocation -> usuario);

            assertThatThrownBy(() -> underTest.insert(dto))
                    .isInstanceOf(UserAlreadyCreatedException.class)
                    .hasMessageContaining("Usuário com o email \"" + dto.getEmail() + "\" e CPF \"" + dto.getCpf() + "\" informados já existe.");
        }

        @Test
        void throwExceptionWhenDTOEmailIsEqual() {
            var list = seedWithUsuarioDTO();
            var dto = list.get(0);
            var usuario = passDtoToEntity(dto);
            dto.setCpf("999.999.999-99");

            when(underTest.insert(dto)).thenCallRealMethod();
            when(underTest.loadUserByUsername(dto.getEmail())).then(invocation -> usuario);

            assertThatThrownBy(() -> underTest.insert(dto))
                    .isInstanceOf(UserAlreadyCreatedException.class)
                    .hasMessageContaining("Usuário com o email \"" + dto.getEmail() + "\" já existe.");
        }
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