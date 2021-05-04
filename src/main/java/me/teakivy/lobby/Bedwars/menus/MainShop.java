package me.teakivy.lobby.Bedwars.menus;

import me.teakivy.lobby.Bedwars.util.BedwarsInventory;
import me.teakivy.lobby.Main;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.Arrays;

public class MainShop {
    public static ArrayList<Player> ShopOpen = new ArrayList<>();

    public static void openShopKeeper(Player player) {
        Inventory inv = player.getInventory();
        if (!ShopOpen.contains(player)) ShopOpen.add(player);

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

        Inventory shopGUI = Bukkit.createInventory(player, 6 * 9, ChatColor.GOLD + "Game Shop");

        ItemStack air = new ItemStack(Material.AIR);

        ItemStack SelectorQuickBuy = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Quick Buy", new String[]{ChatColor.GRAY + "Items added to your quick", ChatColor.GRAY + "buy will appear here!"}, 1, (byte) 13);
        ItemStack SelectorMelee = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Melee", new String[]{ChatColor.GRAY + "Attack your enemies at", ChatColor.GRAY + "full power!"}, 1, (byte) 7);
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

        shopGUI.setItem(9, SelectorQuickBuy);
        shopGUI.setItem(10, SelectorBlocks);
        shopGUI.setItem(11, SelectorMelee);
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

    public static void addItemToQuickBuyMenu(Player player, ItemStack item) {
        Inventory inv = player.getInventory();
        if (!ShopOpen.contains(player)) ShopOpen.add(player);

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

        Inventory shopGUI = Bukkit.createInventory(player, 6 * 9, ChatColor.GOLD + "Game Shop - Quick Buy");
        Document doc = BedwarsInventory.getQuickBuyDocument(player);

        ItemStack slot19 = BedwarsInventory.getQuickBuy(19, doc, player);
        ItemStack slot20 = BedwarsInventory.getQuickBuy(20, doc, player);
        ItemStack slot21 = BedwarsInventory.getQuickBuy(21, doc, player);
        ItemStack slot22 = BedwarsInventory.getQuickBuy(22, doc, player);
        ItemStack slot23 = BedwarsInventory.getQuickBuy(23, doc, player);
        ItemStack slot24 = BedwarsInventory.getQuickBuy(24, doc, player);
        ItemStack slot25 = BedwarsInventory.getQuickBuy(25, doc, player);

        ItemStack slot26 = BedwarsInventory.getQuickBuy(26, doc, player);
        ItemStack slot27 = BedwarsInventory.getQuickBuy(27, doc, player);
        ItemStack slot28 = BedwarsInventory.getQuickBuy(28, doc, player);
        ItemStack slot29 = BedwarsInventory.getQuickBuy(29, doc, player);
        ItemStack slot30 = BedwarsInventory.getQuickBuy(30, doc, player);
        ItemStack slot31 = BedwarsInventory.getQuickBuy(31, doc, player);
        ItemStack slot32 = BedwarsInventory.getQuickBuy(32, doc, player);
        ItemStack slot33 = BedwarsInventory.getQuickBuy(33, doc, player);
        ItemStack slot34 = BedwarsInventory.getQuickBuy(34, doc, player);

        ItemStack slot37 = BedwarsInventory.getQuickBuy(37, doc, player);
        ItemStack slot38 = BedwarsInventory.getQuickBuy(38, doc, player);
        ItemStack slot39 = BedwarsInventory.getQuickBuy(39, doc, player);
        ItemStack slot40 = BedwarsInventory.getQuickBuy(40, doc, player);
        ItemStack slot41 = BedwarsInventory.getQuickBuy(41, doc, player);
        ItemStack slot42 = BedwarsInventory.getQuickBuy(42, doc, player);
        ItemStack slot43 = BedwarsInventory.getQuickBuy(43, doc, player);



        ItemMeta selectorMeta = item.getItemMeta();
        selectorMeta.setLore(Arrays.asList("", ChatColor.YELLOW + "Click here to select", ChatColor.YELLOW + "this slot!"));

        ItemMeta slot19Meta = slot19.getItemMeta();
        slot19Meta.setLore(Arrays.asList("", ChatColor.YELLOW + "Click here to select", ChatColor.YELLOW + "this slot!"));

        ItemMeta slot20Meta = slot20.getItemMeta();
        slot20Meta.setLore(Arrays.asList("", ChatColor.YELLOW + "Click here to select", ChatColor.YELLOW + "this slot!"));

        ItemMeta slot21Meta = slot21.getItemMeta();
        slot21Meta.setLore(Arrays.asList("", ChatColor.YELLOW + "Click here to select", ChatColor.YELLOW + "this slot!"));

        ItemMeta slot22Meta = slot22.getItemMeta();
        slot22Meta.setLore(Arrays.asList("", ChatColor.YELLOW + "Click here to select", ChatColor.YELLOW + "this slot!"));

        ItemMeta slot23Meta = slot23.getItemMeta();
        slot23Meta.setLore(Arrays.asList("", ChatColor.YELLOW + "Click here to select", ChatColor.YELLOW + "this slot!"));

        ItemMeta slot24Meta = slot24.getItemMeta();
        slot24Meta.setLore(Arrays.asList("", ChatColor.YELLOW + "Click here to select", ChatColor.YELLOW + "this slot!"));

        ItemMeta slot25Meta = slot25.getItemMeta();
        slot25Meta.setLore(Arrays.asList("", ChatColor.YELLOW + "Click here to select", ChatColor.YELLOW + "this slot!"));

        ItemMeta slot28Meta = slot28.getItemMeta();
        slot28Meta.setLore(Arrays.asList("", ChatColor.YELLOW + "Click here to select", ChatColor.YELLOW + "this slot!"));

        ItemMeta slot29Meta = slot29.getItemMeta();
        slot29Meta.setLore(Arrays.asList("", ChatColor.YELLOW + "Click here to select", ChatColor.YELLOW + "this slot!"));

        ItemMeta slot30Meta = slot30.getItemMeta();
        slot30Meta.setLore(Arrays.asList("", ChatColor.YELLOW + "Click here to select", ChatColor.YELLOW + "this slot!"));

        ItemMeta slot31Meta = slot31.getItemMeta();
        slot31Meta.setLore(Arrays.asList("", ChatColor.YELLOW + "Click here to select", ChatColor.YELLOW + "this slot!"));

        ItemMeta slot32Meta = slot32.getItemMeta();
        slot32Meta.setLore(Arrays.asList("", ChatColor.YELLOW + "Click here to select", ChatColor.YELLOW + "this slot!"));

        ItemMeta slot33Meta = slot33.getItemMeta();
        slot33Meta.setLore(Arrays.asList("", ChatColor.YELLOW + "Click here to select", ChatColor.YELLOW + "this slot!"));

        ItemMeta slot34Meta = slot34.getItemMeta();
        slot34Meta.setLore(Arrays.asList("", ChatColor.YELLOW + "Click here to select", ChatColor.YELLOW + "this slot!"));

        ItemMeta slot37Meta = slot37.getItemMeta();
        slot37Meta.setLore(Arrays.asList("", ChatColor.YELLOW + "Click here to select", ChatColor.YELLOW + "this slot!"));

        ItemMeta slot38Meta = slot38.getItemMeta();
        slot38Meta.setLore(Arrays.asList("", ChatColor.YELLOW + "Click here to select", ChatColor.YELLOW + "this slot!"));

        ItemMeta slot39Meta = slot39.getItemMeta();
        slot39Meta.setLore(Arrays.asList("", ChatColor.YELLOW + "Click here to select", ChatColor.YELLOW + "this slot!"));

        ItemMeta slot40Meta = slot40.getItemMeta();
        slot40Meta.setLore(Arrays.asList("", ChatColor.YELLOW + "Click here to select", ChatColor.YELLOW + "this slot!"));

        ItemMeta slot41Meta = slot41.getItemMeta();
        slot41Meta.setLore(Arrays.asList("", ChatColor.YELLOW + "Click here to select", ChatColor.YELLOW + "this slot!"));

        ItemMeta slot42Meta = slot42.getItemMeta();
        slot42Meta.setLore(Arrays.asList("", ChatColor.YELLOW + "Click here to select", ChatColor.YELLOW + "this slot!"));

        ItemMeta slot43Meta = slot43.getItemMeta();
        slot43Meta.setLore(Arrays.asList("", ChatColor.YELLOW + "Click here to select", ChatColor.YELLOW + "this slot!"));


        slot19.setItemMeta(slot19Meta);
        slot20.setItemMeta(slot20Meta);
        slot21.setItemMeta(slot21Meta);
        slot22.setItemMeta(slot22Meta);
        slot23.setItemMeta(slot23Meta);
        slot24.setItemMeta(slot24Meta);
        slot25.setItemMeta(slot25Meta);

        slot28.setItemMeta(slot28Meta);
        slot29.setItemMeta(slot29Meta);
        slot30.setItemMeta(slot30Meta);
        slot31.setItemMeta(slot31Meta);
        slot32.setItemMeta(slot32Meta);
        slot33.setItemMeta(slot33Meta);
        slot34.setItemMeta(slot34Meta);

        slot37.setItemMeta(slot37Meta);
        slot38.setItemMeta(slot38Meta);
        slot39.setItemMeta(slot39Meta);
        slot40.setItemMeta(slot40Meta);
        slot41.setItemMeta(slot41Meta);
        slot42.setItemMeta(slot42Meta);
        slot43.setItemMeta(slot43Meta);


        shopGUI.setItem(19, slot19);
        shopGUI.setItem(20, slot20);
        shopGUI.setItem(21, slot21);
        shopGUI.setItem(22, slot22);
        shopGUI.setItem(23, slot23);
        shopGUI.setItem(24, slot24);
        shopGUI.setItem(25, slot25);

        shopGUI.setItem(28, slot28);
        shopGUI.setItem(29, slot29);
        shopGUI.setItem(30, slot30);
        shopGUI.setItem(31, slot31);
        shopGUI.setItem(32, slot32);
        shopGUI.setItem(33, slot33);
        shopGUI.setItem(34, slot34);

        shopGUI.setItem(37, slot37);
        shopGUI.setItem(38, slot38);
        shopGUI.setItem(39, slot39);
        shopGUI.setItem(40, slot40);
        shopGUI.setItem(41, slot41);
        shopGUI.setItem(42, slot42);
        shopGUI.setItem(43, slot43);

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setLore(Arrays.asList(ChatColor.GOLD + "Click a slot to add", ChatColor.GOLD + "this item to your", ChatColor.GOLD + "Quick Buy!"));
        item.setItemMeta(itemMeta);

        shopGUI.setItem(4, item);

        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta barrierMeta = barrier.getItemMeta();
        barrierMeta.setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + "CANCEL");
        barrier.setItemMeta(barrierMeta);

        shopGUI.setItem(53, barrier);


        player.openInventory(shopGUI);
    }


