package com.uniprojecao.fabrica.gprojuridico.repositories;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.uniprojecao.fabrica.gprojuridico.dto.body.DeleteBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.body.UpdateBodyDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.min.*;
import com.uniprojecao.fabrica.gprojuridico.dto.vinculados.AtendimentoVinculadoDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.vinculados.ProcessoVinculadoDTO;
import com.uniprojecao.fabrica.gprojuridico.models.Envolvido;
import com.uniprojecao.fabrica.gprojuridico.models.MedidaJuridica;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.Assistido;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.AssistidoCivil;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.AssistidoFull;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.AssistidoTrabalhista;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.*;
import com.uniprojecao.fabrica.gprojuridico.models.autocomplete.AssistidoAutocomplete;
import com.uniprojecao.fabrica.gprojuridico.models.autocomplete.AtendimentoAutocomplete;
import com.uniprojecao.fabrica.gprojuridico.models.autocomplete.UsuarioAutocomplete;
import com.uniprojecao.fabrica.gprojuridico.models.processo.Processo;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Estagiario;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.services.AtendimentoService;
import com.uniprojecao.fabrica.gprojuridico.services.MedidaJuridicaService;
import com.uniprojecao.fabrica.gprojuridico.services.UsuarioService;
import jakarta.validation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.google.cloud.firestore.Query.Direction.DESCENDING;
import static com.uniprojecao.fabrica.gprojuridico.repositories.FirestoreRepository.DocumentSnapshotService.*;
import static com.uniprojecao.fabrica.gprojuridico.utils.Constants.*;
import static com.uniprojecao.fabrica.gprojuridico.utils.Utils.convertUsingReflection;
import static com.uniprojecao.fabrica.gprojuridico.utils.Utils.filterValidKeys;

@Repository
@Primary
public class FirestoreRepository {

    public static Firestore firestore;

    @Value("${spring.cloud.gcp.project-id}")
    private String projectId;

    @PostConstruct
    private void init() {
        var clazz = this.getClass().getSimpleName();
        if (clazz == FirestoreRepository.class.getSimpleName()) {
            firestore = firestore();
        }
    }

    public Firestore firestore() {
        FirestoreOptions options = FirestoreOptions.newBuilder()
                .setProjectId(projectId)
                .build();
        return options.getService();
    }

