package in.prismar.library.spigot.nms;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import in.prismar.library.common.tuple.Tuple;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public final class NmsUtil {

    public static Tuple<String, String> getTextureAndSignature(Player player) {
        ServerPlayer serverPlayer = ((CraftPlayer)player).getHandle();
        GameProfile profile = serverPlayer.getGameProfile();
        Property property = profile.getProperties().get("textures").iterator().next();
        return new Tuple<>(property.getValue(), property.getSignature());
    }
}
