package com.uniprojecao.fabrica.gprojuridico.services.utils;

import com.uniprojecao.fabrica.gprojuridico.services.exceptions.ResourceNotFoundException;
import com.google.cloud.firestore.DocumentSnapshot;
import jakarta.annotation.Nullable;

import java.lang.reflect.Field;
import java.util.*;

public class Utils {
    public static Map<String, Object> convertUsingReflection(Object object, @Nullable Boolean useSuperClass) throws IllegalAccessException {
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
            map.put(field.getName(), field.get(object));
        }

        return map;
    }

    public static List<String> getAllFieldNames(List<String> fields, Class<?> type) {
        for (Field field: type.getDeclaredFields()) {
            fields.add(field.getName());
        }
        return fields;
    }

    public static void verifySnapshotIfDocumentExists(DocumentSnapshot snapshot) {
        /*
         * Existe um erro no método .get() do DocumentSnapshot, pois um documento
         * que não existe no Firestore é, de alguma forma, encontrado e retornado
         * com campos null. Por isso, faz-se necessário essa condicional abaixo.
         */
        if (!snapshot.exists()) {
            throw new ResourceNotFoundException();
        }
    }
}