    public static void insert(String collectionName, String CustomId, Object data) {
        try {
            firestore.collection(collectionName).document(CustomId).set(data).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String findLastDocumentId(String collectionName) {
        try {
            var list = firestore
                    .collection(collectionName)
                    .orderBy(FieldPath.documentId(), DESCENDING)
                    .limit(1)
                    .get()
                    .get()
                    .getDocuments();
            return !list.isEmpty() ? list.get(0).getId() : null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void update(String collectionName, String id, Map<String, Object> data) {
        firestore.collection(collectionName).document(id).update(convertMap(data));
    }

    private static Map<String, Object> convertMap(Map<String, Object> inputMap) {
        Map<String, Object> outputMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : inputMap.entrySet()) {
            processEntry(entry.getKey(), entry.getValue(), outputMap, "");
        }
        return outputMap;
    }

    private static void processEntry(String key, Object value, Map<String, Object> outputMap, String parentKey) {
        // Se o valor é um Map, deve-se percorrer o método recursivamente para concatenar as chaves aninhadas
        if (value instanceof Map) {
            @SuppressWarnings("unchecked")
            var subMap = (Map<String, Object>) value;
            for (Map.Entry<String, Object> entry : subMap.entrySet()) {
                processEntry(entry.getKey(), entry.getValue(), outputMap, parentKey + key + ".");
            }
        } else {
            outputMap.put(parentKey + key, value); // Ex. de entrada percorrida recursivamente a ser adicionada: {"ficha.parteContraria.nome", "Mauro Silva"}
        }
    }

    public static Map<String, Object> getDocuments(
            String collectionName,
            String startAfter,
            int pageSize,
            Filter filter,
            String returnType
    )
            throws Exception
    {
        CollectionReference collection = firestore.collection(collectionName);
        String[] fieldNames = getFieldNames(collectionName, returnType);

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
            var document = convertSnapshot(collectionName, snapshot, returnType);
            list.add(document);
        }

        Object lastDoc = list.get(list.size() - 1);
        int collectionSize = future.get().size();

        return generateMap(list, lastDoc, collectionSize);
    }

    private static Map<String, Object> generateMap(List<Object> list, Object lastDoc, int count) {
        return Map.of(
                "list", list,
                "lastDoc", lastDoc,
                "count", count);
    }

    private static String[] getFieldNames(String collection, String returnType) {
        return switch (collection) {
            case ASSISTIDOS_COLLECTION -> {
                if (returnType == "min") yield new String[]{"nome", "email", "quantidade.atendimentos", "quantidade.processos", "telefone"};
                if (returnType == "autoComplete") yield new String[]{"nome"};
                throw new RuntimeException("returnType invalid. Checks if the returnType is correct.");
            }
            case ATENDIMENTOS_COLLECTION -> {
                if (returnType == "min") yield new String[]{"area", "status", "envolvidos.assistido"};
                if (returnType == "autoComplete") yield new String[]{"id"};
                if (returnType == "forAssistido") yield new String[]{"area", "status", "envolvidos.assistido", "envolvidos.estagiario", "instante"};
                throw new RuntimeException("returnType invalid. Checks if the returnType is correct.");
            }
            case MEDIDAS_JURIDICAS_COLLECTION -> new String[]{"area", "descricao"};
            case PROCESSOS_COLLECTION -> {
                if (returnType == "forAssistido") yield new String[]{"vara", "status"};
                yield new String[]{"numero", "atendimentoId", "nome", "dataDistribuicao", "vara", "forum", "status"};
            }
            case USUARIOS_COLLECTION -> {
                if (returnType == "min") yield new String[]{"nome", "email", "role", "status", "matricula", "semestre"};
                if (returnType == "autoComplete") yield new String[]{"nome", "role"};
                throw new RuntimeException("returnType invalid. Checks if the returnType is correct.");
            }

            default -> throw new RuntimeException("Collection name invalid. Checks if the collection exists.");
        };
    }

    private static Object convertSnapshot(String collection, DocumentSnapshot snapshot, String returnType) {
        return switch (collection) {
            case ASSISTIDOS_COLLECTION -> {
                    if (returnType == "min") yield snapshotToAssistido(snapshot, true, false);
                    if (returnType == "autoComplete") yield snapshotToAssistido(snapshot, false, true);
                    throw new RuntimeException("returnType invalid. Checks if the returnType is correct.");
            }
            case ATENDIMENTOS_COLLECTION -> {
                if (returnType == "min") yield snapshotToAtendimento(snapshot, true, false, false);
                if (returnType == "autoComplete") yield snapshotToAtendimento(snapshot, false, true, false);
                if (returnType == "forAssistido") yield snapshotToAtendimento(snapshot, false, false, true);
                throw new RuntimeException("returnType invalid. Checks if the returnType is correct.");
            }
            case MEDIDAS_JURIDICAS_COLLECTION -> snapshot.toObject(MedidaJuridica.class);
            case PROCESSOS_COLLECTION -> {
                if (returnType == "forAssistido") yield snapshotToProcessoVinculado(snapshot);
                yield snapshot.toObject(Processo.class);
            }
            case USUARIOS_COLLECTION -> {
                if (returnType == "min") yield snapshotToUsuario(snapshot, true, false);
                yield snapshotToUsuario(snapshot, false, false);
            }
            default -> throw new RuntimeException("Collection name invalid. Checks if the collection exists.");
        };
    }

    public static void deleteDocuments(String collectionName, DeleteBodyDTO payload) {
        var ids = payload.ids();

        for (var id : ids) {
            firestore.collection(collectionName).document(id).delete();
        }
    }

    public static Object getDocumentById(String collectionName, String id) throws Exception {
        DocumentReference document = firestore.collection(collectionName).document(id);
        DocumentSnapshot snapshot = document.get().get();
        if (!snapshot.exists()) return null;
        return convertSnapshot(collectionName, snapshot, null);
    }

    public static Object insertDocument(String collectionName, Object payload) throws Exception {
        Object data;

        switch (collectionName) {
            case ASSISTIDOS_COLLECTION:
                data = getObject(payload, Assistido.class);
                var assistido = (Assistido) data;
                FirestoreRepository.insert(ASSISTIDOS_COLLECTION, assistido.getCpf(), assistido);
                return assistido;
            case ATENDIMENTOS_COLLECTION:
                data = getObject(payload, Atendimento.class);
                return new AtendimentoService().insert((Atendimento) data);
            case MEDIDAS_JURIDICAS_COLLECTION:
                data = getObject(payload, MedidaJuridica.class);
                return new MedidaJuridicaService().insert((MedidaJuridica) data);
            case PROCESSOS_COLLECTION:
                data = getObject(payload, Processo.class);
                var processo = (Processo) data;
                FirestoreRepository.insert(PROCESSOS_COLLECTION, processo.getNumero(), processo);
                return processo;
            case USUARIOS_COLLECTION:
                data = getObject(payload, Usuario.class);
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

    public static void updateDocument(String collectionName, UpdateBodyDTO payload) {
        var body = (Map<String, Object>) payload.body();
        var classType = payload.classType();
        var id = payload.id();

        Class<?> clazz;

        switch (collectionName) {
            case ASSISTIDOS_COLLECTION:
                clazz = identifyChildClass(Assistido.class.getSimpleName(), classType);
                FirestoreRepository.update(ASSISTIDOS_COLLECTION, id, filterValidKeys(body, clazz));
                break;
            case ATENDIMENTOS_COLLECTION:
                clazz = identifyChildClass(Atendimento.class.getSimpleName(), classType);
                FirestoreRepository.update(ATENDIMENTOS_COLLECTION, id, filterValidKeys(body, clazz));
                break;
            case MEDIDAS_JURIDICAS_COLLECTION:
                FirestoreRepository.update(MEDIDAS_JURIDICAS_COLLECTION, id, filterValidKeys(body, MedidaJuridica.class));
                break;
            case PROCESSOS_COLLECTION:
                FirestoreRepository.update(PROCESSOS_COLLECTION, id, filterValidKeys(body, Processo.class));
                break;
            case USUARIOS_COLLECTION:
                identifyChildClass(Usuario.class.getSimpleName(), classType);
                new UsuarioService().update(id, body, classType);
                break;
            default:
                throw new RuntimeException("Collection name invalid. Checks if the collection exists.");
        }
    }

    static class DocumentSnapshotService {
        public static Object snapshotToAssistido(DocumentSnapshot snapshot, Boolean returnMinDTO,
                                                 Boolean returnAutocomplete) {

            if (snapshot == null) return null;

            if (returnMinDTO) {
                var object = snapshot.toObject(AssistidoMinDTO.class);
                object.setCpf(snapshot.getId());
                return object;
            }

            if (returnAutocomplete) {
                return snapshot.toObject(AssistidoAutocomplete.class);
            }

            Boolean dadosFCivil =
                    snapshot.contains("naturalidade") &&
                            snapshot.contains("dataNascimento") &&
                            snapshot.contains("dependentes");
            Boolean dadosFTrabalhista =
                    snapshot.contains("ctps") &&
                            snapshot.contains("pis") &&
                            snapshot.contains("empregadoAtualmente");

            if (dadosFCivil && dadosFTrabalhista) return snapshot.toObject(AssistidoFull.class);
            else if (dadosFCivil) return snapshot.toObject(AssistidoCivil.class);
            else if (dadosFTrabalhista) return snapshot.toObject(AssistidoTrabalhista.class);
            else throw new RuntimeException("Error to convert snapshot into Assistido.");
        }

        public static Object snapshotToAtendimento(DocumentSnapshot snapshot, Boolean returnMinDTO,
                                                   Boolean returnAutocomplete,
                                                   Boolean returnAtendimentosDeAssistidoDTO) {
            if (returnMinDTO) {
                var assistidoMap = convertUsingReflection(snapshot.get("envolvidos.assistido"), false);

                // Defini o atributo "assistido"
                var assistidoEnvolvido =
                        new Envolvido(
                                (String) assistidoMap.get("id"),
                                (String) assistidoMap.get("nome"));

                var date = snapshot.getCreateTime().toDate();

                LocalDateTime localDateTime = date.toInstant()
                        .atZone(ZoneId.of("America/Sao_Paulo"))
                        .toLocalDateTime();

                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                String formattedDateTime = localDateTime.format(formatter);


                return new AtendimentoMinDTO(
                        snapshot.getId(),
                        (String) snapshot.get("area"),
                        (String) snapshot.get("status"),
                        assistidoEnvolvido,
                        formattedDateTime
                );
            }

            if (returnAutocomplete) {
                return new AtendimentoAutocomplete(
                        snapshot.getId()
                );
            }

            if (returnAtendimentosDeAssistidoDTO) {
                var assistidoMap = convertUsingReflection(snapshot.get("envolvidos.assistido"), false);
                var estagiarioMap = convertUsingReflection(snapshot.get("envolvidos.estagiario"), false);

                // Defini o atributo "assistido"
                var assistidoEnvolvido =
                        new Envolvido(
                                (String) assistidoMap.get("id"),
                                (String) assistidoMap.get("nome"));

                // Defini o atributo "estagiario"
                var estagiarioEnvolvido =
                        new Envolvido(
                                (String) estagiarioMap.get("id"),
                                (String) estagiarioMap.get("nome"));

                return new AtendimentoVinculadoDTO(
                        snapshot.getId(),
                        (String) snapshot.get("area"),
                        (String) snapshot.get("status"),
                        assistidoEnvolvido,
                        estagiarioEnvolvido,
                        (String) snapshot.get("instante")
                );
            }

            if (snapshot == null) return null;

            String area = snapshot.getString("area");
            if (Objects.equals(area, "Trabalhista")) {
                return snapshot.toObject(AtendimentoTrabalhista.class);
            } else if (Objects.equals(area, "Civil") || Objects.equals(area, "Criminal") || Objects.equals(area, "Família")) {
                return snapshot.toObject(AtendimentoCivil.class);
            }
            return null;
        }

        public static Object snapshotToUsuario(DocumentSnapshot snapshot, Boolean returnMinDTO,
                                               Boolean returnAutocomplete) {
            if (snapshot == null) return null;

            boolean dadosEstagiario = snapshot.contains("matricula");

            if (returnMinDTO) {
                if (dadosEstagiario) {
                    var object = snapshot.toObject(EstagiarioMinDTO.class);
                    object.setId(snapshot.getId());
                    return object;
                } else {
                    var object = snapshot.toObject(UsuarioMinDTO.class);
                    object.setId(snapshot.getId());
                    return object;
                }
            }

            if (returnAutocomplete) {
                return snapshot.toObject(UsuarioAutocomplete.class);
            }

            return (dadosEstagiario) ? snapshot.toObject(Estagiario.class) : snapshot.toObject(Usuario.class);
        }

        public static ProcessoVinculadoDTO snapshotToProcessoVinculado(DocumentSnapshot snapshot) {
            return new ProcessoVinculadoDTO(
                    snapshot.getId(),
                    (String) snapshot.get("vara"),
                    (String) snapshot.get("status")
            );
        }
    }
}
