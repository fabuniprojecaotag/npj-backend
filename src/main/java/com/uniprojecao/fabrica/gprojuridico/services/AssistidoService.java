package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.models.assistido.Assistido;
import com.uniprojecao.fabrica.gprojuridico.repositories.FirestoreRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.utils.Constants.ASSISTIDOS_COLLECTION;
import static com.uniprojecao.fabrica.gprojuridico.utils.Utils.identifyChildClass;

@Service
public class AssistidoService {

    private final FirestoreRepositoryImpl firestoreRepository = new FirestoreRepositoryImpl(ASSISTIDOS_COLLECTION);

    public Assistido insert(Assistido assistido) throws Exception {
        String customId = assistido.getCpf();
        return (Assistido) firestoreRepository.insert(customId, assistido);
    }

    public void update(String recordId, Map<String, Object> data, String classType) {
        Class<?> clazz = identifyChildClass(Assistido.class.getSimpleName(), classType);
        firestoreRepository.update(recordId, data, clazz);
    }
}
