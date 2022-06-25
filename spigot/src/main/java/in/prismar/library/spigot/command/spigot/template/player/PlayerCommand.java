package in.prismar.library.spigot.command.spigot.template.player;

import in.prismar.library.spigot.command.spigot.SpigotCommand;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public abstract class PlayerCommand extends SpigotCommand<Player> {

    public PlayerCommand(String name) {
        super(name);
        setSenders(Player.class);
    }
}
