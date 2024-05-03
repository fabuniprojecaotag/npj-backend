package com.uniprojecao.fabrica.gprojuridico.aggregators;

import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Estagiario;
import com.uniprojecao.fabrica.gprojuridico.domains.usuario.SupervisorMin;
import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Usuario;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;

public class UsuarioAggregator implements ArgumentsAggregator {
    @Override
    public Object aggregateArguments(
            ArgumentsAccessor accessor,
            ParameterContext context
    ) {
        if (accessor.toList().size() == 11) {
            var e = new Estagiario();
            e.setId((String) accessor.get(0));
            e.setEmail((String) accessor.get(1));
            e.setNome((String) accessor.get(2));
            e.setCpf((String) accessor.get(3));
            e.setUnidadeInstitucional((String) accessor.get(4));
            e.setSenha((String) accessor.get(5));
            e.setStatus(Boolean.valueOf(accessor.get(6).toString()));
            e.setRole((String) accessor.get(7));

            e.setMatricula((String) accessor.get(8));
            e.setSemestre((String) accessor.get(9));
            e.setSupervisor(new SupervisorMin((String) accessor.get(10), (String) accessor.get(11)));

            return e;
        }

        return new Usuario(
                accessor.getString(0),
                accessor.getString(1),
                accessor.getString(2),
                accessor.getString(3),
                accessor.getString(4),
                accessor.getString(5),
                accessor.getBoolean(6),
                accessor.getString(7));
    }
}
