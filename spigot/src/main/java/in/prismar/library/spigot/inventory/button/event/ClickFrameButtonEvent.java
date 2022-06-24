package in.prismar.library.spigot.inventory.button.event;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public interface ClickFrameButtonEvent extends FrameButtonEvent {

    void onClick(Player player, InventoryClickEvent event);
}
