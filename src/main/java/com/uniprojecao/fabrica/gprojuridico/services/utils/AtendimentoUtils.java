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

import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.convertUsingReflection;

public class AtendimentoUtils {
    /**
     * Converts the passed snapshot to the corresponding DTO through the
     * registered service area
     */
    public static Object snapshotToAtendimento(DocumentSnapshot snapshot, Boolean returnMinDTO) {
        if (returnMinDTO) {
            var dto = snapshot.toObject(AtendimentoMinDTO.class);
            dto.setId(snapshot.getId());
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
        var o = convertUsingReflection(dto, false);

        if (dto instanceof AtendimentoCivilDTO) System.out.println("AtendimentoCivilDTO recebido!");
        if (dto instanceof AtendimentoTrabalhistaDTO) System.out.println("AtendimentoTrabalhistaDTO recebido!");

        if (dto.getArea() == "Civil") {
            var ficha = (FichaCivilDTO) o.get("ficha");
            return new AtendimentoCivil(
                    dto.getId(),
                    dto.getStatus(),
                    dto.getArea(),
                    dto.getInstante(),
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
                            dto.getFicha().getAssinatura(),
                            dto.getFicha().getDadosSensiveis(),
                            dto.getFicha().getTestemunhas()
                                    .stream()
                                    .map(t -> new Ficha.Testemunha(
                                            t.getNome(),
                                            t.getQualificao(),
                                            t.getEndereco()
                                            )
                                    ).toList(),
                            ficha.getParteContraria(),
                            ficha.getMedidaJudicial()
                    )
            );
        } else {
            var ficha = (FichaTrabalhistaDTO) o.get("ficha");
            return new AtendimentoTrabalhista(
                    dto.getId(),
                    dto.getStatus(),
                    dto.getArea(),
                    dto.getInstante(),
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
                            dto.getFicha().getAssinatura(),
                            dto.getFicha().getDadosSensiveis(),
                            dto.getFicha().getTestemunhas()
                                    .stream()
                                    .map(t -> new Ficha.Testemunha(
                                                    t.getNome(),
                                                    t.getQualificao(),
                                                    t.getEndereco()
                                            )
                                    ).toList(),
                            ficha.getReclamado(),
                            ficha.getRelacaoEmpregaticia(),
                            ficha.getDocumentosDepositadosNpj(),
                            ficha.getOutrasInformacoes()
                    )
            );
        }
    }

    public static AtendimentoDTO atendimentoToDTO(Atendimento a) {
        var o = convertUsingReflection(a, false);

        if (a.getArea() == "Civil") {
            var ac = new AtendimentoCivilDTO();
            var f = (FichaCivil) o.get("ficha");
            var historicoDTO = a.getHistorico().stream().map(
                    e -> new AtendimentoDTO.EntradaHistoricoDTO(e.getId(), e.getTitulo(), e.getDescricao(), e.getInstante(),
                            new AtendimentoDTO.EntradaHistoricoDTO.UsuarioMinDTO(
                                    e.getCriadoPor().getEmail(),
                                    e.getCriadoPor().getNome(),
                                    e.getCriadoPor().getRole()))
            ).toList();
            ac.setId(a.getId());
            ac.setStatus(a.getStatus());
            ac.setArea(a.getArea());
            ac.setInstante(a.getInstante());
            ac.setHistorico(historicoDTO);
            ac.setEnvolvidos(a.getEnvolvidos());

            var testemunhasDTO = f.getTestemunhas()
                    .stream()
                    .map(t -> new FichaDTO.TestemunhaDTO(t.getNome(), t.getQualificao(), t.getEndereco()))
                    .toList();
            ac.setFicha(new FichaCivilDTO(f.getAssinatura(), f.getDadosSensiveis(), testemunhasDTO, f.getParteContraria(), f.getMedidaJudicial()));

            return ac;
        } else {
            var at = new AtendimentoTrabalhistaDTO();
            var f = (FichaTrabalhista) o.get("ficha");
            var historicoDTO = a.getHistorico().stream().map(
                    e -> new AtendimentoDTO.EntradaHistoricoDTO(e.getId(), e.getTitulo(), e.getDescricao(), e.getInstante(),
                            new AtendimentoDTO.EntradaHistoricoDTO.UsuarioMinDTO(
                                    e.getCriadoPor().getEmail(),
                                    e.getCriadoPor().getNome(),
                                    e.getCriadoPor().getRole()))
            ).toList();
            at.setId(a.getId());
            at.setStatus(a.getStatus());
            at.setArea(a.getArea());
            at.setInstante(a.getInstante());
            at.setHistorico(historicoDTO);
            at.setEnvolvidos(a.getEnvolvidos());
            var testemunhasDTO = f.getTestemunhas()
                    .stream()
                    .map(t -> new FichaDTO.TestemunhaDTO(t.getNome(), t.getQualificao(), t.getEndereco()))
                    .toList();
            at.setFicha(new FichaTrabalhistaDTO(f.getAssinatura(), f.getDadosSensiveis(), testemunhasDTO, f.getReclamado(), f.getRelacaoEmpregaticia(), f.getDocumentosDepositadosNpj(), f.getOutrasInformacoes()));

            return at;
        }
    }
}