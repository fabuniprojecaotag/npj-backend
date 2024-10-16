package com.uniprojecao.fabrica.gprojuridico.services;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.uniprojecao.fabrica.gprojuridico.utils.Utils.print;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IdServiceTest {

    @Test
    void generateId() {
        var prefixes = List.of("ATE", "MEDJUR", "ABCDEF");
        var ExpectedIds = List.of("ATE00001", "MEDJUR00001", "ABCDEF00001");

        var i = 0;
        for (var id : prefixes) {
            var expected = ExpectedIds.get(i);

            var res = IdService.generateId(id);
            print("Prefix: " + id + ", Actual: " + res + ", Expected: " + expected);
            assertEquals(expected, res);

            i++;
        }
    }

    @Test
    void incrementId() {
        var originalIds = List.of("ATE00398", "ATE00111", "ATE00999", "ATE00015", "ATE99999");
        var updatedIds = List.of("ATE00399", "ATE00112", "ATE01000", "ATE00016", "ATE100000");

        var i = 0;
        for (var id : originalIds) {
            var updatedValue = IdService.incrementId(id);
            var expectedValue = updatedIds.get(i);

            print("Original: " + id + ", updated: " + updatedValue);
            assertEquals(updatedValue, expectedValue);

            i++;
        }
    }
}