package com.uniprojecao.fabrica.gprojuridico.services;

import com.google.cloud.firestore.DocumentSnapshot;
import com.uniprojecao.fabrica.gprojuridico.dto.min.*;
import com.uniprojecao.fabrica.gprojuridico.models.Envolvido;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.AssistidoCivil;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.AssistidoFull;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.AssistidoTrabalhista;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.AtendimentoCivil;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.AtendimentoTrabalhista;
import com.uniprojecao.fabrica.gprojuridico.models.autocomplete.AssistidoAutocomplete;
import com.uniprojecao.fabrica.gprojuridico.models.autocomplete.AtendimentoAutocomplete;
import com.uniprojecao.fabrica.gprojuridico.models.autocomplete.UsuarioAutocomplete;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Estagiario;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Usuario;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static com.uniprojecao.fabrica.gprojuridico.utils.Utils.convertUsingReflection;

public class DocumentSnapshotService {
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

    public enum UserUniqueField { EMAIL }

    public static Object snapshotToUsuario(DocumentSnapshot snapshot, Boolean returnMinDTO,
                                           Boolean returnAutocomplete) {
        if (snapshot == null) return null;

        boolean dadosEstagiario = snapshot.contains("matricula");

        if (returnMinDTO) {
            if (dadosEstagiario) {
                var object = snapshot.toObject(EstagiarioMinDTO.class);
                object.setId(snapshot.getId());
                return object;
            } else {
                var object = snapshot.toObject(UsuarioMinDTO.class);
                object.setId(snapshot.getId());
                return object;
            }
        }

        if (returnAutocomplete) {
            return snapshot.toObject(UsuarioAutocomplete.class);
        }

        return (dadosEstagiario) ? snapshot.toObject(Estagiario.class) : snapshot.toObject(Usuario.class);
    }
}
