package org.example.backend.services.utilServices;

import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class EntityUpdateUtil {

    public static void updateField(Consumer<String> setter, String value) {
        if (value != null && !value.isBlank()) {
            setter.accept(value);
        }
    }

    public static void updateField(Consumer<Double> setter, Double value) {
        if (value != null && !value.isNaN()) {
            setter.accept(value);
        }
    }

    public static void updateField(Consumer<Long> setter, Long value) {
        if (value != null) {
            setter.accept(value);
        }
    }

    public static void updateField(Consumer<Integer> setter, Integer value) {
        if (value != null) {
            setter.accept(value);
        }
    }

    public static <T> void updateField(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }

}
