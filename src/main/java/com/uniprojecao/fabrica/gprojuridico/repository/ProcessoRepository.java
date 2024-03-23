package com.uniprojecao.fabrica.gprojuridico.repository;

import com.uniprojecao.fabrica.gprojuridico.domains.processo.Processo;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import jakarta.annotation.Nullable;

import java.util.List;

public class ProcessoRepository extends BaseRepository {

    private static final String COLLECTION_NAME = "processos";
    private static final Class<Processo> TYPE = Processo.class;

    public List<Processo> findAll(int limit, @Nullable QueryFilter queryFilter) {
        return BaseRepository.findAll(COLLECTION_NAME, null, TYPE, limit, queryFilter)
                .stream()
                .map(o -> (Processo) o)
                .toList();
    }

    public Processo findById(String id) {
        return (Processo) BaseRepository.findById(COLLECTION_NAME, TYPE, id);
    }
}
