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
public class NoPermissionException extends CommandException {


    public NoPermissionException() {
        super("Sender does not have enough permission");
    }

}
