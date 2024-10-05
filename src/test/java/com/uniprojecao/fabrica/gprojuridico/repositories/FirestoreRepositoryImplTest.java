package com.uniprojecao.fabrica.gprojuridico.repositories;

import com.uniprojecao.fabrica.gprojuridico.dto.body.UpdateBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.models.Endereco;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.Assistido;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.AssistidoCivil;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.Filiacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.uniprojecao.fabrica.gprojuridico.utils.Constants.ASSISTIDOS_COLLECTION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class FirestoreRepositoryImplTest {

    @Autowired
    FirestoreRepositoryImpl<Assistido> firestoreRepository;

    @BeforeEach
    void setUp() throws ExecutionException, InterruptedException {
        firestoreRepository = new FirestoreRepositoryImpl<>(ASSISTIDOS_COLLECTION);

        firestoreRepository.delete(List.of("123", "AS00002"));

        AssistidoCivil assistido = new AssistidoCivil("Assistido teste", "4324", "534535435",
                "Brasileiro", "", "", "", "", "assistido.teste@gmail.com", new Filiacao(), "",
                new Endereco("", "", "", "", "", ""), "Brasileira",
                LocalDateTime.now().minusYears(20).toString(), "0");

        var resultado = firestoreRepository.insert("123", assistido);
        assertNotNull(resultado);
    }


    @Test
    void testList() throws InvalidPropertiesFormatException, ExecutionException, InterruptedException {
        var resultado = firestoreRepository.findAll(null, 10, null, "min");

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalSize());
    }

    @Test
    void testCreate() throws ExecutionException, InterruptedException, InvalidPropertiesFormatException {
        var resultado = firestoreRepository.insert("AS00002", new AssistidoCivil("Teste 2", "", "", "", "", "", "", "", "", new Filiacao(), "", new Endereco(), "", LocalDateTime.now().minusYears(30).toString(), "0"));
        var resultadoSalvo = firestoreRepository.findById("AS00002");

        assertNotNull(resultado);
        assertEquals(resultado.getNome(), resultadoSalvo.getNome());
    }

    @Test
    void testFindById() throws InvalidPropertiesFormatException, ExecutionException, InterruptedException {
        var resultado = firestoreRepository.findById("123");

        assertNotNull(resultado);
    }

    @Test
    void testUpdate() throws ExecutionException, InterruptedException, InvalidPropertiesFormatException {
        var updateAssistido = new AssistidoCivil();
        updateAssistido.setNome("Albert Einstein");
        updateAssistido.setDependentes("2");
        updateAssistido.setCpf("123.456.789-01");

        UpdateBodyDTO<AssistidoCivil> updateData = new UpdateBodyDTO<>(updateAssistido, "Civil");

        firestoreRepository.update("AS00001", (Map<String, Object>) updateData.getBody());

        var assistidoResult = firestoreRepository.findById("AS00001");

        assertEquals(updateAssistido.getNome(), assistidoResult.getNome());
    }

    @Test
    void testDelete() throws InvalidPropertiesFormatException, ExecutionException, InterruptedException {
        List<String> idsList = new ArrayList<>();
        idsList.add("AS00001");

        firestoreRepository.delete(idsList);
        var firestoreList = firestoreRepository.findAll(null, 10, null, "min");

        assertEquals(1, firestoreList.getTotalSize());
    }
}
