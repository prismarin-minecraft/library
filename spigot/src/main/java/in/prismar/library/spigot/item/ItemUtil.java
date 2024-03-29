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

    public static boolean compare(ItemStack primary, ItemStack second) {
        if(primary.hasItemMeta()) {
            if(primary.getItemMeta().hasDisplayName()) {
                if(!second.hasItemMeta()) {
                    return false;
                }
                if(!second.getItemMeta().hasDisplayName()) {
                    return false;
                }
                return primary.getItemMeta().getDisplayName().equals(second.getItemMeta().getDisplayName());
            }
        }
        return primary.getType() == second.getType();
    }

    public static boolean compareDeep(ItemStack primary, ItemStack second) {
        if(primary.hasItemMeta()) {
            if(primary.getItemMeta().hasDisplayName()) {
                if(!second.hasItemMeta()) {
                    return false;
                }
                if(!second.getItemMeta().hasDisplayName()) {
                    return false;
                }
                if(primary.getItemMeta().hasLore()) {
                    if(!second.getItemMeta().hasLore()) {
                        return false;
                    }
                    int index = 0;
                    for(String lore : primary.getItemMeta().getLore()) {
                        if(!lore.equals(second.getItemMeta().getLore().get(index))) {
                            return false;
                        }
                        index++;
                    }
                }
                return primary.getItemMeta().getDisplayName().equals(second.getItemMeta().getDisplayName());
            }
        }
        return primary.getType() == second.getType();
    }

    public static void sendFakeMainHeadEquipment(Player player, ItemStack stack) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ClientboundSetEquipmentPacket packet = new ClientboundSetEquipmentPacket(craftPlayer.getEntityId(), List
                .of(new Pair<>(EquipmentSlot.MAINHAND, CraftItemStack.asNMSCopy(stack))));
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
