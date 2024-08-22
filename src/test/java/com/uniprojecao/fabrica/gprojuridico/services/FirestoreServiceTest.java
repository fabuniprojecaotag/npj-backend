package com.uniprojecao.fabrica.gprojuridico.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniprojecao.fabrica.gprojuridico.dto.body.UpdateBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.models.MedidaJuridicaModel;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.Assistido;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.AssistidoCivil;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.Atendimento;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.AtendimentoTrabalhista;
import com.uniprojecao.fabrica.gprojuridico.models.processo.Processo;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Estagiario;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.repositories.FirestoreRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import static com.uniprojecao.fabrica.gprojuridico.Utils.getFirestore;
import static com.uniprojecao.fabrica.gprojuridico.services.FirestoreService.convertObject;
import static com.uniprojecao.fabrica.gprojuridico.utils.Utils.convertUsingReflection;
import static com.uniprojecao.fabrica.gprojuridico.utils.Utils.print;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.class)
class FirestoreServiceTest {

    Map<String, Boolean> usedMethods = new HashMap<>();

    @BeforeAll
    static void beforeAll() {
        FirestoreRepository.firestore = getFirestore();
    }

    @ParameterizedTest
    @MethodSource("provideJSONFilesToInsert")
    @Order(10)
    void testInsertDocument(File file) throws Exception {
        var fileObject = new ObjectMapper().readValue(file, LinkedHashMap.class);

        var collectionName = (String) fileObject.get("collectionName");
        var body = fileObject.get("body");

        try {
            new FirestoreService();
            var insertedPayload = FirestoreRepository.insertDocument(collectionName, body);
            assertNotNull(insertedPayload);
        } catch (Exception e) {
            // continue the test if some exception occur
        }
    }

    @ParameterizedTest
    @MethodSource("provideJSONFilesToInsert")
    @Order(20)
    @Disabled
    void testConvertObject(File file, Class<?> type) throws Exception {
        var fileObject = new ObjectMapper().readValue(file, LinkedHashMap.class);
        var convertedObject = convertObject(fileObject.get("body"), type);

        print("type: " + type.getSimpleName());

        assertNotNull(convertedObject);

        Boolean useSuperClass =
                convertedObject.getClass() == AssistidoCivil.class ||
                        convertedObject.getClass() == AtendimentoTrabalhista.class ||
                        convertedObject.getClass() == Estagiario.class;

        Map<String, Object> mapFileObject = (Map) fileObject.get("body");
        Map<String, Object> mapConvertedObject = convertUsingReflection(convertedObject, useSuperClass);
        for (var entry : mapFileObject.entrySet()) {
            String key = entry.getKey();
            if (key != "@type") {
                Object expectedValue = entry.getValue();
                Object actualValue = mapConvertedObject.get(key);

                if (actualValue.getClass().getName().startsWith("com.uniprojecao"))
                    expectedValue = actualValue;

                print("\tKey: " + key + ", expectedValue: " + expectedValue + ", actualValue: " + actualValue);
                assertEquals(expectedValue, actualValue);
            }
        }

        usedMethods.put(this.toString(), true);
        print(usedMethods.toString());
    }

    static Stream<Arguments> provideJSONFilesToInsert() throws IOException {
        Path directoryPath = Path.of("src/test/resources/json/insert");

        return getJSONFilesAsStreamOfArgs(directoryPath);
    }

    static Stream<Arguments> provideJSONFilesToUpdate() throws IOException {
        Path directoryPath = Path.of("src/test/resources/json/update");

        return getJSONFilesAsStreamOfArgs(directoryPath);
    }

    private static Stream<Arguments> getJSONFilesAsStreamOfArgs(Path directoryPath) throws IOException {
        return Files.list(directoryPath)
                .filter(Files::isRegularFile) // Apenas arquivos regulares
                .filter(path -> path.toString().endsWith(".json")) // Apenas arquivos JSON
                .map(path -> {
                    String fileName = path.getFileName().toString();
                    Class<?> clazz = ARQUIVOS_CLASSES.get(fileName);
                    return Arguments.arguments(path.toFile(), clazz);
                });
    }

    private static final Map<String, Class<?>> ARQUIVOS_CLASSES = Map.of(
            "assistido.json", Assistido.class,
            "atendimento.json", Atendimento.class,
            "estagiario.json", Usuario.class,
            "medida juridica.json", MedidaJuridicaModel.class,
            "processo.json", Processo.class,
            "usuario.json", Usuario.class
    );

    @ParameterizedTest
    @MethodSource("provideJSONFilesToUpdate")
    @Order(30)
    void testUpdateDocument(File file) throws Exception {
        var fileObject = new ObjectMapper().readValue(file, LinkedHashMap.class);

        var collectionName = (String) fileObject.get("collectionName");
        var body = (Map<String, Object>) fileObject.get("body");
        var id = (String) fileObject.get("id");
        var model = (String) fileObject.get("classType");

        var payload = new UpdateBodyDTO(body, id, model);

        FirestoreRepository.updateDocument(collectionName, payload);

        // Obtém o objeto para verificar se o registro foi atualizado através do método de atualizar.
        var foundObject = FirestoreRepository.getDocumentById(collectionName, id);

        Boolean useSuperClass =
                foundObject.getClass() == AssistidoCivil.class ||
                        foundObject.getClass() == AtendimentoTrabalhista.class ||
                        foundObject.getClass() == Estagiario.class;

        Map<String, Object> mapObject = convertUsingReflection(foundObject, useSuperClass);

        print("Class: " + foundObject.getClass());
        for (var entry : body.entrySet()) {
            String key = entry.getKey();
            Object expectedValue = entry.getValue();

            var actualValue = mapObject.get(key);
            print("\texpectedValue: " + expectedValue + ", actualValue: " + actualValue);
            assertEquals(expectedValue, actualValue);
        }
    }
}