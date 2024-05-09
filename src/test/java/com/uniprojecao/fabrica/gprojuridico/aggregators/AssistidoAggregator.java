package com.uniprojecao.fabrica.gprojuridico.aggregators;

import com.uniprojecao.fabrica.gprojuridico.domains.assistido.Assistido;
import com.uniprojecao.fabrica.gprojuridico.domains.assistido.AssistidoCivil;
import com.uniprojecao.fabrica.gprojuridico.domains.assistido.AssistidoTrabalhista;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;

import java.util.HashMap;

public class AssistidoAggregator implements ArgumentsAggregator {
    @Override
    public Object aggregateArguments(
            ArgumentsAccessor accessor,
            ParameterContext context
    ) {
        /* Usando apenas dois campos (logradouro e número) da Entidade Endereço, é tido que:
        *
        * A classe abstrata Assistido possui 12 atributos e 14 campos,
        * A classe AssistidoCivil possui 15 atributos e 17 campos,
        * A classe AssistidoTrabalhista possui 15 atributos e 19 campos.
        * */
        if (accessor.toList().size() == 17) {
            var ac = new AssistidoCivil();
            ac.setNome(accessor.getString(0));
            ac.setRg(accessor.getString(1));
            ac.setCpf(accessor.getString(2));
            ac.setNacionalidade(accessor.getString(3));
            ac.setEscolaridade(accessor.getString(4));
            ac.setEstadoCivil(accessor.getString(5));
            ac.setProfissao(accessor.getString(6));
            ac.setTelefone(accessor.getString(7));
            ac.setEmail(accessor.getString(8));
            ac.setFiliacao(new Assistido.Filiacao(accessor.getString(9), accessor.getString(10)));
            ac.setRemuneracao(accessor.getString(11));

//            ac.setEndereco(new Endereco(accessor.getString(12), accessor.getString(13)));
            ac.setEndereco(new HashMap<>());

            ac.setNaturalidade(accessor.getString(14));
            ac.setDataNascimento(accessor.getString(15));
            ac.setDependentes(accessor.getInteger(16));

            return ac;
        } else if (accessor.toList().size() == 19) {
            var at = new AssistidoTrabalhista();
            at.setNome(accessor.getString(0));
            at.setRg(accessor.getString(1));
            at.setCpf(accessor.getString(2));
            at.setNacionalidade(accessor.getString(3));
            at.setEscolaridade(accessor.getString(4));
            at.setEstadoCivil(accessor.getString(5));
            at.setProfissao(accessor.getString(6));
            at.setTelefone(accessor.getString(7));
            at.setEmail(accessor.getString(8));
            at.setFiliacao(new Assistido.Filiacao(accessor.getString(9), accessor.getString(10)));
            at.setRemuneracao(accessor.getString(11));
            at.setEndereco(new HashMap<>());

            at.setCtps(new AssistidoTrabalhista.Ctps(accessor.getString(14), accessor.getString(15), accessor.getString(16)));
            at.setPis(accessor.getString(17));
            at.setEmpregadoAtualmente(accessor.getBoolean(18));

            return at;
        }

        return null;
    }
}
