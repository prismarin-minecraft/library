package in.prismar.library.spigot.location.copier;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class Copier {


    private Location pivot;
    private World source;
    private final int xMin;
    private final int xMax;
    private final int yMin;
    private final int yMax;
    private final int zMin;
    private final int zMax;

    private int x;
    private int y;
    private int z;

    private Location offset;

    private long maxWorkerTime;

    private CopierBlockCallback callback;


    @Setter
    private ExecutorService threadPool;

    private Location min;

    private Set<Location> changed;

    public Copier(Location pivot, Location min, Location max) {
        this.pivot = pivot;
        this.changed = new HashSet<>();
        this.min = min;
        this.xMin = Math.min(min.getBlockX(), max.getBlockX());
        this.xMax = Math.max(min.getBlockX(), max.getBlockX());
        this.yMin = Math.min(min.getBlockY(), max.getBlockY());
        this.yMax = Math.max(min.getBlockY(), max.getBlockY());
        this.zMin = Math.min(min.getBlockZ(), max.getBlockZ());
        this.zMax = Math.max(min.getBlockZ(), max.getBlockZ());
        this.source = min.getWorld();
    }

    public void printNumbers() {
        System.out.println("Max: " + xMax + " / " + yMax + " / " + zMax);
        System.out.println("Current: " + x + " / " + y + " / " + z);
    }


    public CompletableFuture<CopierResult> pasteAsync(Plugin plugin, Location location, long maxWorkerTime) {
        CompletableFuture<CopierResult> future = new CompletableFuture<>();
        prepareAsync(location, maxWorkerTime);

        CopierProcess process = new CopierProcess(this, future);
        process.runTaskTimer(plugin, 2, 2);
        return future;
    }

    protected void prepareAsync(Location location, long maxWorkerTime) {
        offset = location.clone().subtract(pivot);
        x = xMin;
        y = yMin;
        z = zMin;
        this.maxWorkerTime = maxWorkerTime;
        changed.clear();
    }

    /**
     * Returns true if something was done
     * @return
     */
    protected boolean work() {
        Preconditions.checkState(offset != null, "Must call prepareAsync() first");
        long now = System.currentTimeMillis();
        for (;this.x <= xMax; ++this.x) {
            for (this.y = yMin;this.y <= yMax; ++this.y) {
                for (this.z = zMin;this.z <= zMax; ++this.z) {
                    Block from = source.getBlockAt(this.x, this.y, this.z);
                    if(changed.contains(from.getLocation())) {
                       continue;
                    }
                    changed.add(from.getLocation());
                    Location pasteLocation = from.getLocation().add(offset);
                    pasteLocation.setWorld(offset.getWorld());

                    Block to = pasteLocation.getBlock();
                    to.setType(from.getType(), false);
                    to.setBlockData(from.getBlockData(), false);


                }
                if(System.currentTimeMillis() - now >= maxWorkerTime) {
                    return true;
                }
            }
        }
        return false;
    }

    public CopierResult paste(Location location) {
        Location offset = location.clone().subtract(pivot);
        for (int x = xMin; x <= xMax; ++x) {
            for (int y = yMin; y <= yMax; ++y) {
                for (int z = zMin; z <= zMax; ++z) {
                    Block from = source.getBlockAt(x, y, z);

                    Location pasteLocation = from.getLocation().add(offset);
                    pasteLocation.setWorld(location.getWorld());

                    Block to = pasteLocation.getBlock();
                    to.setType(from.getType(), false);
                    to.setBlockData(from.getBlockData(), false);


                    BlockState toState = to.getState();
                    BlockState fromState = from.getState();

                    if(toState instanceof Sign toSign && fromState instanceof Sign fromSign) {
                        for (int i = 0; i < fromSign.getLines().length; i++) {
                            toSign.setLine(i, fromSign.getLines()[i]);
                        }
                        toSign.update(true);
                    }

                }
            }
        }
        return new CopierResult(true);
    }

    public ExecutorService getThreadPool() {
        if(threadPool == null) {
            threadPool = Executors.newCachedThreadPool();
        }
        return threadPool;
    }

}
