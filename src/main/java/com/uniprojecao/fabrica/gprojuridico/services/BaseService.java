package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.repository.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.initFilter;
import static java.lang.Integer.parseInt;

public class BaseService {

    private final String collectionName;

    public BaseService(String collectionName) {
        this.collectionName = collectionName;
    }

    public void update(String id, Map<String, Object> data) {
        BaseRepository.update(collectionName, id, data);
    }

    public void delete(String id) {
        BaseRepository.delete(collectionName, id);
    }

    public void deleteAll(String limit, String field, String filter, String value) {
        BaseRepository.deleteAll(collectionName, null, parseInt(limit), initFilter(field, filter, value)); // TODO: Converter método para static
    }


}
