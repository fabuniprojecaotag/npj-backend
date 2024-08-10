package com.uniprojecao.fabrica.gprojuridico.repository;

import com.uniprojecao.fabrica.gprojuridico.enums.FilterType;
import com.uniprojecao.fabrica.gprojuridico.models.processo.Processo;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.interfaces.CsvToProcesso;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static com.uniprojecao.fabrica.gprojuridico.Utils.seedDatabase;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.PROCESSOS_COLLECTION;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProcessoRepositoryTest {

    ProcessoRepository repository = new ProcessoRepository();

    @BeforeAll
    static void beforeAll() {
        seedDatabase(0, PROCESSOS_COLLECTION);
    }

    @Test
    void findAll() {
        QueryFilter queryFilter = new QueryFilter("vara", "2ª vara", FilterType.GREATER_THAN_OR_EQUAL);

        var list1 = repository.findAll(20, null);
        assertNotNull(list1);

        var list2 = repository.findAll(20, queryFilter);
        assertNotNull(list2);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/processos.csv")
    void findById(@CsvToProcesso Processo processo) {
        String id = processo.getNumero();

        Processo result = repository.findByNumero(id);
        assertNotNull(result);
    }
}