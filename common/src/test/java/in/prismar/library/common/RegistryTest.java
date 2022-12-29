package in.prismar.library.common;

import in.prismar.library.common.registry.LocalMapRegistry;
import in.prismar.library.common.registry.MapRegistry;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class RegistryTest {


    @Test
    public void testMapRegistry() {
        MapRegistry<Integer, String> registry = new LocalMapRegistry<>(false, false);

        registry.register(1, "Test");
        Assertions.assertEquals(1, registry.getAll().size());

        String value = registry.getById(1);
        Assertions.assertEquals("Test", value);

        registry.unregister(1);
        Assertions.assertEquals(0 , registry.getAll().size());
    }





}
