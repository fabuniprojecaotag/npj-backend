package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.dto.body.UpdateBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.Atendimento;
import com.uniprojecao.fabrica.gprojuridico.repositories.FirestoreRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

import static com.uniprojecao.fabrica.gprojuridico.services.IdService.defineId;
import static com.uniprojecao.fabrica.gprojuridico.utils.Constants.ATENDIMENTOS_COLLECTION;
import static com.uniprojecao.fabrica.gprojuridico.utils.Utils.identifyChildClass;

@Service
public class AtendimentoService {

    private final FirestoreRepositoryImpl<Atendimento> firestoreRepository = new FirestoreRepositoryImpl<>(ATENDIMENTOS_COLLECTION);

    public Atendimento insert(Atendimento atendimento) throws ExecutionException, InterruptedException {
        var atendimentoWithNewId = (Atendimento) defineId(atendimento, ATENDIMENTOS_COLLECTION, "ATE");
        return firestoreRepository.insert(atendimentoWithNewId.getId(), atendimentoWithNewId);
    }

    public void update(String recordId, UpdateBodyDTO<Atendimento> data, String classType) {
        Class<?> clazz = identifyChildClass(Atendimento.class.getSimpleName(), classType);
        firestoreRepository.update(recordId, data, clazz);
    }
}
