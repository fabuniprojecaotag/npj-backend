package com.uniprojecao.fabrica.gprojuridico;

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
        return new Usuario(
                accessor.getString(0),
                accessor.getString(1),
                accessor.getString(2),
                accessor.getString(3),
                accessor.getString(4),
                accessor.getBoolean(5),
                accessor.getString(6)
        );
    }
}
