package in.prismar.library.spigot.inventory.listener;

import in.prismar.library.spigot.inventory.Frame;
import in.prismar.library.spigot.inventory.FrameBootstrap;
import in.prismar.library.spigot.inventory.button.FrameButton;
import in.prismar.library.spigot.inventory.button.event.ClickFrameButtonEvent;
import in.prismar.library.spigot.inventory.button.event.FrameButtonEvent;
import in.prismar.library.spigot.inventory.event.FrameClickEvent;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AllArgsConstructor
public class FrameInventoryClickListener implements Listener {

    private final FrameBootstrap bootstrap;

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(event.getCurrentItem() != null) {
            if(event.getWhoClicked() instanceof Player player) {
                if (bootstrap.getFrames().containsKey(player.getUniqueId())) {
                    Frame frame = bootstrap.getFrames().get(player.getUniqueId());
                    if(frame.getProperties().getFilling() != null) {
                        if(event.getCurrentItem().isSimilar(frame.getProperties().getFilling())) {
                            event.setCancelled(true);
                            event.setResult(Event.Result.DENY);
                            return;
                        }
                    }
                    frame.getEventBus().publish(new FrameClickEvent(frame, player, event));

                    if(frame.getButtons().containsKey(event.getRawSlot())) {
                        FrameButton button = frame.getButtons().get(event.getRawSlot());
                        if(button.isCancelClick()) {
                            event.setCancelled(true);
                            event.setResult(Event.Result.DENY);
                        }
                        if(button.hasEvents()) {
                            for(FrameButtonEvent buttonEvent : button.getEvents()) {
                                if(buttonEvent instanceof ClickFrameButtonEvent clickFrameButtonEvent) {
                                    clickFrameButtonEvent.onClick(player, event);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
