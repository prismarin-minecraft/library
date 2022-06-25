package in.prismar.library.spigot.command.spigot;

import in.prismar.library.spigot.command.CommandChildNode;
import in.prismar.library.spigot.command.CommandNode;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.exception.impl.NoPermissionException;
import in.prismar.library.spigot.command.exception.impl.SenderException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.Optional;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public abstract class SpigotCommand<T extends CommandSender> extends CommandNode<T, SpigotArguments> {

    public SpigotCommand(String name) {
        super(name);
    }

    public boolean execute(T sender, String[] args) {
         try {
             if(!hasSenders()) {
                 throw new SenderException("Senders are missing");
             }
             if(!isSender(sender.getClass())) {
                 throw new SenderException("Wrong sender");
             }
             if(hasPermission()) {
                if(!sender.hasPermission(getPermission())) {
                    throw new NoPermissionException(sender);
                }
             }
             SpigotArguments arguments = new SpigotArguments(args);
             Optional<CommandChildNode<T, SpigotArguments>> childOptional = retrieveChild(arguments);
             if(childOptional.isPresent()) {
                 SpigotSubCommand<T> subCommand = (SpigotSubCommand<T>) childOptional.get();
                 if(subCommand.execute(sender, arguments)) {
                     return true;
                 }
             }
             return send(sender, arguments);
         } catch (CommandException exception) {
             if(hasMapper()) {
                 getMapper().map(exception);
             }
         }
         return false;
    }

    @Override
    public boolean isSender(Class<?> sender) {
        for(Class<?> type : getSenders()) {
            if(sender.getSimpleName().equalsIgnoreCase("TerminalConsoleCommandSender")
            && type == ConsoleCommandSender.class) {
                return true;
            }
        }
        return super.isSender(sender);
    }
}
