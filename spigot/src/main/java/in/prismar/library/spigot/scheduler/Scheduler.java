package in.prismar.library.spigot.scheduler;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public final class Scheduler {

    private static Plugin plugin;

    public static void setPlugin(Plugin plugin) {
        Scheduler.plugin = plugin;
    }

    public static BukkitTask run(Runnable runnable) {
        Preconditions.checkNotNull(plugin, "Plugin was not set");
        return Bukkit.getScheduler().runTask(plugin, runnable);
    }

    public static BukkitTask runDelayed(long ticks, Runnable runnable) {
        Preconditions.checkNotNull(plugin, "Plugin was not set");
        return Bukkit.getScheduler().runTaskLater(plugin, runnable, ticks);
    }

    public static BukkitTask runTimer(long delay, long repeat, Runnable runnable) {
        Preconditions.checkNotNull(plugin, "Plugin was not set");
        return Bukkit.getScheduler().runTaskTimer(plugin, runnable, delay, repeat);
    }

    public static BukkitTask runTimer(long delay, long repeat, SchedulerRunnable runnable) {
        Preconditions.checkNotNull(plugin, "Plugin was not set");
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                if(runnable.isCancelled()) {
                    cancel();
                    return;
                }
                runnable.run();
                runnable.setCurrentTicks(runnable.getCurrentTicks() + repeat);
            }
        }.runTaskTimer(plugin, delay, repeat);
        return task;
    }

    public static BukkitTask runTimerFor(long delay, long repeat, long forTicks, SchedulerRunnable runnable) {
        Preconditions.checkNotNull(plugin, "Plugin was not set");
        runnable.setCurrentTicks(forTicks);
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                if(runnable.isCancelled()) {
                    cancel();
                    return;
                }
                runnable.run();
                runnable.setCurrentTicks(runnable.getCurrentTicks() - repeat);
                if(runnable.getCurrentTicks() <= 0) {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, delay, repeat);
        return task;
    }
}
