package com.uniprojecao.fabrica.gprojuridico.services.utils;

import com.uniprojecao.fabrica.gprojuridico.dto.AtendimentoCivilDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.AtendimentoTrabalhistaDTO;
import com.google.cloud.firestore.DocumentSnapshot;

import java.util.Objects;

public class AtendimentoUtils {
    /**
     * Converts the passed snapshot to the corresponding DTO through the
     * registered service area
     */
    public static Object convertSnapshotToCorrespondingAtendimentoDTO(DocumentSnapshot snapshot) {
        String area = snapshot.getString("area");
        if (Objects.equals(area, "Trabalhista")) {
            return snapshot.toObject(AtendimentoTrabalhistaDTO.class);
        } else if (Objects.equals(area, "Civil") || Objects.equals(area, "Família") || Objects.equals(area, "Penal")) {
            return snapshot.toObject(AtendimentoCivilDTO.class);
        }
        return null;
    }
}