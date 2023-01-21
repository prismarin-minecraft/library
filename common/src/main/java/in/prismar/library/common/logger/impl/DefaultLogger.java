package in.prismar.library.common.logger.impl;

import in.prismar.library.common.logger.AbstractLogger;
import in.prismar.library.common.logger.LogLevel;
import in.prismar.library.common.logger.LogRecord;
import in.prismar.library.common.logger.event.LogRecordEvent;
import lombok.Setter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class DefaultLogger extends AbstractLogger {

    private boolean includeTimestamp;
    @Setter
    private SimpleDateFormat timeFormat;


    public DefaultLogger(String name, boolean includeTimestamp) {
        super(name);
        this.includeTimestamp = includeTimestamp;
        this.timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public LogRecord log(LogLevel level, String message, Object... data) {
        LogRecord record = record(level, message, data);
        final String fullMessage = "["+level.name()+"] ["+timeFormat.format(record.getTimestamp())+"] " + record.getMessage();
        System.out.println(fullMessage);
        return record;
    }

    protected LogRecord record(LogLevel level, String message, Object... data) {
        LogRecord record = new LogRecord(level, String.format(message, data));
        getEventBus().publish(new LogRecordEvent(record));
        return record;
    }
}
