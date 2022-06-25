package in.prismar.library.spigot.command;

import in.prismar.library.spigot.command.exception.CommandExceptionMapper;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public abstract class CommandNode<T, A extends Arguments> implements Command<T, A>{


    private String name;
    private List<String> aliases;
    private String permission;
    private Class<?>[] senders;

    private List<CommandChildNode<T, A>> children;

    private CommandExceptionMapper mapper;

    public CommandNode(String name) {
        this.name = name;
        this.children = new ArrayList<>();
        this.mapper = CommandFactory.getDefaultExceptionMapper();
    }

    public Optional<CommandChildNode<T, A>> retrieveChild(A arguments) {
        for(CommandChildNode<T, A> child : getChildren()) {
            if(arguments.getLength() >= child.getIndex() + 1) {
                if(child.check(arguments.getString(child.getIndex()))) {
                    return Optional.of(child);
                }
            }
        }
        return Optional.empty();
    }



    public boolean check(String command) {
        if(getName().equalsIgnoreCase(command)) {
            return true;
        }
        if(getAliases() != null) {
            for(String alias : aliases) {
                if(alias.equalsIgnoreCase(command)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasPermission() {
        return this.permission != null;
    }

    public boolean isSender(Class<?> sender) {
        for(Class<?> type : senders) {
            if(type == sender) {
                return true;
            }
            for(Class<?> interfaceClass : sender.getInterfaces()) {
                if(interfaceClass == type) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setSenders(Class<?>... senders) {
        this.senders = senders;
    }

    public boolean hasSenders() {
        return this.senders != null;
    }

    public boolean hasMapper() {
        return this.mapper != null;
    }

    public void setMapper(CommandExceptionMapper mapper) {
        this.mapper = mapper;
    }

    public void addChild(CommandChildNode<T, A> node) {
        this.children.add(node);
    }

    public void setAliases(String... aliases) {
        this.aliases = Arrays.asList(aliases);
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
