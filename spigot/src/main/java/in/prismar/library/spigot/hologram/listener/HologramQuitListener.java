package in.prismar.library.spigot.hologram.listener;

import lombok.AllArgsConstructor;
import in.prismar.library.spigot.hologram.Hologram;
import in.prismar.library.spigot.hologram.HologramBootstrap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AllArgsConstructor
public class HologramQuitListener implements Listener {

    private final HologramBootstrap bootstrap;

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        for(Hologram hologram : bootstrap.getHolograms()) {
            if(hologram.isGlobal()) {
                hologram.removeViewer(player);
            }
        }
    }
}
