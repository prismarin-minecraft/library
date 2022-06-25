package in.prismar.library.spigot.command.spigot;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class SpigotCommandListener<T extends CommandSender> extends Command {

    private SpigotCommand<T> command;

    public SpigotCommandListener(SpigotCommand<T> command) {
        super(command.getName());
        this.command = command;
        if(command.getAliases() != null) {
            setAliases(command.getAliases());
        }
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        return command.execute((T) sender, args);
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        return super.tabComplete(sender, alias, args);
    }
}
