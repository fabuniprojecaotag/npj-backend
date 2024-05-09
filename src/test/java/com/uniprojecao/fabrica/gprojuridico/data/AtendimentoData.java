package com.uniprojecao.fabrica.gprojuridico.data;

import com.uniprojecao.fabrica.gprojuridico.domains.Endereco;
import com.uniprojecao.fabrica.gprojuridico.domains.atendimento.*;
import com.uniprojecao.fabrica.gprojuridico.dto.EnvolvidoDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AtendimentoData {
    public static List<Atendimento> seedWithAtendimento() {
        // Para o atributo "historico"
        var usuario = new Atendimento
                .EntradaHistorico
                .UsuarioMin("leticia.alves@projecao.br", "Letícia Alves Martins", "PROFESSOR");
        var entrada = new Atendimento.EntradaHistorico(null, "Elaborada a petição inicial", null, null, usuario);
        List<Atendimento.EntradaHistorico> historico = new ArrayList<>();
        historico.add(entrada);

        // Para o atributo "envolvidos"
        var estagiario = new EnvolvidoDTO("202101055@projecao.edu.br", "Emilly Letícia Cordeiro");
        var professor = new EnvolvidoDTO("rebeca.lopes@projecao.br", "Rebeca Lopes Silva");
        var secretaria = new EnvolvidoDTO("leticia.alves@projecao.br", "Letícia Alves Martins");
        var assistido = new EnvolvidoDTO("028.283.199-43", "Jonathan Alves");
        Map<String, EnvolvidoDTO> envolvidos = new HashMap<>(
                Map.of(
                        "estagiario", estagiario,
                        "professor", professor,
                        "secretaria", secretaria,
                        "assistido", assistido));

        // Para o atributo "ficha" (Ficha Civil)
        var enderecoParteContraria = new Endereco("Rua das Flores", "São Paulo", "123", "Apartamento 101", "01234-567", "São Paulo");
        var parteContraria = new ParteContraria("Alberto Gomes Pereira", "Padeiro", "4.223.124-5", "948.234.153-23", "alberto.gomes@example.com", enderecoParteContraria, "(61) 98392-2934");
        var fichaCivil = new FichaCivil(null, false, new ArrayList<>(), parteContraria, null);

        // Para o atributo "ficha" (Ficha Trabalhista)
        var reclamado = new Reclamado();
        var relacaoEmpregaticia = new RelacaoEmpregaticia();
        var docDepositadosNpj= new DocumentosDepositadosNpj();
        var fichaTrabalhista = new FichaTrabalhista(null, false, new ArrayList<>(), reclamado, relacaoEmpregaticia, docDepositadosNpj, null);

        return List.of(
                new AtendimentoCivil("ATE00052", "Processo ativo", "Civil", null, historico, envolvidos, fichaCivil),
                new AtendimentoTrabalhista("ATE00071", "Aguardando documentos", "Trabalhista", null, historico, envolvidos, fichaTrabalhista)
        );
    }

    public static Atendimento seedWithOneAtendimento() {
        // Para o atributo "historico"
        var usuario = new Atendimento.EntradaHistorico.UsuarioMin("leticia.alves@projecao.br", "Letícia Alves Martins", "PROFESSOR");
        var entrada = new Atendimento.EntradaHistorico(null, "Elaborada a petição inicial", null, null, usuario);
        List<Atendimento.EntradaHistorico> historico = new ArrayList<>();
        historico.add(entrada);

        // Para o atributo "envolvidos"
        var estagiario = new EnvolvidoDTO("202101055@projecao.edu.br", "Emilly Letícia Cordeiro");
        var professor = new EnvolvidoDTO("rebeca.lopes@projecao.br", "Rebeca Lopes Silva");
        var secretaria = new EnvolvidoDTO("leticia.alves@projecao.br", "Letícia Alves Martins");
        var assistido = new EnvolvidoDTO("028.283.199-43", "Jonathan Alves");
        Map<String, EnvolvidoDTO> envolvidos = new HashMap<>(
                Map.of(
                        "estagiario", estagiario,
                        "professor", professor,
                        "secretaria", secretaria,
                        "assistido", assistido));

        // Para o atributo "ficha" (Ficha Trabalhista)
        var reclamado = new Reclamado();
        var relacaoEmpregaticia = new RelacaoEmpregaticia();
        var docDepositadosNpj= new DocumentosDepositadosNpj();
        var fichaTrabalhista = new FichaTrabalhista(null, false, new ArrayList<>(), reclamado, relacaoEmpregaticia, docDepositadosNpj, null);


        return new AtendimentoTrabalhista("ATE00071", "Aguardando documentos", "Trabalhista", null, historico, envolvidos, fichaTrabalhista);
    }
}