    public static void openShopKeeperBlocks(Player player) {
        Inventory inv = player.getInventory();
        if (!ShopOpen.contains(player)) ShopOpen.add(player);

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

        Inventory shopGUI = Bukkit.createInventory(player, 6 * 9, ChatColor.GOLD + "Game Shop - Blocks");

        ItemStack air = new ItemStack(Material.AIR);

        ItemStack SelectorQuickBuy = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Quick Buy", new String[]{ChatColor.GRAY + "Items added to your quick", ChatColor.GRAY + "buy will appear here!"}, 1, (byte) 7);
        ItemStack SelectorMelee = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Melee", new String[]{ChatColor.GRAY + "Attack your enemies at", ChatColor.GRAY + "full power!"}, 1, (byte) 7);
        ItemStack SelectorBlocks = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Blocks", new String[]{ChatColor.GRAY + "Cross those gaps and", ChatColor.GRAY + "keep your bed safe!"}, 1, (byte) 13);
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

        ItemStack QuickBuy = createItem(Material.STAINED_GLASS_PANE, ChatColor.GOLD + "Quick Buy", new String[]{ChatColor.GRAY + "Shift click an item to", ChatColor.GRAY + "add it to your Quick Buy", "", ChatColor.GRAY + "Coming Soon..."}, 1, (byte) 14);

        ItemStack ItemHotbarManager = createItem(Material.BLAZE_POWDER, ChatColor.YELLOW + "Hotbar Manager", new String[]{ChatColor.GRAY + "Change your hotbar defaults!", "", ChatColor.GRAY + "Coming Soon..."});

        shopGUI.setItem(0, ItemQuickBuy);
        shopGUI.setItem(1, ItemBlocks);
        shopGUI.setItem(2, ItemMelee);
        shopGUI.setItem(3, ItemArmor);
        shopGUI.setItem(4, ItemTools);
        shopGUI.setItem(5, ItemProjectiles);
        shopGUI.setItem(6, ItemPotions);
        shopGUI.setItem(7, ItemUtilities);

        shopGUI.setItem(9, SelectorQuickBuy);
        shopGUI.setItem(10, SelectorBlocks);
        shopGUI.setItem(11, SelectorMelee);
        shopGUI.setItem(12, SelectorArmor);
        shopGUI.setItem(13, SelectorTools);
        shopGUI.setItem(14, SelectorProjectiles);
        shopGUI.setItem(15, SelectorPotions);
        shopGUI.setItem(16, SelectorUtilities);
        shopGUI.setItem(17, SelectorEmpty);




        shopGUI.setItem(19, createShopItem(Material.WOOL, "Wool", 4, ironAmount, "Iron", 16));
        shopGUI.setItem(20, createShopItem(Material.HARD_CLAY, "Hardened Clay", 12, ironAmount, "Iron", 16));
        shopGUI.setItem(21, createShopItem(Material.GLASS, "Blast-Proof Glass", 12, ironAmount, "Iron", 4));
        shopGUI.setItem(22, createShopItem(Material.ENDER_STONE, "End Stone", 24, ironAmount, "Iron", 12));
        shopGUI.setItem(23, createShopItem(Material.LADDER, "Ladder", 4, ironAmount, "Iron", 16));
        shopGUI.setItem(24, createShopItem(Material.WOOD, "Wood", 4, goldAmount, "Gold", 16));
        shopGUI.setItem(25, createShopItem(Material.OBSIDIAN, "Obsidian", 4, emeraldAmount, "Emerald", 4));


        player.openInventory(shopGUI);
    }

