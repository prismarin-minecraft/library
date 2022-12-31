package in.prismar.library.common;

import in.prismar.library.common.random.UniqueRandomizer;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class UniqueRandomizerTest {

    @Test
    public void test() {
        List<Integer> numbers = List.of(4, 2, 1, 5);

        int old = -1;
        for (int i = 0; i < 10; i++) {
            int chosen = UniqueRandomizer.getRandom("test", numbers);
            Assertions.assertEquals(false, old == chosen);
            old = chosen;
        }
    }
}
