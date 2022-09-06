package in.prismar.library.spigot.item.container;

import in.prismar.library.spigot.serializer.BukkitObjectSerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Setter
@NoArgsConstructor
public class ItemContainer {

    private String value;
    private transient ItemStack item;

    public ItemContainer(ItemStack item) {
        this.item = item;
        serialize();
    }

    public void serialize() {
        this.value = BukkitObjectSerializer.getSerializer().serialize(item);
    }

    public void deserialize() {
        this.item = (ItemStack) BukkitObjectSerializer.getSerializer().deserialize(value);
    }
}
