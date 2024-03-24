package com.uniprojecao.fabrica.gprojuridico.repository;

import com.google.cloud.firestore.Firestore;
import com.uniprojecao.fabrica.gprojuridico.domains.assistido.Assistido;
import com.uniprojecao.fabrica.gprojuridico.domains.enums.FilterType;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.interfaces.CsvToAssistido;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static com.uniprojecao.fabrica.gprojuridico.Utils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AssistidoRepositoryTest {

    private final BaseRepository baseRepository;
    private final AssistidoRepository underTest;
    private static Firestore firestore;
    private static Boolean databaseEmpty = true;

    public AssistidoRepositoryTest() {
        baseRepository = mock(BaseRepository.class);
        underTest = mock(AssistidoRepository.class);
    }

    @Timeout(7)
    @BeforeAll
    static void beforeAll() {
        firestore = getFirestoreOptions();

        BaseRepository.firestore = firestore;
        databaseEmpty = seedDatabase(databaseEmpty, "Assistido");
    }

    @Test
    void findAll() {
        QueryFilter queryFilter = new QueryFilter("escolaridade", "Superior", FilterType.EQUAL);

        when(underTest.findAll(20, null)).thenCallRealMethod();
        when(underTest.findAll(20, queryFilter)).thenCallRealMethod();

        var list1 = underTest.findAll(20, null);
        assertEquals(3, list1.size());

        var list2 = underTest.findAll(20, queryFilter);
        assertEquals(2, list2.size());
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/assistidos.csv")
    void findById(@CsvToAssistido Assistido assistido) {
        String id = assistido.getCpf();

        when(underTest.findById(id)).thenCallRealMethod();

        Assistido result = underTest.findById(id);
        assertEquals(result, assistido);
    }

    @AfterAll
    static void afterAll() {
        clearDatabase(null, "Assistido");
    }
}