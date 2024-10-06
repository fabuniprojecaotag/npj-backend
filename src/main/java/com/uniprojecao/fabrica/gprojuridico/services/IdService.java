package com.uniprojecao.fabrica.gprojuridico.services;

import com.uniprojecao.fabrica.gprojuridico.models.MedidaJuridica;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.Assistido;
import com.uniprojecao.fabrica.gprojuridico.models.atendimento.Atendimento;
import com.uniprojecao.fabrica.gprojuridico.models.processo.Processo;
import com.uniprojecao.fabrica.gprojuridico.repositories.FirestoreRepositoryImpl;
import lombok.NoArgsConstructor;

import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.uniprojecao.fabrica.gprojuridico.utils.Constants.*;

@NoArgsConstructor()
public class IdService<T> {
    /**
     * Gera um ID de 5 casas decimais, contando a partir do 1, com base no prefixo passado.
     *
     * @param prefix O prefixo a ser passado; OBS: O tamanho não pode exercer 6 caracteres.
     * @return O ID com o prefixo passado e o formato descrito.
     */
    static String generateId(String prefix) {
        int prefixLimit = 6;
        int decimalPlaces = 5;

        if (prefix.length() > prefixLimit) throw new IllegalArgumentException("The prefix length cannot be greater than 6.");

        // Gera o ID formatado com zeros à esquerda
        return String.format("%s%0" + decimalPlaces + "d", prefix.toUpperCase(), 1);
    }

    /**
     * Incrementa a parte numérica de um ID, mantendo o prefixo de letras maiúsculas inalterado.
     *
     * @param id O ID a ser incrementado.
     * @return O ID incrementado com a parte numérica aumentada em 1, mantendo o mesmo número de dígitos.
     */
    static String incrementId(String id) {
        Matcher prefixMatcher = Pattern.compile("^[A-Z]+").matcher(id);
        Matcher numberMatcher = Pattern.compile("\\d+$").matcher(id);

        String prefix;
        String number;

        if (prefixMatcher.find()) {
            prefix = prefixMatcher.group();
        } else {
            throw new IllegalArgumentException("ID deve conter um prefixo.");
        }

        if (numberMatcher.find()) {
            number = numberMatcher.group();
        } else {
            throw new IllegalArgumentException("ID deve conter uma parte numérica.");
        }

        // Incrementa a parte numérica
        int incrementedNumber = Integer.parseInt(number) + 1;

        // Calcula o número de zeros à esquerda que devem ser mantidos
        int length = number.length();

        // Preenche com zeros à esquerda, mantendo o comprimento original ou adaptando se necessário
        String incrementedNumberString = String.valueOf(incrementedNumber);
        int incrementedLength = incrementedNumberString.length();

        // Se o comprimento do número incrementado for maior, não preencher com zeros
        if (incrementedLength > length) {
            return String.format("%s%s", prefix, incrementedNumberString);
        } else {
            return String.format("%s%0" + length + "d", prefix, incrementedNumber);
        }
    }


    public T defineId(T data, String collectionName, String prefix) throws ExecutionException, InterruptedException {
        String id = new FirestoreRepositoryImpl<T>(collectionName).findLastDocumentId(); // Armazena o id
        String newId = (id != null) ? incrementId(id) : generateId(prefix); // Incrementa o id ou gera um novo, caso se não houver um documento criado no banco de dados

        return switch (collectionName) {
            case ASSISTIDOS_COLLECTION -> {
                var model = (Assistido) data;
                model.setId(newId);
                yield data;
            }
            case ATENDIMENTOS_COLLECTION -> {
                var model = (Atendimento) data;
                model.setId(newId);
                yield data;
            }
            case MEDIDAS_JURIDICAS_COLLECTION -> {
                var model = (MedidaJuridica) data;
                model.setId(newId);
                yield data;
            }
            case PROCESSOS_COLLECTION -> {
                var model = (Processo) data;
                model.setId(newId);
                yield data;
            }
            default -> throw new IllegalStateException("Unexpected value: " + collectionName);
        };
    }
}
