package com.uniprojecao.fabrica.gprojuridico.repository;

import com.uniprojecao.fabrica.gprojuridico.domains.enums.FilterType;
import com.uniprojecao.fabrica.gprojuridico.domains.processo.Processo;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.interfaces.CsvToProcesso;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static com.uniprojecao.fabrica.gprojuridico.Utils.*;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.PROCESSOS_COLLECTION;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Sem esta annotation, o atributo "count" não é incrementado sequencialmente.
class ProcessoRepositoryTest {

    private final ProcessoRepository underTest = new ProcessoRepository();
    private Integer count = 0;

    public ProcessoRepositoryTest() {
        BaseRepository.firestore = getFirestore();
    }

    @BeforeEach
    void setUp() {
        seedDatabase(count, PROCESSOS_COLLECTION);
    }

    @Test
    void findAll() {
        QueryFilter queryFilter = new QueryFilter("vara", "2ª vara", FilterType.GREATER_THAN_OR_EQUAL);

        var list1 = underTest.findAll(20, null);
        assertEquals(3, list1.size());

        var list2 = underTest.findAll(20, queryFilter);
        assertEquals(1, list2.size());

        count++;
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/processos.csv")
    void findById(@CsvToProcesso Processo processo) {
        String id = processo.getNumero();

        Processo result = underTest.findByNumero(id);
        assertEquals(result, processo);

        count++;
    }

    @AfterEach
    void tearDown() {
        if (count == 4) clearDatabase(null, "processos");
    }
}