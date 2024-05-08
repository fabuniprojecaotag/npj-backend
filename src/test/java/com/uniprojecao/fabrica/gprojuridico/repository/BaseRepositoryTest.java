package com.uniprojecao.fabrica.gprojuridico.repository;

import com.uniprojecao.fabrica.gprojuridico.domains.atendimento.Atendimento;
import com.uniprojecao.fabrica.gprojuridico.domains.atendimento.AtendimentoCivil;
import com.uniprojecao.fabrica.gprojuridico.interfaces.CsvToAtendimento;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.HashMap;
import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.Utils.getFirestore;
import static com.uniprojecao.fabrica.gprojuridico.Utils.seedDatabase;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.ATENDIMENTOS_COLLECTION;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.sleep;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BaseRepositoryTest {

    private final AtendimentoRepository underTest = new AtendimentoRepository();
    private Integer count = 0;

    public BaseRepositoryTest() {
        BaseRepository.firestore = getFirestore();
    }

    @BeforeEach
    void setUp() {
        seedDatabase(count, ATENDIMENTOS_COLLECTION);
    }

    @Order(1)
    @ParameterizedTest
    @CsvFileSource(resources = "/atendimentos.csv", numLinesToSkip = 1)
    void update(@CsvToAtendimento Atendimento atendimento) {
        Map<String, Object> data = new HashMap<>();

        var parteContraria = Map.of("nome", "Mauro Silva");
        Map<String, Object> ficha = new HashMap<>();
        ficha.put("dadosSensiveis", false);
        ficha.put("parteContraria", parteContraria);
        data.put("status", "Processo arquivado");
        data.put("ficha", ficha);

        String id = atendimento.getId();

        BaseRepository.update("atendimentos", id, data);
        var updatedAtendimento = (AtendimentoCivil) underTest.findById(id);

        assertNotEquals(atendimento, updatedAtendimento);

        count++;
    }

    @Order(2)
    @ParameterizedTest
    @CsvFileSource(resources = "/atendimentos.csv")
    void delete(@CsvToAtendimento Atendimento atendimento) {
        String id = atendimento.getId();

        BaseRepository.delete("atendimentos", id);
        sleep(3);
        var result = underTest.findById(id);

        assertNull(result);

        count = 0;
    }

    @Order(3)
    @Test
    void deleteAll() {
        BaseRepository.deleteAll("atendimentos", null, 20, null);
        var result = underTest.findAll(20, null);
        assertTrue(result.isEmpty());
    }
}