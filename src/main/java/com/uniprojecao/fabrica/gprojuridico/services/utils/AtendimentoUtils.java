package com.uniprojecao.fabrica.gprojuridico.services.utils;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentSnapshot;
import com.uniprojecao.fabrica.gprojuridico.domains.atendimento.AtendimentoCivil;
import com.uniprojecao.fabrica.gprojuridico.domains.atendimento.AtendimentoTrabalhista;
import com.uniprojecao.fabrica.gprojuridico.dto.EnvolvidoDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.min.AtendimentoMinDTO;
import com.uniprojecao.fabrica.gprojuridico.projections.AtendimentosDoAssistidoDTO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.convertUsingReflection;

public class AtendimentoUtils {
    /**
     * Converts the passed snapshot to the corresponding DTO through the
     * registered service area
     */
    public static Object snapshotToAtendimento(DocumentSnapshot snapshot, Boolean returnMinDTO,
                                               Boolean returnAtendimentosDeAssistidoDTO) {
        if (returnMinDTO) {
            var assistidoMap = convertUsingReflection(snapshot.get("envolvidos.assistido"), false);

            // Defini o atributo "assistido"
            var assistidoEnvolvido =
                    new EnvolvidoDTO(
                            (String) assistidoMap.get("id"),
                            (String) assistidoMap.get("nome"));

            // Defini o atributo "dataCriacao"
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = snapshot.getCreateTime().toDate();
            var dataCriacao = df.format(date);

            return new AtendimentoMinDTO(
                    snapshot.getId(),
                    (String) snapshot.get("area"),
                    (String) snapshot.get("status"),
                    assistidoEnvolvido,
                    dataCriacao
            );
        }

        if (returnAtendimentosDeAssistidoDTO) {
            var assistidoMap = convertUsingReflection(snapshot.get("envolvidos.assistido"), false);
            var estagiarioMap = convertUsingReflection(snapshot.get("envolvidos.estagiario"), false);

            // Defini o atributo "assistido"
            var assistidoEnvolvido =
                    new EnvolvidoDTO(
                            (String) assistidoMap.get("id"),
                            (String) assistidoMap.get("nome"));

            // Defini o atributo "estagiario"
            var estagiarioEnvolvido =
                    new EnvolvidoDTO(
                            (String) estagiarioMap.get("id"),
                            (String) estagiarioMap.get("nome"));

            return new AtendimentosDoAssistidoDTO(
                    snapshot.getId(),
                    (String) snapshot.get("area"),
                    (String) snapshot.get("status"),
                    assistidoEnvolvido,
                    estagiarioEnvolvido,
                    (Timestamp) snapshot.get("instante")
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