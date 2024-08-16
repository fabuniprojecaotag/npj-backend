package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.repository.BaseRepository;

import java.util.Map;

public class BaseService {

    private final String collectionName;

    public BaseService(String collectionName) {
        this.collectionName = collectionName;
    }

    public void update(String id, Map<String, Object> data) {
        BaseRepository.update(collectionName, id, data);
    }
}
