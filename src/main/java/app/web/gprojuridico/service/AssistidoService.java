package app.web.gprojuridico.service;

import app.web.gprojuridico.exception.ResourceNotFoundException;
import app.web.gprojuridico.model.AssistidoCivil;
import app.web.gprojuridico.model.AssistidoFull;
import app.web.gprojuridico.model.AssistidoTrabalhista;
import app.web.gprojuridico.model.Ctps;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class AssistidoService {

    CollectionReference collection = FirestoreClient.getFirestore().collection("assistidos");

    public Map<String, Object> insert(Object data) {

        try {
            System.out.println("\nPayload recebido: " + data.toString());
            Object verifiedData = verifyDataToInsertAssistido(data);
            DocumentReference result = collection.add(verifiedData).get();
            String assistidoId = result.getId();

            System.out.println("\nAssistido adicionado. ID: " + assistidoId);

            Map<String, Object> objectMap = convertUsingReflection(verifiedData);
            objectMap.put("id", assistidoId);

            return objectMap;
        } catch (InterruptedException | ExecutionException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Object> findAll() {

        try {
            QuerySnapshot query = collection.get().get();
            List<Object> list = new ArrayList<>();
            for (QueryDocumentSnapshot document : query) {
                Object object = convertSnapshotToCorrespondingModel(document);
                list.add(object);
            }

            return list;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public Object findById(String id) {
        
        try {
            DocumentReference document = collection.document(id);
            DocumentSnapshot snapshot = document.get().get();

            return verifySnapshotToFindObjectById(snapshot);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean update(String id,  Map<String, Object> data) {

        try {
            DocumentReference document = collection.document(id);
            DocumentSnapshot snapshot = document.get().get();
            verifySnapshotToUpdateObject(snapshot, document, data);
            return true;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean delete(String id) {

        try {
            DocumentReference document = collection.document(id);
            DocumentSnapshot snapshot = document.get().get();
            verifySnapshotToDeleteObject(snapshot, document);
            return true;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private Boolean objectHasProperty(Object obj, String propertyName){
        List<Field> properties = getAllFields(new ArrayList<>(), obj.getClass());
        for (Field field : properties){
            if (field.getName().equalsIgnoreCase(propertyName)){
                return true;
            }
        }
        return false;
    }

    private static List<Field> getAllFields(List<Field> fields, Class<?> type) {
        for (Field field: type.getDeclaredFields()) {
            fields.add(field);
        }
        return fields;
    }

    private static List<String> getAllFieldNames(List<String> fields, Class<?> type) {
        for (Field field: type.getDeclaredFields()) {
            fields.add(field.getName());
        }
        return fields;
    }

    private static Boolean isInstanceOf(Object obj, Class<?> type) {
        List<Field> properties = getAllFields(new ArrayList<>(), obj.getClass());
        for (Field field : properties){
            String name = field.getType().getSimpleName();
            String classType = type.getSimpleName();
            if (classType.equals(name)){
                return true;
            }
        }
        return false;
    }

    private Map<String, Object> convertUsingReflection(Object object) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field: fields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(object));
        }

        return map;
    }

    private Object verifyDataToInsertAssistido(Object data) {

        Boolean naturalidade = objectHasProperty(data, "naturalidade");
        Boolean dataNascimento = objectHasProperty(data, "dataNascimento");
        Boolean dependentes = objectHasProperty(data, "dependentes");

        Boolean ctps = isInstanceOf(data, Ctps.class);
        Boolean pis = objectHasProperty(data, "pis");
        Boolean empregadoAtualmente = objectHasProperty(data, "empregadoAtualmente");

        boolean dadosFCivil = naturalidade && dataNascimento && dependentes;
        boolean dadosFTrabalhista = ctps && pis && empregadoAtualmente;

        if (dadosFCivil || dadosFTrabalhista) {
            return data;
        } else {
            throw new RuntimeException("O objeto passado não corresponde à nenhuma instância de Assistido.");
        }
    }

    private Map<String, Object> verifyDataToUpdateAssistido(Map<String, Object> data) {
        List<String> assistidoFields = getAllFieldNames(new ArrayList<>(), AssistidoFull.class);

        for (String key : data.keySet()) {
            if (!assistidoFields.contains(key)) {
                throw new RuntimeException("O campo " + key + "de payload recebido não existe em Assistido");
            }
        }

        return data;
    }

    private Object verifySnapshotToFindObjectById(DocumentSnapshot snapshot) {
        /*
         * Existe um erro no método .get() do DocumentSnapshot, pois um documento
         * que não existe no Firestore é, de alguma forma, encontrado e retornado
         * com campos null. Por isso, faz-se necessário essa condicional abaixo.
         */
        if (snapshot.exists()) {
            return convertSnapshotToCorrespondingModel(snapshot);
        } else {
            throw new ResourceNotFoundException();
        }
    }

    private void verifySnapshotToUpdateObject(DocumentSnapshot snapshot, DocumentReference document, Map<String, Object> data) {
        /*
         * Existe um erro no método .get() do DocumentSnapshot, pois um documento
         * que não existe no Firestore é, de alguma forma, encontrado e retornado
         * com campos null. Por isso, faz-se necessário essa condicional abaixo.
         */
        if (snapshot.exists()) {
            document.update(verifyDataToUpdateAssistido(data));
        } else {
            throw new ResourceNotFoundException();
        }
    }

    private void verifySnapshotToDeleteObject(DocumentSnapshot snapshot, DocumentReference document) {
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

    /**
     * Converts the passed snapshot to the corresponding model through the
     * relationship that the person assisted has with the service(s).
     */
    private Object convertSnapshotToCorrespondingModel(DocumentSnapshot snapshot) {

        Boolean dadosFCivil = snapshot.contains("naturalidade") && snapshot.contains("dataNascimento") && snapshot.contains("dependentes");
        Boolean dadosFTrabalhista = snapshot.contains("ctps") && snapshot.contains("pis") && snapshot.contains("empregadoAtualmente");

        // primeiro, se o snapshot passado possui dados exclusivos de ambas as fichas (civil e trabalhista) para um assistido...
        if (dadosFCivil && dadosFTrabalhista) {
            // então o snapshot é uma instância de AssistidoFull.
            return snapshot.toObject(AssistidoFull.class);
        }
        // ou se o snapshot passado possui dados exclusivos da ficha civil para um assistido...
        else if (dadosFCivil) {
            // então o snapshot é uma instância de AssistidoCivil.
            return snapshot.toObject(AssistidoCivil.class);
        }
        // ou se o snapshot passado possui dados exclusivos da ficha trabalhista para um assistido...
        else if (dadosFTrabalhista) {
            // então o snapshot é uma instância de AssistidoTrabalhista.
            return snapshot.toObject(AssistidoTrabalhista.class);
        } else {
            // senão, caso não haja compatatibilidade do snapshot com as três condicionais acima, lance então uma exceção.
            throw new ResourceNotFoundException();
        }
    }
}

