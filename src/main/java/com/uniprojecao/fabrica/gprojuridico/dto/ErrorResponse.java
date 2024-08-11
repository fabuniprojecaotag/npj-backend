package com.uniprojecao.fabrica.gprojuridico.dto;

import lombok.Getter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ErrorResponse {

    private final String time;
    private final Integer status;
    private final String message;
    private final List<FieldMessage> errors = new ArrayList<>();

    public ErrorResponse(Integer status, String message) {

        Instant instant = Instant.now().with(ChronoField.NANO_OF_SECOND , 0);
        ZonedDateTime zdt = instant.atZone(ZoneId.of("America/Sao_Paulo"));

        this.time = zdt.toLocalDateTime().toString() + " UTC-03";
        this.status = status;
        this.message = message;
    }

    public void addError(String field, String message) {
        errors.removeIf(x -> x.field().equals(field));
        errors.add(new FieldMessage(field, message));
    }

    public record FieldMessage(String field, String message) {

    }
}