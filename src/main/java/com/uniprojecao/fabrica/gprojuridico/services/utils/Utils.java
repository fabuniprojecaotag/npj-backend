package com.uniprojecao.fabrica.gprojuridico.services.utils;

import com.google.cloud.firestore.Filter;
import com.uniprojecao.fabrica.gprojuridico.domains.enums.FilterType;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    public static <T> Map<String, Object> filterValidKeys(Map<String, Object> inputMap, Class<T> clazz) {
        Map<String, Object> filteredMap = new HashMap<>();

        // Gets all fields of the class passed as a parameter
        Field[] fields = clazz.getDeclaredFields();

        // Creates a Set with the names of the fields in the class passed as parameters
        Set<String> validKeys = getAllFields2(clazz).stream()
                .map(Field::getName)
                .collect(Collectors.toSet());

        // Filters the inputMap, keeping only valid keys
        for (Map.Entry<String, Object> entry : inputMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (validKeys.contains(key)) {
                if (value instanceof Map) {
                    Field field = getFieldByName(clazz, key);
                    if (field != null) {
                        Class<?> fieldType = field.getType();
                        if (!fieldType.getName().startsWith("java.lang") && !fieldType.isPrimitive()) {
                            value = filterValidKeys((Map<String, Object>) value, fieldType);
                        }
                    }
                }
                filteredMap.put(key, value);
            }
        }

        return filteredMap;
    }

    // Recursive method to get all fields of a class and its superclasses
    private static Set<Field> getAllFields2(Class<?> clazz) {
        Set<Field> fields = new HashSet<>();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                fields.add(field);
                // Checks if the field is a custom class (not primitive or String)
                if (!field.getType().isPrimitive() && !field.getType().getName().startsWith("java.lang")) {
                    fields.addAll(getAllFields2(field.getType()));
                }
            }
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    private static Field getFieldByName(Class<?> clazz, String fieldName) {
        while (clazz != null) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
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

    /**
     * Método auxiliar que converte Lista em String.
     *
     * @param list A list de strings a ser convertida em uma única String.
     * @return A lista convertida em String.
     */
    public static String listToString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s);
        }
        return sb.toString();
    }

    /**
     * Método auxiliar que converte String em Lista.
     *
     * @param string A String a ser convertida em List.
     * @return A String convertida em List.
     */
    public static List<String> stringToList(String string) {
        return new ArrayList<>(List.of(string.split("")));
    }

    /**
     * Método utilitário que otimiza a chamada de método para imprimir uma messagem no Console.
     *
     * @param message A mensagem a ser impressa.
     */
    public static void print(String message) {
        System.out.println(message);
    }
}
