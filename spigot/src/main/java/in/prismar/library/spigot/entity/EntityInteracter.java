package in.prismar.library.spigot.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class EntityInteracter {

    private final Plugin plugin;

    @Getter
    private final Map<Location, Consumer<Player>> interactions;

    public EntityInteracter(Plugin plugin) {
        this.plugin = plugin;
        this.interactions = new HashMap<>();
        Bukkit.getPluginManager().registerEvents(new PlayerInteractEntityListener(this), plugin);
    }

    public void register(Entity entity, Consumer<Player> consumer) {
        this.interactions.put(entity.getLocation(), consumer);
    }

    public void unregister(Entity entity) {
        this.interactions.remove(entity.getLocation());
    }

    public void unregister(Location location) {
        this.interactions.remove(location);
    }

    @AllArgsConstructor
    public class PlayerInteractEntityListener implements Listener {

        private final EntityInteracter interacter;

        @EventHandler
        public void onCall(PlayerInteractEntityEvent event) {
            if (event.getHand() == EquipmentSlot.HAND) {
                Entity entity = event.getRightClicked();
                if(interacter.getInteractions().containsKey(entity.getLocation())) {
                    interacter.getInteractions().get(entity.getLocation()).accept(event.getPlayer());
                }
            }
        }
    }
}
