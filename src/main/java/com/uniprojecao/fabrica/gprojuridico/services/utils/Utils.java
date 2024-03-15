package com.uniprojecao.fabrica.gprojuridico.services.utils;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.google.cloud.firestore.Filter;
import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.dto.usuario.UsuarioDTO;
import com.uniprojecao.fabrica.gprojuridico.services.exceptions.ResourceNotFoundException;
import com.google.cloud.firestore.DocumentSnapshot;
import jakarta.annotation.Nullable;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.cloud.firestore.Filter.*;

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
}
