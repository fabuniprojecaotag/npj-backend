package com.uniprojecao.fabrica.gprojuridico.repository;

import com.uniprojecao.fabrica.gprojuridico.domains.processo.Processo;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProcessoRepository extends BaseRepository {

    private static final String COLLECTION_NAME = "processos";
    private static final Class<Processo> TYPE = Processo.class;

    public List<Processo> findAll(int limit, @Nullable QueryFilter queryFilter) {
        String[] columnList = new String[]{"numero", "atendimentoId", "nome", "dataDistribuicao", "vara", "forum", "status"};
        return findAll(COLLECTION_NAME, columnList, TYPE, limit, queryFilter)
                .stream()
                .map(o -> (Processo) o)
                .toList();
    }

    public Processo findByNumero(String numero) {
        return (Processo) findById(COLLECTION_NAME, TYPE, numero);
    }
}
