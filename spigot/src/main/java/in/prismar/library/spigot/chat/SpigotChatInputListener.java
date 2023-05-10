package in.prismar.library.spigot.chat;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AllArgsConstructor
public class SpigotChatInputListener implements Listener {

    private final ChatInputRequester requester;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if(requester.existsInputByUUID(player.getUniqueId())) {
            ChatInput input = requester.getInputByUUID(player.getUniqueId());
            event.setCancelled(true);
            final String message = event.getMessage();
            if(message.toLowerCase().startsWith("cancel")) {
                requester.unregister(player.getUniqueId());
                player.sendMessage(input.getCancelMessage());
                return;
            }
            if (input.getCallback().onRequestExecute(message)) {
                requester.unregister(player.getUniqueId());
            }

        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(requester.existsInputByUUID(player.getUniqueId())) {
            requester.unregister(player.getUniqueId());
        }
    }
}
