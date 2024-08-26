package com.uniprojecao.fabrica.gprojuridico.services;

import com.google.cloud.firestore.Filter;
import com.uniprojecao.fabrica.gprojuridico.services.exceptions.InvalidFilterException;
import jakarta.annotation.Nullable;

import static com.google.cloud.firestore.Filter.*;

public class QueryFilterService {

    public static Filter getFilter(String field, @Nullable String operator, String value) {
        boolean isInvalidFilter = field.isEmpty() && value.isEmpty();

        if (isInvalidFilter) throw new InvalidFilterException();

        var condition = operator != null ?
                FilterCondition.valueOf(operator.toUpperCase()) :
                FilterCondition.EQUAL;

        return switch (condition) {
            case EQUAL -> equalTo(field, value);
            case GREATER_THAN -> greaterThan(field, value);
            case GREATER_THAN_OR_EQUAL -> greaterThanOrEqualTo(field, value);
            case LESS_THAN -> lessThan(field, value);
            case LESS_THAN_OR_EQUAL -> lessThanOrEqualTo(field, value);
            case NOT_EQUAL -> notEqualTo(field, value);
        };
    }

    private enum FilterCondition {
        EQUAL, GREATER_THAN, GREATER_THAN_OR_EQUAL, LESS_THAN, LESS_THAN_OR_EQUAL, NOT_EQUAL
    }
}
