package com.uniprojecao.fabrica.gprojuridico.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.google.cloud.firestore.Query.Direction.DESCENDING;

@Repository
@Primary
public class BaseRepository {

    public static Firestore firestore;

    @Value("${spring.cloud.gcp.project-id}")
    private String projectId;

    @PostConstruct
    private void init() {
        var clazz = this.getClass().getSimpleName();
        if (clazz == BaseRepository.class.getSimpleName()) {
            firestore = firestore();
        }
    }

    public Firestore firestore() {
        FirestoreOptions options = FirestoreOptions.newBuilder()
                .setProjectId(projectId)
                .build();
        return options.getService();
    }

    public static void insert(String collectionName, String CustomId, Object data) {
        try {
            firestore.collection(collectionName).document(CustomId).set(data).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Object> findAll(String collectionName, @Nullable String[] fields, @Nullable Class<?> type, @Nonnull Integer limit, @Nullable Filter queryFilter) {
        List<Object> list = new ArrayList<>();

        try {
            var result = getDocSnapshots(collectionName, fields, limit, queryFilter);
            for (QueryDocumentSnapshot document : result) {
                if (type != null) list.add(document.toObject(type));
                else list.add(document);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DocumentSnapshot findLast(String collectionName) {
        try {
            var list = firestore
                    .collection(collectionName)
                    .orderBy(FieldPath.documentId(), DESCENDING)
                    .limit(1)
                    .get()
                    .get()
                    .getDocuments();
            return (!list.isEmpty()) ? list.get(0) : null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void update(String collectionName, String id, Map<String, Object> data) {
        firestore.collection(collectionName).document(id).update(convertMap(data));
    }

    private static List<QueryDocumentSnapshot> getDocSnapshots(String collectionName, @Nullable String[] list, @Nonnull Integer limit, @Nullable Filter filter) {
        if (list == null) list = new String[]{"id"};

        ApiFuture<QuerySnapshot> future;

        future = (filter != null) ?
                firestore.collection(collectionName).where(filter).select(list).limit(limit).get() :
                firestore.collection(collectionName).select(list).limit(limit).get();

        try {
            return future.get().getDocuments();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<String, Object> convertMap(Map<String, Object> inputMap) {
        Map<String, Object> outputMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : inputMap.entrySet()) {
            processEntry(entry.getKey(), entry.getValue(), outputMap, "");
        }
        return outputMap;
    }

    private static void processEntry(String key, Object value, Map<String, Object> outputMap, String parentKey) {
        // Se o valor é um Map, deve-se percorrer o método recursivamente para concatenar as chaves aninhadas
        if (value instanceof Map) {
            @SuppressWarnings("unchecked")
            var subMap = (Map<String, Object>) value;
            for (Map.Entry<String, Object> entry : subMap.entrySet()) {
                processEntry(entry.getKey(), entry.getValue(), outputMap, parentKey + key + ".");
            }
        } else {
            outputMap.put(parentKey + key, value); // Ex. de entrada percorrida recursivamente a ser adicionada: {"ficha.parteContraria.nome", "Mauro Silva"}
        }
    }
}
