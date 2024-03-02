package app.web.gprojuridico.services;

import app.web.gprojuridico.data.FakeData;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

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
    }

    // ---------- Test Suite for Insertion Methods ----------
    @Nested
    @DisplayName("Tests nested for insert Assistido")
    @Disabled
    class InsertAssistido {

        @Test
        @DisplayName("Insert data that is equivilant to an AssistidoCivil object")
        void shouldlInsertAssistidoCivil() throws Exception {
            insertAssistido(FakeData.assistidoCivil());
        }

        @Test
        @DisplayName("Insert data that is equivilant to an AssistidoTrabalhista object")
        void shouldInsertAssistidoTrabalhista() throws Exception {
            insertAssistido(FakeData.assistidoTrabalhista());
        }

        @Test
        @DisplayName("Throw exception when inserted data is not valid")
        void shouldThrowExceptionWhenInsertedDataIsNotValid() {
            // given
            Map<String, Object> data = FakeData.incorrectAssistido();

            // when
            when(underTest.insert(data)).thenCallRealMethod();

            // then
            assertThatThrownBy(() -> underTest.insert(data))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("O objeto passado não corresponde à nenhuma instância de Assistido.");
        }
    }

    // ---------- Test Suite for Find Methods ----------
    @Nested
    @DisplayName("Tests nested for find Assistido")
    @Disabled
    class FindAssistido {
        @Test
        @DisplayName("Verify if the inserted Assistido object is contained in findAll()")
        void verifyFindAllAssistidos() {
            // given

            // when
            when(underTest.findAll(null)).thenCallRealMethod();

            // then
            verify(underTest).findAll(null);
        }

        @Test
        @DisplayName("Verify if the inserted Assistido object was returned by findById()")
        void verifyFindAssistidoById() throws Exception {
            // given
            Map<String, Object> data = FakeData.assistidoCivil();

            // when
            when(underTest.insert(any())).thenCallRealMethod();

            // then
            Object object = underTest.insert(data);
            verify(underTest).findById(any(String.class));
            assertEquals(object, data);
        }
    }

    // ---------- Test Suite for Update Method ----------
    @Nested
    @DisplayName("Tests nested for update Assistido")
    @Disabled
    class UpdateAssistido {

        @Test
        @DisplayName("Update AssistidoCivil object with some data")
        void updateAssistidoCivil() throws Exception {
            updateAssistido(FakeData.assistidoCivil(), FakeData.fieldsToUpdateAssistido());
        }

        @Test
        @DisplayName("Update AssistidoFull object with some data")
        void updateAssistidoFull() throws Exception {
            updateAssistido(FakeData.assistidoFull(), FakeData.fieldsToUpdateAssistido());
        }

        @Test
        @DisplayName("Update AssistidoTrabalhista object with some data")
        void updateAssistidoTrabalhista() throws Exception {
            updateAssistido(FakeData.assistidoTrabalhista(), FakeData.fieldsToUpdateAssistido());
        }

        @Test
        @DisplayName("Update Assistido object to allow this have a linked labor service")
        void updateAssistidoCivilToFull() throws Exception {
            updateAssistido(FakeData.assistidoCivil(), FakeData.fieldsToUpdateAssistidoCivilToFull());
        }

        @Test
        @DisplayName("Update Assistido object to allow this have a linked civil service")
        void updateAssistidoTrabalhistaToFull() throws Exception {
            updateAssistido(FakeData.assistidoTrabalhista(), FakeData.fieldsToUpdateAssistidoTrabalhistaToFull());
        }
    }

    // ---------- Test for Delete Method ----------
    @Test
    @DisplayName("Delete the inserted Assistido object by id")
    @Disabled
    void deleteAssistido () throws Exception {
        // given
        Map<String, Object> assistido = FakeData.assistidoCivil();

        // when
        when(underTest.insert(any())).thenCallRealMethod();
        when(underTest.delete(any())).thenCallRealMethod();

        // then
        Map<String, Object> document = underTest.insert(assistido);

        System.out.println("\n------ Sleep method was called ------");
        Thread.sleep(2000);
        Boolean deletedDocument = underTest.delete((String) document.get("id"));
        assertTrue(deletedDocument);
    }

    private void insertAssistido (Map < String, Object > data){
        // given

        // when
        when(underTest.insert(data)).thenCallRealMethod();
        underTest.insert(data);

        // then
        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(underTest).insert((Map<String, Object>) captor.capture());
        Object capturedData = captor.getValue();
        assertThat(capturedData).isEqualTo(data);
    }

    private void updateAssistido (Map < String, Object > assistido, Map < String, Object > data){
        // when
        when(underTest.insert(any())).thenCallRealMethod();
        when(underTest.update(any(String.class), any())).thenCallRealMethod();

        // then
        Map<String, Object> document = underTest.insert(assistido);

        Boolean updatedData = underTest.update((String) document.get("id"), data);
        assertTrue(updatedData);
    }

}