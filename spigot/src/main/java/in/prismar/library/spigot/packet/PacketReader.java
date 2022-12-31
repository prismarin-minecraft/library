package in.prismar.library.spigot.packet;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.Getter;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.*;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class PacketReader implements Listener {

    private String name;
    private Plugin plugin;
    private Map<UUID, Channel> players;
    private List<PacketReaderListener> listeners;


    public PacketReader(String name, Plugin plugin, boolean autoInjection) {
        this.name = name;
        this.plugin = plugin;
        this.listeners = new ArrayList<>();
        this.players = new HashMap<>();

        if(autoInjection) {
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        inject(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        uninject(player);
    }

    public PacketReader addListener(PacketReaderListener listener) {
        this.listeners.add(listener);
        return this;
    }

    public PacketReader inject(Player player) {
        if(!players.containsKey(player.getUniqueId())) {
            try {
                CraftPlayer craftPlayer = (CraftPlayer) player;
                Channel channel = craftPlayer.getHandle().connection.connection.channel;
                channel.pipeline().addBefore("packet_handler", name, new ChannelDuplexHandler() {
                    @Override
                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                        super.write(ctx, msg, promise);
                    }

                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        boolean success = true;
                        for(PacketReaderListener listener : listeners) {
                            success = listener.onPacket(player, msg);
                        }
                        if(success) {
                            super.channelRead(ctx, msg);
                        }
                    }
                });
            }catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return this;
    }

    public void uninject(Player player) {
        if(players.containsKey(player.getUniqueId())) {
            Channel channel = players.get(player.getUniqueId());
            if(channel.pipeline().get(name) != null) {
                channel.pipeline().remove(name);
            }
            players.remove(player.getUniqueId());
        }
    }
}
