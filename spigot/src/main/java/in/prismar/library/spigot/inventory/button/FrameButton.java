package in.prismar.library.spigot.inventory.button;

import in.prismar.library.spigot.inventory.button.event.FrameButtonEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;


/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class FrameButton {

    @Setter
    private ItemStack item;
    private FrameButtonEvent[] events;
    private boolean cancelClick = true;

    public FrameButton(ItemStack item) {
        this.item = item;
    }

    public FrameButton addEvents(FrameButtonEvent... events) {
        this.events = events;
        return this;
    }

    public FrameButton enableClick() {
        this.cancelClick = false;
        return this;
    }



    public boolean hasEvents() {
        return this.events != null;
    }
}
