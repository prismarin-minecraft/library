package in.prismar.library.spigot.chat;


import lombok.Getter;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class ChatInputRequester {

    @Getter
    private static ChatInputRequester instance;

    private final Map<UUID, ChatInput> requests;

    public ChatInputRequester(Plugin plugin) {
        instance = this;
        this.requests = new HashMap<>();
        plugin.getServer().getPluginManager().registerEvents(new SpigotChatInputListener(this), plugin);
    }

    public void register(ChatInput input) {
        requests.put(input.getPlayer().getUniqueId(), input);
    }

    public void unregister(UUID uuid) {
        requests.remove(uuid);
    }

    public boolean existsInputByUUID(UUID uuid) {
        return requests.containsKey(uuid);
    }

    public ChatInput getInputByUUID(UUID uuid) {
        return requests.get(uuid);
    }


}
