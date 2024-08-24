package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.models.atendimento.Atendimento;
import com.uniprojecao.fabrica.gprojuridico.repositories.FirestoreRepository;
import org.springframework.stereotype.Service;

import static com.uniprojecao.fabrica.gprojuridico.services.IdService.defineId;
import static com.uniprojecao.fabrica.gprojuridico.utils.Constants.ATENDIMENTOS_COLLECTION;

@Service
public class AtendimentoService {

    public Atendimento insert(Atendimento data) throws Exception {
        data = (Atendimento) defineId(data, ATENDIMENTOS_COLLECTION, "ATE");
        return (Atendimento) FirestoreRepository.insert(ATENDIMENTOS_COLLECTION, data);
    }
}
