package com.uniprojecao.fabrica.gprojuridico.services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.uniprojecao.fabrica.gprojuridico.dto.DeleteBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.InsertBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.UpdateBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.models.MedidaJuridicaModel;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.Assistido;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.AssistidoCivil;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.AssistidoTrabalhista;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.*;
import com.uniprojecao.fabrica.gprojuridico.models.processo.Processo;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Estagiario;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.repository.*;
import com.uniprojecao.fabrica.gprojuridico.services.exceptions.InvalidModelPropertyException;
import jakarta.validation.*;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import static com.uniprojecao.fabrica.gprojuridico.services.DocumentSnapshotService.*;
import static com.uniprojecao.fabrica.gprojuridico.utils.Constants.*;
import static com.uniprojecao.fabrica.gprojuridico.utils.Utils.filterValidKeys;

@Service
public class FirestoreService extends BaseRepository {

    public Map<String, Object> getDocuments(
            String collectionName,
            String startAfter,
            int pageSize,
            Filter filter
    )
            throws Exception
    {
        CollectionReference collection = firestore.collection(collectionName);
        String[] fieldNames = getFieldNames(collectionName);

        Query query = (filter != null) ?
                collection.orderBy("__name__").where(filter).select(fieldNames).limit(pageSize) :
                collection.orderBy("__name__").select(fieldNames).limit(pageSize);

        if (startAfter != null) {
            DocumentSnapshot lastDoc = firestore.collection(collectionName).document(startAfter).get().get();
            query = query.startAfter(lastDoc);
        }

        ApiFuture<QuerySnapshot> future = query.get();

        List<Object> list = new ArrayList<>();

        for (QueryDocumentSnapshot snapshot : future.get()) {
            var document = convertSnapshot(collectionName, snapshot, true);
            list.add(document);
        }

        Object lastDoc = list.get(list.size() - 1);
        int collectionSize = future.get().size();

        return generateMap(list, lastDoc, collectionSize);
    }

    private Map<String, Object> generateMap(List<Object> list, Object lastDoc, int count) {
        return Map.of(
                "list", list,
                "lastDoc", lastDoc,
                "count", count);
    }

    private String[] getFieldNames(String collection) {
        return switch (collection) {
            case ASSISTIDOS_COLLECTION ->
                    new String[]{"nome", "email", "quantidade.atendimentos", "quantidade.processos", "telefone"};
            case ATENDIMENTOS_COLLECTION -> new String[]{"area", "status", "envolvidos.assistido"};
            case MEDIDAS_JURIDICAS_COLLECTION -> new String[]{"area", "descricao"};
            case PROCESSOS_COLLECTION ->
                    new String[]{"numero", "atendimentoId", "nome", "dataDistribuicao", "vara", "forum", "status"};
            case USUARIOS_COLLECTION -> new String[]{"nome", "email", "role", "status", "matricula", "semestre"};
            default -> throw new RuntimeException("Collection name invalid. Checks if the collection exists.");
        };
    }

    private Object convertSnapshot(String collection, DocumentSnapshot snapshot, Boolean returnMinDTO) {
        return switch (collection) {
            case ASSISTIDOS_COLLECTION -> snapshotToAssistido(snapshot, returnMinDTO, false);
            case ATENDIMENTOS_COLLECTION -> snapshotToAtendimento(snapshot, returnMinDTO, false, false);
            case MEDIDAS_JURIDICAS_COLLECTION -> snapshot.toObject(MedidaJuridicaModel.class);
            case PROCESSOS_COLLECTION -> snapshot.toObject(Processo.class);
            case USUARIOS_COLLECTION -> snapshotToUsuario(snapshot, returnMinDTO, false);
            default -> throw new RuntimeException("Collection name invalid. Checks if the collection exists.");
        };
    }

    public void deleteDocuments(DeleteBodyDTO payload) {
        var collectionName = payload.collectionName();
        var ids = payload.ids();

        for (var id : ids) {
            firestore.collection(collectionName).document(id).delete();
        }
    }

    public Object getDocumentById(String collectionName, String id) throws Exception {
        DocumentReference document = firestore.collection(collectionName).document(id);
        DocumentSnapshot snapshot = document.get().get();
        if (!snapshot.exists()) return null;
        return convertSnapshot(collectionName, snapshot, false);
    }

    public Object insertDocument(InsertBodyDTO payload) throws Exception {

        var collectionName = payload.collectionName();
        var body = payload.body();
        Object data;

        switch (collectionName) {
            case ASSISTIDOS_COLLECTION:
                data = getObject(body, Assistido.class);
                return new AssistidoRepository().insert((Assistido) data);
            case ATENDIMENTOS_COLLECTION:
                data = getObject(body, Atendimento.class);
                return new AtendimentoService().insert((Atendimento) data);
            case MEDIDAS_JURIDICAS_COLLECTION:
                data = getObject(body, MedidaJuridicaModel.class);
                return new MedidaJuridicaService().insert((MedidaJuridicaModel) data);
            case PROCESSOS_COLLECTION:
                data = getObject(body, Processo.class);
                return new ProcessoRepository().insert((Processo) data);
            case USUARIOS_COLLECTION:
                data = getObject(body, Usuario.class);
                return new UsuarioService().insert((Usuario) data);
            default:
                throw new RuntimeException("Collection name invalid. Checks if the collection exists.");
        }
    }

