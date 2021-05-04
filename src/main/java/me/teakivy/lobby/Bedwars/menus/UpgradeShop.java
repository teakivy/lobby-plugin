package me.teakivy.lobby.Bedwars.menus;

import me.teakivy.lobby.Bedwars.util.BedwarsInventory;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class UpgradeShop {

    public static void openShopKeeper(Player player) {
        Inventory inv = player.getInventory();

        int ironAmount = 0;
        int goldAmount = 0;
        int diamondAmount = 0;
        int emeraldAmount = 0;
        for (int i = 0; i < 36; i++) {
            if (inv.getItem(i) != null) {
                if (inv.getItem(i).getType() == Material.IRON_INGOT) {
                    ironAmount = ironAmount + inv.getItem(i).getAmount();
                }
                if (inv.getItem(i).getType() == Material.GOLD_INGOT) {
                    goldAmount = goldAmount + inv.getItem(i).getAmount();
                }
                if (inv.getItem(i).getType() == Material.DIAMOND) {
                    diamondAmount = diamondAmount + inv.getItem(i).getAmount();
                }
                if (inv.getItem(i).getType() == Material.EMERALD) {
                    emeraldAmount = emeraldAmount + inv.getItem(i).getAmount();
                }
            }
        }

        Inventory shopGUI = Bukkit.createInventory(player, 3 * 9, ChatColor.GOLD + "Upgrades Shop");

        ItemStack air = new ItemStack(Material.AIR);

        ItemStack Sharpness = createItem(Material.IRON_SWORD, canPurchaseColor(diamondAmount >= 4) + "Sharpness", new String[]{ChatColor.GRAY + "Cost: " + ChatColor.AQUA + "4 Diamonds", "", ChatColor.GRAY + "Protect yourself at full" + ChatColor.GRAY + "power!", "", canPurchaseText(diamondAmount >= 4, "Diamonds")});
        ItemStack Protection1 = createItem(Material.IRON_CHESTPLATE, canPurchaseColor(diamondAmount >= 2) + "Protection I", new String[]{ChatColor.GRAY + "Cost: " + ChatColor.AQUA + "4 Diamonds", "", ChatColor.GRAY + "Protect yourself at full" + ChatColor.GRAY + "power!", "", canPurchaseText(diamondAmount >= 4, "Diamonds")});
        ItemStack SelectorBlocks = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Blocks", new String[]{ChatColor.GRAY + "Cross those gaps and", ChatColor.GRAY + "keep your bed safe!"}, 1, (byte) 7);
        ItemStack SelectorArmor = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Armor", new String[]{ChatColor.GRAY + "Protect your head from", ChatColor.GRAY + "falling swords!"}, 1, (byte) 7);
        ItemStack SelectorTools = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Tools", new String[]{ChatColor.GRAY + "Mine those bed defenses", ChatColor.GRAY + "at max speed!"}, 1, (byte) 7);
        ItemStack SelectorProjectiles = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Projectiles", new String[]{ChatColor.GRAY + "Shoot those enemies down!"}, 1, (byte) 7);
        ItemStack SelectorPotions = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Potions", new String[]{ChatColor.GRAY + "Shh! Sneaky Magic!"}, 1, (byte) 7);
        ItemStack SelectorUtilities = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Utilities", new String[]{ChatColor.GRAY + "Got something to do?", ChatColor.GRAY + "I got something for it!"}, 1, (byte) 7);
        ItemStack SelectorEmpty = createItem(Material.STAINED_GLASS_PANE," ", new String[]{""}, 1, (byte) 7);

        ItemStack ItemQuickBuy = createItem(Material.NETHER_STAR, ChatColor.YELLOW + "Quick Buy", new String[]{ChatColor.GRAY + "Items added to your quick", ChatColor.GRAY + "buy will appear here!"});
        ItemStack ItemMelee = createItem(Material.GOLD_SWORD, ChatColor.YELLOW + "Melee", new String[]{ChatColor.GRAY + "Attack your enemies at", ChatColor.GRAY + "full power!"});
        ItemStack ItemBlocks = createItem(Material.HARD_CLAY, ChatColor.YELLOW + "Blocks", new String[]{ChatColor.GRAY + "Cross those gaps and", ChatColor.GRAY + "keep your bed safe!"});
        ItemStack ItemArmor = createItem(Material.CHAINMAIL_BOOTS, ChatColor.YELLOW + "Armor", new String[]{ChatColor.GRAY + "Protect your head from", ChatColor.GRAY + "falling swords!"});
        ItemStack ItemTools = createItem(Material.STONE_PICKAXE, ChatColor.YELLOW + "Tools", new String[]{ChatColor.GRAY + "Mine those bed defenses", ChatColor.GRAY + "at max speed!"});
        ItemStack ItemProjectiles = createItem(Material.BOW, ChatColor.YELLOW + "Projectiles", new String[]{ChatColor.GRAY + "Shoot those enemies down!"});
        ItemStack ItemPotions = createItem(Material.BREWING_STAND_ITEM, ChatColor.YELLOW + "Potions", new String[]{ChatColor.GRAY + "Shh! Sneaky Magic!"});
        ItemStack ItemUtilities = createItem(Material.TNT, ChatColor.YELLOW + "Utilities", new String[]{ChatColor.GRAY + "Got something to do?", ChatColor.GRAY + "I got something for it!"});

        ItemStack QuickBuy = createItem(Material.STAINED_GLASS_PANE, ChatColor.GOLD + "Quick Buy", new String[]{ChatColor.GRAY + "Shift click an item to", ChatColor.GRAY + "add it to your Quick Buy"}, 1, (byte) 14);

        ItemStack ItemHotbarManager = createItem(Material.BLAZE_POWDER, ChatColor.YELLOW + "Hotbar Manager", new String[]{ChatColor.GRAY + "Change your hotbar defaults!", "", ChatColor.GRAY + "Coming Soon..."});

        shopGUI.setItem(0, ItemQuickBuy);
        shopGUI.setItem(1, ItemBlocks);
        shopGUI.setItem(2, ItemMelee);
        shopGUI.setItem(3, ItemArmor);
        shopGUI.setItem(4, ItemTools);
        shopGUI.setItem(5, ItemProjectiles);
        shopGUI.setItem(6, ItemPotions);
        shopGUI.setItem(7, ItemUtilities);

//        shopGUI.setItem(9, SelectorQuickBuy);
        shopGUI.setItem(10, SelectorBlocks);
//        shopGUI.setItem(11, SelectorMelee);
        shopGUI.setItem(12, SelectorArmor);
        shopGUI.setItem(13, SelectorTools);
        shopGUI.setItem(14, SelectorProjectiles);
        shopGUI.setItem(15, SelectorPotions);
        shopGUI.setItem(16, SelectorUtilities);
        shopGUI.setItem(17, SelectorEmpty);


// createShopItem(Material.WOOL, "Wool", 4, ironAmount, "Iron", 16)

//        shopGUI.setItem(19, createShopItem(Material.WOOL, "Wool", 4, ironAmount, "Iron", 16));
//        shopGUI.setItem(20, createShopItem(Material.STONE_SWORD, "Stone Sword", 10, ironAmount, "Iron"));
//        shopGUI.setItem(21, createArmorShopItem(player, Material.CHAINMAIL_BOOTS, "Permanent Chainmail Armor", 40, ironAmount, "Iron"));
//        shopGUI.setItem(22, QuickBuy);
//        shopGUI.setItem(23, createShopItem(Material.BOW, "Bow", 12, goldAmount, "Gold"));
//        shopGUI.setItem(24, createShopItem(Material.POTION, "Speed Potion II (45 Seconds)", 1, emeraldAmount, "Emerald", 1, (byte) 8195));
//        shopGUI.setItem(25, createShopItem(Material.TNT, "TNT", 4, goldAmount, "Gold"));
//        shopGUI.setItem(28, createShopItem(Material.WOOD, "Wood", 4, goldAmount, "Gold", 16));
//        shopGUI.setItem(29, createShopItem(Material.IRON_SWORD, "Iron Sword", 7, goldAmount, "Gold"));
//        shopGUI.setItem(30, createArmorShopItem(player, Material.IRON_BOOTS, "Permanent Iron Armor", 12, goldAmount, "Gold"));
//        shopGUI.setItem(31, createShearShopItem(ironAmount, player));
//        shopGUI.setItem(32, createShopItem(Material.ARROW, "Arrow", 2, goldAmount, "Gold", 8));
//        shopGUI.setItem(33, createShopItem(Material.POTION, "Invisibility Potion (30 Seconds)", 2, emeraldAmount, "Emerald", 1, (byte) 8206));
//        shopGUI.setItem(34, createShopItem(Material.WATER_BUCKET, "Water Bucket", 3, goldAmount, "Gold"));

        Document doc = BedwarsInventory.getQuickBuyDocument(player);

        shopGUI.setItem(19, BedwarsInventory.getQuickBuy(19, doc, player));
        shopGUI.setItem(20, BedwarsInventory.getQuickBuy(20, doc, player));
        shopGUI.setItem(21, BedwarsInventory.getQuickBuy(21, doc, player));
        shopGUI.setItem(22, BedwarsInventory.getQuickBuy(22, doc, player));
        shopGUI.setItem(23, BedwarsInventory.getQuickBuy(23, doc, player));
        shopGUI.setItem(24, BedwarsInventory.getQuickBuy(24, doc, player));
        shopGUI.setItem(25, BedwarsInventory.getQuickBuy(25, doc, player));

        shopGUI.setItem(28, BedwarsInventory.getQuickBuy(28, doc, player));
        shopGUI.setItem(29, BedwarsInventory.getQuickBuy(29, doc, player));
        shopGUI.setItem(30, BedwarsInventory.getQuickBuy(30, doc, player));
        shopGUI.setItem(31, BedwarsInventory.getQuickBuy(31, doc, player));
        shopGUI.setItem(32, BedwarsInventory.getQuickBuy(32, doc, player));
        shopGUI.setItem(33, BedwarsInventory.getQuickBuy(33, doc, player));
        shopGUI.setItem(34, BedwarsInventory.getQuickBuy(34, doc, player));

        shopGUI.setItem(37, BedwarsInventory.getQuickBuy(37, doc, player));
        shopGUI.setItem(38, BedwarsInventory.getQuickBuy(38, doc, player));
        shopGUI.setItem(39, BedwarsInventory.getQuickBuy(39, doc, player));
        shopGUI.setItem(40, BedwarsInventory.getQuickBuy(40, doc, player));
        shopGUI.setItem(41, BedwarsInventory.getQuickBuy(41, doc, player));
        shopGUI.setItem(42, BedwarsInventory.getQuickBuy(42, doc, player));
        shopGUI.setItem(43, BedwarsInventory.getQuickBuy(43, doc, player));


        player.openInventory(shopGUI);
    }

    public static void updateMainShop(Player player) {
        Inventory inv = player.getInventory();
        int ironAmount = 0;
        int goldAmount = 0;
        int diamondAmount = 0;
        int emeraldAmount = 0;
        for (int i = 0; i < 36; i++) {
            if (inv.getItem(i) != null) {
                if (inv.getItem(i).getType() == Material.IRON_INGOT) {
                    ironAmount = ironAmount + inv.getItem(i).getAmount();
                }
                if (inv.getItem(i).getType() == Material.GOLD_INGOT) {
                    goldAmount = goldAmount + inv.getItem(i).getAmount();
                }
                if (inv.getItem(i).getType() == Material.DIAMOND) {
                    diamondAmount = diamondAmount + inv.getItem(i).getAmount();
                }
                if (inv.getItem(i).getType() == Material.EMERALD) {
                    emeraldAmount = emeraldAmount + inv.getItem(i).getAmount();
                }
            }
        }

        Inventory shopGUI = player.getOpenInventory().getTopInventory();
        Document doc = BedwarsInventory.getQuickBuyDocument(player);

        shopGUI.setItem(19, BedwarsInventory.getQuickBuy(19, doc, player));
        shopGUI.setItem(20, BedwarsInventory.getQuickBuy(20, doc, player));
        shopGUI.setItem(21, BedwarsInventory.getQuickBuy(21, doc, player));
        shopGUI.setItem(22, BedwarsInventory.getQuickBuy(22, doc, player));
        shopGUI.setItem(23, BedwarsInventory.getQuickBuy(23, doc, player));
        shopGUI.setItem(24, BedwarsInventory.getQuickBuy(24, doc, player));
        shopGUI.setItem(25, BedwarsInventory.getQuickBuy(25, doc, player));

        shopGUI.setItem(28, BedwarsInventory.getQuickBuy(28, doc, player));
        shopGUI.setItem(29, BedwarsInventory.getQuickBuy(29, doc, player));
        shopGUI.setItem(30, BedwarsInventory.getQuickBuy(30, doc, player));
        shopGUI.setItem(31, BedwarsInventory.getQuickBuy(31, doc, player));
        shopGUI.setItem(32, BedwarsInventory.getQuickBuy(32, doc, player));
        shopGUI.setItem(33, BedwarsInventory.getQuickBuy(33, doc, player));
        shopGUI.setItem(34, BedwarsInventory.getQuickBuy(34, doc, player));

        shopGUI.setItem(37, BedwarsInventory.getQuickBuy(37, doc, player));
        shopGUI.setItem(38, BedwarsInventory.getQuickBuy(38, doc, player));
        shopGUI.setItem(39, BedwarsInventory.getQuickBuy(39, doc, player));
        shopGUI.setItem(40, BedwarsInventory.getQuickBuy(40, doc, player));
        shopGUI.setItem(41, BedwarsInventory.getQuickBuy(41, doc, player));
        shopGUI.setItem(42, BedwarsInventory.getQuickBuy(42, doc, player));
        shopGUI.setItem(43, BedwarsInventory.getQuickBuy(43, doc, player));
    }



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

    public static ChatColor canPurchaseColor(boolean bool) {
        if (bool) return ChatColor.GREEN;
        return ChatColor.RED;
    }

    public static String canPurchaseText(boolean bool, String type) {
        if (bool) return ChatColor.YELLOW + "Click To Purchase!";
        return ChatColor.RED + "You Don't Have Enough " + type + "!";
    }
}
