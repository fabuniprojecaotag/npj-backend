package com.uniprojecao.fabrica.gprojuridico.utils;

import com.uniprojecao.fabrica.gprojuridico.models.assistido.AssistidoCivil;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.AssistidoTrabalhista;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.AtendimentoCivil;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.AtendimentoTrabalhista;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.FichaCivil;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.FichaTrabalhista;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Estagiario;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.services.exceptions.InvalidCollectionNameException;
import com.uniprojecao.fabrica.gprojuridico.services.exceptions.InvalidReturnTypeException;
import jakarta.validation.*;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

import static com.uniprojecao.fabrica.gprojuridico.utils.Constants.*;

@Slf4j
@NoArgsConstructor
public class Utils<T> {
    public static <T> Map<String, Object> convertUsingReflection(T object, Boolean useSuperClass) {
        if (object instanceof Map<?, ?>) {
            @SuppressWarnings("unchecked")
            Map<String, Object> castedMap = (Map<String, Object>) object;
            return castedMap;
        }

        Map<String, Object> map = new HashMap<>();
        Class<?> t = object.getClass();
        Field[] fields;

        if (Boolean.TRUE.equals(useSuperClass)) {
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

    public T convertGenericObjectToClassInstanceWithValidation(T body, Class<?> type) throws Exception {
        var data = convertGenericObjectToClassInstance(body, type);
        validateDataConstraints(data);
        return data;
    }

    public T convertGenericObjectToClassInstance(T object, Class<?> destinyClazz) throws Exception {
        if (destinyClazz.isInstance(object)) {
            return (T) destinyClazz.cast(object);
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
                                value = convertGenericObjectToClassInstance((T) value, fieldType);
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

    private void validateDataConstraints(T data) {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();

            Set<ConstraintViolation<T>> violations = validator.validate(data);

            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        }
    }

    public static List<String> getSpecificFieldNamesToReturnClassInstance(String collection, String returnType) {
        boolean isReturnTypeNull = returnType == null;
        boolean isReturnTypeEqualsToMin = false;
        boolean isReturnTypeEqualsToAutoComplete = false;
        boolean isReturnTypeEqualsToForAssistido = false;

        if (!isReturnTypeNull) {
            isReturnTypeEqualsToMin = returnType.equals("min");
            isReturnTypeEqualsToAutoComplete = returnType.equals("autoComplete");
            isReturnTypeEqualsToForAssistido = returnType.equals("forAssistido");
        }

        return switch (collection) {
            case ASSISTIDOS_COLLECTION -> {
                if (isReturnTypeEqualsToMin) {
                    yield List.of("nome", "email", "quantidade.atendimentos", "quantidade.processos", "telefone");
                }
                if (isReturnTypeEqualsToAutoComplete) {
                    yield List.of("nome");
                }
                throw new InvalidReturnTypeException(returnType);
            }
            case ATENDIMENTOS_COLLECTION -> {
                if (isReturnTypeEqualsToMin) {
                    yield List.of("area", "status", "envolvidos.assistido");
                }
                if (isReturnTypeEqualsToAutoComplete) {
                    yield List.of("id");
                }
                if (isReturnTypeEqualsToForAssistido) {
                    yield List.of("area", "status", "envolvidos.assistido", "envolvidos.estagiario", "instante");
                }
                throw new InvalidReturnTypeException(returnType);
            }
            case MEDIDAS_JURIDICAS_COLLECTION -> List.of("area", "nome", "descricao");
            case PROCESSOS_COLLECTION -> {
                if (isReturnTypeEqualsToForAssistido) {
                    yield List.of("vara", "status");
                }
                yield List.of("numero", "atendimentoId", "nome", "dataDistribuicao", "vara", "forum", "status");
            }
            case USUARIOS_COLLECTION -> {
                if (isReturnTypeEqualsToMin) {
                    yield List.of("nome", "email", "role", "status", "matricula", "semestre");
                }
                if (isReturnTypeEqualsToAutoComplete) {
                    yield List.of("nome", "role");
                }
                throw new InvalidReturnTypeException(returnType);
            }
            default -> throw new InvalidCollectionNameException();
        };
    }

}
