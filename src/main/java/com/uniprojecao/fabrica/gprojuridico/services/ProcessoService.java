package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.dto.body.UpdateBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.models.processo.Processo;
import com.uniprojecao.fabrica.gprojuridico.repositories.FirestoreRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

import static com.uniprojecao.fabrica.gprojuridico.utils.Constants.PROCESSOS_COLLECTION;

@Service
public class ProcessoService {

    private final FirestoreRepositoryImpl<Processo> firestoreRepository = new FirestoreRepositoryImpl<>(PROCESSOS_COLLECTION);

    public Processo insert(Processo processo) throws ExecutionException, InterruptedException {
        return firestoreRepository.insert(processo.getNumero(), processo);
    }

    public void update(String recordId, UpdateBodyDTO<Processo> data) {
        firestoreRepository.update(recordId, data, Processo.class);
    }
}
