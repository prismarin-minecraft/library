package in.prismar.library.common;

import java.util.Optional;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public final class EnumUtil {

    public static <T extends Enum> T getEnumByName(T[] types, String name) {
        for (T type : types) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

    public static <T extends Enum> Optional<T> getEnumByNameOptional(T[] types, String name) {
        T result = getEnumByName(types, name);
        return result == null ? Optional.empty() : Optional.of(result);
    }

    public static <T extends Enum> String[] getEnumNames(T[] types) {
        String[] names = new String[types.length];
        for (int i = 0; i < types.length; i++) {
            names[i] = types[i].name();
        }
        return names;
    }

}
