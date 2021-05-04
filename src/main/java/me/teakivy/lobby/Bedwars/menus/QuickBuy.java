package me.teakivy.lobby.Bedwars.menus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class QuickBuy {

//    public static void QuickBuy(Player player, ItemStack item) {
//        Inventory shopGUI = Bukkit.createInventory(player, 6 * 9, ChatColor.GOLD + "Game Shop");
//
//        ItemStack SelectorQuickBuy = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Quick Buy", new String[]{ChatColor.GRAY + "Items added to your quick", ChatColor.GRAY + "buy will appear here!"}, 1, (byte) 13);
//        ItemStack SelectorMelee = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Melee", new String[]{ChatColor.GRAY + "Attack your enemies at", ChatColor.GRAY + "full power!"}, 1, (byte) 7);
//        ItemStack SelectorBlocks = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Blocks", new String[]{ChatColor.GRAY + "Cross those gaps and", ChatColor.GRAY + "keep your bed safe!"}, 1, (byte) 7);
//        ItemStack SelectorArmor = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Armor", new String[]{ChatColor.GRAY + "Protect your head from", ChatColor.GRAY + "falling swords!"}, 1, (byte) 7);
//        ItemStack SelectorTools = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Tools", new String[]{ChatColor.GRAY + "Mine those bed defenses", ChatColor.GRAY + "at max speed!"}, 1, (byte) 7);
//        ItemStack SelectorProjectiles = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Projectiles", new String[]{ChatColor.GRAY + "Shoot those enemies down!"}, 1, (byte) 7);
//        ItemStack SelectorPotions = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Potions", new String[]{ChatColor.GRAY + "Shh! Sneaky Magic!"}, 1, (byte) 7);
//        ItemStack SelectorUtilities = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Utilities", new String[]{ChatColor.GRAY + "Got something to do?", ChatColor.GRAY + "I got something for it!"}, 1, (byte) 7);
//        ItemStack SelectorEmpty = createItem(Material.STAINED_GLASS_PANE," ", new String[]{""}, 1, (byte) 7);
//
//        ItemStack ItemQuickBuy = createItem(Material.NETHER_STAR, ChatColor.YELLOW + "Quick Buy", new String[]{ChatColor.GRAY + "Items added to your quick", ChatColor.GRAY + "buy will appear here!"});
//        ItemStack ItemMelee = createItem(Material.GOLD_SWORD, ChatColor.YELLOW + "Melee", new String[]{ChatColor.GRAY + "Attack your enemies at", ChatColor.GRAY + "full power!"});
//        ItemStack ItemBlocks = createItem(Material.HARD_CLAY, ChatColor.YELLOW + "Blocks", new String[]{ChatColor.GRAY + "Cross those gaps and", ChatColor.GRAY + "keep your bed safe!"});
//        ItemStack ItemArmor = createItem(Material.CHAINMAIL_BOOTS, ChatColor.YELLOW + "Armor", new String[]{ChatColor.GRAY + "Protect your head from", ChatColor.GRAY + "falling swords!"});
//        ItemStack ItemTools = createItem(Material.STONE_PICKAXE, ChatColor.YELLOW + "Tools", new String[]{ChatColor.GRAY + "Mine those bed defenses", ChatColor.GRAY + "at max speed!"});
//        ItemStack ItemProjectiles = createItem(Material.BOW, ChatColor.YELLOW + "Projectiles", new String[]{ChatColor.GRAY + "Shoot those enemies down!"});
//        ItemStack ItemPotions = createItem(Material.BREWING_STAND_ITEM, ChatColor.YELLOW + "Potions", new String[]{ChatColor.GRAY + "Shh! Sneaky Magic!"});
//        ItemStack ItemUtilities = createItem(Material.TNT, ChatColor.YELLOW + "Utilities", new String[]{ChatColor.GRAY + "Got something to do?", ChatColor.GRAY + "I got something for it!"});
//
//        ItemStack QuickBuy = createItem(Material.STAINED_GLASS_PANE, ChatColor.GOLD + "Quick Buy", new String[]{ChatColor.GRAY + "Shift click an item to", ChatColor.GRAY + "add it to your Quick Buy", "", ChatColor.GRAY + "Coming Soon..."}, 1, (byte) 14);
//
//        ItemStack ItemHotbarManager = createItem(Material.BLAZE_POWDER, ChatColor.YELLOW + "Hotbar Manager", new String[]{ChatColor.GRAY + "Change your hotbar defaults!", "", ChatColor.GRAY + "Coming Soon..."});
//
//        shopGUI.setItem(0, ItemQuickBuy);
//        shopGUI.setItem(1, ItemBlocks);
//        shopGUI.setItem(2, ItemMelee);
//        shopGUI.setItem(3, ItemArmor);
//        shopGUI.setItem(4, ItemTools);
//        shopGUI.setItem(5, ItemProjectiles);
//        shopGUI.setItem(6, ItemPotions);
//        shopGUI.setItem(7, ItemUtilities);
//
//        shopGUI.setItem(9, SelectorQuickBuy);
//        shopGUI.setItem(10, SelectorBlocks);
//        shopGUI.setItem(11, SelectorMelee);
//        shopGUI.setItem(12, SelectorArmor);
//        shopGUI.setItem(13, SelectorTools);
//        shopGUI.setItem(14, SelectorProjectiles);
//        shopGUI.setItem(15, SelectorPotions);
//        shopGUI.setItem(16, SelectorUtilities);
//        shopGUI.setItem(17, SelectorEmpty);
//
//
//
//
//        shopGUI.setItem(19, createShopItem(Material.WOOL, "Wool", 4, ironAmount, "Iron", 16));
//        shopGUI.setItem(20, createShopItem(Material.STONE_SWORD, "Stone Sword", 10, ironAmount, "Iron"));
//        shopGUI.setItem(21, createShopItem(Material.CHAINMAIL_BOOTS, "Permanent Chainmail Armor", 40, ironAmount, "Iron"));
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
//
//        shopGUI.setItem(37, QuickBuy);
//        shopGUI.setItem(38, QuickBuy);
//        shopGUI.setItem(39, QuickBuy);
//        shopGUI.setItem(40, QuickBuy);
//        shopGUI.setItem(41, QuickBuy);
//        shopGUI.setItem(42, QuickBuy);
//        shopGUI.setItem(43, QuickBuy);
//
//
//        player.openInventory(shopGUI);
//    }
//    }
}
