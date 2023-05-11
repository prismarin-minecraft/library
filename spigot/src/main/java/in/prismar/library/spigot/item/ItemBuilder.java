package in.prismar.library.spigot.item;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class ItemBuilder {

    private final Material material;
    private String name;
    private List<String> lore;

    private int customModelData;

    private Map<Enchantment, Integer> enchantments;

    private ItemFlag[] flags;

    private int amount = 1;

    public ItemBuilder(Material material) {
        this.material = material;
    }

    public ItemBuilder(ItemStack stack) {
        this.material = stack.getType();
        this.amount = stack.getAmount();
        if (stack.hasItemMeta()) {
            ItemMeta meta = stack.getItemMeta();
            if (meta.hasDisplayName()) {
                this.name = meta.getDisplayName();
            }
            if (meta.hasLore()) {
                this.lore = meta.getLore();
            }
            if (meta.hasEnchants()) {
                this.enchantments = meta.getEnchants();
            }
            if (!meta.getItemFlags().isEmpty()) {
                this.flags = meta.getItemFlags().toArray(new ItemFlag[0]);
            }
            if(meta.hasCustomModelData()) {
                if(meta.getCustomModelData() > 0) {
                    this.customModelData = meta.getCustomModelData();
                }
            }

        }
    }

    public ItemBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder glow() {
        addEnchantment(Enchantment.ARROW_DAMAGE, 1);
        return allFlags();
    }

    public ItemBuilder allFlags() {
        this.flags = new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE};
        return this;
    }

    public ItemBuilder addFlags(ItemFlag... flags) {
        this.flags = flags;
        return this;
    }

    public ItemBuilder setCustomModelData(int data) {
        this.customModelData = data;
        return this;
    }

    public ItemBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemBuilder setEnchantments(Map<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        if (this.enchantments == null) {
            this.enchantments = new HashMap<>();
        }
        this.enchantments.put(enchantment, level);
        return this;
    }

    public ItemBuilder addLore(String... lore) {
        if (this.lore == null) {
            this.lore = new ArrayList<>();
        }
        this.lore.addAll(Arrays.asList(lore));
        return this;
    }

    public ItemStack build() {
        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();
        if (name != null) {
            meta.setDisplayName(name);
        }
        if (lore != null) {
            meta.setLore(lore);
        }
        if (enchantments != null) {
            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                meta.addEnchant(entry.getKey(), entry.getValue(), true);
            }
        }
        if (flags != null) {
            meta.addItemFlags(flags);
        }
        if(customModelData > 0) {
            meta.setCustomModelData(customModelData);
        }
        stack.setItemMeta(meta);
        if(amount > 1) {
            stack.setAmount(amount);
        }
        return stack;
    }


}
