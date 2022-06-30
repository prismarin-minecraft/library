package in.prismar.library.spigot.command.exception.impl;

import in.prismar.library.spigot.command.exception.CommandException;
import lombok.Getter;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class PlayerOfflineException extends CommandException {

    private final String input;

    public PlayerOfflineException(String input) {
        super("Player " + input + " is not online");
        this.input = input;
    }

}
