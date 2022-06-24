package in.prismar.library.spigot.inventory.event;

import in.prismar.library.common.event.Event;
import in.prismar.library.spigot.inventory.Frame;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@AllArgsConstructor
public class FrameCloseEvent implements Event {

    private final Frame frame;
    private final Player player;
    private final InventoryCloseEvent event;
}
