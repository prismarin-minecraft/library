package in.prismar.library.spigot.command;

import lombok.Getter;
import lombok.Setter;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Setter
public abstract class CommandChildNode<T, A extends Arguments> extends CommandNode<T, A> {

    private int index;

    public CommandChildNode(String name) {
        super(name);
    }
}
