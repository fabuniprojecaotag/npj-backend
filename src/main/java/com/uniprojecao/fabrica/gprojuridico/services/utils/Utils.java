package com.uniprojecao.fabrica.gprojuridico.services.utils;

import com.google.cloud.firestore.Filter;
import com.uniprojecao.fabrica.gprojuridico.domains.enums.FilterType;
import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.dto.usuario.UsuarioDTO;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.cloud.firestore.Filter.*;

public class Utils {
    public static <T> Map<String, Object> convertUsingReflection(T object, Boolean useSuperClass) {
        if (object instanceof Map<?, ?>) {
            @SuppressWarnings("unchecked") // Suppress warning as it's safe due to the instanceof check
            Map<String, Object> castedMap = (Map<String, Object>) object;
            return castedMap;
        }

        Map<String, Object> map = new HashMap<>();
        Class<?> t = object.getClass();
        Field[] fields;

        if (useSuperClass) {
            fields = getAllFields(t);
        } else {
            fields = t.getDeclaredFields();
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

    // Helper method to get all fields, including those from superclasses
    private static Field[] getAllFields(Class<?> clazz) {
        Map<String, Field> fieldsMap = new HashMap<>();
        while (clazz != null && clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                if (!fieldsMap.containsKey(field.getName())) {
                    fieldsMap.put(field.getName(), field);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return fieldsMap.values().toArray(new Field[0]);
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

    public static class ModelMapper {
        private static final org.modelmapper.ModelMapper modelMapper = new org.modelmapper.ModelMapper();

        public static UsuarioDTO toDto(Usuario entity) {
            return modelMapper.map(entity, UsuarioDTO.class);
        }

        public static Usuario toEntity(UsuarioDTO dto) {
            return modelMapper.map(dto, Usuario.class);
        }


    }
}
