package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.models.atendimento.Atendimento;
import com.uniprojecao.fabrica.gprojuridico.repositories.FirestoreRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.IdService.defineId;
import static com.uniprojecao.fabrica.gprojuridico.utils.Constants.ATENDIMENTOS_COLLECTION;
import static com.uniprojecao.fabrica.gprojuridico.utils.Utils.identifyChildClass;

@Service
public class AtendimentoService {

    private final FirestoreRepositoryImpl firestoreRepository = new FirestoreRepositoryImpl(ATENDIMENTOS_COLLECTION);

    public Atendimento insert(Atendimento atendimento) throws Exception {
        var atendimentoWithNewId = (Atendimento) defineId(atendimento, ATENDIMENTOS_COLLECTION, "ATE");
        return (Atendimento) firestoreRepository.insert(atendimentoWithNewId.getId(), atendimentoWithNewId);
    }

    public void update(String recordId, Map<String, Object> data, String classType) {
        Class<?> clazz = identifyChildClass(Atendimento.class.getSimpleName(), classType);
        firestoreRepository.update(recordId, data, clazz);
    }
}
