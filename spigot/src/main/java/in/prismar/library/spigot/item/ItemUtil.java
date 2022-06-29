package in.prismar.library.spigot.item;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public final class ItemUtil {

    public static boolean giveItem(Player player, ItemStack stack) {
        if(player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItem(player.getLocation(), stack);
            return false;
        } else {
            player.getInventory().addItem(stack);
            return true;
        }
    }

    public static boolean hasItemInHand(Player player, boolean left) {
        if(left) {
            if(player.getInventory().getItemInMainHand() != null) {
                if(player.getInventory().getItemInMainHand().getType() != Material.AIR) {
                    return true;
                }
            }
            return false;
        } else {
            if(player.getInventory().getItemInOffHand() != null) {
                if(player.getInventory().getItemInOffHand().getType() != Material.AIR) {
                    return true;
                }
            }
            return false;
        }
    }

    public static boolean hasItemInHandAndHasDisplayName(Player player, boolean left) {
        if(hasItemInHand(player, left)) {
            if(left) {
                if(player.getInventory().getItemInMainHand().hasItemMeta()) {
                    if(player.getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) {
                        return true;
                    }
                }
                return false;
            } else {
                if(player.getInventory().getItemInOffHand().hasItemMeta()) {
                    if(player.getInventory().getItemInOffHand().getItemMeta().hasDisplayName()) {
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }
}
