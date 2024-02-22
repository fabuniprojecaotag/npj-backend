package app.web.gprojuridico.service.utils;

import app.web.gprojuridico.exception.ResourceNotFoundException;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;

import java.lang.reflect.Field;
import java.util.List;

public class Utils {
    public static List<String> getAllFieldNames(List<String> fields, Class<?> type) {
        for (Field field: type.getDeclaredFields()) {
            fields.add(field.getName());
        }
        return fields;
    }

    public static void verifySnapshotToDeleteObject(DocumentSnapshot snapshot, DocumentReference document) {
        /*
         * Existe um erro no método .get() do DocumentSnapshot, pois um documento
         * que não existe no Firestore é, de alguma forma, encontrado e retornado
         * com campos null. Por isso, faz-se necessário essa condicional abaixo.
         */
        if (snapshot.exists()) {
            document.delete();
        } else {
            throw new ResourceNotFoundException();
        }
    }
}
