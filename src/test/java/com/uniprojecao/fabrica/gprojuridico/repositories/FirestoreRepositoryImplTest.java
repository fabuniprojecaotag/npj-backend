package com.uniprojecao.fabrica.gprojuridico.repositories;

import com.uniprojecao.fabrica.gprojuridico.models.Endereco;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.AssistidoCivil;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.Filiacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class FirestoreRepositoryImplTest {

    @Autowired
    FirestoreRepositoryImpl firestoreRepository;

    @BeforeEach
    void setUp() throws ExecutionException, InterruptedException {
        firestoreRepository = new FirestoreRepositoryImpl("assistidos");

        firestoreRepository.delete(List.of("123.456.789-01", "372.897.451-08"));

        AssistidoCivil assistido = new AssistidoCivil("Assistido teste", "4324", "534535435",
                "Brasileiro", "", "", "", "", "assistido.teste@gmail.com", new Filiacao(), "",
                new Endereco("", "", "", "", "", ""), "Brasileira",
                LocalDateTime.now().minusYears(20).toString(), "0");

        var resultado = firestoreRepository.insert("123.456.789-01", assistido);
        assertNotNull(resultado);
        await().atLeast(1, TimeUnit.SECONDS);
    }


    @Test
    void testList() throws InvalidPropertiesFormatException, ExecutionException, InterruptedException {
        var resultado = firestoreRepository.findAll(null, 10, null, "min");
        assertNotNull(resultado);

        assertEquals(1, resultado.get("totalSize"));
    }

    @Test
    void testCreate() throws ExecutionException, InterruptedException {
        var resultado = firestoreRepository.insert("372.897.451-08", new AssistidoCivil("", "", "", "", "", "", "", "", "", new Filiacao(), "", new Endereco(), "", LocalDateTime.now().minusYears(30).toString(), "0"));
        assertNotNull(resultado);

        assertNotNull(resultado);
    }

    @Test
    void testFindById() throws InvalidPropertiesFormatException, ExecutionException, InterruptedException {
        var resultado = firestoreRepository.findById("123.456.789-01");

        assertNotNull(resultado);
    }

//    @Test
//    void testUpdate() throws ExecutionException, InterruptedException, InvalidPropertiesFormatException {
//        var updateAssistido = new AssistidoCivil();
//        updateAssistido.setNome("Albert Einstein");
//
//        Map<String, Object> updateData = new HashMap<>();
//        updateData.put("nome", updateAssistido.getNome());
//
//        firestoreRepository.update("123.456.789-01", updateData, AssistidoCivil.class);
//
//        var assistidoResult = firestoreRepository.findById("123.456.789-01");
//
//        assertEquals(updateAssistido.getNome(), ((AssistidoCivil) assistidoResult).getNome());
//    }



    @Test
    void testDelete() throws InvalidPropertiesFormatException, ExecutionException, InterruptedException {
        List<String> idsList = new ArrayList<>();
        idsList.add("372.897.451-08");

        firestoreRepository.delete(idsList);
        var firestoreList = firestoreRepository.findAll(null, 10, null, "min");

        assertEquals(1, firestoreList.get("totalSize"));
    }
}
