package in.prismar.library.spigot.command;

import in.prismar.library.spigot.command.exception.CommandExceptionMapper;


/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public final class CommandFactory {

    private static CommandExceptionMapper DEFAULT_EXCEPTION_MAPPER = null;

    public static void setDefaultExceptionMapper(CommandExceptionMapper mapper) {
        DEFAULT_EXCEPTION_MAPPER = mapper;
    }

    public static CommandExceptionMapper getDefaultExceptionMapper() {
        return DEFAULT_EXCEPTION_MAPPER;
    }
}
