package com.uniprojecao.fabrica.gprojuridico.repository;

import com.google.cloud.firestore.DocumentSnapshot;
import com.uniprojecao.fabrica.gprojuridico.models.Autocomplete.AssistidoAutocomplete;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.Assistido;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.dto.min.AssistidoMinDTO;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.AssistidoUtils.snapshotToAssistido;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.ASSISTIDOS_COLLECTION;

@Repository
@DependsOn("baseRepository")
public class AssistidoRepository extends BaseRepository {

    private final String collectionName = ASSISTIDOS_COLLECTION;

    public List<AssistidoMinDTO> findAll(@Nonnull Integer limit, @Nullable QueryFilter queryFilter) {
        String[] columnList = {"nome", "email", "quantidade.atendimentos", "quantidade.processos", "telefone"};
        return findAll(collectionName, columnList, null, limit, queryFilter)
                .stream()
                .map(o -> (AssistidoMinDTO) snapshotToAssistido((DocumentSnapshot) o, true, false))
                .toList();
    }

    public List<AssistidoAutocomplete> findAllMin(@Nonnull Integer limit, @Nullable QueryFilter queryFilter) {
        String[] columnList = {"nome"};
        return findAll(collectionName, columnList, null, limit, queryFilter)
                .stream()
                .map(o -> (AssistidoAutocomplete) snapshotToAssistido((DocumentSnapshot) o, false, true))
                .toList();
    }

    public Assistido findById(String id) {
        var snapshot = (DocumentSnapshot) findById(collectionName, null, id);
        return (Assistido) snapshotToAssistido(snapshot, false, false);
    }
}
