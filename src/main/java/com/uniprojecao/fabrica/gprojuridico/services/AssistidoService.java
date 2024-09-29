package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.dto.body.UpdateBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.Assistido;
import com.uniprojecao.fabrica.gprojuridico.repositories.FirestoreRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

import static com.uniprojecao.fabrica.gprojuridico.utils.Constants.ASSISTIDOS_COLLECTION;
import static com.uniprojecao.fabrica.gprojuridico.utils.Utils.identifyChildClass;

@Service
public class AssistidoService {

    private final FirestoreRepositoryImpl<Assistido> firestoreRepository = new FirestoreRepositoryImpl<>(ASSISTIDOS_COLLECTION);

    public Assistido insert(Assistido assistido) throws ExecutionException, InterruptedException {
        String customId = assistido.getCpf();
        return firestoreRepository.insert(customId, assistido);
    }

    public void update(String recordId, UpdateBodyDTO<Assistido> data, String classType) {
        Class<?> clazz = identifyChildClass(Assistido.class.getSimpleName(), classType);
        firestoreRepository.update(recordId, data, clazz);
    }
}
