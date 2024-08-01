package com.uniprojecao.fabrica.gprojuridico.services;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.listToString;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.stringToList;

public class IdUtils {
    public static String generateId(String prefix, Integer decimalPlaces) {
        return null;
    }

    /**
     * Incrementa a parte numérica de um ID, mantendo o prefixo de letras maiúsculas inalterado.
     *
     * @param id O ID a ser incrementado.
     * @return O ID incrementado com a parte numérica aumentada em 1, mantendo o mesmo número de dígitos.
     */
    public static String incrementId(String id) {
        Matcher prefixMatcher = Pattern.compile("^[A-Z]+").matcher(id);
        Matcher numberMatcher = Pattern.compile("\\d+").matcher(id);

        String prefix = "";
        String number = "";

        if (prefixMatcher.find()) {
            prefix = prefixMatcher.group();
        }

        if (numberMatcher.find()) {
            number = numberMatcher.group();
        }

        var res = incrementByOne(stringToList(number), number.length());

        return prefix + listToString(res);
    }

    /**
     * Usado como método auxiliar, incrementa somente a parte numérica de um ID.
     *
     * @param numbers O ID a ser incrementado, que consiste somente de números.
     * @param decimalPlaces A quantidade de casas decimais ou o comprimento que o ID possui.
     * @return A parte numérica do ID aumentada em 1, mantendo o mesmo número de dígitos.
     */
    private static List<String> incrementByOne(List<String> numbers, int decimalPlaces) {
        var length = decimalPlaces - 1;
        var last = numbers.get(length);
        var sum = Integer.parseInt(last) + 1;

        // Treats the sum made by taking 1 from 10 and placing it in the decimal place to the left.
        if (sum == 10) {
            // If there are no more places to the left, the decimal place must receive the number as is.
            if (length == 0) {
                numbers.remove(length);
                numbers.add(length, String.valueOf(sum));

                return numbers;
            }

            var tenDivided = List.of(Integer.toString(sum).split(""));
            var zero = tenDivided.get(tenDivided.size() - 1);

            numbers.remove(length); // Removes 9.
            numbers.add(zero);

            return incrementByOne(numbers, length);
        }

        numbers.remove(length);
        numbers.add(length, String.valueOf(sum)); // Adds the number as is.

        return numbers;
    }
}
