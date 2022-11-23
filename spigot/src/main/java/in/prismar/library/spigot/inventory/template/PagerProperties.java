package in.prismar.library.spigot.inventory.template;

import in.prismar.library.spigot.item.CustomSkullBuilder;
import in.prismar.library.spigot.item.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Setter
public class PagerProperties {

    private static final ItemStack NEXT_ITEM_DEFAULT = new CustomSkullBuilder("http://textures.minecraft.net/texture/d34ef0638537222b20f480694dadc0f85fbe0759d581aa7fcdf2e43139377158")
            .setName("§8» §aNext page").build();
    private static final ItemStack PREVIOUS_ITEM_DEFAULT = new CustomSkullBuilder("http://textures.minecraft.net/texture/f7aacad193e2226971ed95302dba433438be4644fbab5ebf818054061667fbe2")
            .setName("§8» §ePrevious page").build();
    private static final ItemStack PREVIOUS_ITEM_NO_PAGE_DEFAULT = new ItemBuilder(Material.BARRIER).setName("§8» §cNo previous page").build();

    private final int[] slots;

    private int nextItemSlot;
    private ItemStack nextItem;

    private int previousItemSlot;
    private ItemStack previousItem;
    private ItemStack previousItemNoPage;

    private int showPagesItemSlot;
    private ItemStack showsPagesItem;

    private Sound soundOnSwitchPage;

    public PagerProperties(int[] slots, int rows) {
        this.slots = slots;

        int maxSize = rows * 9 - 1;

        this.nextItemSlot = maxSize;
        this.previousItemSlot = maxSize - 8;

        this.nextItem = NEXT_ITEM_DEFAULT;
        this.previousItem = PREVIOUS_ITEM_DEFAULT;
        this.previousItemNoPage = PREVIOUS_ITEM_NO_PAGE_DEFAULT;

        this.showPagesItemSlot = maxSize - 4;
        this.showsPagesItem = new ItemBuilder(Material.BOOK).setName("§8» §bCurrent§8: §7%page%").build();

        this.soundOnSwitchPage = Sound.UI_BUTTON_CLICK;
    }

    public boolean isShowPagesItem() {
        return this.showPagesItemSlot != -1;
    }
}
