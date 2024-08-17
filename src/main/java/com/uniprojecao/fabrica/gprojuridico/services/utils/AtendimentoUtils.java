package com.uniprojecao.fabrica.gprojuridico.services.utils;

import com.google.cloud.firestore.DocumentSnapshot;
import com.uniprojecao.fabrica.gprojuridico.models.autocomplete.AtendimentoAutocomplete;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.AtendimentoCivil;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.AtendimentoTrabalhista;
import com.uniprojecao.fabrica.gprojuridico.models.Envolvido;
import com.uniprojecao.fabrica.gprojuridico.dto.min.AtendimentoMinDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.min.AtendimentoVinculado;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.convertUsingReflection;

public class AtendimentoUtils {
    /**
     * Converts the passed snapshot to the corresponding DTO through the
     * registered service area
     */
    public static Object snapshotToAtendimento(DocumentSnapshot snapshot, Boolean returnMinDTO,
                                               Boolean returnAutocomplete,
                                               Boolean returnAtendimentosDeAssistidoDTO) {
        if (returnMinDTO) {
            var assistidoMap = convertUsingReflection(snapshot.get("envolvidos.assistido"), false);

            // Defini o atributo "assistido"
            var assistidoEnvolvido =
                    new Envolvido(
                            (String) assistidoMap.get("id"),
                            (String) assistidoMap.get("nome"));

            var date = snapshot.getCreateTime().toDate();

            LocalDateTime localDateTime = date.toInstant()
                    .atZone(ZoneId.of("America/Sao_Paulo"))
                    .toLocalDateTime();

            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            String formattedDateTime = localDateTime.format(formatter);


            return new AtendimentoMinDTO(
                    snapshot.getId(),
                    (String) snapshot.get("area"),
                    (String) snapshot.get("status"),
                    assistidoEnvolvido,
                    formattedDateTime
            );
        }

        if (returnAutocomplete) {
            return new AtendimentoAutocomplete(
                    snapshot.getId()
            );
        }

        if (returnAtendimentosDeAssistidoDTO) {
            var assistidoMap = convertUsingReflection(snapshot.get("envolvidos.assistido"), false);
            var estagiarioMap = convertUsingReflection(snapshot.get("envolvidos.estagiario"), false);

            // Defini o atributo "assistido"
            var assistidoEnvolvido =
                    new Envolvido(
                            (String) assistidoMap.get("id"),
                            (String) assistidoMap.get("nome"));

            // Defini o atributo "estagiario"
            var estagiarioEnvolvido =
                    new Envolvido(
                            (String) estagiarioMap.get("id"),
                            (String) estagiarioMap.get("nome"));

            return new AtendimentoVinculado(
                    snapshot.getId(),
                    (String) snapshot.get("area"),
                    (String) snapshot.get("status"),
                    assistidoEnvolvido,
                    estagiarioEnvolvido,
                    (String) snapshot.get("instante")
            );
        }

        if (snapshot == null) return null;

        String area = snapshot.getString("area");
        if (Objects.equals(area, "Trabalhista")) {
            return snapshot.toObject(AtendimentoTrabalhista.class);
        } else if (Objects.equals(area, "Civil") || Objects.equals(area, "Criminal") || Objects.equals(area, "Família")) {
            return snapshot.toObject(AtendimentoCivil.class);
        }
        return null;
    }
}