package com.uniprojecao.fabrica.gprojuridico.repository;

import com.uniprojecao.fabrica.gprojuridico.domains.atendimento.Atendimento;
import com.uniprojecao.fabrica.gprojuridico.domains.enums.FilterType;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.interfaces.CsvToAtendimento;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static com.uniprojecao.fabrica.gprojuridico.Utils.*;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.ATENDIMENTOS_COLLECTION;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Sem esta annotation, o atributo "count" não é incrementado sequencialmente.
class AtendimentoRepositoryTest {

    private final AtendimentoRepository underTest = new AtendimentoRepository();
    private Integer count = 0;


    public AtendimentoRepositoryTest() {
        BaseRepository.firestore = getFirestore();
    }

    @BeforeEach
    void setUp() {
        seedDatabase(count, ATENDIMENTOS_COLLECTION);
    }

    @Test
    void findAll() {
        QueryFilter queryFilter = new QueryFilter("area", "Civil", FilterType.EQUAL);

        var list1 = underTest.findAll(20, null);
        assertEquals(2, list1.size());

        var list2 = underTest.findAll(20, queryFilter);
        assertEquals(1, list2.size());

        count++;
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/atendimentos.csv")
    void findById(@CsvToAtendimento Atendimento atendimento) {
        String id = atendimento.getId();

        Atendimento result = underTest.findById(id);
        assertEquals(result, atendimento);

        count++;
    }

    @AfterEach
    void tearDown() {
        if (count == 3) clearDatabase(null, "atendimentos");
    }
}