package in.prismar.library.spigot.inventory;

import in.prismar.library.spigot.inventory.listener.FrameInventoryClickListener;
import in.prismar.library.spigot.inventory.listener.FrameInventoryCloseListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class FrameBootstrap {

    @Getter
    private static FrameBootstrap instance;

    private final Plugin plugin;
    private Map<UUID, Frame> frames;

    private ExecutorService threadPool;

    public FrameBootstrap(Plugin plugin) {
        this.plugin = plugin;
        this.frames = new ConcurrentHashMap<>();
        this.threadPool = Executors.newCachedThreadPool();
        instance = this;
    }

    public void register() {
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new FrameInventoryClickListener(this), plugin);
        manager.registerEvents(new FrameInventoryCloseListener(this), plugin);
    }
}
