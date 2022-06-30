package in.prismar.library.spigot.command.spigot;

import in.prismar.library.spigot.command.Arguments;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.exception.impl.PlayerOfflineException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class SpigotArguments extends Arguments {

    public SpigotArguments(String[] args) {
        super(args);
    }

    public Player getPlayer(int index) {
        return Bukkit.getPlayer(getArgs()[index]);
    }

    public Player getOnlinePlayer(int index) throws CommandException {
        final String input = getArgs()[index];
        Player target = Bukkit.getPlayer(input);
        if(target == null) {
            throw new PlayerOfflineException(input);
        }
        return target;
    }

}
