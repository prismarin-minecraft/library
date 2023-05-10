package in.prismar.library.spigot.chat;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class ChatInput {

    private Player player;
    private ChatInputCallback callback;

    @Setter
    private String cancelMessage;

    public ChatInput(Player player, ChatInputCallback callback) {
        this.player = player;
        this.callback = callback;

        ChatInputRequester.getInstance().register(this);
    }
}
