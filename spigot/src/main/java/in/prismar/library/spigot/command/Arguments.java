package in.prismar.library.spigot.command;

import in.prismar.library.common.EnumUtil;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.exception.impl.WrongEnumTypeException;
import in.prismar.library.spigot.command.exception.impl.WrongNumberFormatException;
import in.prismar.library.spigot.text.TextUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@AllArgsConstructor
public class Arguments {

    private String[] args;

    public String getCombinedArgsFrom(int from) {
        return TextUtil.getArgsFrom(args, from);
    }

    public <T extends Enum> T getEnumType(T[] types, int index) throws CommandException {
        Optional<T> typeOptional = EnumUtil.getEnumByNameOptional(types, getString(index));
        if(!typeOptional.isPresent()) {
            throw new WrongEnumTypeException(types);
        }
        return typeOptional.get();
    }

    public int getInteger(int index) throws CommandException {
        final String input = args[index];
        int number;
        try {
            number = Integer.valueOf(input);
        }catch (NumberFormatException exception) {
            throw new WrongNumberFormatException(input);
        }
        return number;
    }

    public long getLong(int index) throws CommandException {
        final String input = args[index];
        long number;
        try {
            number = Long.valueOf(input);
        }catch (NumberFormatException exception) {
            throw new WrongNumberFormatException(input);
        }
        return number;
    }

    public double getDouble(int index) throws CommandException {
        final String input = args[index];
        double number;
        try {
            number = Double.valueOf(input);
        }catch (NumberFormatException exception) {
            throw new WrongNumberFormatException(input);
        }
        return number;
    }

    public String getString(int index) {
        return args[index];
    }

    public int getLength() {
        return this.args.length;
    }
}
