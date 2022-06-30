package in.prismar.library.spigot.command.spigot;

import in.prismar.library.spigot.command.CommandChildNode;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.exception.impl.NoPermissionException;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;

import java.util.Optional;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Setter
@Getter
public abstract class SpigotSubCommand<T extends CommandSender> extends CommandChildNode<T, SpigotArguments> {

    public SpigotSubCommand(String name) {
        super(name);
    }

    public boolean execute(T sender, SpigotArguments arguments) throws CommandException {
        if(hasPermission()) {
            if(!sender.hasPermission(getPermission())) {
                throw new NoPermissionException();
            }
        }
        Optional<CommandChildNode<T, SpigotArguments>> childOptional = retrieveChild(arguments);
        if(childOptional.isPresent()) {
            SpigotSubCommand<T> subCommand = (SpigotSubCommand<T>) childOptional.get();
            if(subCommand.execute(sender, arguments)) {
                return true;
            }
        }
        return send(sender, arguments);
    }

}
