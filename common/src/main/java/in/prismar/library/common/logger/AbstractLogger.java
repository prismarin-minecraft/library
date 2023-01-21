package in.prismar.library.common.logger;

import in.prismar.library.common.event.EventBus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@RequiredArgsConstructor
@Getter
public abstract class AbstractLogger implements Logger {

    private final String name;

    private EventBus eventBus = new EventBus();

    @Override
    public LogRecord info(String message, Object... format) {
        return this.log(LogLevel.INFO, message, format);
    }

    @Override
    public LogRecord warn(String message, Object... format) {
        return this.log(LogLevel.WARNING, message, format);
    }

    @Override
    public LogRecord error(String message, Throwable throwable, Object... format) {
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        final String exceptionAsString = stringWriter.toString();
        LogRecord record = log(LogLevel.ERROR, message + ": \n" + exceptionAsString, format);
        return record;
    }
}
