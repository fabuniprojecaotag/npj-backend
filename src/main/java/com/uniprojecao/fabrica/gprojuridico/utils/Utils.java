package com.uniprojecao.fabrica.gprojuridico.utils;

import com.uniprojecao.fabrica.gprojuridico.models.assistido.AssistidoCivil;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.AssistidoTrabalhista;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.AtendimentoCivil;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.AtendimentoTrabalhista;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.FichaCivil;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.FichaTrabalhista;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Estagiario;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Usuario;
import jakarta.validation.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

import static com.uniprojecao.fabrica.gprojuridico.utils.Constants.*;

public class Utils {
    public static <T> Map<String, Object> convertUsingReflection(T object, Boolean useSuperClass) {
        if (object instanceof Map<?, ?>) {
            @SuppressWarnings("unchecked")
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

    public static Object convertGenericObjectToClassInstanceWithValidation(Object body, Class<?> type) throws Exception {
        var data = convertGenericObjectToClassInstance(body, type);
        validateDataConstraints(data);
        return data;
    }

    public static <T> T convertGenericObjectToClassInstance(Object object, Class<T> destinyClazz) throws Exception {
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
                                value = convertGenericObjectToClassInstance(value, fieldType);
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

    public static Class<?> identifyChildClass(String baseClass, String classType) {
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

    public static Map<String, Object> processNestedKeysIntoOne(Map<String, Object> inputMap) {
        Map<String, Object> outputMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : inputMap.entrySet()) {
            processMapEntry(entry.getKey(), entry.getValue(), outputMap, "");
        }
        return outputMap;
    }

    private static void processMapEntry(String key, Object value, Map<String, Object> outputMap, String parentKey) {
        // Se o valor é um Map, deve-se percorrer o método recursivamente para concatenar as chaves aninhadas
        if (value instanceof Map) {
            @SuppressWarnings("unchecked")
            var subMap = (Map<String, Object>) value;
            for (Map.Entry<String, Object> entry : subMap.entrySet()) {
                processMapEntry(entry.getKey(), entry.getValue(), outputMap, parentKey + key + ".");
            }
        } else {
            outputMap.put(parentKey + key, value); // Ex. de entrada percorrida recursivamente a ser adicionada: {"ficha.parteContraria.nome", "Mauro Silva"}
        }
    }

    public static String[] getSpecificFieldNamesToReturnClassInstance(String collection, String returnType) {
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
}
