package me.teakivy.lobby.Bedwars.util;

import com.mongodb.client.MongoCollection;
import me.teakivy.lobby.Bedwars.Bedwars;
import me.teakivy.lobby.Bedwars.menus.MainShop;
import me.teakivy.lobby.Main;
import me.teakivy.lobby.utils.ItemStackSerializer;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.mongodb.client.model.Updates.set;
import static me.teakivy.lobby.Bedwars.Bedwars.*;
import static me.teakivy.lobby.Bedwars.menus.MainShop.*;

public class BedwarsInventory implements Listener {

    public static Map< Player, Material > PlayerArmor = new HashMap< >();

    public static Map< Player, Boolean > PlayerShears = new HashMap< >();

    public static Map< Player, Material > PlayerPickaxe = new HashMap< >();
    public static Map< Player, Material > PlayerAxe = new HashMap< >();



    public static void setBedwarsInv(Player player, Color color) {
        PlayerInventory inventory = player.getInventory();
        inventory.clear();

        ItemStack sword = new ItemStack(Material.WOOD_SWORD);
        ItemMeta swordMeta = sword.getItemMeta();
        swordMeta.spigot().setUnbreakable(true);
        sword.setItemMeta(swordMeta);

        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta helmetMeta = (LeatherArmorMeta) helmet.getItemMeta();
        helmetMeta.setColor(color);
        helmetMeta.spigot().setUnbreakable(true);
        helmet.setItemMeta(helmetMeta);

        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta chestplateMeta = (LeatherArmorMeta) chestplate.getItemMeta();
        chestplateMeta.spigot().setUnbreakable(true);
        chestplateMeta.setColor(color);
        chestplate.setItemMeta(chestplateMeta);

        ItemStack leatherLeggings = new ItemStack(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta leatherLeggingsMeta = (LeatherArmorMeta) leatherLeggings.getItemMeta();
        leatherLeggingsMeta.spigot().setUnbreakable(true);
        leatherLeggingsMeta.setColor(color);
        leatherLeggings.setItemMeta(leatherLeggingsMeta);

        ItemStack leatherBoots = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta leatherBootsMeta = (LeatherArmorMeta) leatherBoots.getItemMeta();
        leatherBootsMeta.spigot().setUnbreakable(true);
        leatherBootsMeta.setColor(color);
        leatherBoots.setItemMeta(leatherBootsMeta);


        ItemStack chainLeggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);
        ItemMeta chainLeggingsMeta = chainLeggings.getItemMeta();
        chainLeggingsMeta.spigot().setUnbreakable(true);
        chainLeggings.setItemMeta(chainLeggingsMeta);

        ItemStack chainBoots = new ItemStack(Material.CHAINMAIL_BOOTS);
        ItemMeta chainBootsMeta = chainBoots.getItemMeta();
        chainBootsMeta.spigot().setUnbreakable(true);
        chainBoots.setItemMeta(chainBootsMeta);


        ItemStack ironLeggings = new ItemStack(Material.IRON_LEGGINGS);
        ItemMeta ironLeggingsMeta = ironLeggings.getItemMeta();
        ironLeggingsMeta.spigot().setUnbreakable(true);
        ironLeggings.setItemMeta(ironLeggingsMeta);

        ItemStack ironBoots = new ItemStack(Material.IRON_BOOTS);
        ItemMeta ironBootsMeta = ironBoots.getItemMeta();
        ironBootsMeta.spigot().setUnbreakable(true);
        ironBoots.setItemMeta(ironBootsMeta);


        ItemStack diamondLeggings = new ItemStack(Material.DIAMOND_LEGGINGS);
        ItemMeta diamondLeggingsMeta = diamondLeggings.getItemMeta();
        diamondLeggingsMeta.spigot().setUnbreakable(true);
        diamondLeggings.setItemMeta(diamondLeggingsMeta);

        ItemStack diamondBoots = new ItemStack(Material.DIAMOND_BOOTS);
        ItemMeta diamondBootsMeta = diamondBoots.getItemMeta();
        diamondBootsMeta.spigot().setUnbreakable(true);
        diamondBoots.setItemMeta(diamondBootsMeta);


        ItemStack shears = new ItemStack(Material.SHEARS);
        ItemMeta shearsMeta = shears.getItemMeta();
        shearsMeta.spigot().setUnbreakable(true);
        shears.setItemMeta(shearsMeta);

        ItemStack woodPickaxe = new ItemStack(Material.WOOD_PICKAXE);
        ItemMeta woodPickaxeMeta = woodPickaxe.getItemMeta();
        woodPickaxeMeta.spigot().setUnbreakable(true);
        woodPickaxeMeta.addEnchant(Enchantment.DIG_SPEED, 1, true);
        woodPickaxeMeta.setDisplayName(ChatColor.GOLD + "Wood Pickaxe (Efficiency I)");
        woodPickaxe.setItemMeta(woodPickaxeMeta);

        ItemStack ironPickaxe = new ItemStack(Material.IRON_PICKAXE);
        ItemMeta ironPickaxeMeta = ironPickaxe.getItemMeta();
        ironPickaxeMeta.spigot().setUnbreakable(true);
        ironPickaxeMeta.addEnchant(Enchantment.DIG_SPEED, 1, true);
        ironPickaxeMeta.setDisplayName(ChatColor.GOLD + "Iron Pickaxe (Efficiency I)");
        ironPickaxe.setItemMeta(ironPickaxeMeta);

        ItemStack goldPickaxe = new ItemStack(Material.GOLD_PICKAXE);
        ItemMeta goldPickaxeMeta = goldPickaxe.getItemMeta();
        goldPickaxeMeta.spigot().setUnbreakable(true);
        goldPickaxeMeta.addEnchant(Enchantment.DIG_SPEED, 2, true);
        goldPickaxeMeta.addEnchant(Enchantment.DAMAGE_ALL, 2, true);
        goldPickaxeMeta.setDisplayName(ChatColor.GOLD + "Gold Pickaxe (Efficiency III, Sharpness II)");
        goldPickaxe.setItemMeta(goldPickaxeMeta);

        ItemStack diamondPickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta diamondPickaxeMeta = diamondPickaxe.getItemMeta();
        diamondPickaxeMeta.spigot().setUnbreakable(true);
        diamondPickaxeMeta.addEnchant(Enchantment.DIG_SPEED, 3, true);
        diamondPickaxeMeta.setDisplayName(ChatColor.GOLD + "Diamond Pickaxe (Efficiency II)");
        diamondPickaxe.setItemMeta(diamondPickaxeMeta);




        ItemStack woodAxe = new ItemStack(Material.WOOD_AXE);
        ItemMeta woodAxeMeta = woodAxe.getItemMeta();
        woodAxeMeta.spigot().setUnbreakable(true);
        woodAxeMeta.addEnchant(Enchantment.DIG_SPEED, 1, true);
        woodAxeMeta.setDisplayName(ChatColor.GOLD + "Wood Axe (Efficiency I)");
        woodAxe.setItemMeta(woodAxeMeta);

        ItemStack stoneAxe = new ItemStack(Material.STONE_AXE);
        ItemMeta stoneAxeMeta = stoneAxe.getItemMeta();
        stoneAxeMeta.spigot().setUnbreakable(true);
        stoneAxeMeta.addEnchant(Enchantment.DIG_SPEED, 1, true);
        stoneAxeMeta.setDisplayName(ChatColor.GOLD + "Stone Axe (Efficiency I)");
        stoneAxe.setItemMeta(stoneAxeMeta);

        ItemStack ironAxe = new ItemStack(Material.IRON_AXE);
        ItemMeta ironAxeMeta = ironAxe.getItemMeta();
        ironAxeMeta.spigot().setUnbreakable(true);
        ironAxeMeta.addEnchant(Enchantment.DIG_SPEED, 2, true);
        ironAxeMeta.setDisplayName(ChatColor.GOLD + "Iron Axe (Efficiency II)");
        ironAxe.setItemMeta(ironAxeMeta);

        ItemStack diamondAxe = new ItemStack(Material.DIAMOND_AXE);
        ItemMeta diamondAxeMeta = diamondAxe.getItemMeta();
        diamondAxeMeta.spigot().setUnbreakable(true);
        diamondAxeMeta.addEnchant(Enchantment.DIG_SPEED, 3, true);
        diamondAxeMeta.setDisplayName(ChatColor.GOLD + "Diamond Axe (Efficiency III)");
        diamondAxe.setItemMeta(diamondAxeMeta);

        ItemStack leggings = leatherLeggings;
        ItemStack boots = leatherBoots;

        if (PlayerArmor.get(player).equals(Material.CHAINMAIL_BOOTS)) {
            leggings = chainLeggings;
            boots = chainBoots;
        }

        if (PlayerArmor.get(player).equals(Material.IRON_BOOTS)) {
            leggings = ironLeggings;
            boots = ironBoots;
        }

        if (PlayerArmor.get(player).equals(Material.DIAMOND_BOOTS)) {
            leggings = diamondLeggings;
            boots = diamondBoots;
        }




        inventory.setHelmet(helmet);
        inventory.setChestplate(chestplate);
        inventory.setLeggings(leggings);
        inventory.setBoots(boots);
        inventory.setItem(0, sword);

        if (PlayerShears.get(player)) {
            inventory.addItem(shears);
        }


        if (PlayerPickaxe.get(player).equals(Material.WOOD_PICKAXE)) {
            inventory.addItem(woodPickaxe);
        }
        if (PlayerPickaxe.get(player).equals(Material.IRON_PICKAXE)) {
            inventory.addItem(ironPickaxe);
        }
        if (PlayerPickaxe.get(player).equals(Material.GOLD_PICKAXE)) {
            inventory.addItem(goldPickaxe);
        }
        if (PlayerPickaxe.get(player).equals(Material.DIAMOND_PICKAXE)) {
            inventory.addItem(diamondPickaxe);
        }

        if (PlayerAxe.get(player).equals(Material.WOOD_AXE)) {
            inventory.addItem(woodAxe);
        }
        if (PlayerAxe.get(player).equals(Material.STONE_AXE)) {
            inventory.addItem(stoneAxe);
        }
        if (PlayerAxe.get(player).equals(Material.IRON_AXE)) {
            inventory.addItem(ironAxe);
        }
        if (PlayerAxe.get(player).equals(Material.DIAMOND_AXE)) {
            inventory.addItem(diamondAxe);
        }


    }

