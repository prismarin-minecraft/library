package in.prismar.library.common.logger.event;

import in.prismar.library.common.event.Event;
import in.prismar.library.common.logger.LogRecord;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@AllArgsConstructor
public class LogRecordEvent implements Event {

    private final LogRecord record;
}
