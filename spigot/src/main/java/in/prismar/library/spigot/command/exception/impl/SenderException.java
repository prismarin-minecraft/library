package in.prismar.library.spigot.command.exception.impl;

import in.prismar.library.spigot.command.exception.CommandException;
import lombok.Getter;
import org.bukkit.command.CommandSender;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class SenderException extends CommandException {

    private CommandSender sender;

    public SenderException(String message, CommandSender sender) {
        super(message);
        this.sender = sender;
    }

}
