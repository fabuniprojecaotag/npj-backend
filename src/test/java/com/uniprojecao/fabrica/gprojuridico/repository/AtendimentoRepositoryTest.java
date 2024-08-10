package com.uniprojecao.fabrica.gprojuridico.repository;

import com.uniprojecao.fabrica.gprojuridico.models.atendimento.Atendimento;
import com.uniprojecao.fabrica.gprojuridico.enums.FilterType;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.interfaces.CsvToAtendimento;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static com.uniprojecao.fabrica.gprojuridico.Utils.seedDatabase;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.ATENDIMENTOS_COLLECTION;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.sleep;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AtendimentoRepositoryTest {

    AtendimentoRepository repository = new AtendimentoRepository();

    @BeforeAll
    static void beforeAll() {
        seedDatabase(0, ATENDIMENTOS_COLLECTION);
    }

    @Test
    void findAll() {
        QueryFilter queryFilter = new QueryFilter("area", "Civil", FilterType.EQUAL);

        var list1 = repository.findAll(20, null);
        assertNotNull(list1);

        var list2 = repository.findAll(20, queryFilter);
        assertNotNull(list2);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/atendimentos.csv")
    void findById(@CsvToAtendimento Atendimento atendimento) {
        String id = atendimento.getId();

        Atendimento result = repository.findById(id);
        assertNotNull(result);
    }

    @AfterAll
    static void afterAll() throws Exception {
        BaseRepository.firestore.close();
        sleep(1000);
    }
}