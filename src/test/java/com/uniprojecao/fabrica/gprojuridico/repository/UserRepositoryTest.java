package com.uniprojecao.fabrica.gprojuridico.repository;

import com.google.cloud.NoCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.uniprojecao.fabrica.gprojuridico.domains.enums.FilterType;
import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.interfaces.CsvToEstagiario;
import com.uniprojecao.fabrica.gprojuridico.interfaces.CsvToUser;
import jakarta.annotation.Nullable;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserRepositoryTest {

    private final UserRepository underTest;
    private static Firestore firestore;
    private static Boolean databaseEmpty = true;

    public UserRepositoryTest() {
        underTest = mock(UserRepository.class);
    }

    @BeforeAll
    static void beforeAll() {
        FirestoreOptions options = FirestoreOptions
                .getDefaultInstance()
                .toBuilder()
                .setHost("localhost:8090")
                .setCredentials(NoCredentials.getInstance())
                .setProjectId("gprojuridico-dev")
                .build();
        firestore = options.getService();
    }

    @BeforeEach
    void setUp() {
        underTest.firestore = firestore;
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class UsuarioTest {
        @Timeout(3) // Por segurança: caso o desenvolvedor esqueça de ativar o emulador para testes, o Timeout lançará a exceção e parará a execução demorada dos testes.
        @BeforeEach
        void setUp() {
            seedDatabaseIfNecessary();
        }

        @Test
        @Order(1)
        void findAll() {
            when(underTest.findAll(20, null, true))
                    .thenCallRealMethod();

            var list = underTest.findAll(20, null, true);
            assertEquals(4, list.size()); // Haverá 4 documentos no database. Logo, o método findAll() deve apresentar, pelo menos, o tamanho correspondente.
        }

        @Test
        @Order(2)
        void findAllWithFilter() {
            var filter = FilterType.EQUAL;
            var queryFilter = new QueryFilter("role", "PROFESSOR", filter);

            when(underTest.findAll(20, queryFilter, true))
                    .thenCallRealMethod();

            var list = underTest.findAll(20, queryFilter, true);
            assertEquals(3, list.size()); // Haverá 4 documentos no database, a qual 3 serão professores. Logo, o método findAll() deve apresentar, pelo menos, o tamanho correspondente.
        }

        @ParameterizedTest
        @CsvFileSource(resources = "/usuarios.csv", numLinesToSkip = 3) // Fornece apenas o último registro da planilha para executar este teste, dado que 1 registro é suficiente para testar o método chamado.
        @Order(3)
        void update(@CsvToUser Usuario usuario) {
            String id = usuario.getEmail();
            var data = Map.of("unidadeInstitucional", (Object) "Guará");

            when(underTest.update(any(String.class), any()))
                    .thenCallRealMethod();
            when(underTest.findById(any(String.class)))
                    .thenCallRealMethod();

            underTest.update(id, data);
            Usuario result = underTest.findById(id);

            assertEquals(result.getUnidadeInstitucional(), data.get("unidadeInstitucional"));
            assertNotEquals(result, usuario);
        }

        @ParameterizedTest
        @CsvFileSource(resources = "/usuarios.csv")
        @Order(4)
        void delete(@CsvToUser Usuario usuario){
            String id = usuario.getEmail();

            var filter = FilterType.EQUAL;
            var queryFilter = new QueryFilter("email", id, filter);

            when(underTest.delete(any(String.class)))
                    .thenCallRealMethod();
            when(underTest.findAll(1, queryFilter, false))
                    .thenCallRealMethod();

            underTest.delete(id);
            var result = underTest.findAll(1, queryFilter, false); // Se o underTest.findById() for usado, este lançará uma exceção de ID não encontrado e o teste, portanto, falhará.

            assertEquals(0, result.size());
        }

        @Test
        @Order(5)
        void deleteAll() {
            seedDatabaseIfNecessary();
            clearDatabase(null);
            assertTrue(databaseEmpty);
        }

        @Test
        @Order(6)
        void deleteAllWithFilter() {
            seedDatabaseIfNecessary(); // Após o teste deleteAll() executar, não haverá mais registros na base de dados. Logo, um seed é desejável para assegurar a eficácia deste teste.

            var filter = FilterType.EQUAL;
            var queryFilter = new QueryFilter("role", "PROFESSOR", filter);

            clearDatabase(queryFilter);

            assertTrue(databaseEmpty); // Devido ao método databaseSeed(), haverá apenas 3 usuários com role de 'Professor'. Logo, o atributo 'databaseEmpty' deve retornar true.
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Disabled
    class EstagiarioTest {
        @ParameterizedTest
        @CsvFileSource(resources = "/estagiarios.csv")
        @Order(1)
        void saveWithCustomId(@CsvToEstagiario Usuario usuario) {
            String id = usuario.getEmail();

            when(underTest.saveWithCustomId(any(String.class), any(Usuario.class)))
                    .thenCallRealMethod();

            Usuario o = underTest.saveWithCustomId(id, usuario);
            assertNotNull(o);
        }

        @ParameterizedTest
        @CsvFileSource(resources = "/estagiarios.csv")
        @Order(2)
        void findById(@CsvToEstagiario Usuario usuario) {
            String id = usuario.getEmail();

            when(underTest.findById(any(String.class)))
                    .thenCallRealMethod();

            Usuario o = underTest.findById(id);
            assertNotNull(o);
        }

        @ParameterizedTest
        @CsvFileSource(resources = "/estagiarios.csv")
        @Order(3)
        void update(@CsvToEstagiario Usuario usuario) {
            String id = usuario.getEmail();
            var data = Map.of("unidadeInstitucional", (Object) "Guará");

            when(underTest.update(any(String.class), any()))
                    .thenCallRealMethod();

            Boolean updated = underTest.update(id, data);
            assertTrue(updated);
        }

        @ParameterizedTest
        @CsvFileSource(resources = "/estagiarios.csv")
        @Order(4)
        @Disabled
        void delete(@CsvToEstagiario Usuario usuario) {
            String id = usuario.getEmail();

            when(underTest.delete(any(String.class)))
                    .thenCallRealMethod();

            Boolean deleted = underTest.delete(id);
            assertTrue(deleted);
        }
    }

    private void seedDatabaseIfNecessary() {
        if (databaseEmpty) {
            var list = List.of(
                    new Usuario("marcos.silva@projecao.br", "Marcos Silva", "028.923.381-02", "Taguatinga", "123456", true, "PROFESSOR"),
                    new Usuario("rebeca.lopes@projecao.br", "Rebeca Lopes Silva", "392.234.119-93", "Taguatinga", "123456", true, "PROFESSOR"),
                    new Usuario("isaque.costa@projecao.br", "Isaque Costa Carvalho", "773.224.145-03", "Taguatinga", "123456", true, "PROFESSOR"),
                    new Usuario("leticia.alves@projecao.br", "Letícia Alves Martins", "723.125.889-73", "Taguatinga", "123456", true, "SECRETARIA")
            );

            when(underTest.saveWithCustomId(anyString(), any())).thenCallRealMethod();

            for (var item : list) {
                underTest.saveWithCustomId(item.getEmail(), item);
            }

            databaseEmpty = false;
        }
    }

    private void clearDatabase(@Nullable QueryFilter queryFilter) {
        int limit = 20;

        when(underTest.deleteAll(limit, queryFilter))
                .thenCallRealMethod();
        when(underTest.findAll(limit, queryFilter, false))
                .thenCallRealMethod();

        underTest.deleteAll(limit, queryFilter);
        var list = underTest.findAll(limit, queryFilter, false);

        databaseEmpty = list.isEmpty();
    }
}