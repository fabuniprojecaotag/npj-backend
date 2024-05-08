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
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.*;

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

    public static boolean seedDatabase(int count, String collectionName) {
        var acceptedCollectionNames = List.of(
                USUARIOS_COLLECTION,
                ASSISTIDOS_COLLECTION,
                PROCESSOS_COLLECTION,
                ATENDIMENTOS_COLLECTION
        );
        var result = acceptedCollectionNames.stream().filter(name -> collectionName == name).findFirst();

        if (result.isEmpty()) throw new RuntimeException("O nome de coleção passado não é válido");

        if (count == 0) { // TODO: analisar se deve ser feito rollback disso (voltar para ser boolean)
            BaseRepository.firestore = getFirestore();
            switch (collectionName) {
                case ASSISTIDOS_COLLECTION:
                    var assistidos = seedWithAssistido();
                    for (var assistido : assistidos) {
                        BaseRepository.save(ASSISTIDOS_COLLECTION, assistido.getCpf(), assistido);
                    }
                    break;
                case ATENDIMENTOS_COLLECTION:
                    var atendimentos = seedWithAtendimento();
                    for (var atendimento : atendimentos) {
                        BaseRepository.save(ATENDIMENTOS_COLLECTION, atendimento.getId(), atendimento);
                    }
                    break;
                case PROCESSOS_COLLECTION:
                    var processos = seedWithProcesso();
                    for (var processo : processos) {
                        BaseRepository.save(PROCESSOS_COLLECTION, processo.getNumero(), processo);
                    }
                    break;
                case USUARIOS_COLLECTION:
                    var usuarios = seedWithUsuario();
                    for (var usuario : usuarios) {
                        BaseRepository.save(USUARIOS_COLLECTION, usuario.getId(), usuario);
                    }
                    break;
            }
            return false;
        }
        return true;
    }

    public static boolean clearDatabase(@Nullable QueryFilter queryFilter, String collectionName) {
        var acceptedCollectionNames = List.of(
                USUARIOS_COLLECTION,
                ASSISTIDOS_COLLECTION,
                PROCESSOS_COLLECTION,
                ATENDIMENTOS_COLLECTION
        );
        var result = acceptedCollectionNames.stream().filter(name -> collectionName == name).findFirst();

        if (result.isEmpty()) throw new RuntimeException("O nome de coleção passado não é válido");

        BaseRepository.firestore = getFirestore();
        BaseRepository.deleteAll(collectionName, null, 20, queryFilter);
        var database = BaseRepository.findAll(collectionName, null, null, 20, queryFilter);

        return database.isEmpty();
    }
}
