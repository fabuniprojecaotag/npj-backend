package com.uniprojecao.fabrica.gprojuridico.repository;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.WriteResult;
import com.uniprojecao.fabrica.gprojuridico.domains.enums.FilterType;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.Map;

interface CrudRepository {

    DocumentReference save(String collection, Map<String, Object> data);

    DocumentReference save(String collection, Object data);

    WriteResult saveWithCustomId(String collectionName, String CustomId, Object data);

    List<QueryDocumentSnapshot> findAll(String collection, Integer limit);

    List<QueryDocumentSnapshot> findAll(String collectionName, @Nullable Integer limit, String field, FilterType filterType, String value);

    DocumentSnapshot findLast(String collectionName);

    DocumentSnapshot findById(String collection, String id);

    Boolean update(String collection, String id, Map<String, Object> data);

    Boolean delete(String collection, String id);

    Boolean deleteAll(String collection, Integer limit);

    Boolean deleteAll(String collectionName, Integer limit, @Nonnull String field,
                      @Nonnull FilterType filterType , @Nonnull String value);
}
