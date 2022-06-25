package in.prismar.library.spigot.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

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

    public String getString(int index) {
        return args[index];
    }

    public int getLength() {
        return this.args.length;
    }
}
