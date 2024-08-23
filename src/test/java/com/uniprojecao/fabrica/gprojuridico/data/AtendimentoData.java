package com.uniprojecao.fabrica.gprojuridico.data;

import com.uniprojecao.fabrica.gprojuridico.models.Endereco;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.*;
import com.uniprojecao.fabrica.gprojuridico.models.Envolvido;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AtendimentoData {
    public static List<Atendimento> seedWithAtendimento() {

        // Para o atributo "envolvidos"
        var estagiario = new Envolvido("202101055@projecao.edu.br", "Emilly Letícia Cordeiro");
        var professor = new Envolvido("rebeca.lopes@projecao.br", "Rebeca Lopes Silva");
        var secretaria = new Envolvido("leticia.alves@projecao.br", "Letícia Alves Martins");
        var assistido = new Envolvido("288.610.170-29", "Cleyton Pina Auzier");
        Map<String, Envolvido> envolvidos = new HashMap<>(
                Map.of(
                        "estagiario", estagiario,
                        "professor", professor,
                        "secretaria", secretaria,
                        "assistido", assistido));

        // Para o atributo "ficha" (Ficha Civil)
        var enderecoParteContraria = new Endereco("Rua das Flores", "São Paulo", "123", "Apartamento 101", "01234-567", "São Paulo");
        var parteContraria = new ParteContraria("Alberto Gomes Pereira", "Padeiro", "4.223.124-5", "948.234.153-23", "alberto.gomes@example.com", enderecoParteContraria, "(61) 98392-2934", "Nada a constar");
        var fichaCivil = new FichaCivil(null, false, new ArrayList<>(), parteContraria, "Ação de guarda");

        // Para o atributo "ficha" (Ficha Trabalhista)
        var reclamado = new Reclamado();
        var relacaoEmpregaticia = new RelacaoEmpregaticia();
        var docDepositadosNpj= new DocumentosDepositadosNpj();
        var fichaTrabalhista = new FichaTrabalhista(null, false, "Reclamação trabalhista", new ArrayList<>(), reclamado, relacaoEmpregaticia, docDepositadosNpj, null);

        return List.of(
                new AtendimentoCivil("ATE00052", "Processo ativo", "Civil", null, null, envolvidos, fichaCivil),
                new AtendimentoTrabalhista("ATE00071", "Aguardando documentos", "Trabalhista", null, null, envolvidos, fichaTrabalhista)
        );
    }

    public static Atendimento seedWithOneAtendimento() {
        // Para o atributo "envolvidos"
        var estagiario = new Envolvido("202101055@projecao.edu.br", "Emilly Letícia Cordeiro");
        var professor = new Envolvido("rebeca.lopes@projecao.br", "Rebeca Lopes Silva");
        var secretaria = new Envolvido("leticia.alves@projecao.br", "Letícia Alves Martins");
        var assistido = new Envolvido("288.610.170-29", "Cleyton Pina Auzier");
        Map<String, Envolvido> envolvidos = new HashMap<>(
                Map.of(
                        "estagiario", estagiario,
                        "professor", professor,
                        "secretaria", secretaria,
                        "assistido", assistido));

        // Para o atributo "ficha" (Ficha Trabalhista)
        var reclamado = new Reclamado();
        var relacaoEmpregaticia = new RelacaoEmpregaticia();
        var docDepositadosNpj= new DocumentosDepositadosNpj();
        var fichaTrabalhista = new FichaTrabalhista(null, false, "Reclamação trabalhista", new ArrayList<>(), reclamado, relacaoEmpregaticia, docDepositadosNpj, null);


        return new AtendimentoTrabalhista("ATE00071", "Aguardando documentos", "Trabalhista", null, null, envolvidos, fichaTrabalhista);
    }
}
