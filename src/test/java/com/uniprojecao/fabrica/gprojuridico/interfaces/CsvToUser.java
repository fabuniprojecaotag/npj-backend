package com.uniprojecao.fabrica.gprojuridico.interfaces;

import com.uniprojecao.fabrica.gprojuridico.aggregators.UsuarioAggregator;
import org.junit.jupiter.params.aggregator.AggregateWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@AggregateWith(UsuarioAggregator.class)
public @interface CsvToUser {
}
