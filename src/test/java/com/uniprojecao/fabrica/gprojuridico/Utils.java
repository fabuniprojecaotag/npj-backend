package com.uniprojecao.fabrica.gprojuridico;

import com.google.cloud.NoCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.repository.*;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.mockito.MockedStatic;

import static com.uniprojecao.fabrica.gprojuridico.data.AssistidoData.seedWithAssistido;
import static com.uniprojecao.fabrica.gprojuridico.data.AtendimentoData.seedWithAtendimento;
import static com.uniprojecao.fabrica.gprojuridico.data.ProcessoData.seedWithProcesso;
import static com.uniprojecao.fabrica.gprojuridico.data.UsuarioData.seedWithUsuario;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class Utils {
    @Nonnull
    public static Firestore getFirestoreOptions() {
        return FirestoreOptions
                .getDefaultInstance()
                .toBuilder()
                .setHost("localhost:8090")
                .setCredentials(NoCredentials.getInstance())
                .setProjectId("gprojuridico-dev")
                .build()
                .getService();
    }

    // save() method is tested with seedDatabase() method
    public static boolean seedDatabase(boolean databaseEmpty, String clazz) {
        if (databaseEmpty) {
            switch (clazz) {
                case "Usuario":
                    var uRepository = (mock(UsuarioRepository.class));
                    var usuarios = seedWithUsuario();

                    doCallRealMethod().when(uRepository).save(anyString(), anyString(), any());

                    for (var usuario : usuarios) {
                        uRepository.save("usuarios", usuario.getEmail(), usuario);
                    }
                case "Assistido":
                    var aRepository = (mock(AssistidoRepository.class));
                    var assistidos = seedWithAssistido();

                    doCallRealMethod().when(aRepository).save(anyString(), anyString(), any());

                    for (var assistido : assistidos) {
                        aRepository.save("assistidos", assistido.getCpf(), assistido);
                    }
                case "Processo":
                    var pRepository = (mock(ProcessoRepository.class));
                    var processos = seedWithProcesso();

                    doCallRealMethod().when(pRepository).save(anyString(), anyString(), any());

                    for (var processo : processos) {
                        pRepository.save("processos", processo.getNumero(), processo);
                    }
                case "Atendimento":
                    var atRepository = (mock(AtendimentoRepository.class));
                    var atendimentos = seedWithAtendimento();

                    doCallRealMethod().when(atRepository).save(anyString(), anyString(), any());

                    for (var atendimento : atendimentos) {
                        atRepository.save("atendimentos", atendimento.getId(), atendimento);
                    }
            }
            return false;
        }
        return true;
    }

    // deleteAll() method is tested with clearDatabase() method
    public static boolean clearDatabase(@Nullable QueryFilter queryFilter, String clazz) {
        int limit = 20;
        String collectionName;

        switch (clazz) {
            case "Usuario":
                var uRepository = (mock(UsuarioRepository.class));
                collectionName = "usuarios";

                try (MockedStatic<BaseRepository> baseRepository = mockStatic(BaseRepository.class)) {
                    baseRepository.when(() -> uRepository.deleteAll(collectionName, null, limit, queryFilter)).thenCallRealMethod();
                }

                doCallRealMethod().when(uRepository).deleteAll(collectionName, null, limit, queryFilter);
                when(uRepository.findAll(limit, queryFilter)).thenCallRealMethod();

                uRepository.deleteAll(collectionName, null, limit, queryFilter);
                var usuarios = uRepository.findAll(limit, queryFilter);

                return usuarios.isEmpty();
            case "Assistido":
                var aRepository = (mock(AssistidoRepository.class));
                collectionName = "assistidos";

                try (MockedStatic<BaseRepository> baseRepository = mockStatic(BaseRepository.class)) {
                    baseRepository.when(() -> aRepository.deleteAll(collectionName, null, limit, queryFilter)).thenCallRealMethod();
                }

                doCallRealMethod().when(aRepository).deleteAll(collectionName, null, limit, queryFilter);
                when(aRepository.findAll(limit, queryFilter)).thenCallRealMethod();

                aRepository.deleteAll(collectionName, null, limit, queryFilter);
                var assistidos = aRepository.findAll(limit, queryFilter);

                return assistidos.isEmpty();
            case "Processo":
                var pRepository = (mock(ProcessoRepository.class));
                collectionName = "processos";

                try (MockedStatic<BaseRepository> baseRepository = mockStatic(BaseRepository.class)) {
                    baseRepository.when(() -> pRepository.deleteAll(collectionName, null, limit, queryFilter)).thenCallRealMethod();
                }

                doCallRealMethod().when(pRepository).deleteAll(collectionName, null, limit, queryFilter);
                when(pRepository.findAll(limit, queryFilter)).thenCallRealMethod();

                pRepository.deleteAll(collectionName, null, limit, queryFilter);
                var processos = pRepository.findAll(limit, queryFilter);

                return processos.isEmpty();
            case "Atendimento":
                var atRepository = (mock(AtendimentoRepository.class));
                collectionName = "atendimentos";

                try (MockedStatic<BaseRepository> baseRepository = mockStatic(BaseRepository.class)) {
                    baseRepository.when(() -> atRepository.deleteAll(collectionName, null, limit, queryFilter)).thenCallRealMethod();
                }

                doCallRealMethod().when(atRepository).deleteAll(collectionName, null, limit, queryFilter);
                when(atRepository.findAll(limit, queryFilter)).thenCallRealMethod();

                atRepository.deleteAll(collectionName, null, limit, queryFilter);
                var atendimentos = atRepository.findAll(limit, queryFilter);

                return atendimentos.isEmpty();
        }
        return false;
    }
}
