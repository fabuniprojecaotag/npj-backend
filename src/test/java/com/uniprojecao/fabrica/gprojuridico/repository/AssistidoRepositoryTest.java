package com.uniprojecao.fabrica.gprojuridico.repository;

import com.uniprojecao.fabrica.gprojuridico.domains.assistido.Assistido;
import com.uniprojecao.fabrica.gprojuridico.domains.enums.FilterType;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.interfaces.CsvToAssistido;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static com.uniprojecao.fabrica.gprojuridico.Utils.*;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.ASSISTIDOS_COLLECTION;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Sem esta annotation, o atributo "count" não é incrementado sequencialmente.
class AssistidoRepositoryTest {

    private final AssistidoRepository underTest = new AssistidoRepository();
    private Integer count = 0;

    public AssistidoRepositoryTest() {
        BaseRepository.firestore = getFirestore();
    }

    @BeforeEach
    void setUp() {
        seedDatabase(count, ASSISTIDOS_COLLECTION);
    }

    @Test
    void findAll() {
        QueryFilter queryFilter = new QueryFilter("escolaridade", "Superior", FilterType.EQUAL);

        var list1 = underTest.findAll(20, null);
        assertEquals(3, list1.size());

        var list2 = underTest.findAll(20, queryFilter);
        assertEquals(2, list2.size());

        count++;
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/assistidos.csv")
    void findById(@CsvToAssistido Assistido assistido) {
        String id = assistido.getCpf();

        Assistido result = underTest.findById(id);
        assertEquals(result, assistido);

        count++;
    }

    @AfterEach
    void tearDown() {
        if (count == 4) clearDatabase(null, "assistidos");
    }
}