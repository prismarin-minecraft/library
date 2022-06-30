package in.prismar.library.spigot.command.spigot;

import in.prismar.library.spigot.command.Arguments;
import in.prismar.library.spigot.command.CommandChildNode;
import in.prismar.library.spigot.command.CommandNode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class SpigotCommandListener<T extends CommandSender> extends Command {

    private final SpigotCommand<T> command;

    public SpigotCommandListener(SpigotCommand<T> command) {
        super(command.getName());
        this.command = command;
        if (command.getAliases() != null) {
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
        List<String> list = new ArrayList<>();
        if (args.length >= 1) {
            check(list, args, command);
        }
        if(list.isEmpty()) {
            return super.tabComplete(sender, alias, args);
        }
        return list;
    }

    private void check(List<String> list, String[] args, CommandNode<T, ? extends Arguments> node) {
        final String sub = args[args.length - 1];
        for (CommandChildNode child : node.getChildren()) {
            if (child.getName().toLowerCase().startsWith(sub.toLowerCase()) && child.getIndex() == (args.length - 1)) {
                list.add(child.getName());
            }
            if (!child.getChildren().isEmpty()) {
                check(list, args, node);
            }
        }
    }


}
