package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.models.assistido.Assistido;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.AssistidoCivil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.uniprojecao.fabrica.gprojuridico.utils.Constants.ASSISTIDOS_COLLECTION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IdServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(IdServiceTest.class);
    private IdService<Object> idService; // Use Object ou o tipo específico que você espera

    @BeforeEach
    void setUp() {
        idService = new IdService<>(); // Instancia o IdService
    }

    @Test
    void testGenerateId() {
        var prefixes = List.of("ATE", "MEDJUR", "ABCDEF");
        var expectedIds = List.of("ATE00001", "MEDJUR00001", "ABCDEF00001");

        for (int i = 0; i < prefixes.size(); i++) {
            var id = prefixes.get(i);
            var expected = expectedIds.get(i);

            var res = IdService.generateId(id);
            logger.info("Prefix: {}, Actual: {}, Expected: {}", id, res, expected);
            assertEquals(expected, res);
        }
    }

    @Test
    void testIncrementId() {
        assertEquals("A002", IdService.incrementId("A001"));
        assertEquals("A1000", IdService.incrementId("A0999"));
        assertEquals("B101", IdService.incrementId("B100"));
        assertEquals("C011", IdService.incrementId("C010"));
        assertEquals("C100", IdService.incrementId("C099"));
        assertEquals("D0001", IdService.incrementId("D0000"));
        assertEquals("E001", IdService.incrementId("E000")); // Prefixo e zero

        // Casos de erro
        assertThrows(IllegalArgumentException.class, () -> IdService.incrementId("")); // ID vazio
        assertThrows(IllegalArgumentException.class, () -> IdService.incrementId("A")); // Sem parte numérica
        assertThrows(IllegalArgumentException.class, () -> IdService.incrementId("123")); // Sem prefixo
    }

    @Test
    void testDefineId() throws ExecutionException, InterruptedException {
        // Aqui você pode criar uma instância de Assistido ou qualquer outra classe apropriada
        Assistido assistido = new AssistidoCivil();
        String prefix = "AST";

        Assistido result = (Assistido) idService.defineId(assistido, ASSISTIDOS_COLLECTION, prefix);
        logger.info("Assigned ID: {}", result.getId());

        assertEquals("ATE00001", result.getId());
    }
}
