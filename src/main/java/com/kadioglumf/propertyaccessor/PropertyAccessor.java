package com.kadioglumf.propertyaccessor;

import java.util.function.Supplier;

public class PropertyAccessor {

    public static <T> T valueOrDefault(Supplier<T> supplier, T defaultValue) {
        try {
            return supplier.get();
        }
        catch (NullPointerException ex) {
            return defaultValue;
        }
    }

    private PropertyAccessor() {
    }
}
