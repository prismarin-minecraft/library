package in.prismar.library.spigot.command.spigot.template.player;

import in.prismar.library.spigot.command.spigot.SpigotSubCommand;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public abstract class PlayerSubCommand extends SpigotSubCommand<Player> {

    public PlayerSubCommand(String name) {
        super(name);
    }
}
