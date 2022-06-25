package in.prismar.library.spigot.command.spigot.template.help;

import in.prismar.library.spigot.command.spigot.SpigotSubCommand;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Setter
public abstract class HelpSubCommand<T extends CommandSender> extends SpigotSubCommand<T> {

    private String usage;
    private String description;

    public HelpSubCommand(String name) {
        super(name);
        setUsage("");
        setDescription("");
    }

}