    public static void updateBlocksShop(Player player) {
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

        shopGUI.setItem(19, createShopItem(Material.WOOL, "Wool", 4, ironAmount, "Iron", 16));
        shopGUI.setItem(20, createShopItem(Material.HARD_CLAY, "Hardened Clay", 12, ironAmount, "Iron", 16));
        shopGUI.setItem(21, createShopItem(Material.GLASS, "Blast-Proof Glass", 12, ironAmount, "Iron", 4));
        shopGUI.setItem(22, createShopItem(Material.ENDER_STONE, "End Stone", 24, ironAmount, "Iron", 12));
        shopGUI.setItem(23, createShopItem(Material.LADDER, "Ladder", 4, ironAmount, "Iron", 16));
        shopGUI.setItem(24, createShopItem(Material.WOOD, "Wood", 4, goldAmount, "Gold", 16));
        shopGUI.setItem(25, createShopItem(Material.OBSIDIAN, "Obsidian", 4, emeraldAmount, "Emerald", 4));
    }

        public static void openShopKeeperMelee(Player player) {
            Inventory inv = player.getInventory();
            if (!ShopOpen.contains(player)) ShopOpen.add(player);

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

            Inventory shopGUI = Bukkit.createInventory(player, 6 * 9, ChatColor.GOLD + "Game Shop - Melee");

            ItemStack air = new ItemStack(Material.AIR);

            ItemStack SelectorQuickBuy = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Quick Buy", new String[]{ChatColor.GRAY + "Items added to your quick", ChatColor.GRAY + "buy will appear here!"}, 1, (byte) 7);
            ItemStack SelectorMelee = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Melee", new String[]{ChatColor.GRAY + "Attack your enemies at", ChatColor.GRAY + "full power!"}, 1, (byte) 13);
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

            ItemStack QuickBuy = createItem(Material.STAINED_GLASS_PANE, ChatColor.GOLD + "Quick Buy", new String[]{ChatColor.GRAY + "Shift click an item to", ChatColor.GRAY + "add it to your Quick Buy", "", ChatColor.GRAY + "Coming Soon..."}, 1, (byte) 14);

            ItemStack ItemHotbarManager = createItem(Material.BLAZE_POWDER, ChatColor.YELLOW + "Hotbar Manager", new String[]{ChatColor.GRAY + "Change your hotbar defaults!", "", ChatColor.GRAY + "Coming Soon..."});

            shopGUI.setItem(0, ItemQuickBuy);
            shopGUI.setItem(1, ItemBlocks);
            shopGUI.setItem(2, ItemMelee);
            shopGUI.setItem(3, ItemArmor);
            shopGUI.setItem(4, ItemTools);
            shopGUI.setItem(5, ItemProjectiles);
            shopGUI.setItem(6, ItemPotions);
            shopGUI.setItem(7, ItemUtilities);

            shopGUI.setItem(9, SelectorQuickBuy);
            shopGUI.setItem(10, SelectorBlocks);
            shopGUI.setItem(11, SelectorMelee);
            shopGUI.setItem(12, SelectorArmor);
            shopGUI.setItem(13, SelectorTools);
            shopGUI.setItem(14, SelectorProjectiles);
            shopGUI.setItem(15, SelectorPotions);
            shopGUI.setItem(16, SelectorUtilities);
            shopGUI.setItem(17, SelectorEmpty);




            shopGUI.setItem(19, createShopItem(Material.STONE_SWORD, "Stone Sword", 10, ironAmount, "Iron"));
            shopGUI.setItem(20, createShopItem(Material.IRON_SWORD, "Iron Sword", 7, goldAmount, "Gold"));
            shopGUI.setItem(21, createShopItem(Material.DIAMOND_SWORD, "Diamond Sword", 4, emeraldAmount, "Emerald"));
            shopGUI.setItem(22, createShopItem(Material.STICK, "Stick (Knockback I)", 5, goldAmount, "Gold", Enchantment.KNOCKBACK));


            player.openInventory(shopGUI);
        }

    public static void updateMeleeShop(Player player) {
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

        shopGUI.setItem(19, createShopItem(Material.STONE_SWORD, "Stone Sword", 10, ironAmount, "Iron"));
        shopGUI.setItem(20, createShopItem(Material.IRON_SWORD, "Iron Sword", 7, goldAmount, "Gold"));
        shopGUI.setItem(21, createShopItem(Material.DIAMOND_SWORD, "Diamond Sword", 4, emeraldAmount, "Emerald"));
        shopGUI.setItem(22, createShopItem(Material.STICK, "Stick (Knockback I)", 5, goldAmount, "Gold", Enchantment.KNOCKBACK));
    }

    public static void openShopKeeperArmor(Player player) {
        Inventory inv = player.getInventory();
        if (!ShopOpen.contains(player)) ShopOpen.add(player);

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

        Inventory shopGUI = Bukkit.createInventory(player, 6 * 9, ChatColor.GOLD + "Game Shop - Armor");

        ItemStack air = new ItemStack(Material.AIR);

        ItemStack SelectorQuickBuy = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Quick Buy", new String[]{ChatColor.GRAY + "Items added to your quick", ChatColor.GRAY + "buy will appear here!"}, 1, (byte) 7);
        ItemStack SelectorMelee = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Melee", new String[]{ChatColor.GRAY + "Attack your enemies at", ChatColor.GRAY + "full power!"}, 1, (byte) 7);
        ItemStack SelectorBlocks = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Blocks", new String[]{ChatColor.GRAY + "Cross those gaps and", ChatColor.GRAY + "keep your bed safe!"}, 1, (byte) 7);
        ItemStack SelectorArmor = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Armor", new String[]{ChatColor.GRAY + "Protect your head from", ChatColor.GRAY + "falling swords!"}, 1, (byte) 13);
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

        ItemStack QuickBuy = createItem(Material.STAINED_GLASS_PANE, ChatColor.GOLD + "Quick Buy", new String[]{ChatColor.GRAY + "Shift click an item to", ChatColor.GRAY + "add it to your Quick Buy", "", ChatColor.GRAY + "Coming Soon..."}, 1, (byte) 14);

        ItemStack ItemHotbarManager = createItem(Material.BLAZE_POWDER, ChatColor.YELLOW + "Hotbar Manager", new String[]{ChatColor.GRAY + "Change your hotbar defaults!", "", ChatColor.GRAY + "Coming Soon..."});

        shopGUI.setItem(0, ItemQuickBuy);
        shopGUI.setItem(1, ItemBlocks);
        shopGUI.setItem(2, ItemMelee);
        shopGUI.setItem(3, ItemArmor);
        shopGUI.setItem(4, ItemTools);
        shopGUI.setItem(5, ItemProjectiles);
        shopGUI.setItem(6, ItemPotions);
        shopGUI.setItem(7, ItemUtilities);

        shopGUI.setItem(9, SelectorQuickBuy);
        shopGUI.setItem(10, SelectorBlocks);
        shopGUI.setItem(11, SelectorMelee);
        shopGUI.setItem(12, SelectorArmor);
        shopGUI.setItem(13, SelectorTools);
        shopGUI.setItem(14, SelectorProjectiles);
        shopGUI.setItem(15, SelectorPotions);
        shopGUI.setItem(16, SelectorUtilities);
        shopGUI.setItem(17, SelectorEmpty);




        shopGUI.setItem(19, createArmorShopItem(player, Material.CHAINMAIL_BOOTS, "Permanent Chainmail Armor", 40, ironAmount, "Iron"));
        shopGUI.setItem(20, createArmorShopItem(player, Material.IRON_BOOTS, "Permanent Iron Armor", 12, goldAmount, "Gold"));
        shopGUI.setItem(21, createArmorShopItem(player, Material.DIAMOND_BOOTS, "Permanent Diamond Armor", 6, emeraldAmount, "Emerald"));


        player.openInventory(shopGUI);
    }

