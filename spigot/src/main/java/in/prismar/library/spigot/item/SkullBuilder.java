package in.prismar.library.spigot.item;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

@Getter
public class SkullBuilder extends ItemBuilder {

    private String owner;

    public SkullBuilder(String owner) {
        super(Material.PLAYER_HEAD);
        this.owner = owner;
    }

    @Override
    public ItemStack build() {
        ItemStack stack = super.build();
        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        meta.setOwner(owner);
        stack.setItemMeta(meta);
        return stack;
    }
}