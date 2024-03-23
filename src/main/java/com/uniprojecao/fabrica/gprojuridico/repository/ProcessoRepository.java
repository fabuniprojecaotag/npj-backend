package com.uniprojecao.fabrica.gprojuridico.repository;

import com.uniprojecao.fabrica.gprojuridico.domains.processo.Processo;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.Map;

public class ProcessoRepository extends BaseRepository {

    private static final String collectionName = "processos";
    private static final Class<Processo> type = Processo.class;

    public Processo save(Processo data) {
        return (Processo) super.save(collectionName, type, data);
    }

    public List<Processo> findAll(int limit, @Nullable QueryFilter queryFilter) {
        return super.findAll(collectionName, null, type, limit, queryFilter)
                .stream()
                .map(o -> (Processo) o)
                .toList();
    }

    public Processo findById(String id) {
        return (Processo) super.findById(collectionName, type, id);
    }

    public void update(String id, Map<String, Object> data) {
        super.update(collectionName, id, data);
    }

    public void delete(String id) {
        super.delete(collectionName, id);
    }

    public void deleteAll(int limit, @Nullable QueryFilter queryFilter) {
        super.deleteAll(collectionName, null, limit, queryFilter);
    }
}
