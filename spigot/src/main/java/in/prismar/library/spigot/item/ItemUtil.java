package in.prismar.library.spigot.item;

import com.mojang.datafixers.util.Pair;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.world.entity.EquipmentSlot;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public final class ItemUtil {

    public static void sendFakeEquipment(Player player, EquipmentSlot slot, ItemStack stack) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ClientboundSetEquipmentPacket packet = new ClientboundSetEquipmentPacket(craftPlayer.getEntityId(), List
                .of(new Pair<>(slot, CraftItemStack.asNMSCopy(stack))));
        sendPacket(player, packet);
    }

    private static void sendPacket(Player player, Packet<?> packet) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        craftPlayer.getHandle().connection.send(packet);
    }

    public static ItemStack takeItemFromHand(Player player, boolean right) {
        ItemStack stack = right ? player.getInventory().getItemInMainHand() : player.getInventory().getItemInOffHand();
        if(stack != null) {
            if(stack.getAmount() <= 1) {
                if(right) {
                    player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                } else {
                    player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                }
            } else {
                stack.setAmount(stack.getAmount() - 1);
            }
        }
        player.updateInventory();
        return stack;
    }

    public static boolean giveItem(Player player, ItemStack stack) {
        if(player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItem(player.getLocation(), stack);
            return false;
        } else {
            player.getInventory().addItem(stack);
            return true;
        }
    }

    public static boolean hasItemInHand(Player player, boolean right) {
        if(right) {
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

    public static boolean hasItemInHandAndHasDisplayName(Player player, boolean right) {
        if(hasItemInHand(player, right)) {
            if(right) {
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
