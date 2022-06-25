package in.prismar.library.spigot.command.spigot;

import in.prismar.library.spigot.command.Arguments;
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


}
