package in.prismar.library.spigot.hologram;

import in.prismar.library.spigot.hologram.line.HologramLine;
import in.prismar.library.spigot.packet.PacketReader;
import in.prismar.library.spigot.packet.PacketReaderListener;
import lombok.Getter;
import in.prismar.library.spigot.hologram.listener.HologramJoinListener;
import in.prismar.library.spigot.hologram.listener.HologramQuitListener;
import lombok.Setter;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Setter
public class HologramBootstrap implements Runnable{

    @Getter
    private static HologramBootstrap instance;

    private List<Hologram> holograms;

    private double minSpawnDistance;
    private double spaceBetweenLineTexts;
    private double spaceBetweenLineHeads;

    private PacketReader reader;

    private final Class<?> packetInUseClass;
    private final Field entityIdField;



    public HologramBootstrap(Plugin plugin) {
        instance = this;

        this.holograms = new ArrayList<>();

        this.minSpawnDistance = 900;
        this.spaceBetweenLineTexts = 0.25;
        this.spaceBetweenLineHeads = 1.25;

        this.reader = new PacketReader("Hologram", plugin, true);

        try {
            this.packetInUseClass = Class.forName("net.minecraft.network.protocol.game.PacketPlayInUseEntity");
            this.entityIdField = packetInUseClass.getDeclaredField("a");
            this.entityIdField.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.reader.addListener((player, packet) -> {
            if(packet.getClass().getSimpleName().equals("PacketPlayInUseEntity")) {
                if(this.entityIdField.canAccess(packet)) {
                    try {
                        int entityId = entityIdField.getInt(packet);
                        for(Hologram hologram : getHolograms()) {
                            if(hologram.getInteraction() == null) {
                                continue;
                            }
                            if(!hologram.existsViewer(player)) {
                                continue;
                            }
                            HologramViewer viewer = hologram.getViewer(player);
                            for(HologramLine line : viewer.getLines()) {
                                if(line.getStand().getId() == entityId) {
                                    hologram.getInteraction().onInteract(player);
                                    return true;
                                }
                            }
                        }
                    } catch (IllegalAccessException e) {
                        System.out.println("There was an error while trying to get entity id");
                    }
                }
            }
            return true;
        });

        Bukkit.getScheduler().runTaskTimer(plugin, this, 20, 20);

        Bukkit.getPluginManager().registerEvents(new HologramJoinListener(this), plugin);
        Bukkit.getPluginManager().registerEvents(new HologramQuitListener(this), plugin);
    }

    public void addHologram(Hologram hologram) {
        this.holograms.add(hologram);
        if(hologram.isGlobal()) {
            for(Player online : Bukkit.getOnlinePlayers()) {
                hologram.addViewer(online);
            }
        }
    }

    public void removeHologram(Hologram hologram) {
        this.holograms.remove(hologram);
        hologram.clear();
    }


    @Override
    public void run() {
        for(Hologram hologram : this.holograms) {
            hologram.updateViewers();
        }
    }
}
