package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.models.processo.Processo;
import com.uniprojecao.fabrica.gprojuridico.interfaces.CsvToProcesso;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.Utils.seedDatabase;
import static com.uniprojecao.fabrica.gprojuridico.data.ProcessoData.seedWithOneProcesso;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.PROCESSOS_COLLECTION;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.sleep;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProcessoServiceTest {

    ProcessoService service = new ProcessoService();

    @BeforeAll
    static void beforeAll() {
        seedDatabase(0, PROCESSOS_COLLECTION);
    }

    @Test
    @Order(1)
    void findAll() {
        var result = service.findAll("20", "", "", "");
        assertNotNull(result);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/processos.csv")
    @Order(2)
    void findByNumero(@CsvToProcesso Processo processToFind) {
        var processFound = service.findByNumero(processToFind.getNumero());
        assertEquals(processToFind, processFound);
    }

    @Test
    @Order(3)
    void update() {
        var numero = "0001175-18.2013.5.05.0551";

        var originalVara = service.findByNumero(numero).getVara();
        service.update(numero, Map.of("vara", "12ª vara do trabalho do Distrito Federal"));
        sleep(1000);
        var updatedVara = service.findByNumero(numero).getVara();

        assertNotEquals(originalVara, updatedVara);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/processos.csv")
    @Order(4)
    void delete(@CsvToProcesso Processo processToDelete) {
        var numero = processToDelete.getNumero();
        service.delete(numero);
        sleep(1000);
        var processDeleted = service.findByNumero(numero);

        assertNull(processDeleted);
    }

    @Test
    @Order(5)
    void deleteAll() {
        seedDatabase(0, PROCESSOS_COLLECTION);

        service.deleteAll("20", "", "", "");
        sleep(1000);
        var result = service.findAll("20", "", "", "");

        assertTrue(result.isEmpty());
    }

    @Test
    @Order(6)
    void insert() {
        var processToEnter = seedWithOneProcesso();
        var processEntered = service.insert(processToEnter);
        var processFound = service.findByNumero(processEntered.getNumero());

        assertEquals(processEntered, processFound);
    }
}