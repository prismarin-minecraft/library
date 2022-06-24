package in.prismar.library.spigot.inventory.listener;

import in.prismar.library.spigot.inventory.Frame;
import in.prismar.library.spigot.inventory.FrameBootstrap;
import in.prismar.library.spigot.inventory.event.FrameCloseEvent;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AllArgsConstructor
public class FrameInventoryCloseListener implements Listener {

    private final FrameBootstrap bootstrap;

    @EventHandler
    public void onClick(InventoryCloseEvent event) {
        if(event.getInventory() != null) {
            if(event.getPlayer() instanceof Player player) {
                if (bootstrap.getFrames().containsKey(player.getUniqueId())) {
                    Frame frame = bootstrap.getFrames().remove(player.getUniqueId());
                    frame.getEventBus().publish(new FrameCloseEvent(frame, player, event));
                }
            }
        }
    }
}