    private static Object getObject(Object body, Class<?> type) throws Exception {
        var data = convertObject(body, type);
        validateDataConstraints(data);
        return data;
    }

    public static <T> T convertObject(Object object, Class<T> destinyClazz) throws Exception {
        if (destinyClazz.isInstance(object)) {
            return destinyClazz.cast(object);
        }

        if (object instanceof LinkedHashMap<?, ?> map) {

            Class<?> childClass = destinyClazz;
            if (map.containsKey("@type")) {
                String childClassType = (String) map.get("@type");
                childClass = identifyChildClass(destinyClazz.getSimpleName(), childClassType);
                if (childClass == null) {
                    throw new IllegalArgumentException("Unrecognized child class type.");
                }
            }

            T instance = (T) childClass.getDeclaredConstructor().newInstance();


            Map<String, Field> fieldsMap = new HashMap<>();
            while (childClass != null && childClass != Object.class) {
                for (Field field : childClass.getDeclaredFields()) {
                    if (!fieldsMap.containsKey(field.getName())) {
                        fieldsMap.put(field.getName(), field);
                    }
                }
                childClass = childClass.getSuperclass();
            }


            for (Map.Entry<?, ?> entry : map.entrySet()) {
                String key = (String) entry.getKey();
                Object value = entry.getValue();

                try {
                    if (fieldsMap.containsKey(key)) {
                        Field field = fieldsMap.get(key);
                        field.setAccessible(true);
                        boolean isInteger = field.getType() == String.class;

                        if (value instanceof LinkedHashMap) {
                            // Recursivamente converte o LinkedHashMap em uma instância da classe apropriada.
                            var superClass = field.getType().getSuperclass();
                            int mod;
                            mod = Objects.requireNonNullElseGet(superClass, field::getType).getModifiers();
                            boolean isAbstract = Modifier.isAbstract(mod);

                            Class<?> fieldType = isAbstract ? field.getType().getSuperclass() : field.getType();
                            if (fieldType != null)
                                value = convertObject(value, fieldType);
                            field.set(instance, isInteger ? value.toString() : value);
                        } else {
                            field.set(instance, isInteger ? value.toString() : value);
                        }
                    }

                } catch (NoSuchFieldException e) {
                    // Field not found, continue without fail.
                }
            }

            return instance;
        }

        throw new IllegalArgumentException("Unsupported object type.");
    }

    private static Class<?> identifyChildClass(String baseClass, String classType) {
        final String CIVIL = "Civil";
        final String TRABALHISTA = "Trabalhista";

        var validTypes = switch (baseClass) {
            case "Atendimento" -> Map.of(
                    CIVIL, AtendimentoCivil.class,
                    TRABALHISTA, AtendimentoTrabalhista.class
            );
            case "Assistido" -> Map.of(
                    CIVIL, AssistidoCivil.class,
                    TRABALHISTA, AssistidoTrabalhista.class
            );
            case "Ficha" -> Map.of(
                    CIVIL, FichaCivil.class,
                    TRABALHISTA, FichaTrabalhista.class
            );
            case "Usuario" -> Map.of(
                    "Usuario", Usuario.class,
                    "Estagiario", Estagiario.class
            );
            default -> throw new IllegalStateException("Unexpected baseClass: " + baseClass);
        };

        for (var entry : validTypes.entrySet()) {
            if (classType.equals(entry.getKey())) {
                return entry.getValue();
            }
        }

        throw new IllegalStateException("Unexpected classType: " + classType);
    }

    private static void validateDataConstraints(Object data) {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();

            Set<ConstraintViolation<Object>> violations = validator.validate(data);

            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        }
    }

    public void updateDocument(UpdateBodyDTO payload) {
        var collectionName = payload.collectionName();
        var body = (Map<String, Object>) payload.body();
        var model = payload.model();
        var id = payload.id();

        Class<?> clazz;

        switch (collectionName) {
            case ASSISTIDOS_COLLECTION:
                clazz = identifyChildClass(Assistido.class.getSimpleName(), model);
                BaseRepository.update(ASSISTIDOS_COLLECTION, id, filterValidKeys(body, clazz));
                break;
            case ATENDIMENTOS_COLLECTION:
                clazz = identifyChildClass(Atendimento.class.getSimpleName(), model);
                BaseRepository.update(ATENDIMENTOS_COLLECTION, id, filterValidKeys(body, clazz));
                break;
            case MEDIDAS_JURIDICAS_COLLECTION:
                BaseRepository.update(MEDIDAS_JURIDICAS_COLLECTION, id, filterValidKeys(body, MedidaJuridicaModel.class));
                break;
            case PROCESSOS_COLLECTION:
                BaseRepository.update(PROCESSOS_COLLECTION, id, filterValidKeys(body, Processo.class));
                break;
            case USUARIOS_COLLECTION:
                identifyChildClass(Usuario.class.getSimpleName(), model);
                new UsuarioService().update(id, body, model);
                break;
            default:
                throw new RuntimeException("Collection name invalid. Checks if the collection exists.");
        }
    }
}
