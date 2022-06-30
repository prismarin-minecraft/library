package in.prismar.library.spigot.command.exception;

import org.bukkit.command.CommandSender;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public interface CommandExceptionMapper {

    void map(CommandSender sender, CommandException exception);
}
