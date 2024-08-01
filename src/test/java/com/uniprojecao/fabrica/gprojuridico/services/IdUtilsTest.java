package com.uniprojecao.fabrica.gprojuridico.services;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.print;
import static org.junit.jupiter.api.Assertions.*;

class IdUtilsTest {

    @Test
    void generateId() {
    }

    @Test
    void incrementId() {
        var originalIds = List.of("ATE00398", "ATE00111", "ATE00999", "ATE00015", "ATE99999");
        var updatedIds = List.of("ATE00399", "ATE00112", "ATE01000", "ATE00016", "ATE100000");

        var i = 0;
        for (var id : originalIds) {
            var updatedValue = IdUtils.incrementId(id);
            var expectedValue = updatedIds.get(i);

            print("Original: " + id + ", updated: " + updatedValue);
            assertEquals(updatedValue, expectedValue);

            i++;
        }
    }
}