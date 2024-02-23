package app.web.gprojuridico.service.utils;

import app.web.gprojuridico.exception.ResourceNotFoundException;
import app.web.gprojuridico.model.AssistidoCivil;
import app.web.gprojuridico.model.AssistidoFull;
import app.web.gprojuridico.model.AssistidoTrabalhista;
import com.google.cloud.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static app.web.gprojuridico.service.utils.Utils.getAllFieldNames;

public class AssistidoUtils {
    public static Map<String, Object> verifyDataToInsertAssistido(Map<String, Object> data) {
        Object field = data.get("tipo");
        System.out.println("\nTipo de payload recebido para verificar: " + field);

        if (Objects.equals(field, "AssistidoCivil")) {
            Boolean naturalidade = data.containsKey("naturalidade");
            Boolean dataNascimento = data.containsKey("dataNascimento");
            Boolean dependentes = data.containsKey("dependentes");
            if (naturalidade && dataNascimento && dependentes) {
                return data;
            } else {
                throw new RuntimeException("O objeto passado não corresponde à nenhuma instância de Assistido.");
            }
        } else if (Objects.equals(field, "AssistidoTrabalhista")) {
            Boolean ctps = data.containsKey("ctps");
            Boolean pis = data.containsKey("pis");
            Boolean empregadoAtualmente = data.containsKey("empregadoAtualmente");
            if (ctps && pis && empregadoAtualmente) {
                return data;
            } else {
                throw new RuntimeException("O objeto passado não corresponde à nenhuma instância de Assistido.");
            }
        }

        return null;
    }

    public static Map<String, Object> verifyDataToUpdateAssistido(Map<String, Object> data) {
        List<String> assistidoFields = getAllFieldNames(new ArrayList<>(), AssistidoFull.class);

        for (String key : data.keySet()) {
            if (!assistidoFields.contains(key)) {
                throw new RuntimeException("O campo " + key + "de payload recebido não existe em Assistido");
            }
        }

        return data;
    }

    public static Object verifySnapshotToFindAssistidoById(DocumentSnapshot snapshot) {
        /*
         * Existe um erro no método .get() do DocumentSnapshot, pois um documento
         * que não existe no Firestore é, de alguma forma, encontrado e retornado
         * com campos null. Por isso, faz-se necessário essa condicional abaixo.
         */
        if (snapshot.exists()) {
            return convertSnapshotToCorrespondingAssistidoModel(snapshot);
        } else {
            throw new ResourceNotFoundException();
        }
    }

    /**
     * Converts the passed snapshot to the corresponding model through the
     * relationship that the person assisted has with the service(s).
     */
    public static Object convertSnapshotToCorrespondingAssistidoModel(DocumentSnapshot snapshot) {

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
