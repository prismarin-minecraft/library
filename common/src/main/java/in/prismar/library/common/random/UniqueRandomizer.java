package in.prismar.library.common.random;

import in.prismar.library.common.math.MathUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public final class UniqueRandomizer {

    private static final Random RANDOM = new Random();
    private static final Map<String, Object> RANDOMIZED_VALUES = new HashMap<>();

    public static <T> T getRandom(String uniqueId, List<T> list) {
        if(!RANDOMIZED_VALUES.containsKey(uniqueId)) {
            T object = list.get(RANDOM.nextInt(list.size()));
            RANDOMIZED_VALUES.put(uniqueId, object);
            return object;
        }
        if(list.size() == 1) {
            return list.get(0);
        }
        T current = (T) RANDOMIZED_VALUES.get(uniqueId);
        T object = list.get(RANDOM.nextInt(list.size()));
        while (current.equals(object)) {
            object = list.get(RANDOM.nextInt(list.size()));
        }
        RANDOMIZED_VALUES.put(uniqueId, object);
        return object;
    }
}
