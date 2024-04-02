package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.repository.BaseRepository;

import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.initFilter;
import static java.lang.Integer.parseInt;

public class BaseService {

    private final BaseRepository REPOSITORY;
    private final String COLLECTION_NAME;

    public BaseService(BaseRepository repository, String collectionName) {
        REPOSITORY = repository;
        COLLECTION_NAME = collectionName;
    }

    public void update(String id, Map<String, Object> data) {
        REPOSITORY.update(COLLECTION_NAME, id, data);
    }

    public void delete(String id) {
        REPOSITORY.delete(COLLECTION_NAME, id);
    }

    public void deleteAll(String limit, String field, String filter, String value) {
        REPOSITORY.deleteAll(COLLECTION_NAME, null, parseInt(limit), initFilter(field, filter, value));
    }


}
