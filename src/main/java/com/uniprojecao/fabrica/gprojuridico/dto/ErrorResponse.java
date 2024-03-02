package com.uniprojecao.fabrica.gprojuridico.dto;

import lombok.Getter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;

@Getter
public class ErrorResponse {

    private final String instant;
    private final Integer status;
    private final String error;
    private final String path;

    public ErrorResponse(Integer status, String error, String path) {

        Instant instant = Instant.now().with(ChronoField.NANO_OF_SECOND , 0);
        ZonedDateTime zdt = instant.atZone(ZoneId.of("America/Sao_Paulo"));

        this.instant = zdt.toLocalDateTime().toString() + " UTC-03";
        this.status = status;
        this.error = error;
        this.path = path;
    }
}