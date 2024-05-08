package com.uniprojecao.fabrica.gprojuridico;

import com.google.cloud.NoCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.repository.BaseRepository;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

import static com.uniprojecao.fabrica.gprojuridico.data.AssistidoData.seedWithAssistido;
import static com.uniprojecao.fabrica.gprojuridico.data.AtendimentoData.seedWithAtendimento;
import static com.uniprojecao.fabrica.gprojuridico.data.ProcessoData.seedWithProcesso;
import static com.uniprojecao.fabrica.gprojuridico.data.UsuarioData.seedWithUsuario;

public class Utils {
    @Nonnull
    public static Firestore getFirestore() {
        return FirestoreOptions
                .getDefaultInstance()
                .toBuilder()
                .setHost("localhost:8090")
                .setCredentials(NoCredentials.getInstance())
                .setProjectId("gprojuridico-dev")
                .build()
                .getService();
    }

    public enum Clazz {
        USUARIO, ASSISTIDO, PROCESSO, ATENDIMENTO
    }

    public static boolean seedDatabase(int count, Clazz clazz) {
        if (count == 0) {
            BaseRepository.firestore = getFirestore();
            switch (clazz) {
                case USUARIO:
                    var usuarios = seedWithUsuario();
                    for (var usuario : usuarios) {
                        BaseRepository.save("usuarios", usuario.getId(), usuario);
                    }
                    break;
                case ASSISTIDO:
                    var assistidos = seedWithAssistido();
                    for (var assistido : assistidos) {
                        BaseRepository.save("assistidos", assistido.getCpf(), assistido);
                    }
                    break;
                case PROCESSO:
                    var processos = seedWithProcesso();
                    for (var processo : processos) {
                        BaseRepository.save("processos", processo.getNumero(), processo);
                    }
                    break;
                case ATENDIMENTO:
                    var atendimentos = seedWithAtendimento();
                    for (var atendimento : atendimentos) {
                        BaseRepository.save("atendimentos", atendimento.getId(), atendimento);
                    }
                    break;
            }
            return false;
        }
        return true;
    }

    public static boolean clearDatabase(@Nullable QueryFilter queryFilter, String collectionName) {
        var acceptedCollectionNames = List.of("usuarios", "assistidos", "processos", "atendimentos");
        var result = acceptedCollectionNames.stream().filter(name -> collectionName == name).findFirst();

        if (result.isEmpty()) throw new RuntimeException("O nome de coleção passado não é válido");

        BaseRepository.firestore = getFirestore();
        BaseRepository.deleteAll(collectionName, null, 20, queryFilter);
        var database = BaseRepository.findAll(collectionName, null, null, 20, queryFilter);

        return database.isEmpty();
    }
}
