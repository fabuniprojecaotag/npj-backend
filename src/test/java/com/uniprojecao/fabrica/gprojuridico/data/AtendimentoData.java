package com.uniprojecao.fabrica.gprojuridico.data;

import com.google.cloud.Timestamp;
import com.uniprojecao.fabrica.gprojuridico.domains.atendimento.Atendimento;
import com.uniprojecao.fabrica.gprojuridico.domains.atendimento.AtendimentoCivil;
import com.uniprojecao.fabrica.gprojuridico.domains.atendimento.FichaCivil;
import com.uniprojecao.fabrica.gprojuridico.domains.atendimento.ParteContraria;
import com.uniprojecao.fabrica.gprojuridico.dto.EnvolvidoDTO;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AtendimentoData {
    public static List<Atendimento> seedWithAtendimento() {
        // Para o atributo "prazoEntregaDocumentos"
        Timestamp formattedDate;
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            formattedDate = Timestamp.of(df.parse("20/04/2024"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // Para o atributo "historico"
        var usuario = new Atendimento.EntradaHistorico.UsuarioMin("leticia.alves@projecao.br", "Letícia Alves Martins", "PROFESSOR");
        var entrada = new Atendimento.EntradaHistorico(null, "Elaborada a petição inicial", null, null, usuario);
        List<Atendimento.EntradaHistorico> historico = new ArrayList<>();
        historico.add(entrada);

        // Para o atributo "envolvidos"
        var estagiario = new EnvolvidoDTO("202101055@projecao.edu.br", "Emilly Letícia Cordeiro");
        var professor = new EnvolvidoDTO("rebeca.lopes@projecao.br", "Rebeca Lopes Silva");
        var secretaria = new EnvolvidoDTO("leticia.alves@projecao.br", "Letícia Alves Martins");
        Map<String, EnvolvidoDTO> envolvidos = new HashMap<>(Map.of("estagiario", estagiario, "professor", professor, "secretaria", secretaria));

        // Para o atributo "ficha"
        var parteContraria = new ParteContraria("Alberto Gomes Pereira", "Padeiro", "4.223.124-5", "948.234.153-23", "alberto.gomes@example.com", "QSA Conjunto A casa 12", "(61) 98392-2934");
        var fichaCivil = new FichaCivil(null, false, new ArrayList<>(), parteContraria, null);

        return List.of(
                new AtendimentoCivil("ATE00052", "Processo ativo", "Civil", null, formattedDate, historico, envolvidos, fichaCivil)
        );
    }
}
