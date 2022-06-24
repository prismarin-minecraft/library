package in.prismar.library.spigot.inventory;

import in.prismar.library.common.event.EventBus;
import in.prismar.library.spigot.inventory.button.FrameButton;
import in.prismar.library.spigot.inventory.button.event.FrameButtonEvent;
import in.prismar.library.spigot.item.ItemBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class Frame {

    private String title;
    private int rows;
    private boolean async;
    private Map<Integer, FrameButton> buttons;

    private Inventory output;

    private EventBus eventBus;

    public Frame(String title, int rows) {
        this.title = title;
        this.rows = rows;
        this.async = false;
        this.buttons = new HashMap<>();
        this.eventBus = new EventBus();
    }

    public Frame async() {
        this.async = true;
        return this;
    }

    public Frame fillInventory(Material filling) {
        ItemStack item = new ItemBuilder(filling).setName(" ").build();
        for (int i = 0; i < rows * 9; i++) {
            addButton(i, item);
        }
        return this;
    }

    public Frame addButton(int slot, FrameButton button) {
        this.buttons.put(slot, button);
        return this;
    }

    public Frame addButton(int slot, ItemStack stack) {
        return addButton(slot, new FrameButton(stack));
    }

    public Frame addButton(int slot, ItemStack stack, FrameButtonEvent... events) {
        return addButton(slot, new FrameButton(stack).addEvents(events));
    }

    public Frame clearButtons(int... slots) {
        for(int slot : slots) {
            this.buttons.remove(slot);
            if(isBuild()) {
                this.output.setItem(slot, null);
            }
        }
        return this;
    }

    public Frame clearButtons() {
        for(int slot : getButtons().keySet()) {
            this.buttons.remove(slot);
            if(isBuild()) {
                this.output.setItem(slot, null);
            }
        }
        return this;
    }

    public Frame buildButtons() {
        if(isBuild()) {
            if(async) {
                CompletableFuture.runAsync(() -> {
                    for(Map.Entry<Integer, FrameButton> entry : this.buttons.entrySet()) {
                        output.setItem(entry.getKey(), entry.getValue().getItem());
                    }
                });
            } else {
                for(Map.Entry<Integer, FrameButton> entry : this.buttons.entrySet()) {
                    output.setItem(entry.getKey(), entry.getValue().getItem());
                }
            }
        }
        return this;
    }

    public boolean isBuild() {
        return this.output != null;
    }

    public Frame openInventory(Player player, Sound sound, float volume) {
        if(sound != null && isBuild()) {
            player.openInventory(output);
            FrameBootstrap.getInstance().getFrames().put(player.getUniqueId(), this);
        }
        return this;
    }

    public Frame openInventory(Player player, Sound sound) {
        return openInventory(player, sound, 0.5f);
    }

    public Frame openInventory(Player player) {
        return openInventory(player, null, 0.5f);
    }

    public Inventory build() {
        output = Bukkit.createInventory(null, rows * 9, title);
        buildButtons();
        return output;
    }
}
