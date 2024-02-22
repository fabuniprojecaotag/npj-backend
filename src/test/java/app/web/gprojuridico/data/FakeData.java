package app.web.gprojuridico.data;

import app.web.gprojuridico.model.*;
import app.web.gprojuridico.model.enums.Escolaridade;
import app.web.gprojuridico.model.enums.EstadoCivil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class FakeData {

    private static Map<String, Object> convertUsingReflection(Object object) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field: fields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(object));
        }

        return map;
    }

    public static Map<String, Object> assistidoCivil() throws IllegalAccessException {
        AssistidoCivil assistido = new AssistidoCivil(
                "Rafael Mendes Mendonça Carvalho",
                "11.782.956-0",
                "897.688.780-88",
                "Brasileiro",
                Escolaridade.SUPERIOR.getRotulo(),
                EstadoCivil.CASADO.getRotulo(),
                "Professor universitário",
                "(61) 99320-1050",
                "mendoca.rafael@example.com",
                new Filiacao("Maria Santos Carvalho", "Túlio Machado Mendes"),
                "R$ 9300",
                new Endereco("QNB 14 Conjunto D", "12"),
                "Brasiliense",
                "1992-02-10",
                3
        );
       return convertUsingReflection(assistido);
    }

    public static Map<String, Object> assistidoFull() throws IllegalAccessException {
        AssistidoFull assistido = new AssistidoFull(
                "Rafael Mendes Mendonça Carvalho",
                "11.782.956-0",
                "897.688.780-88",
                "Brasileiro",
                Escolaridade.SUPERIOR.getRotulo(),
                EstadoCivil.CASADO.getRotulo(),
                "Professor universitário",
                "(61) 99320-1050",
                "mendoca.rafael@example.com",
                new Filiacao("Maria Santos Carvalho", "Túlio Machado Mendes"),
                "R$ 9300",
                new Endereco("QNB 14 Conjunto D", "12"),
                "Brasiliense",
                "1992-02-10",
                3,
                new Ctps(23, 2, "DF"),
                "92386192",
                true
        );
        return convertUsingReflection(assistido);
    }

    public static Map<String, Object> assistidoTrabalhista() throws IllegalAccessException {
        AssistidoTrabalhista assistido = new AssistidoTrabalhista(
                "Rafael Mendes Mendonça Carvalho",
                "11.782.956-0",
                "897.688.780-88",
                "Brasileiro",
                Escolaridade.SUPERIOR.getRotulo(),
                EstadoCivil.CASADO.getRotulo(),
                "Professor universitário",
                "(61) 99320-1050",
                "mendoca.rafael@example.com",
                new Filiacao("Maria Santos Carvalho", "Túlio Machado Mendes"),
                "R$ 9300",
                new Endereco("QNB 14 Conjunto D", "12"),
                new Ctps(23, 2, "DF"),
                "92386192",
                true
        );
        return convertUsingReflection(assistido);
    }

    public static Map<String, Object> incorrectAssistido() {
        Map<String, Object> object = new HashMap<>();
        object.put("nome", "Helena Rodrigues de Souza Ribeiro");
        object.put("rg", "11.782.956-0");
        object.put("cpf", "897.688.780-88");
        object.put("dataNascimento", "1994-04-20");
        object.put("dependentes", 2);

        return object;
    }

    public static Map<String, Object> fieldsToUpdateAssistido(){
        return Map.of(
                "telefone", "(61) 99327-9391"
        );
    }
    public static Map<String, Object> fieldsToUpdateAssistidoCivilToFull(){
        return Map.of(
                "ctps", new Ctps(),
                "pis", "1286391029",
                "empregadoAtualmente", true
        );
    }
    public static Map<String, Object> fieldsToUpdateAssistidoTrabalhistaToFull(){
        return Map.of(
                "naturalidade", "Brasiliense",
                "dataNascimento", "1998-10-30",
                "dependentes", 2
        );
    }
}
