package com.uniprojecao.fabrica.gprojuridico.repository;

import com.google.cloud.NoCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.uniprojecao.fabrica.gprojuridico.domains.enums.FilterType;
import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Estagiario;
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

import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.sleep;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
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
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class UsuarioTest {
        @Timeout(3) // Por segurança: caso o desenvolvedor esqueça de ativar o emulador para testes, o Timeout lançará a exceção e parará a execução demorada dos testes.
        @BeforeEach
        void setUp() {
            seedDatabaseIfNecessary(Usuario.class);
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
            sleep(500); // Sem um intervalo de tempo, o findBy() pega o valor antigo, resultando em teste falhado.
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
            seedDatabaseIfNecessary(Usuario.class);
            clearDatabase(null);
            assertTrue(databaseEmpty);
        }

        @Test
        @Order(6)
        void deleteAllWithFilter() {
            seedDatabaseIfNecessary(Usuario.class); // Após o teste deleteAll() executar, não haverá mais registros na base de dados. Logo, um seed é desejável para assegurar a eficácia deste teste.

            var filter = FilterType.EQUAL;
            var queryFilter = new QueryFilter("role", "PROFESSOR", filter);

            clearDatabase(queryFilter);

            assertTrue(databaseEmpty); // Devido ao método databaseSeed(), haverá apenas 3 usuários com role de 'Professor'. Logo, o atributo 'databaseEmpty' deve retornar true.
        }
    }

    @Nested
    @Order(2)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class EstagiarioTest {
        @Timeout(3)
        @BeforeEach
        void setUp() {
            seedDatabaseIfNecessary(Estagiario.class);
        }

        @ParameterizedTest
        @CsvFileSource(resources = "/estagiarios.csv")
        @Order(1)
        void findById(@CsvToEstagiario Usuario usuario) {
            String id = usuario.getEmail();

            when(underTest.findById(id))
                    .thenCallRealMethod();

            Usuario result = underTest.findById(id);
            assertEquals(result, usuario);
        }

        @ParameterizedTest
        @CsvFileSource(resources = "/estagiarios.csv", numLinesToSkip = 3)
        @Order(2)
        void update(@CsvToEstagiario Usuario usuario) {
            String id = usuario.getEmail();
            var data = Map.of("semestre", (Object) "9");

            when(underTest.update(any(String.class), any()))
                    .thenCallRealMethod();
            when(underTest.findById(any(String.class)))
                    .thenCallRealMethod();

            underTest.update(id, data);
            sleep(500); // Sem um intervalo de tempo, o findBy() pega o valor antigo, resultando em teste falhado.
            Estagiario result = (Estagiario) underTest.findById(id);

            assertEquals(result.getSemestre(), data.get("semestre"));
            assertNotEquals(result, usuario);
        }

        @AfterEach
        void tearDown() {
            clearDatabase(null);
        }
    }

    private void seedDatabaseIfNecessary(@Nullable Class<?> type) {
        if (databaseEmpty) {
            List<Usuario> list;

            list = (type == Estagiario.class) ?
                    List.of(
                            new Estagiario("202102100@projecao.edu.br", "Aurélio Lima Gonçalves", "829.281.342-93", "Taguatinga", "123456", true, "ESTAGIARIO", "202102100", "8", "marcos.silva@projecao.br"),
                            new Estagiario("202102302@projecao.edu.br", "Ana Passos Lima", "440.332.095-93", "Taguatinga", "123456", true, "ESTAGIARIO", "202102302", "8", "marcos.silva@projecao.br"),
                            new Estagiario("202102299@projecao.edu.br", "Marcos Moura Santos Ferreira", "029.119.234-04", "Taguatinga", "123456", true, "ESTAGIARIO", "202102299", "8", "marcos.silva@projecao.br"),
                            new Estagiario("202101055@projecao.edu.br", "Emilly Letícia Cordeiro", "288.231.842-45", "Taguatinga", "123456", true, "ESTAGIARIO", "202101055", "8", "rebeca.lopes@projecao.br")) :
                    List.of(
                            new Usuario("marcos.silva@projecao.br", "Marcos Silva", "028.923.381-02", "Taguatinga", "123456", true, "PROFESSOR"),
                            new Usuario("rebeca.lopes@projecao.br", "Rebeca Lopes Silva", "392.234.119-93", "Taguatinga", "123456", true, "PROFESSOR"),
                            new Usuario("isaque.costa@projecao.br", "Isaque Costa Carvalho", "773.224.145-03", "Taguatinga", "123456", true, "PROFESSOR"),
                            new Usuario("leticia.alves@projecao.br", "Letícia Alves Martins", "723.125.889-73", "Taguatinga", "123456", true, "SECRETARIA"));

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