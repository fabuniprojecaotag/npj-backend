package com.uniprojecao.fabrica.gprojuridico.dto;

import com.uniprojecao.fabrica.gprojuridico.domains.enums.FilterType;

public record QueryFilter(String field, String value, FilterType filterType) {}
