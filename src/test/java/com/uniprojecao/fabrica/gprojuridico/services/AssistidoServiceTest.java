package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.domains.assistido.Assistido;
import com.uniprojecao.fabrica.gprojuridico.interfaces.CsvToAssistido;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.Utils.seedDatabase;
import static com.uniprojecao.fabrica.gprojuridico.data.AssistidoData.seedWithOneAssistido;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.ASSISTIDOS_COLLECTION;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.ATENDIMENTOS_COLLECTION;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.ModelMapper.toDto;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.sleep;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AssistidoServiceTest {

    AssistidoService service = new AssistidoService();

    @BeforeAll
    static void beforeAll() {
        seedDatabase(0, ASSISTIDOS_COLLECTION);
    }

    @Test
    @Order(1)
    void findAll() {
        var result = service.findAll("20", "", "", "");
        assertNotNull(result);
    }

    @Test
    @Order(1)
    void findAllAtendimentos() {
        var id = "288.610.170-29";
        seedDatabase(0, ATENDIMENTOS_COLLECTION);
        var result = service.findAllAtendimentos(id, "20");
        assertFalse(result.isEmpty(), "List should not be empty.");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/assistidos.csv")
    @Order(2)
    void findById(@CsvToAssistido Assistido assistidoToFind) {
        var assistidoFound = service.findById(assistidoToFind.getCpf());
        assertEquals(toDto(assistidoToFind), assistidoFound);
    }

    @Test
    @Order(3)
    void update() {
        var cpf = "288.610.170-29";

        var originalEmail = service.findById(cpf).getEmail();
        service.update(cpf, Map.of("email", "cleyton.az@example.com"));
        sleep(1000);
        var updatedemail = service.findById(cpf).getEmail();

        assertNotEquals(originalEmail, updatedemail);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/assistidos.csv")
    @Order(4)
    void delete(@CsvToAssistido Assistido assistidoToDelete) {
        var cpf = assistidoToDelete.getCpf();
        service.delete(cpf);
        sleep(1000);
        var assistidoDeleted = service.findById(cpf);

        assertNull(assistidoDeleted);
    }

    @Test
    @Order(5)
    void deleteAll() {
        seedDatabase(0, ASSISTIDOS_COLLECTION);

        service.deleteAll("20", "", "", "");
        sleep(1000);
        var result = service.findAll("20", "", "", "");

        assertTrue(result.isEmpty());
    }

    @Test
    @Order(6)
    void insert() {
        var assistidoToEnter = seedWithOneAssistido();
        var assistidoEntered = service.insert(toDto(assistidoToEnter));
        var assistidoFound = service.findById(assistidoEntered.getCpf());

        assertEquals(assistidoEntered, assistidoFound);
    }
}