    public static void setPlayerArmor(Player player, Material type) {
        PlayerArmor.put(player, type);

        PlayerInventory inventory = player.getInventory();

        Color color = BedwarsPlayer.getColor(player);

        ItemStack sword = new ItemStack(Material.WOOD_SWORD);
        ItemMeta swordMeta = sword.getItemMeta();
        swordMeta.spigot().setUnbreakable(true);
        sword.setItemMeta(swordMeta);

        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta helmetMeta = (LeatherArmorMeta) helmet.getItemMeta();
        helmetMeta.setColor(color);
        helmetMeta.spigot().setUnbreakable(true);
        helmet.setItemMeta(helmetMeta);

        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta chestplateMeta = (LeatherArmorMeta) chestplate.getItemMeta();
        chestplateMeta.spigot().setUnbreakable(true);
        chestplateMeta.setColor(color);
        chestplate.setItemMeta(chestplateMeta);

        ItemStack leatherLeggings = new ItemStack(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta leatherLeggingsMeta = (LeatherArmorMeta) leatherLeggings.getItemMeta();
        leatherLeggingsMeta.spigot().setUnbreakable(true);
        leatherLeggingsMeta.setColor(color);
        leatherLeggings.setItemMeta(leatherLeggingsMeta);

        ItemStack leatherBoots = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta leatherBootsMeta = (LeatherArmorMeta) leatherBoots.getItemMeta();
        leatherBootsMeta.spigot().setUnbreakable(true);
        leatherBootsMeta.setColor(color);
        leatherBoots.setItemMeta(leatherBootsMeta);


        ItemStack chainLeggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);
        ItemMeta chainLeggingsMeta = chainLeggings.getItemMeta();
        chainLeggingsMeta.spigot().setUnbreakable(true);
        chainLeggings.setItemMeta(chainLeggingsMeta);

        ItemStack chainBoots = new ItemStack(Material.CHAINMAIL_BOOTS);
        ItemMeta chainBootsMeta = chainBoots.getItemMeta();
        chainBootsMeta.spigot().setUnbreakable(true);
        chainBoots.setItemMeta(chainBootsMeta);


        ItemStack ironLeggings = new ItemStack(Material.IRON_LEGGINGS);
        ItemMeta ironLeggingsMeta = ironLeggings.getItemMeta();
        ironLeggingsMeta.spigot().setUnbreakable(true);
        ironLeggings.setItemMeta(ironLeggingsMeta);

        ItemStack ironBoots = new ItemStack(Material.IRON_BOOTS);
        ItemMeta ironBootsMeta = ironBoots.getItemMeta();
        ironBootsMeta.spigot().setUnbreakable(true);
        ironBoots.setItemMeta(ironBootsMeta);


        ItemStack diamondLeggings = new ItemStack(Material.DIAMOND_LEGGINGS);
        ItemMeta diamondLeggingsMeta = diamondLeggings.getItemMeta();
        diamondLeggingsMeta.spigot().setUnbreakable(true);
        diamondLeggings.setItemMeta(diamondLeggingsMeta);

        ItemStack diamondBoots = new ItemStack(Material.DIAMOND_BOOTS);
        ItemMeta diamondBootsMeta = diamondBoots.getItemMeta();
        diamondBootsMeta.spigot().setUnbreakable(true);
        diamondBoots.setItemMeta(diamondBootsMeta);



        ItemStack leggings = leatherLeggings;
        ItemStack boots = leatherBoots;

        if (PlayerArmor.get(player).equals(Material.CHAINMAIL_BOOTS)) {
            leggings = chainLeggings;
            boots = chainBoots;
        }

        if (PlayerArmor.get(player).equals(Material.IRON_BOOTS)) {
            leggings = ironLeggings;
            boots = ironBoots;
        }

        if (PlayerArmor.get(player).equals(Material.DIAMOND_BOOTS)) {
            leggings = diamondLeggings;
            boots = diamondBoots;
        }


        inventory.setHelmet(helmet);
        inventory.setChestplate(chestplate);
        inventory.setLeggings(leggings);
        inventory.setBoots(boots);
        player.updateInventory();
    }

