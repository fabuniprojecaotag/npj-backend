package com.uniprojecao.fabrica.gprojuridico.services.utils;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.google.cloud.firestore.Filter;
import com.uniprojecao.fabrica.gprojuridico.domains.enums.FilterType;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.dto.usuario.UsuarioDTO;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.cloud.firestore.Filter.*;

public class Utils {
    public static Map<String, Object> convertUsingReflection(Object object, Boolean useSuperClass) {
        Map<String, Object> map = new HashMap<>();
        Class<?> t = object.getClass();
        List<Field> fields = new ArrayList<>();

        if (useSuperClass) {
            fields.addAll(List.of(t.getSuperclass().getDeclaredFields()));
            fields.addAll(List.of(t.getDeclaredFields()));
        } else {
            fields.addAll(List.of(t.getDeclaredFields()));
        }

        for (Field field: fields) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(object));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return map;
    }

    public static Filter filter(QueryFilter filter) {
        var filterType = filter.filterType();
        var field = filter.field();
        var value = filter.value();

        return switch (filterType) {
            case EQUAL -> equalTo(field, value);
            case GREATER_THAN -> greaterThan(field, value);
            case GREATER_THAN_OR_EQUAL -> greaterThanOrEqualTo(field, value);
            case LESS_THAN -> lessThan(field, value);
            case LESS_THAN_OR_EQUAL -> lessThanOrEqualTo(field, value);
            case NOT_EQUAL -> notEqualTo(field, value);
        };
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static Boolean validateText(String regex, String text) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    public static void encryptPassword(UsuarioDTO u) {
        u.setSenha(BCrypt.withDefaults().hashToString(12, u.getSenha().toCharArray()));
    }

    public static QueryFilter initFilter(String field, String filter, String value) {
        boolean useQueryParams =
                !(field.isEmpty()) &&
                        !(filter.isEmpty()) &&
                        !(value.isEmpty());

        return (useQueryParams) ? new QueryFilter(field, value, FilterType.valueOf(filter)) : null;
    }

    public static URI createUri(String id) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();
    }
}