    public static String armorPurchasable(boolean bool, boolean bool2) {
        if (!bool2) return ChatColor.RED.toString();
        if (bool) return ChatColor.GREEN.toString();
        return ChatColor.RED.toString();
    }

    public static void updateArmorShop(Player player) {
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

        shopGUI.setItem(19, createArmorShopItem(player, Material.CHAINMAIL_BOOTS, "Permanent Chainmail Armor", 40, ironAmount, "Iron"));
        shopGUI.setItem(20, createArmorShopItem(player, Material.IRON_BOOTS, "Permanent Iron Armor", 12, goldAmount, "Gold"));
        shopGUI.setItem(21, createArmorShopItem(player, Material.DIAMOND_BOOTS, "Permanent Diamond Armor", 6, emeraldAmount, "Emerald"));
    }

    public static void openShopKeeperTools(Player player) {
        Inventory inv = player.getInventory();
        if (!ShopOpen.contains(player)) ShopOpen.add(player);

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

        Inventory shopGUI = Bukkit.createInventory(player, 6 * 9, ChatColor.GOLD + "Game Shop - Tools");

        ItemStack air = new ItemStack(Material.AIR);

        ItemStack SelectorQuickBuy = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Quick Buy", new String[]{ChatColor.GRAY + "Items added to your quick", ChatColor.GRAY + "buy will appear here!"}, 1, (byte) 7);
        ItemStack SelectorMelee = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Melee", new String[]{ChatColor.GRAY + "Attack your enemies at", ChatColor.GRAY + "full power!"}, 1, (byte) 7);
        ItemStack SelectorBlocks = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Blocks", new String[]{ChatColor.GRAY + "Cross those gaps and", ChatColor.GRAY + "keep your bed safe!"}, 1, (byte) 7);
        ItemStack SelectorArmor = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Armor", new String[]{ChatColor.GRAY + "Protect your head from", ChatColor.GRAY + "falling swords!"}, 1, (byte) 7);
        ItemStack SelectorTools = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Tools", new String[]{ChatColor.GRAY + "Mine those bed defenses", ChatColor.GRAY + "at max speed!"}, 1, (byte) 13);
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

        ItemStack QuickBuy = createItem(Material.STAINED_GLASS_PANE, ChatColor.GOLD + "Quick Buy", new String[]{ChatColor.GRAY + "Shift click an item to", ChatColor.GRAY + "add it to your Quick Buy", "", ChatColor.GRAY + "Coming Soon..."}, 1, (byte) 14);

        ItemStack ItemHotbarManager = createItem(Material.BLAZE_POWDER, ChatColor.YELLOW + "Hotbar Manager", new String[]{ChatColor.GRAY + "Change your hotbar defaults!", "", ChatColor.GRAY + "Coming Soon..."});

        shopGUI.setItem(0, ItemQuickBuy);
        shopGUI.setItem(1, ItemBlocks);
        shopGUI.setItem(2, ItemMelee);
        shopGUI.setItem(3, ItemArmor);
        shopGUI.setItem(4, ItemTools);
        shopGUI.setItem(5, ItemProjectiles);
        shopGUI.setItem(6, ItemPotions);
        shopGUI.setItem(7, ItemUtilities);

        shopGUI.setItem(9, SelectorQuickBuy);
        shopGUI.setItem(10, SelectorBlocks);
        shopGUI.setItem(11, SelectorMelee);
        shopGUI.setItem(12, SelectorArmor);
        shopGUI.setItem(13, SelectorTools);
        shopGUI.setItem(14, SelectorProjectiles);
        shopGUI.setItem(15, SelectorPotions);
        shopGUI.setItem(16, SelectorUtilities);
        shopGUI.setItem(17, SelectorEmpty);




        shopGUI.setItem(19, createShearShopItem(ironAmount, player));
        shopGUI.setItem(20, createPickaxeShopItem(ironAmount, goldAmount, player));
        shopGUI.setItem(21, createAxeShopItem(ironAmount, goldAmount, player));


        player.openInventory(shopGUI);
    }

    public static void updateToolsShop(Player player) {
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

        shopGUI.setItem(19, createShearShopItem(ironAmount, player));
        shopGUI.setItem(20, createPickaxeShopItem(ironAmount, goldAmount, player));
        shopGUI.setItem(21, createAxeShopItem(ironAmount, goldAmount, player));
    }

    public static void openShopKeeperProjectiles(Player player) {
        Inventory inv = player.getInventory();
        if (!ShopOpen.contains(player)) ShopOpen.add(player);

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

        Inventory shopGUI = Bukkit.createInventory(player, 6 * 9, ChatColor.GOLD + "Game Shop - Projectiles");

        ItemStack air = new ItemStack(Material.AIR);

        ItemStack SelectorQuickBuy = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Quick Buy", new String[]{ChatColor.GRAY + "Items added to your quick", ChatColor.GRAY + "buy will appear here!"}, 1, (byte) 7);
        ItemStack SelectorMelee = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Melee", new String[]{ChatColor.GRAY + "Attack your enemies at", ChatColor.GRAY + "full power!"}, 1, (byte) 7);
        ItemStack SelectorBlocks = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Blocks", new String[]{ChatColor.GRAY + "Cross those gaps and", ChatColor.GRAY + "keep your bed safe!"}, 1, (byte) 7);
        ItemStack SelectorArmor = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Armor", new String[]{ChatColor.GRAY + "Protect your head from", ChatColor.GRAY + "falling swords!"}, 1, (byte) 7);
        ItemStack SelectorTools = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Tools", new String[]{ChatColor.GRAY + "Mine those bed defenses", ChatColor.GRAY + "at max speed!"}, 1, (byte) 7);
        ItemStack SelectorProjectiles = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Projectiles", new String[]{ChatColor.GRAY + "Shoot those enemies down!"}, 1, (byte) 13);
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

        ItemStack QuickBuy = createItem(Material.STAINED_GLASS_PANE, ChatColor.GOLD + "Quick Buy", new String[]{ChatColor.GRAY + "Shift click an item to", ChatColor.GRAY + "add it to your Quick Buy", "", ChatColor.GRAY + "Coming Soon..."}, 1, (byte) 14);

        ItemStack ItemHotbarManager = createItem(Material.BLAZE_POWDER, ChatColor.YELLOW + "Hotbar Manager", new String[]{ChatColor.GRAY + "Change your hotbar defaults!", "", ChatColor.GRAY + "Coming Soon..."});

        shopGUI.setItem(0, ItemQuickBuy);
        shopGUI.setItem(1, ItemBlocks);
        shopGUI.setItem(2, ItemMelee);
        shopGUI.setItem(3, ItemArmor);
        shopGUI.setItem(4, ItemTools);
        shopGUI.setItem(5, ItemProjectiles);
        shopGUI.setItem(6, ItemPotions);
        shopGUI.setItem(7, ItemUtilities);

        shopGUI.setItem(9, SelectorQuickBuy);
        shopGUI.setItem(10, SelectorBlocks);
        shopGUI.setItem(11, SelectorMelee);
        shopGUI.setItem(12, SelectorArmor);
        shopGUI.setItem(13, SelectorTools);
        shopGUI.setItem(14, SelectorProjectiles);
        shopGUI.setItem(15, SelectorPotions);
        shopGUI.setItem(16, SelectorUtilities);
        shopGUI.setItem(17, SelectorEmpty);




        shopGUI.setItem(19, createShopItem(Material.ARROW, "Arrow", 2, goldAmount, "Gold", 8));
        shopGUI.setItem(20, createShopItem(Material.BOW, "Bow", 12, goldAmount, "Gold"));
        shopGUI.setItem(21, createShopItem(Material.BOW, "Bow (Power I)", 24, goldAmount, "Gold", Enchantment.ARROW_DAMAGE));
        shopGUI.setItem(22, createShopItem(Material.BOW, "Bow (Power I, Punch I)", 6, emeraldAmount, "Emerald", Enchantment.ARROW_KNOCKBACK));


        player.openInventory(shopGUI);
    }