    @EventHandler
    public void onDespawn(ItemDespawnEvent event) {
        Item item = event.getEntity();
        if (item.getItemStack().getType() == Material.IRON_INGOT) {
            if (Bedwars.RedGenerator.distanceSquared(item.getLocation()) <= 9) {
                if (redGen.containsKey("iron"))
                    redGen.put("iron", 0);
            }
            if (Bedwars.BlueGenerator.distanceSquared(item.getLocation()) <= 9) {
                if (blueGen.containsKey("iron"))
                    blueGen.put("iron", 0);
            }
        }

        if (item.getItemStack().getType() == Material.GOLD_INGOT) {
            if (Bedwars.RedGenerator.distanceSquared(item.getLocation()) <= 9) {
                if (redGen.containsKey("gold"))
                    redGen.put("gold", 0);
            }
            if (Bedwars.BlueGenerator.distanceSquared(item.getLocation()) <= 9) {
                if (blueGen.containsKey("gold"))
                    blueGen.put("gold", 0);
            }
        }

        if (item.getItemStack().getType() == Material.DIAMOND) {
            if (RedDiamondGenerator.distanceSquared(item.getLocation()) <= 9) {
                if (redDiamondGen.containsKey("diamond"))
                    redDiamondGen.put("diamond", 0);
            }
            if (BlueDiamondGenerator.distanceSquared(item.getLocation()) <= 9) {
                if (blueDiamondGen.containsKey("diamond"))
                    blueDiamondGen.put("diamond", 0);
            }
        }

        if (item.getItemStack().getType() == Material.EMERALD) {
            if (EmeraldGenSpawn1.distanceSquared(item.getLocation()) <= 9) {
                if (emeraldGen1.containsKey("emerald"))
                    emeraldGen1.put("emerald", 0);
            }
            if (EmeraldGenSpawn2.distanceSquared(item.getLocation()) <= 9) {
                if (emeraldGen2.containsKey("emerald"))
                    emeraldGen2.put("emerald", 0);
            }
            if (EmeraldGenSpawn3.distanceSquared(item.getLocation()) <= 9) {
                if (emeraldGen3.containsKey("emerald"))
                    emeraldGen3.put("emerald", 0);
            }
            if (EmeraldGenSpawn4.distanceSquared(item.getLocation()) <= 9) {
                if (emeraldGen4.containsKey("emerald"))
                    emeraldGen4.put("emerald", 0);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        Item item = event.getItem();
        if (item.getItemStack().getType() == Material.IRON_INGOT) {
            if (Bedwars.RedGenerator.distanceSquared(item.getLocation()) <= 9) {
                if (item.getItemStack().getAmount() > 48) {
                    redGen.put("iron", 0);
                } else {
                    redGen.put("iron", ((int) redGen.get("iron") - item.getItemStack().getAmount()));
                }
            }
            if (Bedwars.BlueGenerator.distanceSquared(item.getLocation()) <= 9) {
                if (item.getItemStack().getAmount() > 48) {
                    blueGen.put("iron", 0);
                } else {
                    blueGen.put("iron", ((int) blueGen.get("iron") - item.getItemStack().getAmount()));
                }
            }
        }

        if (item.getItemStack().getType() == Material.GOLD_INGOT) {
            if (Bedwars.RedGenerator.distanceSquared(item.getLocation()) <= 9) {
                if (item.getItemStack().getAmount() > 8) {
                    redGen.put("gold", 0);
                } else {
                    redGen.put("gold", ((int) redGen.get("gold") - item.getItemStack().getAmount()));
                }
            }
            if (Bedwars.BlueGenerator.distanceSquared(item.getLocation()) <= 9) {
                if (item.getItemStack().getAmount() > 8) {
                    blueGen.put("gold", 0);
                } else {
                    blueGen.put("gold", ((int) blueGen.get("gold") - item.getItemStack().getAmount()));
                }
            }
        }

        if (item.getItemStack().getType() == Material.DIAMOND) {
            if (RedDiamondGenerator.distanceSquared(item.getLocation()) <= 9) {
                if (item.getItemStack().getAmount() > 4) {
                    redDiamondGen.put("diamond", 0);
                } else {
                    redDiamondGen.put("diamond", ((int) redDiamondGen.get("diamond") - item.getItemStack().getAmount()));
                }
            }
            if (BlueDiamondGenerator.distanceSquared(item.getLocation()) <= 9) {
                if (item.getItemStack().getAmount() > 4) {
                    blueDiamondGen.put("diamond", 0);
                } else {
                    blueDiamondGen.put("diamond", ((int) blueDiamondGen.get("diamond") - item.getItemStack().getAmount()));
                }
            }
        }

        if (item.getItemStack().getType() == Material.EMERALD) {
            if (EmeraldGenSpawn1.distanceSquared(item.getLocation()) <= 9) {
                if (item.getItemStack().getAmount() > 2) {
                    emeraldGen1.put("emerald", 0);
                } else {
                    emeraldGen1.put("emerald", ((int) emeraldGen1.get("emerald") - item.getItemStack().getAmount()));
                }
            }
            if (EmeraldGenSpawn2.distanceSquared(item.getLocation()) <= 9) {
                if (item.getItemStack().getAmount() > 2) {
                    emeraldGen2.put("emerald", 0);
                } else {
                    emeraldGen2.put("emerald", ((int) emeraldGen2.get("emerald") - item.getItemStack().getAmount()));
                }
            }
            if (EmeraldGenSpawn3.distanceSquared(item.getLocation()) <= 9) {
                if (item.getItemStack().getAmount() > 2) {
                    emeraldGen3.put("emerald", 0);
                } else {
                    emeraldGen3.put("emerald", ((int) emeraldGen3.get("emerald") - item.getItemStack().getAmount()));
                }
            }
            if (EmeraldGenSpawn4.distanceSquared(item.getLocation()) <= 9) {
                if (item.getItemStack().getAmount() > 2) {
                    emeraldGen4.put("emerald", 0);
                } else {
                    emeraldGen4.put("emerald", ((int) emeraldGen4.get("emerald") - item.getItemStack().getAmount()));
                }
            }
        }
    }

    public static void saveQuickBuy(Player player, int slot, ItemStack item) {
        Document filter = new Document("UUID", ((Player) player).getUniqueId().toString());

        Bson update = set("gameSettings.bedwars.quickBuy." + slot, ItemStackSerializer.serialize(item));
        me.teakivy.lobby.Main.getPlugin(me.teakivy.lobby.Main.class).col.findOneAndUpdate(filter, update);
    }

    public static void saveQuickBuy(Document filter, int slot, ItemStack item) {
        Bson update = set("gameSettings.bedwars.quickBuy." + slot, ItemStackSerializer.serialize(item));
        me.teakivy.lobby.Main.getPlugin(me.teakivy.lobby.Main.class).col.findOneAndUpdate(filter, update);
    }

    public static void saveQuickBuy(Document filter, int slot, String itemName) {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(itemName);
        item.setItemMeta(itemMeta);

        Bson update = set("gameSettings.bedwars.quickBuy." + slot, ItemStackSerializer.serialize(item));
        me.teakivy.lobby.Main.getPlugin(me.teakivy.lobby.Main.class).col.findOneAndUpdate(filter, update);
    }

    public static void saveQuickBuy(Player player, int slot, String itemName) {
        Document filter = new Document("UUID", player.getUniqueId().toString());

        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(itemName);
        item.setItemMeta(itemMeta);

        Bson update = set("gameSettings.bedwars.quickBuy." + slot, ItemStackSerializer.serialize(item));
        me.teakivy.lobby.Main.getPlugin(me.teakivy.lobby.Main.class).col.findOneAndUpdate(filter, update);
    }

    public static ItemStack getQuickBuy(Player player, int slot) {
        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) me.teakivy.lobby.Main.getPlugin(me.teakivy.lobby.Main.class).col.find(filter).first().get("gameSettings");
        Document gameType = (Document) obj.get("bedwars");
        Document gameOptions = (Document) gameType.get("quickBuy");
        String option = gameOptions.getString("" + slot);
        ItemStack item = ItemStackSerializer.deserialize(option);
        return item;
    }

    public static ItemStack getQuickBuy(int slot, Document document, Player player) {
        Inventory inv = player.getInventory();

        String option = document.getString("" + slot);
        ItemStack item = createItem(Material.STAINED_GLASS_PANE, ChatColor.GOLD + "Quick Buy", new String[]{ChatColor.GRAY + "Shift click an item to", ChatColor.GRAY + "add it to your Quick Buy"}, 1, (byte) 14);
        if (option != null) {
            item = ItemStackSerializer.deserialize(option);
        }
        if (!item.hasItemMeta()) return item;
        return getShopItem(player, item.getItemMeta().getDisplayName());
    }

    public static ItemStack getShopItem(Player player, String name) {
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
        ItemStack item = createItem(Material.STAINED_GLASS_PANE, ChatColor.GOLD + "Quick Buy", new String[]{ChatColor.GRAY + "Shift click an item to", ChatColor.GRAY + "add it to your Quick Buy"}, 1, (byte) 14);

        switch (name) {
            case "Wool":
                return MainShop.createShopItem(Material.WOOL, "Wool", 4, ironAmount, "Iron", 16);
            case "Stone Sword":
                return MainShop.createShopItem(Material.STONE_SWORD, "Stone Sword", 10, ironAmount, "Iron");
            case "Permanent Chainmail Armor":
                return MainShop.createArmorShopItem(player, Material.CHAINMAIL_BOOTS, "Permanent Chainmail Armor", 40, ironAmount, "Iron");
            case "Bow":
                return MainShop.createShopItem(Material.BOW, "Bow", 12, goldAmount, "Gold");
            case "Speed II Potion (45 Seconds)":
                return MainShop.createShopItem(Material.POTION, "Speed II Potion (45 Seconds)", 1, emeraldAmount, "Emerald", 1, (byte) 8195);
            case "TNT":
                return MainShop.createShopItem(Material.TNT, "TNT", 4, goldAmount, "Gold");
            case "Wood":
                return MainShop.createShopItem(Material.WOOD, "Wood", 4, goldAmount, "Gold", 16);
            case "Iron Sword":
                return MainShop.createShopItem(Material.IRON_SWORD, "Iron Sword", 7, goldAmount, "Gold");
            case "Permanent Iron Armor":
                return MainShop.createArmorShopItem(player, Material.IRON_BOOTS, "Permanent Iron Armor", 12, goldAmount, "Gold");
            case "Permanent Shears":
                return MainShop.createShearShopItem(ironAmount, player);
            case "Arrow":
                return MainShop.createShopItem(Material.ARROW, "Arrow", 2, goldAmount, "Gold", 8);
            case "Invisibility Potion (30 Seconds)":
                return MainShop.createShopItem(Material.POTION, "Invisibility Potion (30 Seconds)", 2, emeraldAmount, "Emerald", 1, (byte) 8206);
            case "Water Bucket":
                return MainShop.createShopItem(Material.WATER_BUCKET, "Water Bucket", 3, goldAmount, "Gold");
            case "Hardened Clay":
                return createShopItem(Material.HARD_CLAY, "Hardened Clay", 12, ironAmount, "Iron", 16);
            case "Blast-Proof Glass":
                return createShopItem(Material.GLASS, "Blast-Proof Glass", 12, ironAmount, "Iron", 4);
            case "End Stone":
                return createShopItem(Material.ENDER_STONE, "End Stone", 24, ironAmount, "Iron", 12);
            case "Ladder":
                return createShopItem(Material.LADDER, "Ladder", 4, ironAmount, "Iron", 16);
            case "Obsidian":
                return createShopItem(Material.OBSIDIAN, "Obsidian", 4, emeraldAmount, "Emerald", 4);
            case "Diamond Sword":
                return createShopItem(Material.DIAMOND_SWORD, "Diamond Sword", 4, emeraldAmount, "Emerald");
            case "Stick (Knockback I)":
                return createShopItem(Material.STICK, "Stick (Knockback I)", 5, goldAmount, "Gold", Enchantment.KNOCKBACK);
            case "Permanent Diamond Armor":
                return createArmorShopItem(player, Material.DIAMOND_BOOTS, "Permanent Diamond Armor", 6, emeraldAmount, "Emerald");
            case "Pickaxe":
                return createPickaxeShopItem(ironAmount, goldAmount, player);
            case "Axe":
                return createAxeShopItem(ironAmount, goldAmount, player);
            case "Bow (Power I)":
                return createShopItem(Material.BOW, "Bow (Power I)", 24, goldAmount, "Gold", Enchantment.ARROW_DAMAGE);
            case "Bow (Power I, Punch I)":
                return createShopItem(Material.BOW, "Bow (Power I, Punch I)", 6, emeraldAmount, "Emerald", Enchantment.ARROW_KNOCKBACK);
            case "Jump V Potion (45 Seconds)":
                return createShopItem(Material.POTION, "Jump V Potion (45 Seconds)", 1, emeraldAmount, "Emerald", 1, (byte) 8235);
            case "Golden Apple":
                return createShopItem(Material.GOLDEN_APPLE, "Golden Apple", 3, goldAmount, "Gold");
            case "Bedbug":
                return createShopItem(Material.SNOW_BALL, "Bedbug", 40, ironAmount, "Iron");
            case "Dream Defender":
                return createShopItem(Material.MONSTER_EGG, "Dream Defender", 120, ironAmount, "Iron");
            case "Fireball":
                return createShopItem(Material.FIREBALL, "Fireball", 40, ironAmount, "Iron");
            case "Ender Pearl":
                return createShopItem(Material.ENDER_PEARL, "Ender Pearl", 4, emeraldAmount, "Emerald");
            case "Bridge Egg":
                return createShopItem(Material.EGG, "Bridge Egg", 2, emeraldAmount, "Emerald");
            case "Magic Milk":
                return createShopItem(Material.MILK_BUCKET, "Magic Milk", 4, goldAmount, "Gold");
            case "Sponge":
                return createShopItem(Material.SPONGE, "Sponge", 3, goldAmount, "Gold");
            case "Compact Pop-Up Tower":
                return createShopItem(Material.CHEST, "Compact Pop-Up Tower", 24, ironAmount, "Iron");
            case "Quick Buy":
                return createItem(Material.STAINED_GLASS_PANE, ChatColor.GOLD + "Quick Buy", new String[]{ChatColor.GRAY + "Shift click an item to", ChatColor.GRAY + "add it to your Quick Buy"}, 1, (byte) 14);
        }
        return item;
    }

    public static Document getQuickBuyDocument(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());
        Document obj = (Document) me.teakivy.lobby.Main.getPlugin(me.teakivy.lobby.Main.class).col.find(filter).first().get("gameSettings");
        Document gameType = (Document) obj.get("bedwars");
        return (Document) gameType.get("quickBuy");
    }
}
