package com.uniprojecao.fabrica.gprojuridico.repositories;

import com.google.cloud.firestore.Filter;

import java.util.List;
import java.util.Map;

public interface FirestoreRepository {
    Object insert(String customId, Object data) throws Exception;

    Map<String, Object> findAll(String startAfter, int pageSize, Filter filter, String returnType) throws Exception;

    Object findById(String id) throws Exception;

    void update(String recordId, Map<String, Object> data, Class<?> clazz);

    void delete(List<String> ids);
}
