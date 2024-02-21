package app.web.gprojuridico.service;

import app.web.gprojuridico.data.FakeData;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class Auxiliar {

    static Boolean initializedApp = false;
}


@ExtendWith(MockitoExtension.class)
class AssistidoServiceTest {

    @Mock
    private AssistidoService underTest;

    @BeforeEach
    void setUp() throws Exception {
        if (!Auxiliar.initializedApp) {
            GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
            String projectId = "gprojuridico";
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .setProjectId(projectId)
                    .build();
            FirebaseApp.initializeApp(options);

            Auxiliar.initializedApp = true;
        }
            underTest.collection = FirestoreClient.getFirestore().collection("assistidos");
    }


    // ---------- Test Suite for Insertion Methods ----------
    @Test
    @DisplayName("Should insert a data that is equivilant to an AssistidoCivil object")
    void shouldlInsertAssistidoCivil() {
        insertAssistido(FakeData.assistidoCivil());
    }

    @Test
    @DisplayName("Should insert a data that is equivilant to an AssistidoFull object")
    void shouldInsertAssistidoFull() {
        insertAssistido(FakeData.assistidoFull());
    }

    @Test
    @DisplayName("Should insert a data that is equivilant to an AssistidoTrabalhista object")
    void shouldInsertAssistidoTrabalhista() {
        insertAssistido(FakeData.assistidoTrabalhista());
    }

    @Test
    @DisplayName("Should throw exception when inserted data is not valid")
    void shouldThrowExceptionWhenInsertedDataIsNotValid() {
        // given
        Object data = FakeData.incorrectAssistido();

        // when
        when(underTest.insert(data)).thenCallRealMethod();

        // then
        assertThatThrownBy(() -> underTest.insert(data))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("O objeto passado não corresponde à nenhuma instância de Assistido.");
    }

    // ---------- Test Suite for Find Methods ----------
    @Test
    @DisplayName("Must verify if inserted Assistido object is contained in findAll()")
    void verifyFindAllAssistidos() {
        // given
        Object data = FakeData.assistidoCivil();

        // when
        when(underTest.insert(any(Object.class))).thenCallRealMethod();
        when(underTest.findById(any(String.class))).thenCallRealMethod();
        when(underTest.findAll()).thenCallRealMethod();

        // then
        Object object = underTest.insert(data);
        List<Object> result = underTest.findAll();
        verify(underTest).findAll();
        assertTrue(result.contains(object));
    }

    @Test
    @DisplayName("Must verify if the inserted Assistido object was returned by findById()")
    void verifyFindAssistidoById() {
        // given
        Object data = FakeData.assistidoCivil();

        // when
        when(underTest.insert(any(Object.class))).thenCallRealMethod();
        when(underTest.findById(any(String.class))).thenCallRealMethod();

        // then
        Object object = underTest.insert(data);
        verify(underTest).findById(any(String.class));
        assertEquals(object, data);
    }

    private void insertAssistido(Object data) {
        // given

        // when
        when(underTest.insert(data)).thenCallRealMethod();
        underTest.insert(data);

        // then
        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(underTest).insert(captor.capture());
        Object capturedData = captor.getValue();
        assertThat(capturedData).isEqualTo(data);
    }
}