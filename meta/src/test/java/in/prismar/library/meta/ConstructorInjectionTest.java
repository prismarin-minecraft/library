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
public class ConstructorInjectionTest {

    private final MetaRegistry registry = new MetaRegistry();

    @Inject
    ConstructorInstance instance;

    @Test
    public void test() {
        registry.registerEntity(this);
        registry.build(ConstructorInjectionTest.class.getClassLoader(), "in.prismar.library.meta");
        Assertions.assertEquals("Works", instance.execute());
    }

    public String value() {
        return "Works";
    }
}
