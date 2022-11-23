package in.prismar.library.spigot.inventory.template;

import in.prismar.library.spigot.inventory.Frame;
import in.prismar.library.spigot.inventory.button.event.ClickFrameButtonEvent;
import in.prismar.library.spigot.inventory.button.event.FrameButtonEvent;
import in.prismar.library.spigot.item.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class Pager extends Frame {

    private static final int[] DEFAULT_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43,
    };

    private final PagerProperties pagerProperties;

    private List<PagerItem> items;

    @Setter
    private int currentPage = 1;

    public Pager(String title, int rows, int[] slots) {
        super(title, rows);
        this.pagerProperties = new PagerProperties(slots, rows);
        this.items = new ArrayList<>();
    }

    public Pager(String title, int rows) {
        super(title, rows);
        this.pagerProperties = new PagerProperties(DEFAULT_SLOTS, rows);
        this.items = new ArrayList<>();
    }

    private void firstRender() {
        addButton(pagerProperties.getNextItemSlot(), pagerProperties.getNextItem(), (ClickFrameButtonEvent) (player, event) -> {
            if (openNextPage()) {
                player.playSound(player.getLocation(), pagerProperties.getSoundOnSwitchPage(), 0.65f, 1);
            }
        });

        addButton(pagerProperties.getPreviousItemSlot(), pagerProperties.getPreviousItemNoPage(), (ClickFrameButtonEvent) (player, event) -> {
            if (openPreviousPage()) {
                player.playSound(player.getLocation(), pagerProperties.getSoundOnSwitchPage(), 0.65f, 1);
            }
        });
        if(pagerProperties.isShowPagesItem()) {
            addButton(pagerProperties.getShowPagesItemSlot(), pagerProperties.getShowsPagesItem());
        }
    }

    private void render() {
        updateButton(pagerProperties.getPreviousItemSlot(), currentPage >= 2 ? pagerProperties.getPreviousItem() : pagerProperties.getPreviousItemNoPage());
        updateShowPagesItem();
        clearButtons(pagerProperties.getSlots());

        List<PagerItem> items = currentPage == 1 ? loadItems(0, pagerProperties.getSlots().length) :
                loadItems(((currentPage-1) * pagerProperties.getSlots().length), currentPage * pagerProperties.getSlots().length);
        int index = 0;
        for(PagerItem item : items) {
            addButton(pagerProperties.getSlots()[index], item);
            index++;
        }
        buildButtons();
    }

    public boolean openNextPage() {
        final int size = items.size();
        final int full = currentPage * pagerProperties.getSlots().length;
        if(size > full) {
            setCurrentPage(currentPage + 1);
            render();
            return true;
        }
        return false;
    }

    public boolean openPreviousPage() {
        if(currentPage > 1) {
            setCurrentPage(currentPage - 1);
            render();
            return true;
        }
        return false;
    }

    private void updateShowPagesItem() {
        if(pagerProperties.isShowPagesItem()) {
            ItemBuilder builder = new ItemBuilder(pagerProperties.getShowsPagesItem());
            builder.setName(builder.getName().replace("%page%", "" + currentPage));
            updateButton(pagerProperties.getShowPagesItemSlot(), builder.build());
        }
    }

    public Pager update() {
        clearButtons(pagerProperties.getSlots());
        render();
        return this;
    }

    public Pager addItem(PagerItem item) {
        items.add(item);
        return this;
    }

    public Pager addItem(ItemStack stack) {
        return addItem(new PagerItem(stack));
    }

    public Pager addItem(ItemStack stack, FrameButtonEvent event) {
        return addItem(new PagerItem(stack, event));
    }

    public List<PagerItem> loadItems(int from, int to) {
        List<PagerItem> newList = new ArrayList<>();
        for (int i = from; i < to; i++) {
            if(items.size() > i){
                if(items.get(i) != null){
                    newList.add(items.get(i));
                }
            }
        }
        return newList;
    }

    @Override
    public Inventory build() {
        firstRender();
        Inventory inventory = super.build();
        render();
        return inventory;
    }
}
