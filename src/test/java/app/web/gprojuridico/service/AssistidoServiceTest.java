package app.web.gprojuridico.service;

import app.web.gprojuridico.model.enums.Escolaridade;
import app.web.gprojuridico.model.enums.EstadoCivil;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssistidoServiceTest {

    @Mock
    private AssistidoService underTest;

    @BeforeEach
    void setUp() throws Exception {
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
        String projectId = "gprojuridico";
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .setProjectId(projectId)
                .build();
        FirebaseApp.initializeApp(options);
    }


    // ---------- Insert methods tests ----------
    @Test
    @DisplayName("Should insert a data that will be equivilant a AssistidoCivil object")
    void shoudlInsertAssistidoCivil() {
        // given
        underTest.collection = FirestoreClient.getFirestore().collection("assistidos");
        Map<String, Object> data = getInstanceDataOfAssistidoCivil();

        // when
        when(underTest.insert(data)).thenCallRealMethod();
        underTest.insert(data);

        // then
        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(underTest).insert(captor.capture());
        Object capturedData = captor.getValue();
        assertThat(capturedData).isEqualTo(data);
    }

    @Test
    @Disabled
    void shoudInsertAssistidoTrabalhista() {}

    @Test
    @Disabled
    void shouldInsertAssistidoFull() {}

    @Test
    @Disabled
    void shouldThrowsExceptionWhenInsertedDataIsNotValid() {}

    // ---------- Find methods tests ----------
    @Test
    @DisplayName("Should find and return all Assistido objects available in Firestore")
    @Disabled("The 'then' clause must perform a relevant operation")
    void shouldFindAllAssistidos() {
        // given
        underTest.collection = FirestoreClient.getFirestore().collection("assistidos");
        // when
        when(underTest.findAll()).thenCallRealMethod();
        List<Object> list = underTest.findAll();
        // then
        assertTrue(list.isEmpty());
    }

    // ---------- Auxiliar methods ----------
    private static Map<String, Object> getInstanceDataOfAssistidoCivil() {
        Map<String, String> filiacao = new HashMap<>();
        filiacao.put("nomeMae", "Cecília Almeida Ribeiro");
        filiacao.put("nomePai", "José Alves Souza");

        Map<String, String> endereco = new HashMap<>();
        endereco.put("logradouro", "QNB 14 Conjunto D");
        endereco.put("numero", "12");

        Map<String, Object> data = new HashMap<>();
        data.put("nome", "Helena Rodrigues de Souza Ribeiro");
        data.put("rg", "11.782.956-0");
        data.put("cpf", "897.688.780-88");
        data.put("nacionalidade", "Brasileira");
        data.put("escolaridade", Escolaridade.SUPERIOR.getRotulo());
        data.put("estadoCivil", EstadoCivil.CASADO.getRotulo());
        data.put("profissao", "Gerente bancária");
        data.put("telefone", "(61) 99320-1050");
        data.put("email", "helena.rodrigues@example.com");
        data.put("filicao", filiacao);
        data.put("remuneracao", "R$ 8.200");
        data.put("endereco", endereco);
        data.put("naturalidade", "Brasiliense");
        data.put("dataNascimento", "1994-04-20");
        data.put("numDependentes", 2);

        return data;
    }

}