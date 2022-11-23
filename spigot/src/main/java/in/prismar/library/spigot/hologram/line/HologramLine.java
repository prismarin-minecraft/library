package in.prismar.library.spigot.hologram.line;

import com.mojang.datafixers.util.Pair;
import lombok.Getter;
import net.minecraft.network.chat.ChatMessageContent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;
import in.prismar.library.common.tuple.Tuple;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class HologramLine {

    private ArmorStand stand;
    private final Location startLocation;
    private HologramLineType type;
    private Object content;

    private boolean small;
    private final List<Tuple<EquipmentSlot, ItemStack>> equipment;

    public HologramLine(Location startLocation, HologramLineType type, Object content, boolean small) {
        this.startLocation = startLocation;
        this.equipment = new ArrayList<>();
        this.type = type;
        this.content = content;
        this.small = small;
        createStand();
    }

    protected void createStand() {
        Level level = ((CraftWorld)startLocation.getWorld()).getHandle();
        this.stand = new ArmorStand(level, startLocation.getX(), startLocation.getY(), startLocation.getZ());
        this.stand.setSmall(small);
        this.stand.setInvisible(true);
        updateTypeAndContent();
    }

    private void updateTypeAndContent() {
        switch (type) {
            case TEXT:
                this.stand.setCustomName(new ChatMessageContent(content.toString()).decorated());
                this.stand.setCustomNameVisible(true);
                break;
            case ITEM_HEAD:
                addEquipment(EquipmentSlot.HEAD, (ItemStack) content);
                this.stand.setCustomNameVisible(false);
                break;
        }
    }

    public void update(Player player, HologramLineType type, Object content) {
        this.type = type;
        this.content = content;
        updateTypeAndContent();
        sendMetaData(player);
    }

    public void spawn(Player player) {
        ClientboundAddEntityPacket packet = new ClientboundAddEntityPacket(stand);
        sendPacket(player, packet);

        sendMetaData(player);
        sendEquipment(player);
    }


    public void despawn(Player player) {
        ClientboundRemoveEntitiesPacket packet = new ClientboundRemoveEntitiesPacket(stand.getBukkitEntity().getEntityId());
        sendPacket(player, packet);
    }

    public void addEquipment(EquipmentSlot slot, ItemStack stack) {
        this.equipment.add(new Tuple<>(slot, stack));
        this.stand.setItemSlot(slot, CraftItemStack.asNMSCopy(stack));
    }

    protected void sendEquipment(Player player) {
        if(!this.equipment.isEmpty()) {
            List<Pair<EquipmentSlot, net.minecraft.world.item.ItemStack>> pairs = new ArrayList<>();
            for(Tuple<EquipmentSlot, ItemStack> tuple : equipment) {
                net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(tuple.getSecond());
                pairs.add(new Pair<>(tuple.getFirst(), nmsItem));
            }
            ClientboundSetEquipmentPacket packet = new ClientboundSetEquipmentPacket(stand.getBukkitEntity().getEntityId(), pairs);
            sendPacket(player, packet);
        }
    }

    public void sendMetaData(Player player) {
        ClientboundSetEntityDataPacket packet = new ClientboundSetEntityDataPacket(stand.getBukkitEntity().getEntityId(), stand.getEntityData(), true);
        sendPacket(player, packet);
    }

    protected void sendPacket(Player player, Packet<?> packet) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        craftPlayer.getHandle().connection.send(packet);
    }
}
