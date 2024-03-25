package com.uniprojecao.fabrica.gprojuridico.services.utils;

import com.google.cloud.firestore.DocumentSnapshot;
import com.uniprojecao.fabrica.gprojuridico.domains.atendimento.*;
import com.uniprojecao.fabrica.gprojuridico.dto.atendimento.*;
import com.uniprojecao.fabrica.gprojuridico.dto.min.AtendimentoMinDTO;
import jakarta.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AtendimentoUtils {
    /**
     * Converts the passed snapshot to the corresponding DTO through the
     * registered service area
     */
    public static Object snapshotToAtendimento(DocumentSnapshot snapshot, Boolean returnMinDTO) {
        if (returnMinDTO) {
            var dto = snapshot.toObject(AtendimentoMinDTO.class);
            dto.setAssistido((String) snapshot.get("envolvidos.assistido.nome")); // don't use toString() because if the snapshot doesn't contain this field, NullException is thrown
            Date date = snapshot.getCreateTime().toDate();

            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            dto.setDataCriacao(df.format(date));
            return dto;
        }

        String area = snapshot.getString("area");
        if (Objects.equals(area, "Trabalhista")) {
            return snapshot.toObject(AtendimentoTrabalhista.class);
        } else if (Objects.equals(area, "Civil") || Objects.equals(area, "Criminal") || Objects.equals(area, "Família")) {
            return snapshot.toObject(AtendimentoCivil.class);
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

    public static String setAndReturnId(AtendimentoDTO data, @Nullable String id) {
        if (id != null) {
            String customId = generateCustomId(id);
            data.setId(customId);
            return customId;
        }
        String customId = "ATE00001";
        data.setId(customId);
        return customId;
    }

    public static Atendimento dtoToAtendimento(AtendimentoDTO dto) {
        if (dto instanceof AtendimentoCivilDTO ac) {
            return new AtendimentoCivil(
                    dto.getId(),
                    dto.getStatus(),
                    dto.getArea(),
                    dto.getInstante(),
                    dto.getPrazoEntregaDocumentos(),
                    dto.getHistorico()
                            .stream()
                            .map(e -> new Atendimento.EntradaHistorico(
                                            e.getId(),
                                            e.getTitulo(),
                                            e.getDescricao(),
                                            e.getInstante(),
                                            new Atendimento.EntradaHistorico.UsuarioMin(
                                                    e.getCriadoPor().getEmail(),
                                                    e.getCriadoPor().getNome(),
                                                    e.getCriadoPor().getRole()
                                            )
                                    )
                            ).toList(),
                    dto.getEnvolvidos(),
                    new FichaCivil(
                            ac.getFicha().getAssinatura(),
                            ac.getFicha().getDadosSensiveis(),
                            ac.getFicha().getTestemunhas()
                                    .stream()
                                    .map(t -> new Ficha.Testemunha(
                                            t.getNome(),
                                            t.getQualificao(),
                                            t.getEndereco()
                                            )
                                    ).toList(),
                            ac.getFicha().getParteContraria(),
                            ac.getFicha().getMedidaJudicial()
                    )
            );
        } else if (dto instanceof AtendimentoTrabalhistaDTO at) {
            return new AtendimentoTrabalhista(
                    dto.getId(),
                    dto.getStatus(),
                    dto.getArea(),
                    dto.getInstante(),
                    dto.getPrazoEntregaDocumentos(),
                    dto.getHistorico()
                            .stream()
                            .map(e -> new Atendimento.EntradaHistorico(
                                            e.getId(),
                                            e.getTitulo(),
                                            e.getDescricao(),
                                            e.getInstante(),
                                            new Atendimento.EntradaHistorico.UsuarioMin(
                                                    e.getCriadoPor().getEmail(),
                                                    e.getCriadoPor().getNome(),
                                                    e.getCriadoPor().getRole()
                                            )
                                    )
                            ).toList(),
                    dto.getEnvolvidos(),
                    new FichaTrabalhista(
                            at.getFicha().getAssinatura(),
                            at.getFicha().getDadosSensiveis(),
                            at.getFicha().getTestemunhas()
                                    .stream()
                                    .map(t -> new Ficha.Testemunha(
                                                    t.getNome(),
                                                    t.getQualificao(),
                                                    t.getEndereco()
                                            )
                                    ).toList(),
                            at.getFicha().getReclamado(),
                            at.getFicha().getRelacaoEmpregaticia(),
                            at.getFicha().getDocumentosDepositadosNpj(),
                            at.getFicha().getOutrasInformacoes()
                    )
            );
        }
        return null;
    }

    public static AtendimentoDTO atendimentoToDTO(Atendimento a) {
        var dto = new AtendimentoDTO();
        var historicoDTO = a.getHistorico().stream().map(
                e -> new AtendimentoDTO.EntradaHistoricoDTO(e.getId(), e.getTitulo(), e.getDescricao(), e.getInstante(),
                        new AtendimentoDTO.EntradaHistoricoDTO.UsuarioMinDTO(
                                e.getCriadoPor().getEmail(),
                                e.getCriadoPor().getNome(),
                                e.getCriadoPor().getRole()))
        ).toList();

        dto.setId(a.getId());
        dto.setStatus(a.getStatus());
        dto.setArea(a.getArea());
        dto.setInstante(a.getInstante());
        dto.setPrazoEntregaDocumentos(a.getPrazoEntregaDocumentos());
        dto.setHistorico(historicoDTO);
        dto.setEnvolvidos(a.getEnvolvidos());

        if (a instanceof AtendimentoCivil aCivil) {
            var aCivilDTO = (AtendimentoCivilDTO) dto;
            FichaCivil f = aCivil.getFicha();

            var testemunhasDTO = f.getTestemunhas()
                    .stream()
                    .map(t -> new FichaDTO.TestemunhaDTO(t.getNome(), t.getQualificao(), t.getEndereco()))
                    .toList();
            var fCivilDTO = new FichaCivilDTO(f.getAssinatura(), f.getDadosSensiveis(), testemunhasDTO, f.getParteContraria(), f.getMedidaJudicial());

            aCivilDTO.setFicha(fCivilDTO);

            return aCivilDTO;
        } else if (a instanceof AtendimentoTrabalhista aTrabalhista) {
            var aTrabalhistaDTO = (AtendimentoTrabalhistaDTO) dto;
            FichaTrabalhista f = aTrabalhista.getFicha();

            var testemunhasDTO = f.getTestemunhas()
                    .stream()
                    .map(t -> new FichaDTO.TestemunhaDTO(t.getNome(), t.getQualificao(), t.getEndereco()))
                    .toList();
            var fTrabalhistaDTO = new FichaTrabalhistaDTO(f.getAssinatura(), f.getDadosSensiveis(), testemunhasDTO, f.getReclamado(), f.getRelacaoEmpregaticia(), f.getDocumentosDepositadosNpj(), f.getOutrasInformacoes());

            aTrabalhistaDTO.setFicha(fTrabalhistaDTO);

            return aTrabalhistaDTO;
        }

        return null;
    }
}