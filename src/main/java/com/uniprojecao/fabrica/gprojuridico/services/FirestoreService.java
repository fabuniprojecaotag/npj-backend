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
import com.uniprojecao.fabrica.gprojuridico.services.exceptions.NullPropertyException;
import jakarta.validation.*;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.AssistidoUtils.snapshotToAssistido;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.AtendimentoUtils.snapshotToAtendimento;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.*;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.UsuarioUtils.snapshotToUsuario;

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

        return switch (collectionName) {
            case ASSISTIDOS_COLLECTION ->
                    insertSpecifiedData(body, Assistido.class, new AssistidoRepository());
            case ATENDIMENTOS_COLLECTION ->
                    insertSpecifiedData(body, Atendimento.class, new AtendimentoService());
            case MEDIDAS_JURIDICAS_COLLECTION ->
                    insertSpecifiedData(body, MedidaJuridicaModel.class, new MedidaJuridicaService());
            case PROCESSOS_COLLECTION ->
                    insertSpecifiedData(body, Processo.class, new ProcessoRepository());
            case USUARIOS_COLLECTION ->
                    insertSpecifiedData(body, Usuario.class, new UsuarioService());
            default -> throw new RuntimeException("Collection name invalid. Checks if the collection exists.");
        };
    }

    private static Object insertSpecifiedData(
            Object body,
            Class<?> type,
            Object serviceInstance
    )
            throws Exception
    {
        var data = convertObject(body, type);
        validateDataConstraints(data);

        if (serviceInstance == null) {
            throw new IllegalArgumentException("Service instance must not be null");
        }

        Method insertMethod = serviceInstance.getClass().getMethod("insert", type);

        return insertMethod.invoke(serviceInstance, data);
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
                childClass = (Class<T>) childClass.getSuperclass();
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
                            field.set(instance, getAdjustedValue(isInteger, value));
                        } else {
                            field.set(instance, getAdjustedValue(isInteger, value));
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

    private static Object getAdjustedValue(boolean isString, Object value) {
        return isString ? value.toString() : value;
    }

    private static Class<?> identifyChildClass(String className, String classType) {
        String civil = "Civil";
        String trabalhista = "Trabalhista";
        var map = switch (className) {
            case "Atendimento" -> Map.of(
                    civil, AtendimentoCivil.class,
                    trabalhista, AtendimentoTrabalhista.class
            );
            case "Assistido" -> Map.of(
                    civil, AssistidoCivil.class,
                    trabalhista, AssistidoTrabalhista.class
            );
            case "Ficha" -> Map.of(
                    civil, FichaCivil.class,
                    trabalhista, FichaTrabalhista.class
            );
            case "Usuario" -> Map.of(
                    "Usuario", Usuario.class,
                    "Estagiario", Estagiario.class
            );
            default -> throw new IllegalStateException("Unexpected value: " + className);
        };

        for (var entry : map.entrySet()) {
            if (classType.equals(entry.getKey())) return entry.getValue();
        }
        return null;
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

    public Object updateDocument(UpdateBodyDTO payload) throws Exception {
        var collectionName = payload.collectionName();
        var body = payload.body();
        var model = payload.model();
        var id = payload.id();

        return switch (collectionName) {
            case ASSISTIDOS_COLLECTION -> {
                var className = Assistido.class.getSimpleName();
                var childClasses = List.of(
                        AssistidoCivil.class.getSimpleName().substring(className.length()),
                        AssistidoTrabalhista.class.getSimpleName().substring(className.length())
                );
                checkModelProperty(model, className, childClasses);
                yield updateSpecifiedData(id, body, model, new AssistidoRepository());
            }
            case ATENDIMENTOS_COLLECTION -> {
                var className = Atendimento.class.getSimpleName();
                var childClasses = List.of(
                        AtendimentoCivil.class.getSimpleName().substring(className.length()),
                        AtendimentoTrabalhista.class.getSimpleName().substring(className.length())
                );
                checkModelProperty(model, className, childClasses);
                yield updateSpecifiedData(id, body, model, new AtendimentoRepository());
            }
            case MEDIDAS_JURIDICAS_COLLECTION -> updateSpecifiedData(id, body, null, new MedidaJuridicaRepository());
            case PROCESSOS_COLLECTION -> updateSpecifiedData(id, body, null, new ProcessoRepository());
            case USUARIOS_COLLECTION -> {
                var className = Usuario.class.getSimpleName();
                var childClasses = List.of(
                        Usuario.class.getSimpleName(),
                        Estagiario.class.getSimpleName()
                );
                checkModelProperty(model, className, childClasses);
                yield updateSpecifiedData(id, body, model, new UsuarioService());
            }
            default -> throw new RuntimeException("Collection name invalid. Checks if the collection exists.");
        };
    }

    private static void checkModelProperty(String modelProperty, String classModel, List<String> allowedTypes) {
        if (modelProperty != null) {
            var validModel = allowedTypes.contains(modelProperty);
            if (!validModel)
                throw new InvalidModelPropertyException(modelProperty, classModel, allowedTypes);
        } else {
            throw new NullPropertyException("model", classModel);
        }
    }

    private static Object updateSpecifiedData(
            String id,
            Object body,
            String model,
            Object serviceInstance
    )
            throws Exception
    {
        if (serviceInstance == null) {
            throw new IllegalArgumentException("Service instance must not be null");
        }

        List<Class<?>> paramTypesList;
        Object[] args;

        if (model == null) {
            paramTypesList = List.of(String.class, Map.class);
            args = List.of(id, body).toArray();
        } else {
            paramTypesList = List.of(String.class, Map.class, String.class);
            args = List.of(id, body, model).toArray();
        }

        Class<?>[] paramTypesArray = paramTypesList.toArray(new Class<?>[0]);

        Method updateMethod = serviceInstance.getClass().getMethod("update", paramTypesArray);

        return updateMethod.invoke(serviceInstance, args);
    }
}
