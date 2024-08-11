package com.uniprojecao.fabrica.gprojuridico.services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.uniprojecao.fabrica.gprojuridico.dto.DeleteBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.models.MedidaJuridicaModel;
import com.uniprojecao.fabrica.gprojuridico.models.processo.Processo;
import com.uniprojecao.fabrica.gprojuridico.repository.BaseRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.AssistidoUtils.snapshotToAssistido;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.AtendimentoUtils.snapshotToAtendimento;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.*;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.UsuarioUtils.snapshotToUsuario;

@Service
public class FirestoreService extends BaseRepository {

    public Map<String, Object> getDocuments(
            String collectionName,
            String startAfter,
            int pageSize,
            Filter filter
    )
            throws Exception {
        CollectionReference collection = firestore.collection(collectionName);
        String[] fieldNames = getFieldNames(collectionName);

        Query query = (filter != null) ?
                collection.orderBy("__name__").where(filter).select(fieldNames).limit(pageSize) :
                collection.orderBy("__name__").select(fieldNames).limit(pageSize);

        if (startAfter != null) {
            DocumentSnapshot lastDoc = firestore.collection(collectionName).document(startAfter).get().get();
            query = query.startAfter(lastDoc);
        }

        ApiFuture<QuerySnapshot> future = query.get();

        List<Object> list = new ArrayList<>();

        for (QueryDocumentSnapshot snapshot : future.get()) {
            var document = convertSnapshot(collectionName, snapshot);
            list.add(document);
        }

        Object lastDoc = list.get(list.size() - 1);
        int collectionSize = future.get().size();

        return generateMap(list, lastDoc, collectionSize);
    }

    private Map<String, Object> generateMap(List<Object> list, Object lastDoc, int count) {
        return Map.of(
                "list", list,
                "lastDoc", lastDoc,
                "count", count);
    }

    private String[] getFieldNames(String collection) {
        return switch (collection) {
            case ASSISTIDOS_COLLECTION -> new String[]{"nome", "email", "quantidade.atendimentos", "quantidade.processos", "telefone"};
            case ATENDIMENTOS_COLLECTION -> new String[]{"area", "status", "envolvidos.assistido"};
            case MEDIDAS_JURIDICAS_COLLECTION -> new String[]{"area", "descricao"};
            case PROCESSOS_COLLECTION -> new String[]{"numero", "atendimentoId", "nome", "dataDistribuicao", "vara", "forum", "status"};
            case USUARIOS_COLLECTION -> new String[]{"nome", "email", "role", "status", "matricula", "semestre"};
            default -> throw new RuntimeException("Collection name invalid. Checks if the collection exists.");
        };
    }

    private Object convertSnapshot(String collection, DocumentSnapshot snapshot) {
        return switch (collection) {
            case ASSISTIDOS_COLLECTION -> snapshotToAssistido(snapshot, true, false);
            case ATENDIMENTOS_COLLECTION -> snapshotToAtendimento(snapshot, true, false, false);
            case MEDIDAS_JURIDICAS_COLLECTION -> snapshot.toObject(MedidaJuridicaModel.class);
            case PROCESSOS_COLLECTION -> snapshot.toObject(Processo.class);
            case USUARIOS_COLLECTION -> snapshotToUsuario(snapshot, true, false);
            default -> throw new RuntimeException("Collection name invalid. Checks if the collection exists.");
        };
    }

    public void deleteDocuments(DeleteBodyDTO payload) {
        var collectionName = payload.collectionName();
        var ids = payload.ids();

        for (var id : ids) {
            firestore.collection(collectionName).document(id).delete();
        }
    }
}
