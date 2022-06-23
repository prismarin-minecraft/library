package in.prismar.library.common;

import in.prismar.library.common.registry.LocalMapRegistry;
import in.prismar.library.common.registry.MapRegistry;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class RegistryTest {

    MapRegistry<Integer, String> registry = new LocalMapRegistry<>(false, false);

    @Test
    public void testAll() {
        registry.register(1, "Test");
        Assertions.assertEquals(1, registry.getAll().size());

        String value = registry.getById(1);
        Assertions.assertEquals("Test", value);

        registry.unregister(1);
        Assertions.assertEquals(0 , registry.getAll().size());
    }



}
