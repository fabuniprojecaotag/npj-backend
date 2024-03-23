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
public class AtendimentoRepository extends BaseRepository {

    @Autowired
    public Firestore firestore;

    private static final String collectionName = "atendimentos";
    private static final Class<Atendimento> type = Atendimento.class;

    public Atendimento save(String customId, Atendimento data) {
        super.save(collectionName, customId, data);
        return data;
    }

    public List<AtendimentoMinDTO> findAll(@Nonnull Integer limit, @Nullable QueryFilter queryFilter) {
        String[] list = {"id", "area", "status", "envolvidos.assistido.nome"};
        return super.findAll(collectionName, list, null, limit, queryFilter)
                .stream()
                .map(o -> (AtendimentoMinDTO) snapshotToAtendimento((DocumentSnapshot) o, true))
                .toList();
    }

    public Atendimento findById(String id) {
        return (Atendimento) super.findById(collectionName, type, id);
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