    public static void updateProjectileShop(Player player) {
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

        shopGUI.setItem(19, createShopItem(Material.ARROW, "Arrow", 2, goldAmount, "Gold", 8));
        shopGUI.setItem(20, createShopItem(Material.BOW, "Bow", 12, goldAmount, "Gold"));
        shopGUI.setItem(21, createShopItem(Material.BOW, "Bow (Power I)", 24, goldAmount, "Gold", Enchantment.ARROW_DAMAGE));
        shopGUI.setItem(22, createShopItem(Material.BOW, "Bow (Power I, Punch I)", 6, emeraldAmount, "Emerald", Enchantment.ARROW_KNOCKBACK));
    }

    public static void openShopKeeperPotions(Player player) {
        Inventory inv = player.getInventory();
        if (!ShopOpen.contains(player)) ShopOpen.add(player);

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

        Inventory shopGUI = Bukkit.createInventory(player, 6 * 9, ChatColor.GOLD + "Game Shop - Potions");

        ItemStack air = new ItemStack(Material.AIR);

        ItemStack SelectorQuickBuy = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Quick Buy", new String[]{ChatColor.GRAY + "Items added to your quick", ChatColor.GRAY + "buy will appear here!"}, 1, (byte) 7);
        ItemStack SelectorMelee = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Melee", new String[]{ChatColor.GRAY + "Attack your enemies at", ChatColor.GRAY + "full power!"}, 1, (byte) 7);
        ItemStack SelectorBlocks = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Blocks", new String[]{ChatColor.GRAY + "Cross those gaps and", ChatColor.GRAY + "keep your bed safe!"}, 1, (byte) 7);
        ItemStack SelectorArmor = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Armor", new String[]{ChatColor.GRAY + "Protect your head from", ChatColor.GRAY + "falling swords!"}, 1, (byte) 7);
        ItemStack SelectorTools = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Tools", new String[]{ChatColor.GRAY + "Mine those bed defenses", ChatColor.GRAY + "at max speed!"}, 1, (byte) 7);
        ItemStack SelectorProjectiles = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Projectiles", new String[]{ChatColor.GRAY + "Shoot those enemies down!"}, 1, (byte) 7);
        ItemStack SelectorPotions = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Potions", new String[]{ChatColor.GRAY + "Shh! Sneaky Magic!"}, 1, (byte) 13);
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

        ItemStack QuickBuy = createItem(Material.STAINED_GLASS_PANE, ChatColor.GOLD + "Quick Buy", new String[]{ChatColor.GRAY + "Shift click an item to", ChatColor.GRAY + "add it to your Quick Buy", "", ChatColor.GRAY + "Coming Soon..."}, 1, (byte) 14);

        ItemStack ItemHotbarManager = createItem(Material.BLAZE_POWDER, ChatColor.YELLOW + "Hotbar Manager", new String[]{ChatColor.GRAY + "Change your hotbar defaults!", "", ChatColor.GRAY + "Coming Soon..."});

        shopGUI.setItem(0, ItemQuickBuy);
        shopGUI.setItem(1, ItemBlocks);
        shopGUI.setItem(2, ItemMelee);
        shopGUI.setItem(3, ItemArmor);
        shopGUI.setItem(4, ItemTools);
        shopGUI.setItem(5, ItemProjectiles);
        shopGUI.setItem(6, ItemPotions);
        shopGUI.setItem(7, ItemUtilities);

        shopGUI.setItem(9, SelectorQuickBuy);
        shopGUI.setItem(10, SelectorBlocks);
        shopGUI.setItem(11, SelectorMelee);
        shopGUI.setItem(12, SelectorArmor);
        shopGUI.setItem(13, SelectorTools);
        shopGUI.setItem(14, SelectorProjectiles);
        shopGUI.setItem(15, SelectorPotions);
        shopGUI.setItem(16, SelectorUtilities);
        shopGUI.setItem(17, SelectorEmpty);




        shopGUI.setItem(19, createShopItem(Material.POTION, "Speed II Potion (45 Seconds)", 1, emeraldAmount, "Emerald", 1, (byte) 8195));
        shopGUI.setItem(20, createShopItem(Material.POTION, "Jump V Potion (45 Seconds)", 1, emeraldAmount, "Emerald", 1, (byte) 8235));
        shopGUI.setItem(21, createShopItem(Material.POTION, "Invisibility Potion (30 Seconds)", 2, emeraldAmount, "Emerald", 1, (byte) 8206));


        player.openInventory(shopGUI);
    }

    public static void updatePotionShop(Player player) {
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

        shopGUI.setItem(19, createShopItem(Material.POTION, "Speed II Potion (45 Seconds)", 1, emeraldAmount, "Emerald", 1, (byte) 8195));
        shopGUI.setItem(20, createShopItem(Material.POTION, "Jump V Potion (45 Seconds)", 1, emeraldAmount, "Emerald", 1, (byte) 8235));
        shopGUI.setItem(21, createShopItem(Material.POTION, "Invisibility Potion (30 Seconds)", 2, emeraldAmount, "Emerald", 1, (byte) 8206));
    }

    public static void openShopKeeperUtilities(Player player) {
        Inventory inv = player.getInventory();
        if (!ShopOpen.contains(player)) ShopOpen.add(player);

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

        Inventory shopGUI = Bukkit.createInventory(player, 6 * 9, ChatColor.GOLD + "Game Shop - Utilities");

        ItemStack air = new ItemStack(Material.AIR);

        ItemStack SelectorQuickBuy = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Quick Buy", new String[]{ChatColor.GRAY + "Items added to your quick", ChatColor.GRAY + "buy will appear here!"}, 1, (byte) 7);
        ItemStack SelectorMelee = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Melee", new String[]{ChatColor.GRAY + "Attack your enemies at", ChatColor.GRAY + "full power!"}, 1, (byte) 7);
        ItemStack SelectorBlocks = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Blocks", new String[]{ChatColor.GRAY + "Cross those gaps and", ChatColor.GRAY + "keep your bed safe!"}, 1, (byte) 7);
        ItemStack SelectorArmor = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Armor", new String[]{ChatColor.GRAY + "Protect your head from", ChatColor.GRAY + "falling swords!"}, 1, (byte) 7);
        ItemStack SelectorTools = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Tools", new String[]{ChatColor.GRAY + "Mine those bed defenses", ChatColor.GRAY + "at max speed!"}, 1, (byte) 7);
        ItemStack SelectorProjectiles = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Projectiles", new String[]{ChatColor.GRAY + "Shoot those enemies down!"}, 1, (byte) 7);
        ItemStack SelectorPotions = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Potions", new String[]{ChatColor.GRAY + "Shh! Sneaky Magic!"}, 1, (byte) 7);
        ItemStack SelectorUtilities = createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW + "Utilities", new String[]{ChatColor.GRAY + "Got something to do?", ChatColor.GRAY + "I got something for it!"}, 1, (byte) 13);
        ItemStack SelectorEmpty = createItem(Material.STAINED_GLASS_PANE," ", new String[]{""}, 1, (byte) 7);

