package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.Utils;
import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.dto.usuario.UsuarioDTO;
import com.uniprojecao.fabrica.gprojuridico.interfaces.CsvToUsuario;
import com.uniprojecao.fabrica.gprojuridico.services.exceptions.UserAlreadyCreatedException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.Utils.clearDatabase;
import static com.uniprojecao.fabrica.gprojuridico.Utils.seedDatabase;
import static com.uniprojecao.fabrica.gprojuridico.data.UsuarioData.seedWithOneUsuario;
import static com.uniprojecao.fabrica.gprojuridico.data.UsuarioData.seedWithOneUsuarioDTO;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.USUARIOS_COLLECTION;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.ModelMapper.toDto;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.sleep;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioService underTest;

    @Nested
    @Disabled("Necessário haver correção no stubbing para underTest.findById()")
    class Exception {
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

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class CRUD {
        UsuarioService service = new UsuarioService();

        @BeforeAll
        static void beforeAll() {
            seedDatabase(0, USUARIOS_COLLECTION);
        }

        @ParameterizedTest
        @CsvFileSource(resources = "/usuarios.csv")
        @Order(1)
        void findById(@CsvToUsuario Usuario userToFind) {
            var userFound = service.findById(userToFind.getId());
            assertEquals(toDto(userToFind), userFound);
        }

        @Test
        @Order(2)
        void findAll() {
            var result = service.findAll("20", "", "", "");
            assertNotNull(result);
        }

        @Test
        @Order(3)
        void update() {
            var id = "leticia.alves";
            var rawPassword = "132435";

            var user = service.findById(id);
            service.update(id, Map.of(
                    "nome", "Letícia Neves",
                    "email", "letica.neves@projecao.br",
                    "senha", rawPassword
            ));
            sleep(1000); // Evita que o findById() pegue o valor antigo
            var updatedUser = service.findById(id);
            var encryptedPassword = updatedUser.getSenha();

            System.out.println("original: " + user);
            System.out.println("updated: " + updatedUser);

            assertAll(
                    () -> assertNotEquals(user, updatedUser, "User shoud not have the same data"),
                    () -> assertNotEquals(rawPassword, encryptedPassword, "User shoud have the password encrypted")
            );


        }

        @ParameterizedTest
        @CsvFileSource(resources = "/usuarios.csv")
        @Order(4)
        void delete(@CsvToUsuario Usuario userToDelete) {
            var id = userToDelete.getId();
            service.delete(id);
            sleep(1000); // Evita que o findById() pegue o valor antigo
            var userDeleted = service.findById(id);

            assertNull(userDeleted);
        }

        @Test
        @Order(5)
        void deleteAll() {
            Utils.seedDatabase(0, USUARIOS_COLLECTION);

            service.deleteAll("20", "", "", "");
            sleep(1000);
            var result = service.findAll("20", "", "", "");

            assertTrue(result.isEmpty());
        }

        @Test
        @Order(6)
        void insert() {
            clearDatabase(null, USUARIOS_COLLECTION);
            sleep(1000);

            var userToEnter = seedWithOneUsuario();
            var userEntered = service.insert(toDto(userToEnter));
            var userFound = service.findById(userEntered.getId());

            assertAll("user",
                    // Valida se id foi definido
                    () -> {
                        String noId = userToEnter.getId();
                        String withId = userEntered.getId();
                        assertNotEquals(noId, withId);
                        assertNotNull(withId);
                    },
                    // Valida se senha foi criptografada
                    () -> {
                        String rawPassword = userToEnter.getSenha();
                        String encryptedPassword = userEntered.getSenha();
                        assertNotEquals(rawPassword, encryptedPassword);
                    },
                    // Valida se usuário foi inserido corretamente
                    () -> assertEquals(userEntered, userFound));
        }
    }
}