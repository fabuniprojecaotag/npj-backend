package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.dto.body.DeleteBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.body.ListBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.body.UpdateBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.models.BaseModel;
import com.uniprojecao.fabrica.gprojuridico.repositories.FirestoreRepositoryImpl;

import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public abstract class GenericService<T extends BaseModel> {
    private final FirestoreRepositoryImpl<T> firestoreRepository;
    private final String collectionName;
    private final String prefix;
    protected final IdService<T> idService;

    protected GenericService(String collectionName, String prefix) {
        this.collectionName = collectionName;
        this.prefix = prefix;
        this.idService = new IdService<>();
        this.firestoreRepository = new FirestoreRepositoryImpl<>(collectionName);
    }

    public T findById(String id) throws InvalidPropertiesFormatException, ExecutionException, InterruptedException {
        return firestoreRepository.findById(id);
    }

    public T insert(T entity) throws ExecutionException, InterruptedException {
        var entityWithId = idService.defineId(entity, collectionName, prefix);
        return firestoreRepository.insert(entityWithId.getId(), entity);
    }

    public void update(String recordId, UpdateBodyDTO<T> data) {
        firestoreRepository.update(recordId, (Map<String, Object>) data);
    }

    public ListBodyDTO<T> listAll(String startAfter, int pageSize) throws InvalidPropertiesFormatException, ExecutionException, InterruptedException {
        return firestoreRepository.findAll(startAfter, pageSize, null, "min");
    }

    public void delete(DeleteBodyDTO deleteBodyDTO){
        firestoreRepository.delete(deleteBodyDTO.ids());
    }
}
