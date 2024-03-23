package com.uniprojecao.fabrica.gprojuridico.repository;

import com.uniprojecao.fabrica.gprojuridico.domains.processo.Processo;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.Map;

public class ProcessoRepository {

    private static final String COLLECTION_NAME = "processos";
    private static final Class<Processo> TYPE = Processo.class;

    public Processo save(Processo processo) {
        return (Processo) BaseRepository.save(COLLECTION_NAME, TYPE, processo);
    }

    public List<Processo> findAll(int limit, @Nullable QueryFilter queryFilter) {
        return BaseRepository.findAll(COLLECTION_NAME, null, TYPE, limit, queryFilter)
                .stream()
                .map(o -> (Processo) o)
                .toList();
    }

    public Processo findById(String id) {
        return (Processo) BaseRepository.findById(COLLECTION_NAME, TYPE, id);
    }

    public void update(String id, Map<String, Object> data) {
        BaseRepository.update(COLLECTION_NAME, id, data);
    }

    public void delete(String id) {
        BaseRepository.delete(COLLECTION_NAME, id);
    }

    public void deleteAll(int limit, @Nullable QueryFilter queryFilter) {
        BaseRepository.deleteAll(COLLECTION_NAME, null, limit, queryFilter);
    }
}
