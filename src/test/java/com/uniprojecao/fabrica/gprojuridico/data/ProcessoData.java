package com.uniprojecao.fabrica.gprojuridico.data;

import com.uniprojecao.fabrica.gprojuridico.models.processo.Processo;

import java.util.List;

public class ProcessoData {
    public static List<Processo> seedWithProcesso() {
        return List.of(
                new Processo("0001175-18.2013.5.05.0551", "Cobrança de contribuição sindical", "31/01/2013", "1ª vara do trabalho do Distrito Federal", "Justiça do Trabalho - 2ª região", "ATE00041", "Arquivado", null),
                new Processo("8024623-84.2024.6.24.2940", "Indenização por danos morais", "02/02/2024", "2ª vara cível do Distrito Federal", "Tribunal de Justiça", "ATE00052", "Ativo", null),
                new Processo("0817842-14.2024.0.00.7728", "Divórcio direto litigioso", "08/03/2024", "1ª vara de de família e sucessões", "Defensoria Pública", "ATE00077", "Ativo", null)
        );
    }

    public static Processo seedWithOneProcesso() {
        return new Processo("0001175-18.2013.5.05.0551", "Cobrança de contribuição sindical", "31/01/2013", "1ª vara do trabalho do Distrito Federal", "Justiça do Trabalho - 2ª região", "ATE00041", "Arquivado", null);
    }
}
