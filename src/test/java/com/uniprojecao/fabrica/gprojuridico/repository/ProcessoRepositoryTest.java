package com.uniprojecao.fabrica.gprojuridico.repository;

import com.google.cloud.firestore.Firestore;
import com.uniprojecao.fabrica.gprojuridico.domains.enums.FilterType;
import com.uniprojecao.fabrica.gprojuridico.domains.processo.Processo;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.interfaces.CsvToProcesso;
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

class ProcessoRepositoryTest {

    private final BaseRepository baseRepository;
    private final ProcessoRepository underTest;
    private static Firestore firestore;
    private static Boolean databaseEmpty = true;

    public ProcessoRepositoryTest() {
        baseRepository = mock(BaseRepository.class);
        underTest = mock(ProcessoRepository.class);
    }

    @Timeout(7)
    @BeforeAll
    static void beforeAll() {
        firestore = getFirestoreOptions();

        BaseRepository.firestore = firestore;
        databaseEmpty = seedDatabase(databaseEmpty, "Processo");
    }

    @Test
    void findAll() {
        QueryFilter queryFilter = new QueryFilter("vara", "2ª vara", FilterType.GREATER_THAN_OR_EQUAL);

        when(underTest.findAll(20, null)).thenCallRealMethod();
        when(underTest.findAll(20, queryFilter)).thenCallRealMethod();

        var list1 = underTest.findAll(20, null);
        assertEquals(3, list1.size());

        var list2 = underTest.findAll(20, queryFilter);
        assertEquals(1, list2.size());
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/processos.csv")
    void findById(@CsvToProcesso Processo processo) {
        String id = processo.getNumero();

        when(underTest.findById(id)).thenCallRealMethod();

        Processo result = underTest.findById(id);
        assertEquals(result, processo);
    }

    @AfterAll
    static void afterAll() {
        clearDatabase(null, "Processo");
    }
}