package in.prismar.library.spigot.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import in.prismar.library.spigot.reflection.MinecraftReflections;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

@Getter
public class CustomSkullBuilder extends ItemBuilder {

    private String url;

    public CustomSkullBuilder(String url) {
        super(Material.PLAYER_HEAD);
        this.url = url;
    }

    @Override
    public ItemStack build() {
        ItemStack stack = super.build();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        PropertyMap propertyMap = profile.getProperties();
        if (propertyMap == null) {
            throw new IllegalStateException("Profile doesn't contain a property map");
        }
        byte[] encodedData = java.util.Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        propertyMap.put("textures", new Property("textures", new String(encodedData)));
        ItemMeta headMeta = stack.getItemMeta();
        Class<?> headMetaClass = headMeta.getClass();
        MinecraftReflections.getField(headMetaClass, "profile", GameProfile.class).set(headMeta, profile);
        stack.setItemMeta(headMeta);
        return stack;
    }
}