package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.dto.usuario.UsuarioDTO;
import com.uniprojecao.fabrica.gprojuridico.services.exceptions.UserAlreadyCreatedException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.uniprojecao.fabrica.gprojuridico.data.UsuarioData.seedWithOneUsuarioDTO;
import static com.uniprojecao.fabrica.gprojuridico.data.UsuarioData.seedWithUsuarioDTO;
import static com.uniprojecao.fabrica.gprojuridico.services.UsuarioService.encryptPassword;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioService underTest;

    @Nested
    class CheckUser {
        @Test
        void givenUserFound_whenEmailAndCpfAreEquals_throwsException() {
            // given
            var UserToEnter = seedWithOneUsuarioDTO();

            // when
            when(underTest.insert(UserToEnter)).thenCallRealMethod();
            when(underTest.findById(UserToEnter.getId())).then(invocation -> UserToEnter);

            // then
            assertThatThrownBy(() -> underTest.insert(UserToEnter))
                    .isInstanceOf(UserAlreadyCreatedException.class)
                    .hasMessageContaining("Usuário com o email \"" + UserToEnter.getEmail() + "\" e CPF \"" + UserToEnter.getCpf() + "\" informados já existe.");
        }

        @Test
        void givenUserFound_whenEmailIsEqual_throwsException() {
            // given
            var userToEnter = seedWithOneUsuarioDTO();

            var userFoundInTheDatabase = new UsuarioDTO();
            userFoundInTheDatabase.setEmail(userToEnter.getEmail());
            userFoundInTheDatabase.setCpf("999.999.999-99");

            // when
            when(underTest.insert(userToEnter)).thenCallRealMethod();
            when(underTest.findById(userToEnter.getId())).then(invocation -> userFoundInTheDatabase);

            // then
            assertThatThrownBy(() -> underTest.insert(userToEnter))
                    .isInstanceOf(UserAlreadyCreatedException.class)
                    .hasMessageContaining("Usuário com o email \"" + userToEnter.getEmail() + "\" já existe.");
        }
    }

    @Test
    void validateEncryptedPassword() {
        var list = seedWithUsuarioDTO();
        int changedPassword = 0;

        for (var dto : list) {
            var rawPassword = dto.getSenha();
            try (MockedStatic<UsuarioService> utilities = mockStatic(UsuarioService.class)) {
                utilities.when(() -> encryptPassword(dto)).thenCallRealMethod();
            }
            encryptPassword(dto);
            if (dto.getSenha() != rawPassword) changedPassword++;
        }

        assertEquals(6, changedPassword);
    }
}