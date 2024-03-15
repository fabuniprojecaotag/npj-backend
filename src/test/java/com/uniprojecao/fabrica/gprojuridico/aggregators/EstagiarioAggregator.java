package com.uniprojecao.fabrica.gprojuridico.aggregators;

import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Estagiario;
import com.uniprojecao.fabrica.gprojuridico.domains.usuario.SupervisorMin;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;

public class EstagiarioAggregator implements ArgumentsAggregator {
    @Override
    public Object aggregateArguments(
            ArgumentsAccessor accessor,
            ParameterContext context
    ) {
        var e = new Estagiario();
        e.setEmail((String) accessor.get(0));
        e.setNome((String) accessor.get(1));
        e.setCpf((String) accessor.get(2));
        e.setUnidadeInstitucional((String) accessor.get(3));
        e.setSenha((String) accessor.get(4));
        e.setStatus(Boolean.valueOf(accessor.get(5).toString()));
        e.setRole((String) accessor.get(6));
        e.setMatricula((String) accessor.get(7));
        e.setSemestre((String) accessor.get(8));
        e.setSupervisor(new SupervisorMin((String) accessor.get(9), (String) accessor.get(10)));
        
        return e;
    }
}
