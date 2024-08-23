package com.uniprojecao.fabrica.gprojuridico.aggregators;

import com.uniprojecao.fabrica.gprojuridico.models.Endereco;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.Envolvido;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.*;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AtendimentoAggregator implements ArgumentsAggregator {
    @Override
    public Object aggregateArguments(
            ArgumentsAccessor accessor,
            ParameterContext context
    ) {
        int accessorSize = accessor.size();

        // Para o atributo "envolvidos"
        var estagiario = new Envolvido(accessor.getString(7), accessor.getString(8));
        var professor = new Envolvido(accessor.getString(9), accessor.getString(10));
        var secretaria = new Envolvido(accessor.getString(11), accessor.getString(12));
        var assistido = new Envolvido("028.283.199-43", "Jonathan Alves");
        Map<String, Envolvido> envolvidos = new HashMap<>(
                Map.of(
                        "estagiario", estagiario,
                        "professor", professor,
                        "secretaria", secretaria,
                        "assistido", assistido));

        if (accessorSize == 13) {
            var atendimento = new AtendimentoCivil();
            atendimento.setId(accessor.getString(0));
            atendimento.setStatus(accessor.getString(1));
            atendimento.setArea(accessor.getString(2));
            // instante ignorado
            atendimento.setHistorico(null);
            atendimento.setEnvolvidos(envolvidos);

            // Para o atributo "ficha"
            var parteContraria = new ParteContraria(
                    "Alberto Gomes Pereira",
                    "Padeiro",
                    "4.223.124-5",
                    "948.234.153-23",
                    "alberto.gomes@example.com",
                    new Endereco(),
                    "(61) 98392-2934",
                    null
            );
            var ficha = new FichaCivil(
                    null,
                    false,
                    new ArrayList<>(),
                    parteContraria,
                    "Ação de guarda"
            );
            atendimento.setFicha(ficha);

            return atendimento;
        }
        else if (accessorSize == 14) {

            var atendimento = new AtendimentoTrabalhista();
            atendimento.setId(accessor.getString(0));
            atendimento.setStatus(accessor.getString(1));
            atendimento.setArea(accessor.getString(2));
            // instante ignorado
            atendimento.setHistorico(null);
            atendimento.setEnvolvidos(envolvidos);

            var ficha = new FichaTrabalhista(
                    null,
                    false,
                    "Reclamação trabalhista",
                    new ArrayList<>(),
                    new Reclamado(),
                    new RelacaoEmpregaticia(),
                    new DocumentosDepositadosNpj(),
                    null
            );
            atendimento.setFicha(ficha);

            return atendimento;
        }

        return null;
    }
}
