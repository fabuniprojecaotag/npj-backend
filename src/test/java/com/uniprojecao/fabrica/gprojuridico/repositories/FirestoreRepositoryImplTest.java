package com.uniprojecao.fabrica.gprojuridico.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.InvalidPropertiesFormatException;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test") // Para garantir que o arquivo application-test.properties seja usado
class FirestoreRepositoryImplTest {

    @Autowired
    FirestoreRepositoryImpl firestoreRepository;  // Usando injeção do Spring para obter a instância correta

    @BeforeEach
    void setUp(){
        firestoreRepository = new FirestoreRepositoryImpl("assistidos");
    }

    @Test
    void testFirestoreList() throws InvalidPropertiesFormatException, ExecutionException, InterruptedException {
        // Pode passar null como o filtro, se não precisar de um filtro específico
        var resultado = firestoreRepository.findAll(null, 10, null, "min");
        assertNotNull(resultado);

        // Usando um foreach para percorrer os itens
        resultado.forEach((data, item) -> System.out.println(item));

        assertEquals(1, resultado.get("totalSize"));
    }
}
