package me.teakivy.lobby.Bedwars.Inventory;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class InventoryHelper {

    public static ItemStack createItem(Material material, String name, String[] lore, int amount, byte data) {
        ItemStack item = new ItemStack(material, amount, data);
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta.getLore() != null) {
            itemMeta.getLore().clear();
        }
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        itemMeta.setDisplayName(name);
        itemMeta.setLore(Arrays.asList(lore));
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack createItem(Material material, String name, String[] lore, int amount) {
        return createItem(material, name, lore, amount, (byte) 0);
    }

    public static ItemStack createItem(Material material, String name, String[] lore) {
        return createItem(material, name, lore, 1, (byte) 0);
    }

    public static ItemStack createItem(Material material, String name) {
        return createItem(material, name, new String[]{""}, 1, (byte) 0);
    }
}