        ItemStack ItemQuickBuy = createItem(Material.NETHER_STAR, ChatColor.YELLOW + "Quick Buy", new String[]{ChatColor.GRAY + "Items added to your quick", ChatColor.GRAY + "buy will appear here!"});
        ItemStack ItemMelee = createItem(Material.GOLD_SWORD, ChatColor.YELLOW + "Melee", new String[]{ChatColor.GRAY + "Attack your enemies at", ChatColor.GRAY + "full power!"});
        ItemStack ItemBlocks = createItem(Material.HARD_CLAY, ChatColor.YELLOW + "Blocks", new String[]{ChatColor.GRAY + "Cross those gaps and", ChatColor.GRAY + "keep your bed safe!"});
        ItemStack ItemArmor = createItem(Material.CHAINMAIL_BOOTS, ChatColor.YELLOW + "Armor", new String[]{ChatColor.GRAY + "Protect your head from", ChatColor.GRAY + "falling swords!"});
        ItemStack ItemTools = createItem(Material.STONE_PICKAXE, ChatColor.YELLOW + "Tools", new String[]{ChatColor.GRAY + "Mine those bed defenses", ChatColor.GRAY + "at max speed!"});
        ItemStack ItemProjectiles = createItem(Material.BOW, ChatColor.YELLOW + "Projectiles", new String[]{ChatColor.GRAY + "Shoot those enemies down!"});
        ItemStack ItemPotions = createItem(Material.BREWING_STAND_ITEM, ChatColor.YELLOW + "Potions", new String[]{ChatColor.GRAY + "Shh! Sneaky Magic!"});
        ItemStack ItemUtilities = createItem(Material.TNT, ChatColor.YELLOW + "Utilities", new String[]{ChatColor.GRAY + "Got something to do?", ChatColor.GRAY + "I got something for it!"});

        ItemStack QuickBuy = createItem(Material.STAINED_GLASS_PANE, ChatColor.GOLD + "Quick Buy", new String[]{ChatColor.GRAY + "Shift click an item to", ChatColor.GRAY + "add it to your Quick Buy", "", ChatColor.GRAY + "Coming Soon..."}, 1, (byte) 14);

        ItemStack ItemHotbarManager = createItem(Material.BLAZE_POWDER, ChatColor.YELLOW + "Hotbar Manager", new String[]{ChatColor.GRAY + "Change your hotbar defaults!", "", ChatColor.GRAY + "Coming Soon..."});

        shopGUI.setItem(0, ItemQuickBuy);
        shopGUI.setItem(1, ItemBlocks);
        shopGUI.setItem(2, ItemMelee);
        shopGUI.setItem(3, ItemArmor);
        shopGUI.setItem(4, ItemTools);
        shopGUI.setItem(5, ItemProjectiles);
        shopGUI.setItem(6, ItemPotions);
        shopGUI.setItem(7, ItemUtilities);

        shopGUI.setItem(9, SelectorQuickBuy);
        shopGUI.setItem(10, SelectorBlocks);
        shopGUI.setItem(11, SelectorMelee);
        shopGUI.setItem(12, SelectorArmor);
        shopGUI.setItem(13, SelectorTools);
        shopGUI.setItem(14, SelectorProjectiles);
        shopGUI.setItem(15, SelectorPotions);
        shopGUI.setItem(16, SelectorUtilities);
        shopGUI.setItem(17, SelectorEmpty);




        shopGUI.setItem(19, createShopItem(Material.GOLDEN_APPLE, "Golden Apple", 3, goldAmount, "Gold"));
        shopGUI.setItem(20, createShopItem(Material.SNOW_BALL, "Bedbug", 40, ironAmount, "Iron"));
        shopGUI.setItem(21, createShopItem(Material.MONSTER_EGG, "Dream Defender", 120, ironAmount, "Iron"));
        shopGUI.setItem(22, createShopItem(Material.FIREBALL, "Fireball", 40, ironAmount, "Iron"));
        shopGUI.setItem(23, createShopItem(Material.TNT, "TNT", 4, goldAmount, "Gold"));
        shopGUI.setItem(24, createShopItem(Material.ENDER_PEARL, "Ender Pearl", 4, emeraldAmount, "Emerald"));
        shopGUI.setItem(25, createShopItem(Material.WATER_BUCKET, "Water Bucket", 3, goldAmount, "Gold"));
        shopGUI.setItem(28, createShopItem(Material.EGG, "Bridge Egg", 2, emeraldAmount, "Emerald"));
        shopGUI.setItem(29, createShopItem(Material.MILK_BUCKET, "Magic Milk", 4, goldAmount, "Gold"));
        shopGUI.setItem(30, createShopItem(Material.SPONGE, "Sponge", 3, goldAmount, "Gold"));
        shopGUI.setItem(30, createShopItem(Material.CHEST, "Compact Pop-Up Tower", 24, ironAmount, "Iron"));


        player.openInventory(shopGUI);
    }

    public static void updateUtilitiesShop(Player player) {
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

        shopGUI.setItem(19, createShopItem(Material.GOLDEN_APPLE, "Golden Apple", 3, goldAmount, "Gold"));
        shopGUI.setItem(20, createShopItem(Material.SNOW_BALL, "Bedbug", 40, ironAmount, "Iron"));
        shopGUI.setItem(21, createShopItem(Material.MONSTER_EGG, "Dream Defender", 120, ironAmount, "Iron"));
        shopGUI.setItem(22, createShopItem(Material.FIREBALL, "Fireball", 40, ironAmount, "Iron"));
        shopGUI.setItem(23, createShopItem(Material.TNT, "TNT", 4, goldAmount, "Gold"));
        shopGUI.setItem(24, createShopItem(Material.ENDER_PEARL, "Ender Pearl", 4, emeraldAmount, "Emerald"));
        shopGUI.setItem(25, createShopItem(Material.WATER_BUCKET, "Water Bucket", 3, goldAmount, "Gold"));
        shopGUI.setItem(28, createShopItem(Material.EGG, "Bridge Egg", 2, emeraldAmount, "Emerald"));
        shopGUI.setItem(29, createShopItem(Material.MILK_BUCKET, "Magic Milk", 4, goldAmount, "Gold"));
        shopGUI.setItem(30, createShopItem(Material.SPONGE, "Sponge", 3, goldAmount, "Gold"));
        shopGUI.setItem(30, createShopItem(Material.CHEST, "Compact Pop-Up Tower", 24, ironAmount, "Iron"));
    }



    public static ItemStack createShopItem(Material material, String name, int cost, int currency, String costType, int amount, byte data, Enchantment enchantment, int encLevel) {
        ItemStack item = new ItemStack(material, amount, data);
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta.getLore() != null) {
            itemMeta.getLore().clear();
        }

        if (enchantment != null) {
            itemMeta.addEnchant(enchantment, encLevel, true);
        }
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        itemMeta.setDisplayName(canPurchaseColor(currency >= cost) + name);
        itemMeta.setLore(Arrays.asList(ChatColor.GRAY + "Cost: " + itemCostColor(costType) + cost + " " + costType, "", ChatColor.GRAY + "Shift Click to add", ChatColor.GRAY + "item to Quick Buy", "", canPurchaseText(currency >= cost, costType)));
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack createArmorShopItem(Player player, Material material, String name, int cost, int currency, String costType) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        Material armorWearing = player.getInventory().getBoots().getType();
        if (itemMeta.getLore() != null) {
            itemMeta.getLore().clear();
        }

        String purchaseText = ChatColor.YELLOW + "Click To Purchase!";
        if (armorWearing.equals(material)) {
            purchaseText = ChatColor.RED + "Already Purchased!";
        } else {
            purchaseText = canPurchaseText(currency >= cost, costType);
        }

