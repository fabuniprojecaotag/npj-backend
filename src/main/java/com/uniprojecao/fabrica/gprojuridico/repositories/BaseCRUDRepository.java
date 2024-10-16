package com.uniprojecao.fabrica.gprojuridico.repositories;

import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.FirestoreException;
import com.uniprojecao.fabrica.gprojuridico.dto.body.ListBodyDTO;

import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface BaseCRUDRepository<T> {
    T insert(String customId, T data) throws FirestoreException, IllegalArgumentException, ExecutionException, InterruptedException;

    ListBodyDTO<T> findAll(String startAfter, int pageSize, Filter filter, String returnType) throws FirestoreException, IllegalArgumentException, ExecutionException, InterruptedException, InvalidPropertiesFormatException;

    T findById(String id) throws FirestoreException, IllegalArgumentException, ExecutionException, InterruptedException, InvalidPropertiesFormatException;

    void update(String recordId, Map<String, Object> data);

    void delete(List<String> ids);
}
