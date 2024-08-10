package com.uniprojecao.fabrica.gprojuridico.services.utils;

import com.google.cloud.firestore.DocumentSnapshot;
import com.uniprojecao.fabrica.gprojuridico.models.autocomplete.AssistidoAutocomplete;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.AssistidoCivil;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.AssistidoFull;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.AssistidoTrabalhista;
import com.uniprojecao.fabrica.gprojuridico.dto.min.AssistidoMinDTO;

public class AssistidoUtils {
    public static Object snapshotToAssistido(DocumentSnapshot snapshot, Boolean returnMinDTO,
                                             Boolean returnAutocomplete) {

        if (snapshot == null) return null;

        if (returnMinDTO) {
            var object = snapshot.toObject(AssistidoMinDTO.class);
            object.setCpf(snapshot.getId());
            return object;
        }

        if (returnAutocomplete) {
            return snapshot.toObject(AssistidoAutocomplete.class);
        }

        Boolean dadosFCivil =
                snapshot.contains("naturalidade") &&
                        snapshot.contains("dataNascimento") &&
                        snapshot.contains("dependentes");
        Boolean dadosFTrabalhista =
                snapshot.contains("ctps") &&
                        snapshot.contains("pis") &&
                        snapshot.contains("empregadoAtualmente");

        if (dadosFCivil && dadosFTrabalhista) return snapshot.toObject(AssistidoFull.class);
        else if (dadosFCivil) return snapshot.toObject(AssistidoCivil.class);
        else if (dadosFTrabalhista) return snapshot.toObject(AssistidoTrabalhista.class);
        else throw new RuntimeException("Error to convert snapshot into Assistido.");
    }
}