//        if (enchantment != null) {
//            itemMeta.addEnchant(enchantment, encLevel, true);
//        }
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        itemMeta.setDisplayName(armorPurchasable(!armorWearing.equals(material), (currency >= cost)) + name);
        itemMeta.setLore(Arrays.asList(ChatColor.GRAY + "Cost: " + itemCostColor(costType) + cost + " " + costType, "", ChatColor.GRAY + "Shift Click to add", ChatColor.GRAY + "item to Quick Buy", "", purchaseText));
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack createShopItem(Material material, String name, int cost, int currency, String costType, int amount, byte data, Enchantment enchantment) {
        return createShopItem(material, name, cost, currency, costType, amount, data, enchantment, 1);
    }

    public static ItemStack createShopItem(Material material, String name, int cost, int currency, String costType, Enchantment enchantment) {
        return createShopItem(material, name, cost, currency, costType, 1, (byte) 0, enchantment, 1);
    }

    public static ItemStack createShearShopItem(int currency, Player player) {
        if (!BedwarsInventory.PlayerShears.get(player)) {
            ItemStack item = new ItemStack(Material.SHEARS);
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta.getLore() != null) {
                itemMeta.getLore().clear();
            }

            itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            itemMeta.setDisplayName(canPurchaseColor(currency >= 20) + "Permanent Shears");
            itemMeta.setLore(Arrays.asList(ChatColor.GRAY + "Cost: " + itemCostColor("Iron") + 20 + " " + "Iron", ChatColor.GRAY + "Permanent", "", ChatColor.GRAY + "Shift Click to add", ChatColor.GRAY + "item to Quick Buy", "", canPurchaseText(currency >= 20, "Iron")));
            item.setItemMeta(itemMeta);
            return item;
        } else {
            ItemStack item = new ItemStack(Material.SHEARS);
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta.getLore() != null) {
                itemMeta.getLore().clear();
            }

            itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            itemMeta.setDisplayName(ChatColor.RED + "Permanent Shears");
            itemMeta.setLore(Arrays.asList(ChatColor.GRAY + "Cost: " + itemCostColor("Iron") + 20 + " " + "Iron", ChatColor.GRAY + "Permanent", "", ChatColor.GRAY + "Shift Click to add", ChatColor.GRAY + "item to Quick Buy", "", ChatColor.RED + "Already Purchased!"));
            item.setItemMeta(itemMeta);
            return item;
        }
    }

    public static ItemStack createPickaxeShopItem(int iron, int gold, Player player) {
        if (BedwarsInventory.PlayerPickaxe.get(player).equals(Material.AIR)) {
            ItemStack item = new ItemStack(Material.WOOD_PICKAXE);
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta.getLore() != null) {
                itemMeta.getLore().clear();
            }

            itemMeta.addEnchant(Enchantment.DIG_SPEED, 1, true);

            itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            itemMeta.setDisplayName(canPurchaseColor(iron >= 10) + "Wood Pickaxe (Efficiency I)");
            itemMeta.setLore(Arrays.asList(ChatColor.GRAY + "Cost: " + itemCostColor("Iron") + 10 + " " + "Iron", ChatColor.GOLD + "Tier I", "", ChatColor.GRAY + "Shift Click to add", ChatColor.GRAY + "item to Quick Buy", "", canPurchaseText(iron >= 10, "Iron")));
            item.setItemMeta(itemMeta);
            return item;
        } else if (BedwarsInventory.PlayerPickaxe.get(player).equals(Material.WOOD_PICKAXE)) {
            ItemStack item = new ItemStack(Material.IRON_PICKAXE);
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta.getLore() != null) {
                itemMeta.getLore().clear();
            }

            itemMeta.addEnchant(Enchantment.DIG_SPEED, 1, true);

            itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            itemMeta.setDisplayName(canPurchaseColor(iron >= 10) + "Iron Pickaxe (Efficiency I)");
            itemMeta.setLore(Arrays.asList(ChatColor.GRAY + "Cost: " + itemCostColor("Iron") + 10 + " " + "Iron", ChatColor.GOLD + "Tier II", "", ChatColor.GRAY + "Shift Click to add", ChatColor.GRAY + "item to Quick Buy", "", canPurchaseText(iron >= 10, "Iron")));
            item.setItemMeta(itemMeta);
            return item;
        } else if (BedwarsInventory.PlayerPickaxe.get(player).equals(Material.IRON_PICKAXE)) {
            ItemStack item = new ItemStack(Material.GOLD_PICKAXE);
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta.getLore() != null) {
                itemMeta.getLore().clear();
            }

            itemMeta.addEnchant(Enchantment.DIG_SPEED, 3, true);
            itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 2, true);

            itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            itemMeta.setDisplayName(canPurchaseColor(gold >= 3) + "Gold Pickaxe (Efficiency III, Sharpness II)");
            itemMeta.setLore(Arrays.asList(ChatColor.GRAY + "Cost: " + itemCostColor("Gold") + 3 + " " + "Gold", ChatColor.GOLD + "Tier III", "", ChatColor.GRAY + "Shift Click to add", ChatColor.GRAY + "item to Quick Buy", "", canPurchaseText(gold >= 3, "Gold")));
            item.setItemMeta(itemMeta);
            return item;
        } else if (BedwarsInventory.PlayerPickaxe.get(player).equals(Material.GOLD_PICKAXE)) {
            ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta.getLore() != null) {
                itemMeta.getLore().clear();
            }

            itemMeta.addEnchant(Enchantment.DIG_SPEED, 3, true);

            itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            itemMeta.setDisplayName(canPurchaseColor(gold >= 6) + "Diamond Pickaxe (Efficiency III)");
            itemMeta.setLore(Arrays.asList(ChatColor.GRAY + "Cost: " + itemCostColor("Gold") + 6 + " " + "Gold", ChatColor.GOLD + "Tier IV", "", ChatColor.GRAY + "Shift Click to add", ChatColor.GRAY + "item to Quick Buy", "", canPurchaseText(gold >= 6, "Gold")));
            item.setItemMeta(itemMeta);
            return item;
        } else if (BedwarsInventory.PlayerPickaxe.get(player).equals(Material.DIAMOND_PICKAXE)) {
            ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta.getLore() != null) {
                itemMeta.getLore().clear();
            }

            itemMeta.addEnchant(Enchantment.DIG_SPEED, 3, true);

            itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            itemMeta.setDisplayName(ChatColor.RED + "Diamond Pickaxe (Efficiency III)");
            itemMeta.setLore(Arrays.asList(ChatColor.GRAY + "Cost: " + itemCostColor("Gold") + 6 + " " + "Gold", ChatColor.GOLD + "Tier IV", "", ChatColor.GRAY + "Shift Click to add", ChatColor.GRAY + "item to Quick Buy", "", ChatColor.RED + "Already Purchased!"));
            item.setItemMeta(itemMeta);
            return item;
        } else {
            ItemStack item = new ItemStack(Material.WOOD_PICKAXE);
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta.getLore() != null) {
                itemMeta.getLore().clear();
            }

            itemMeta.addEnchant(Enchantment.DIG_SPEED, 1, true);

            itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            itemMeta.setDisplayName(canPurchaseColor(iron >= 10) + "Wood Pickaxe (Efficiency I)");
            itemMeta.setLore(Arrays.asList(ChatColor.GRAY + "Cost: " + itemCostColor("Iron") + 10 + " " + "Iron", ChatColor.GOLD + "Tier I", "", ChatColor.GRAY + "Shift Click to add", ChatColor.GRAY + "item to Quick Buy", "", canPurchaseText(iron >= 10, "Iron")));
            item.setItemMeta(itemMeta);
            return item;
        }
    }


    public static ItemStack createAxeShopItem(int iron, int gold, Player player) {
        if (BedwarsInventory.PlayerAxe.get(player).equals(Material.AIR)) {
            ItemStack item = new ItemStack(Material.WOOD_AXE);
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta.getLore() != null) {
                itemMeta.getLore().clear();
            }

            itemMeta.addEnchant(Enchantment.DIG_SPEED, 1, true);

            itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            itemMeta.setDisplayName(canPurchaseColor(iron >= 10) + "Wood Axe (Efficiency I)");
            itemMeta.setLore(Arrays.asList(ChatColor.GRAY + "Cost: " + itemCostColor("Iron") + 10 + " " + "Iron", ChatColor.GOLD + "Tier I", "", ChatColor.GRAY + "Shift Click to add", ChatColor.GRAY + "item to Quick Buy", "", canPurchaseText(iron >= 10, "Iron")));
            item.setItemMeta(itemMeta);
            return item;
        } else if (BedwarsInventory.PlayerAxe.get(player).equals(Material.WOOD_AXE)) {
            ItemStack item = new ItemStack(Material.STONE_AXE);
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta.getLore() != null) {
                itemMeta.getLore().clear();
            }

            itemMeta.addEnchant(Enchantment.DIG_SPEED, 1, true);

            itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            itemMeta.setDisplayName(canPurchaseColor(iron >= 10) + "Stone Axe (Efficiency I)");
            itemMeta.setLore(Arrays.asList(ChatColor.GRAY + "Cost: " + itemCostColor("Iron") + 10 + " " + "Iron", ChatColor.GOLD + "Tier II", "", ChatColor.GRAY + "Shift Click to add", ChatColor.GRAY + "item to Quick Buy", "", canPurchaseText(iron >= 10, "Iron")));
            item.setItemMeta(itemMeta);
            return item;
        } else if (BedwarsInventory.PlayerAxe.get(player).equals(Material.STONE_AXE)) {
            ItemStack item = new ItemStack(Material.IRON_AXE);
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta.getLore() != null) {
                itemMeta.getLore().clear();
            }

            itemMeta.addEnchant(Enchantment.DIG_SPEED, 2, true);

            itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            itemMeta.setDisplayName(canPurchaseColor(gold >= 3) + "Iron Axe (Efficiency II)");
            itemMeta.setLore(Arrays.asList(ChatColor.GRAY + "Cost: " + itemCostColor("Gold") + 3 + " " + "Gold", ChatColor.GOLD + "Tier III", "", ChatColor.GRAY + "Shift Click to add", ChatColor.GRAY + "item to Quick Buy", "", canPurchaseText(gold >= 3, "Gold")));
            item.setItemMeta(itemMeta);
            return item;
        } else if (BedwarsInventory.PlayerAxe.get(player).equals(Material.IRON_AXE)) {
            ItemStack item = new ItemStack(Material.DIAMOND_AXE);
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta.getLore() != null) {
                itemMeta.getLore().clear();
            }

            itemMeta.addEnchant(Enchantment.DIG_SPEED, 3, true);

            itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            itemMeta.setDisplayName(canPurchaseColor(gold >= 6) + "Diamond Axe (Efficiency III)");
            itemMeta.setLore(Arrays.asList(ChatColor.GRAY + "Cost: " + itemCostColor("Gold") + 6 + " " + "Gold", ChatColor.GOLD + "Tier IV", "", ChatColor.GRAY + "Shift Click to add", ChatColor.GRAY + "item to Quick Buy", "", canPurchaseText(gold >= 6, "Gold")));
            item.setItemMeta(itemMeta);
            return item;
        } else if (BedwarsInventory.PlayerAxe.get(player).equals(Material.DIAMOND_AXE)) {
            ItemStack item = new ItemStack(Material.DIAMOND_AXE);
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta.getLore() != null) {
                itemMeta.getLore().clear();
            }

            itemMeta.addEnchant(Enchantment.DIG_SPEED, 3, true);

            itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            itemMeta.setDisplayName(ChatColor.RED + "Diamond Axe (Efficiency III)");
            itemMeta.setLore(Arrays.asList(ChatColor.GRAY + "Cost: " + itemCostColor("Gold") + 6 + " " + "Gold", ChatColor.GOLD + "Tier IV", "", ChatColor.GRAY + "Shift Click to add", ChatColor.GRAY + "item to Quick Buy", "", ChatColor.RED + "Already Purchased!"));
            item.setItemMeta(itemMeta);
            return item;
        } else {
            ItemStack item = new ItemStack(Material.WOOD_AXE);
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta.getLore() != null) {
                itemMeta.getLore().clear();
            }

            itemMeta.addEnchant(Enchantment.DIG_SPEED, 1, true);

            itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            itemMeta.setDisplayName(canPurchaseColor(iron >= 10) + "Wood Axe (Efficiency I)");
            itemMeta.setLore(Arrays.asList(ChatColor.GRAY + "Cost: " + itemCostColor("Iron") + 10 + " " + "Iron", ChatColor.GOLD + "Tier I", "", ChatColor.GRAY + "Shift Click to add", ChatColor.GRAY + "item to Quick Buy", "", canPurchaseText(iron >= 10, "Iron")));
            item.setItemMeta(itemMeta);
            return item;
        }
    }


    public static ItemStack createShopItem(Material material, String name, int cost, int currency, String costType, int amount, byte data) {
        return createShopItem(material, name, cost, currency, costType, amount, data, null, 1);
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

    public static ChatColor itemCostColor(String type) {
        if (type.equalsIgnoreCase("iron")) {
            return ChatColor.WHITE;
        }
        if (type.equalsIgnoreCase("gold")) {
            return ChatColor.GOLD;
        }
        if (type.equalsIgnoreCase("diamond")) {
            return ChatColor.AQUA;
        }
        if (type.equalsIgnoreCase("emerald")) {
            return ChatColor.DARK_GREEN;
        }
        return ChatColor.YELLOW;
    }

    public static ItemStack createShopItem(Material material, String name, int cost, int currency, String costType, int amount) {
        return createShopItem(material, name, cost, currency, costType, amount, (byte) 0);
    }

    public static ItemStack createShopItem(Material material, String name, int cost, int currency, String costType) {
        return createShopItem(material, name, cost, currency, costType, 1, (byte) 0);
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
