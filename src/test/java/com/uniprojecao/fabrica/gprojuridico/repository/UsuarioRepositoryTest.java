package com.uniprojecao.fabrica.gprojuridico.repository;

import com.google.cloud.NoCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.uniprojecao.fabrica.gprojuridico.domains.enums.FilterType;
import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.interfaces.CsvToUsuario;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static com.uniprojecao.fabrica.gprojuridico.Utils.clearDatabase;
import static com.uniprojecao.fabrica.gprojuridico.Utils.seedDatabase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UsuarioRepositoryTest {

    private final BaseRepository baseRepository;
    private final UsuarioRepository underTest;
    private static Firestore firestore;
    private static Boolean databaseEmpty = true;

    public UsuarioRepositoryTest() {
        baseRepository = mock(BaseRepository.class);
        underTest = mock(UsuarioRepository.class);
    }

    @Timeout(4)
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

        BaseRepository.firestore = firestore;
        databaseEmpty = seedDatabase(databaseEmpty);
    }

    @Test
    void findAll() {
        QueryFilter queryFilter = new QueryFilter("role", "PROFESSOR", FilterType.EQUAL);

        when(underTest.findAll(20, null)).thenCallRealMethod();
        when(underTest.findAll(20, queryFilter)).thenCallRealMethod();

        var list1 = underTest.findAll(20, null);
        assertEquals(6, list1.size());

        var list2 = underTest.findAll(20, queryFilter);
        assertEquals(2, list2.size());
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/usuarios.csv")
    void findById(@CsvToUsuario Usuario usuario) {
        String id = usuario.getEmail();

        when(underTest.findById(id)).thenCallRealMethod();

        Usuario result = underTest.findById(id);
        assertEquals(result, usuario);
    }

    @AfterAll
    static void afterAll() {
        clearDatabase(null);
    }
}