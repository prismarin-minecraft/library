package in.prismar.library.common;

import in.prismar.library.common.logger.LogLevel;
import in.prismar.library.common.logger.LogRecord;
import in.prismar.library.common.logger.impl.DefaultLogger;
import in.prismar.library.common.logger.Logger;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class LoggerTest {

    private final Logger logger = new DefaultLogger("test", true);

    @Test
    public void testDefault() {
        for(LogLevel level : LogLevel.values()) {
            LogRecord record = logger.log(level, "Test");
            Assertions.assertEquals("Test", record.getMessage());
        }

    }

    @Test
    public void testError() {
        NullPointerException exception = new NullPointerException();
        StringWriter stringWriter = new StringWriter();
        exception.printStackTrace(new PrintWriter(stringWriter));
        final String exceptionAsString = stringWriter.toString();

        LogRecord record = logger.error("Test", exception);
        Assertions.assertEquals("Test: \n" + exceptionAsString, record.getMessage());
    }
}
