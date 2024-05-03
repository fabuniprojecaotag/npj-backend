package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.repository.BaseRepository;

import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.initFilter;
import static java.lang.Integer.parseInt;

public class BaseService {

    private final BaseRepository repository;
    private final String collectionName;

    public BaseService(BaseRepository repository, String collectionName) {
        this.repository = repository;
        this.collectionName = collectionName;
    }

    public void update(String id, Map<String, Object> data) {
        repository.update(collectionName, id, data);
    }

    public void delete(String id) {
        repository.delete(collectionName, id);
    }

    public void deleteAll(String limit, String field, String filter, String value) {
        repository.deleteAll(collectionName, null, parseInt(limit), initFilter(field, filter, value));
    }


}
