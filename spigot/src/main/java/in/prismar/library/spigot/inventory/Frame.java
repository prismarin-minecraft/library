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

    private FrameProperties properties;
    private Map<Integer, FrameButton> buttons;
    private EventBus eventBus;
    private Inventory output;

    public Frame(String title, int rows) {
        this.properties = new FrameProperties(title, rows);
        this.buttons = new HashMap<>();
        this.eventBus = new EventBus();
    }

    public Frame fill() {
        return fill(Material.GRAY_STAINED_GLASS_PANE);
    }

    public Frame fill(Material filling) {
        this.properties.setFilling(filling);
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

    public Frame updateButton(int slot, ItemStack stack) {
        if(buttons.containsKey(slot)) {
            FrameButton button = buttons.get(slot);
            button.setItem(stack);
            if(getOutput() != null) {
                getOutput().setItem(slot, stack);
            }
        }
        return this;
    }

    protected Frame clearButtons(int... slots) {
        for(int slot : slots) {
            this.buttons.remove(slot);
            if(isBuild()) {
                this.output.setItem(slot, null);
            }
        }
        return this;
    }

    protected Frame clearButtons() {
        for(int slot : getButtons().keySet()) {
            this.buttons.remove(slot);
            if(isBuild()) {
                this.output.setItem(slot, null);
            }
        }
        return this;
    }

    protected void placeItems() {
        for(Map.Entry<Integer, FrameButton> entry : this.buttons.entrySet()) {
            output.setItem(entry.getKey(), entry.getValue().getItem());
        }
    }

    protected Frame buildButtons() {
        if(isBuild()) {
            if(getProperties().isAsync()) {
                FrameBootstrap.getInstance().getThreadPool().submit(() -> {
                    placeItems();
                });
            } else {
                placeItems();
            }
        }
        return this;
    }

    public boolean isBuild() {
        return this.output != null;
    }

    public Frame openInventory(Player player, Sound sound, float volume) {
        if(isBuild()) {
            if(sound != null) {
                player.playSound(player.getLocation(), sound, volume, 1);
            }
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
        output = Bukkit.createInventory(null, properties.getRows() * 9, properties.getTitle());
        if(properties.getFilling() != null) {
            ItemStack filling = new ItemStack(properties.getFilling());
            for (int i = 0; i < output.getSize(); i++) {
                output.setItem(i, filling);
            }
        }

        buildButtons();
        return output;
    }
}
