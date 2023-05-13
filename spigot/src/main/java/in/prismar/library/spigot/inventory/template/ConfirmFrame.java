package in.prismar.library.spigot.inventory.template;

import in.prismar.library.spigot.inventory.Frame;
import in.prismar.library.spigot.inventory.button.event.ClickFrameButtonEvent;
import in.prismar.library.spigot.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class ConfirmFrame extends Frame {

    private static final int[] YES_SLOTS = {1, 2, 3, 10, 11, 12, 19, 20, 21};
    private static final int[] NO_SLOTS = {5, 6, 7, 14, 15, 16, 23, 24, 25};
    private static final ItemStack YES_ITEM = new ItemBuilder(Material.LIME_WOOL).setName("§aYes").allFlags().build();
    private static final ItemStack NO_ITEM = new ItemBuilder(Material.RED_WOOL).setName("§cNo").allFlags().build();

    private final ConfirmCallback callback;
    public ConfirmFrame(ConfirmCallback callback) {
        super("§7Are you sure?", 3);
        fill(Material.BLACK_STAINED_GLASS_PANE);
        this.callback = callback;

        for(int slot : YES_SLOTS) {
            addButton(slot, YES_ITEM, (ClickFrameButtonEvent) (player, event) -> {
                callback.onSuccess(player);
            });
        }
        for(int slot : NO_SLOTS) {
            addButton(slot, NO_ITEM, (ClickFrameButtonEvent) (player, event) -> {
                callback.onCancel(player);
            });
        }
        build();
    }

    public interface ConfirmCallback {

        void onSuccess(Player player);
        void onCancel(Player player);
    }
}
