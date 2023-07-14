package in.prismar.library.common;

import in.prismar.library.common.time.TimeUtil;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class TimeTest {

    @Test
    public void test() {
        long seconds = 110;
        Assertions.assertEquals("01:50", TimeUtil.convertToTwoDigits(seconds));


    }

}
