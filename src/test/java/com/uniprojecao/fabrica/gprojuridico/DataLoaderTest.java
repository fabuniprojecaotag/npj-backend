package com.uniprojecao.fabrica.gprojuridico;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.Assistido;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.repositories.FirestoreRepositoryImpl;
import com.uniprojecao.fabrica.gprojuridico.services.AssistidoService;
import com.uniprojecao.fabrica.gprojuridico.services.UsuarioService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;

import static com.uniprojecao.fabrica.gprojuridico.Utils.getProdFirestore;
import static com.uniprojecao.fabrica.gprojuridico.utils.Utils.convertGenericObjectToClassInstance;
import static com.uniprojecao.fabrica.gprojuridico.utils.Utils.print;

class DataLoaderTest {

    static FirestoreRepositoryImpl firestoreRepository;

    AssistidoService assistidoService = new AssistidoService();
    UsuarioService usuarioService = new UsuarioService();

    @BeforeAll
    static void beforeAll() {
        firestoreRepository.firestore = getProdFirestore();
    }

    @Test
    void loadDemoAssistidoData() throws Exception {
        Path path = Path.of("src/test/resources/json/insert-multiple/assistidos-civis.json");
        LinkedHashMap obj = new ObjectMapper().readValue(path.toFile(), LinkedHashMap.class);

        List<Object> list = (List<Object>) obj.get("list");

        for (var o : list) {
            var assistido = convertGenericObjectToClassInstance(o, Assistido.class);
            print("Assistido civil: " + assistido.getNome());
            assistidoService.insert(assistido);
        }

        path = Path.of("src/test/resources/json/insert-multiple/assistidos-trabalhistas.json");
        obj = new ObjectMapper().readValue(path.toFile(), LinkedHashMap.class);
        list = (List<Object>) obj.get("list");

        for (var o : list) {
            var assistido = convertGenericObjectToClassInstance(o, Assistido.class);
            print("Assistido trabalhista: " + assistido.getNome());
            assistidoService.insert(assistido);
        }
    }

    @Test
    void loadDemoUsuarioData() throws Exception {
        Path path = Path.of("src/test/resources/json/insert-multiple/usuarios.json");
        LinkedHashMap obj = new ObjectMapper().readValue(path.toFile(), LinkedHashMap.class);

        List<Object> list = (List<Object>) obj.get("list");

        for (var o : list) {
            var usuario = convertGenericObjectToClassInstance(o, Usuario.class);
            print("Usuário: " + usuario.getNome());
            usuarioService.insert(usuario);
        }

        path = Path.of("src/test/resources/json/insert-multiple/estagiarios.json");
        obj = new ObjectMapper().readValue(path.toFile(), LinkedHashMap.class);
        list = (List<Object>) obj.get("list");

        for (var o : list) {
            var usuario = convertGenericObjectToClassInstance(o, Usuario.class);
            print("Estagiário: " + usuario.getNome());
            usuarioService.insert(usuario);
        }
    }

}
