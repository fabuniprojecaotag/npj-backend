package com.uniprojecao.fabrica.gprojuridico.repository;

import com.google.cloud.firestore.Filter;
import com.uniprojecao.fabrica.gprojuridico.interfaces.CsvToUsuario;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Usuario;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static com.uniprojecao.fabrica.gprojuridico.Utils.seedDatabase;
import static com.uniprojecao.fabrica.gprojuridico.services.QueryFilterService.getFilter;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.USUARIOS_COLLECTION;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UsuarioRepositoryTest {

    UsuarioRepository repository = new UsuarioRepository();

    @BeforeAll
    static void beforeAll() {
        seedDatabase(0, USUARIOS_COLLECTION);
    }

    @Test
    void findAll() {
        Filter queryFilter = getFilter("role", "PROFESSOR", "EQUAL");

        var list1 = repository.findAll(20, null);
        assertNotNull(list1);

        var list2 = repository.findAll(20, queryFilter);
        assertNotNull(list2);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/usuarios.csv")
    void findById(@CsvToUsuario Usuario usuario) {
        String id = usuario.getId();

        Usuario result = repository.findById(id);
        assertNotNull(result);
    }
}