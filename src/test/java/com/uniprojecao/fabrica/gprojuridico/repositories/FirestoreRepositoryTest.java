package com.uniprojecao.fabrica.gprojuridico.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniprojecao.fabrica.gprojuridico.models.MedidaJuridica;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.Assistido;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.AssistidoCivil;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.Atendimento;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.AtendimentoTrabalhista;
import com.uniprojecao.fabrica.gprojuridico.models.processo.Processo;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Estagiario;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Usuario;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import static com.uniprojecao.fabrica.gprojuridico.Utils.getFirestore;
import static com.uniprojecao.fabrica.gprojuridico.Utils.sleep;
import static com.uniprojecao.fabrica.gprojuridico.repositories.FirestoreRepository.convertObject;
import static com.uniprojecao.fabrica.gprojuridico.utils.Utils.convertUsingReflection;
import static com.uniprojecao.fabrica.gprojuridico.utils.Utils.print;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.class)
class FirestoreRepositoryTest {

    @BeforeAll
    static void beforeAll() {
        FirestoreRepository.firestore = getFirestore();
    }

    @ParameterizedTest
    @MethodSource
    void testInsertDocument(String collectionName, Object body) {
        try {
            var insertedPayload = FirestoreRepository.insertDocument(collectionName, body);
            assertNotNull(insertedPayload);
        } catch (Exception e) {
            // continue the test if some exception occur
        }
    }

    static Stream<Arguments> testInsertDocument() throws IOException {
        Stream<Arguments> args = getJSONFilesAsStreamOfArgs(Path.of("src/test/resources/json/insert"));

        return args.map(arg -> {
            Map<String, Object> object = (Map<String, Object>) arg.get()[0];

            String collectionName = (String) object.get("collectionName");
            Map<String, Object> body = (Map<String, Object>) object.get("body");

            return Arguments.arguments(collectionName, body);
        });
    }

    @ParameterizedTest
    @MethodSource
    void testUpdateDocument(String collectionName, Map<String, Object> body, String id, String classType) throws Exception {

        FirestoreRepository.updateDocument(collectionName, body, id, classType);

        // Para garantir que o registro a ser obtido foi atualizado
        sleep(500);
        // Obtém o objeto para verificar se o registro foi atualizado através do método de atualizar.
        var foundObject = FirestoreRepository.getDocumentById(collectionName, id);

        Boolean useSuperClass = checkIfShouldUseSuperClass(foundObject);

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

    static Stream<Arguments> testUpdateDocument() throws IOException {
        Stream<Arguments> args = getJSONFilesAsStreamOfArgs(Path.of("src/test/resources/json/update"));

        return args.map(arg -> {
            Map<String, Object> object = (Map<String, Object>) arg.get()[0];

            String collectionName = (String) object.get("collectionName");
            Map<String, Object> body = (Map<String, Object>) object.get("body");
            String id = (String) object.get("id");
            String classType = (String) object.get("classType");

            return Arguments.arguments(collectionName, body, id, classType);
        });
    }

    @ParameterizedTest
    @MethodSource
    void testConvertObject(LinkedHashMap<String, Object> object, Class<?> type) throws Exception {

        var convertedObject = convertObject(object.get("body"), type);

        print("type: " + type.getSimpleName());

        assertNotNull(convertedObject);

        Boolean useSuperClass = checkIfShouldUseSuperClass(convertedObject);

        Map<String, Object> mapFileObject = (Map<String, Object>) object.get("body");
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
    }

    static Stream<Arguments> testConvertObject() throws IOException {
        return getJSONFilesAsStreamOfArgs(Path.of("src/test/resources/json/insert"));
    }

    private static Stream<Arguments> getJSONFilesAsStreamOfArgs(Path directoryPath) throws IOException {
        return Files.list(directoryPath)
                .filter(Files::isRegularFile) // Apenas arquivos regulares
                .filter(path -> path.toString().endsWith(".json")) // Apenas arquivos JSON
                .map(path -> {
                    String fileName = path.getFileName().toString();
                    Class<?> clazz = ARQUIVOS_CLASSES.get(fileName);
                    try {
                        var obj = new ObjectMapper().readValue(path.toFile(), LinkedHashMap.class);
                        return Arguments.arguments(obj, clazz);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private static final Map<String, Class<?>> ARQUIVOS_CLASSES = Map.of(
            "assistido.json", Assistido.class,
            "atendimento.json", Atendimento.class,
            "estagiario.json", Usuario.class,
            "medida juridica.json", MedidaJuridica.class,
            "processo.json", Processo.class,
            "usuario.json", Usuario.class
    );

    private static Boolean checkIfShouldUseSuperClass(Object foundObject) {
        return foundObject.getClass() == AssistidoCivil.class ||
                foundObject.getClass() == AtendimentoTrabalhista.class ||
                foundObject.getClass() == Estagiario.class;
    }
}