package in.prismar.library.spigot.location;

import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class BlocksProcessor {

    private Plugin plugin;
    private BlockProcessorCallback callback;
    private Set<Block> changed;

    @Setter
    private long maxWorkerTime;

    private final int xMin;
    private final int xMax;
    private final int yMin;
    private final int yMax;
    private final int zMin;
    private final int zMax;

    private int x;
    private int y;
    private int z;


    private final World source;


    public BlocksProcessor(Plugin plugin, Location min, Location max, BlockProcessorCallback callback) {
        this.plugin = plugin;
        this.callback = callback;
        this.maxWorkerTime = 10;
        this.changed = new HashSet<>();

        this.xMin = Math.min(min.getBlockX(), max.getBlockX());
        this.xMax = Math.max(min.getBlockX(), max.getBlockX());
        this.yMin = Math.min(min.getBlockY(), max.getBlockY());
        this.yMax = Math.max(min.getBlockY(), max.getBlockY());
        this.zMin = Math.min(min.getBlockZ(), max.getBlockZ());
        this.zMax = Math.max(min.getBlockZ(), max.getBlockZ());
        this.source = min.getWorld();
    }

    public void start() {
        this.changed.clear();
        this.x = xMin;
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!work()) {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 1, 1);
    }

    protected boolean work() {
        long now = System.currentTimeMillis();
        for (;this.x <= xMax; ++this.x) {
            for (this.y = yMin;this.y <= yMax; ++this.y) {
                for (this.z = zMin;this.z <= zMax; ++this.z) {
                    Block from = source.getBlockAt(this.x, this.y, this.z);
                    if(!changed.contains(from)) {
                        callback.process(from);
                        changed.add(from);
                    }
                }
                if(System.currentTimeMillis() - now >= maxWorkerTime) {
                    return true;
                }
            }
        }
        return false;
    }

    public interface BlockProcessorCallback {

        void process(Block block);
    }
}