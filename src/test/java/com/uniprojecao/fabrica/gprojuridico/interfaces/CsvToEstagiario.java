package com.uniprojecao.fabrica.gprojuridico.interfaces;

import com.uniprojecao.fabrica.gprojuridico.EstagiarioAggregator;
import com.uniprojecao.fabrica.gprojuridico.UsuarioAggregator;
import org.junit.jupiter.params.aggregator.AggregateWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@AggregateWith(EstagiarioAggregator.class)
public @interface CsvToEstagiario {
}
