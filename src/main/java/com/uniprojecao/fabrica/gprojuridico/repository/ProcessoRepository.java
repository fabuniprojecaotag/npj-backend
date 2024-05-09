package com.uniprojecao.fabrica.gprojuridico.repository;

import com.uniprojecao.fabrica.gprojuridico.domains.processo.Processo;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import jakarta.annotation.Nullable;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.PROCESSOS_COLLECTION;

@Repository
@DependsOn("baseRepository")
public class ProcessoRepository extends BaseRepository {

    private final String collectionName = PROCESSOS_COLLECTION;
    private final Class<Processo> TYPE = Processo.class;

    public List<Processo> findAll(int limit, @Nullable QueryFilter queryFilter) {
        String[] columnList = new String[]{"numero", "atendimentoId", "nome", "dataDistribuicao", "vara", "forum", "status"};
        return findAll(collectionName, columnList, TYPE, limit, queryFilter)
                .stream()
                .map(o -> (Processo) o)
                .toList();
    }

    public Processo findByNumero(String numero) {
        return (Processo) findById(collectionName, TYPE, numero);
    }
}
