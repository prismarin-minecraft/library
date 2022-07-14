package in.prismar.library.spigot.hologram;

import in.prismar.library.spigot.hologram.listener.HologramJoinListener;
import in.prismar.library.spigot.hologram.listener.HologramQuitListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class HologramBootstrap implements Runnable{

    @Getter
    private static HologramBootstrap instance;

    private List<Hologram> holograms;

    private double minSpawnDistance;
    private double spaceBetweenLineTexts;
    private double spaceBetweenLineHeads;



    public HologramBootstrap(Plugin plugin) {
        instance = this;

        this.holograms = new ArrayList<>();

        this.minSpawnDistance = 900;
        this.spaceBetweenLineTexts = 0.25;
        this.spaceBetweenLineHeads = 1.25;

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
