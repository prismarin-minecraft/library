package in.prismar.library.common;

import in.prismar.library.common.time.TimeUtil;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Optional;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class EnumUtilTest {

    @Test
    public void testSuccess() {
        Optional<TestEnum> optional = EnumUtil.getEnumByNameOptional(TestEnum.values(), "TEST_VALue_one");
        Assertions.assertEquals(true, optional.isPresent());
    }

    @Test
    public void testFailed() {
        Optional<TestEnum> optional = EnumUtil.getEnumByNameOptional(TestEnum.values(), "wrong_value");
        Assertions.assertEquals(false, optional.isPresent());
    }

    public enum TestEnum {
        TEST_VALUE_ONE,
        TEST_VALUE_TWO;
    }
}
