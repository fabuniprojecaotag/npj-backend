package com.uniprojecao.fabrica.gprojuridico.repository;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.uniprojecao.fabrica.gprojuridico.domains.atendimento.Atendimento;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.dto.min.AtendimentoMinDTO;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.AtendimentoUtils.snapshotToAtendimento;

@Repository
public class AtendimentoRepository {

    @Autowired
    public Firestore firestore;

    private final String COLLECTION_NAME = "atendimentos";

    public Atendimento save(String customId, Atendimento atendimento) {
        BaseRepository.save(COLLECTION_NAME, customId, atendimento);
        return atendimento;
    }

    public List<AtendimentoMinDTO> findAll(@Nonnull Integer limit, @Nullable QueryFilter queryFilter) {
        String[] columnList = {"id", "area", "status", "envolvidos.assistido.nome"};
        return BaseRepository.findAll(COLLECTION_NAME, columnList, null, limit, queryFilter)
                .stream()
                .map(o -> (AtendimentoMinDTO) snapshotToAtendimento((DocumentSnapshot) o, true))
                .toList();
    }

    public DocumentSnapshot findLast() {
        return BaseRepository.findLast(COLLECTION_NAME);
    }

    public Atendimento findById(String id) {
        var snapshot = (DocumentSnapshot) BaseRepository.findById(COLLECTION_NAME, null, id);
        return (Atendimento) snapshotToAtendimento(snapshot, false);
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
