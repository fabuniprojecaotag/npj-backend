package com.uniprojecao.fabrica.gprojuridico.services.utils;

import com.uniprojecao.fabrica.gprojuridico.domains.atendimento.Atendimento;
import com.uniprojecao.fabrica.gprojuridico.dto.AtendimentoCivilDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.AtendimentoTrabalhistaDTO;
import com.google.cloud.firestore.DocumentSnapshot;
import jakarta.annotation.Nullable;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static String generateCustomId(String id) {
        String numbers = id.substring(3); // numbers = "nnnnn" of {"ATE" + "nnnnn"}
        int increment = Integer.parseInt(numbers) + 1;

        Matcher matcher = Pattern.compile("0").matcher(numbers);
        var remainingZeros = new StringBuilder();

        while (matcher.find()) {
            remainingZeros.append("0");
        }

        return "ATE" + remainingZeros + increment; // e.g. "ATE00092", which is equivalent to "ATE" + "000" + "92"
    }

    public static String setAndReturnId(Atendimento data, @Nullable String id) {
        if (id != null) {
            String customId = generateCustomId(id);
            data.setId(customId);
            return customId;
        }
        String customId = "ATE00001";
        data.setId(customId);
        return customId;
    }
}