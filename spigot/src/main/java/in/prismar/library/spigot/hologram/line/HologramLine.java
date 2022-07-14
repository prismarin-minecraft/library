package in.prismar.library.spigot.hologram.line;

import com.mojang.datafixers.util.Pair;
import in.prismar.library.common.tuple.Tuple;
import lombok.Getter;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
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

    private EntityArmorStand stand;
    private final Location startLocation;
    private HologramLineType type;
    private Object content;
    private final List<Tuple<EnumItemSlot, ItemStack>> equipment;

    public HologramLine(Location startLocation, HologramLineType type, Object content) {
        this.startLocation = startLocation;
        this.equipment = new ArrayList<>();
        this.type = type;
        this.content = content;
        createStand();
    }

    protected void createStand() {
        World world = ((CraftWorld)startLocation.getWorld()).getHandle();
        this.stand = new EntityArmorStand(world, startLocation.getX(), startLocation.getY(), startLocation.getZ());
        this.stand.j(true);
        updateTypeAndContent();
    }

    private void updateTypeAndContent() {
        switch (type) {
            case TEXT:
                this.stand.a(new ChatComponentText((String) content));
                this.stand.n(true);
                break;
            case ITEM_HEAD:
                addEquipment(EnumItemSlot.f, (ItemStack) content);
                this.stand.n(false);
                break;
        }
    }

    public void update(HologramLineType type, Object content) {
        this.type = type;
        this.content = content;
        updateTypeAndContent();
    }

    public void spawn(Player player) {
        PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity(stand);
        sendPacket(player, packet);

        sendMetaData(player);
        sendEquipment(player);
    }

    public void despawn(Player player) {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(stand.getBukkitEntity().getEntityId());
        sendPacket(player, packet);
    }

    public void addEquipment(EnumItemSlot slot, ItemStack stack) {
        this.equipment.add(new Tuple<>(slot, stack));
    }

    protected void sendEquipment(Player player) {
        if(!this.equipment.isEmpty()) {
            List<Pair<EnumItemSlot, net.minecraft.world.item.ItemStack>> pairs = new ArrayList<>();
            for(Tuple<EnumItemSlot, ItemStack> tuple : equipment) {
                pairs.add(new Pair<>(tuple.getFirst(), CraftItemStack.asNMSCopy(tuple.getSecond())));
            }
            PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(stand.getBukkitEntity().getEntityId(), pairs);
            sendPacket(player, packet);
        }
    }

    protected void sendMetaData(Player player) {
        PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(stand.getBukkitEntity().getEntityId(), stand.ai(), true);
        sendPacket(player, packet);
    }

    protected void sendPacket(Player player, Packet<?> packet) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        craftPlayer.getHandle().b.a(packet);
    }
}
