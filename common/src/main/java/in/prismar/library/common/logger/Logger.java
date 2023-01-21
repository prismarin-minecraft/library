package in.prismar.library.common.logger;

import in.prismar.library.common.event.EventBus;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public interface Logger {

    LogRecord log(LogLevel level, String message, Object... format);
    LogRecord info(String message, Object... format);
    LogRecord warn(String message, Object... format);
    LogRecord error(String message, Throwable throwable, Object... format);

    String getName();

    EventBus getEventBus();

}
