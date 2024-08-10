package com.uniprojecao.fabrica.gprojuridico.dto;

import com.uniprojecao.fabrica.gprojuridico.enums.FilterType;

public record QueryFilter(String field, String value, FilterType filterType) {}
