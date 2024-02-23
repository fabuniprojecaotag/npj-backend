package app.web.gprojuridico.service.utils;

import app.web.gprojuridico.exception.ResourceNotFoundException;
import com.google.cloud.firestore.DocumentSnapshot;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
    public static Map<String, Object> convertUsingReflection(Object object) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = object.getClass().getDeclaredFields();

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
