package in.prismar.library.meta;

import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.Service;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service(autoRegister = false)
public class DefaultInjectionTest {

    private final MetaRegistry registry = new MetaRegistry();

    @Inject
    SecondServerInstance instance;

    @Test
    public void test() {
        registry.registerEntity(this);
        registry.build(DefaultInjectionTest.class.getClassLoader(), "in.prismar.library.meta");

        Assertions.assertEquals("Works", instance.execute());
    }
}
