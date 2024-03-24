package com.uniprojecao.fabrica.gprojuridico.interfaces;

import com.uniprojecao.fabrica.gprojuridico.aggregators.ProcessoAggregator;
import org.junit.jupiter.params.aggregator.AggregateWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@AggregateWith(ProcessoAggregator.class)
public @interface CsvToProcesso {
}
