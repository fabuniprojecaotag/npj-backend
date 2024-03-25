package com.uniprojecao.fabrica.gprojuridico.repository;

import com.google.cloud.firestore.Firestore;
import com.uniprojecao.fabrica.gprojuridico.domains.atendimento.Atendimento;
import com.uniprojecao.fabrica.gprojuridico.domains.enums.FilterType;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.interfaces.CsvToAtendimento;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static com.uniprojecao.fabrica.gprojuridico.Utils.getFirestoreOptions;
import static com.uniprojecao.fabrica.gprojuridico.Utils.seedDatabase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AtendimentoRepositoryTest {

    private final BaseRepository baseRepository;
    private final AtendimentoRepository underTest;
    private static Firestore firestore;
    private static Boolean databaseEmpty = true;

    public AtendimentoRepositoryTest() {
        baseRepository = mock(BaseRepository.class);
        underTest = mock(AtendimentoRepository.class);
    }

    @Timeout(7)
    @BeforeAll
    static void beforeAll() {
        firestore = getFirestoreOptions();

        BaseRepository.firestore = firestore;
        databaseEmpty = seedDatabase(databaseEmpty, "Atendimento");
    }

    @Test
    void findAll() {
        QueryFilter queryFilter = new QueryFilter("area", "Civil", FilterType.EQUAL);

        when(underTest.findAll(20, null)).thenCallRealMethod();
        when(underTest.findAll(20, queryFilter)).thenCallRealMethod();

        var list1 = underTest.findAll(20, null);
        assertEquals(1, list1.size());

        var list2 = underTest.findAll(20, queryFilter);
        assertEquals(1, list2.size());
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/atendimentos.csv")
    void findById(@CsvToAtendimento Atendimento atendimento) {
        String id = atendimento.getId();

        when(underTest.findById(id)).thenCallRealMethod();

        Atendimento result = underTest.findById(id);
        assertEquals(result, atendimento);
    }

//    @AfterAll
//    static void afterAll() {
//        clearDatabase(null, "Atendimento");
//    }
}