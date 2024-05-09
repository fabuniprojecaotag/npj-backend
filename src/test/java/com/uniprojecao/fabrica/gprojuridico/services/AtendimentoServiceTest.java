package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.domains.atendimento.Atendimento;
import com.uniprojecao.fabrica.gprojuridico.interfaces.CsvToAtendimento;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.Utils.seedDatabase;
import static com.uniprojecao.fabrica.gprojuridico.data.AtendimentoData.seedWithOneAtendimento;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.ATENDIMENTOS_COLLECTION;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.ManualMapper.toDto;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.sleep;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AtendimentoServiceTest {

    AtendimentoService service = new AtendimentoService();

    @BeforeAll
    static void beforeAll() {
        seedDatabase(0, ATENDIMENTOS_COLLECTION);
    }

    @Test
    @Order(1)
    void findAll() {
        var result = service.findAll("20", "", "", "");
        assertNotNull(result);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/atendimentos.csv")
    @Order(2)
    void findById(@CsvToAtendimento Atendimento atendimentoToFind) {
        var atendimentoFound =service.findById(atendimentoToFind.getId());
        assertNotNull(atendimentoFound);
    }

    @Test
    @Order(3)
    void update() {
        var id = "ATE00071";

        var originalStatus = service.findById(id).getStatus();
        service.update(id, Map.of("status", "Processo ativo"));
        sleep(1000);
        var updatedStatus = service.findById(id).getStatus();

        assertNotEquals(originalStatus, updatedStatus);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/atendimentos.csv")
    @Order(4)
    void delete(@CsvToAtendimento Atendimento atendimentoToDelete) {
        var id = atendimentoToDelete.getId();
        service.delete(id);
        sleep(1000);
        var atendimentoDeleted = service.findById(id);

        assertNull(atendimentoDeleted);
    }

    @Test
    @Order(5)
    void deleteAll() {
        seedDatabase(0, ATENDIMENTOS_COLLECTION);

        service.deleteAll("20", "", "", "");
        sleep(1000);
        var result = service.findAll("20", "", "", "");

        assertTrue(result.isEmpty());
    }

    @Test
    @Order(6)
    void insert() {
        var atendimentoToEnter = seedWithOneAtendimento();
        var atendimentoEntered = service.insert(toDto(atendimentoToEnter));
        var atendimentoFound = service.findById(atendimentoEntered.getId());

        assertNotNull(atendimentoFound);
    }
}