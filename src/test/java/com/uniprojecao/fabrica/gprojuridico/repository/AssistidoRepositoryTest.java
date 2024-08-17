//package com.uniprojecao.fabrica.gprojuridico.repository;
//
//import com.google.cloud.firestore.Filter;
//import com.uniprojecao.fabrica.gprojuridico.interfaces.CsvToAssistido;
//import com.uniprojecao.fabrica.gprojuridico.models.assistido.Assistido;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.CsvFileSource;
//
//import static com.uniprojecao.fabrica.gprojuridico.Utils.seedDatabase;
//import static com.uniprojecao.fabrica.gprojuridico.services.QueryFilterService.getFilter;
//import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.ASSISTIDOS_COLLECTION;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//class AssistidoRepositoryTest {
//
//    AssistidoRepository repository = new AssistidoRepository();
//
//    @BeforeAll
//    static void beforeAll() {
//        seedDatabase(0, ASSISTIDOS_COLLECTION);
//    }
//
//    @Test
//    void findAll() {
//        Filter queryFilter = getFilter("escolaridade", "Superior", "EQUAL");
//
//        var list1 = repository.findAll(20, null);
//        assertNotNull(list1);
//
//        var list2 = repository.findAll(20, queryFilter);
//        assertNotNull(list2);
//    }
//
//    @ParameterizedTest
//    @CsvFileSource(resources = "/assistidos.csv")
//    void findById(@CsvToAssistido Assistido assistido) {
//        String id = assistido.getCpf();
//
//        Assistido result = repository.findById(id);
//        assertNotNull(result);
//    }
//}