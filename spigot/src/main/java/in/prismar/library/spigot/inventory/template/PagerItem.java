package in.prismar.library.spigot.inventory.template;

import in.prismar.library.spigot.inventory.button.FrameButton;
import in.prismar.library.spigot.inventory.button.event.FrameButtonEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class PagerItem extends FrameButton {
    public PagerItem(ItemStack item, FrameButtonEvent event) {
        super(item);
        this.addEvents(event);
    }

    public PagerItem(ItemStack item) {
        super(item);
    }
}
