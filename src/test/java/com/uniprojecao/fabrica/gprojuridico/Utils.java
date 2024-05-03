package com.uniprojecao.fabrica.gprojuridico;

import com.google.cloud.NoCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.repository.*;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

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

    // save() method is tested with seedDatabase() method
    public boolean seedDatabase(int count, Clazz clazz) {
        if (count == 0) {
            BaseRepository baseRepository = new BaseRepository();
            baseRepository.firestore = getFirestore();
            switch (clazz) {
                case USUARIO:
                    var usuarios = seedWithUsuario();
                    for (var usuario : usuarios) {
                        baseRepository.save("usuarios", usuario.getEmail(), usuario);
                    }
                    break;
                case ASSISTIDO:
                    var assistidos = seedWithAssistido();
                    for (var assistido : assistidos) {
                        baseRepository.save("assistidos", assistido.getCpf(), assistido);
                    }
                    break;
                case PROCESSO:
                    var processos = seedWithProcesso();
                    for (var processo : processos) {
                        baseRepository.save("processos", processo.getNumero(), processo);
                    }
                    break;
                case ATENDIMENTO:
                    var atendimentos = seedWithAtendimento();
                    for (var atendimento : atendimentos) {
                        baseRepository.save("atendimentos", atendimento.getId(), atendimento);
                    }
                    break;
            }
            return false;
        }
        return true;
    }

    // deleteAll() method is tested with clearDatabase() method
    public boolean clearDatabase(@Nullable QueryFilter queryFilter, Clazz clazz) {
        int limit = 20;
        String collectionName;

        switch (clazz) {
            case USUARIO:
                var uRepository = new UsuarioRepository();
                uRepository.firestore = getFirestore();
                collectionName = "usuarios";

                uRepository.deleteAll(collectionName, null, limit, queryFilter);
                var usuarios = uRepository.findAll(limit, queryFilter);

                return usuarios.isEmpty();
            case ASSISTIDO:
                var aRepository = new AssistidoRepository();
                aRepository.firestore = getFirestore();
                collectionName = "assistidos";

                aRepository.deleteAll(collectionName, null, limit, queryFilter);
                var assistidos = aRepository.findAll(limit, queryFilter);

                return assistidos.isEmpty();
            case PROCESSO:
                var pRepository = new ProcessoRepository();
                pRepository.firestore = getFirestore();
                collectionName = "processos";

                pRepository.deleteAll(collectionName, null, limit, queryFilter);
                var processos = pRepository.findAll(limit, queryFilter);

                return processos.isEmpty();
            case ATENDIMENTO:
                var atRepository = new AtendimentoRepository();
                atRepository.firestore = getFirestore();
                collectionName = "atendimentos";

                atRepository.deleteAll(collectionName, null, limit, queryFilter);
                var atendimentos = atRepository.findAll(limit, queryFilter);

                return atendimentos.isEmpty();
        }
        return false;
    }
}
