package me.teakivy.lobby.Bedwars.util;

import me.teakivy.lobby.Bedwars.Bedwars;
import me.teakivy.lobby.Bedwars.menus.MainShop;
import me.teakivy.lobby.Events.RickClickNPC;
import me.teakivy.lobby.Main;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ShopKeeper implements Listener {

    @EventHandler
    public void onClick(RickClickNPC event) {
        Player player = event.getPlayer();
        if (event.getNPC().getName().equalsIgnoreCase("ShopKeeper")) {
            MainShop.openShopKeeper(player);
        }
//        if (event.getNPC().getName().equalsIgnoreCase("UpgradeShop")) {
//            Lobby.getPlugin(Lobby.class).openProfileMenu(player);
//        }
    }

    @EventHandler
    public void inventoryClose(InventoryCloseEvent event) {
        MainShop.ShopOpen.remove(event.getPlayer());
    }



    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        if (!Bedwars.BedwarsGame.get(player)) return;

        if (event.getClickedInventory() != null) {

            if (player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase(ChatColor.GOLD + "Game Shop")) {
                if (event.getClickedInventory().getTitle().equalsIgnoreCase(ChatColor.GOLD + "Game Shop")) {
                    if (event.getCurrentItem() != null) {
                        if (event.getCurrentItem().getItemMeta() != null) {



                            MainShop.updateMainShop(player);
                            attemptPurchaseEveryItem(player, event);
                            checkPage(player, event);
                        }
                    }
                }
                event.setCancelled(true);
            }

            if (player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase(ChatColor.GOLD + "Game Shop - Blocks")) {
                if (event.getClickedInventory().getTitle().equalsIgnoreCase(ChatColor.GOLD + "Game Shop - Blocks")) {
                    if (event.getCurrentItem() != null) {
                        if (event.getCurrentItem().getItemMeta() != null) {




                            MainShop.updateBlocksShop(player);
                            attemptPurchaseEveryItem(player, event);
                            checkPage(player, event);
                        }
                    }
                }
                event.setCancelled(true);
            }

            if (player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase(ChatColor.GOLD + "Game Shop - Melee")) {
                if (event.getClickedInventory().getTitle().equalsIgnoreCase(ChatColor.GOLD + "Game Shop - Melee")) {
                    if (event.getCurrentItem() != null) {
                        if (event.getCurrentItem().getItemMeta() != null) {





                            MainShop.updateMeleeShop(player);
                            attemptPurchaseEveryItem(player, event);
                            checkPage(player, event);
                        }
                    }
                }
                event.setCancelled(true);
            }

            if (player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase(ChatColor.GOLD + "Game Shop - Armor")) {
                if (event.getClickedInventory().getTitle().equalsIgnoreCase(ChatColor.GOLD + "Game Shop - Armor")) {
                    if (event.getCurrentItem() != null) {
                        if (event.getCurrentItem().getItemMeta() != null) {



                            MainShop.updateArmorShop(player);
                            attemptPurchaseEveryItem(player, event);
                            checkPage(player, event);
                        }
                    }
                }
                event.setCancelled(true);
            }

            if (player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase(ChatColor.GOLD + "Game Shop - Tools")) {
                if (event.getClickedInventory().getTitle().equalsIgnoreCase(ChatColor.GOLD + "Game Shop - Tools")) {
                    if (event.getCurrentItem() != null) {
                        if (event.getCurrentItem().getItemMeta() != null) {



                            MainShop.updateToolsShop(player);
                            attemptPurchaseEveryItem(player, event);
                            checkPage(player, event);
                        }
                    }
                }
                event.setCancelled(true);
            }

            if (player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase(ChatColor.GOLD + "Game Shop - Projectiles")) {
                if (event.getClickedInventory().getTitle().equalsIgnoreCase(ChatColor.GOLD + "Game Shop - Projectiles")) {
                    if (event.getCurrentItem() != null) {
                        if (event.getCurrentItem().getItemMeta() != null) {





                            MainShop.updateProjectileShop(player);
                            attemptPurchaseEveryItem(player, event);
                            checkPage(player, event);
                        }
                    }
                }
                event.setCancelled(true);
            }

            if (player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase(ChatColor.GOLD + "Game Shop - Potions")) {
                if (event.getClickedInventory().getTitle().equalsIgnoreCase(ChatColor.GOLD + "Game Shop - Potions")) {
                    if (event.getCurrentItem() != null) {
                        if (event.getCurrentItem().getItemMeta() != null) {





                            MainShop.updatePotionShop(player);
                            attemptPurchaseEveryItem(player, event);
                            checkPage(player, event);
                        }
                    }
                }
                event.setCancelled(true);
            }

            if (player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase(ChatColor.GOLD + "Game Shop - Utilities")) {
                if (event.getClickedInventory().getTitle().equalsIgnoreCase(ChatColor.GOLD + "Game Shop - Utilities")) {
                    if (event.getCurrentItem() != null) {
                        if (event.getCurrentItem().getItemMeta() != null) {

                            MainShop.updateUtilitiesShop(player);
                            attemptPurchaseEveryItem(player, event);

                            checkPage(player, event);
                        }
                    }
                }
                event.setCancelled(true);
            }

            if (player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase(ChatColor.GOLD + "Game Shop - Quick Buy")) {
                if (event.getClickedInventory().getTitle().equalsIgnoreCase(ChatColor.GOLD + "Game Shop - Quick Buy")) {
                    if (event.getCurrentItem() != null) {
                        if (event.getCurrentItem().getItemMeta() != null) {
                            attemptAddItemToQuickBuy(player, event);
                        }
                    }
                }
                event.setCancelled(true);
            }

        }
    }

    public static void checkPage (Player player, InventoryClickEvent event) {
        changePage(player, "Quick Buy", event);
        changePage(player, "Blocks", event);
        changePage(player, "Melee", event);
        changePage(player, "Armor", event);
        changePage(player, "Tools", event);
        changePage(player, "Projectiles", event);
        changePage(player, "Potions", event);
        changePage(player, "Utilities", event);
    }

    public static void addItem(Player player, ItemStack item) {
        player.getInventory().addItem(item);
    }

    public static void attemptPurchase(Player player, String name, ItemStack item, Material costItem, int cost, InventoryClickEvent event) {
        String materialName = "";
        if (costItem.equals(Material.IRON_INGOT)) {
            materialName = "Iron";
        }
        if (costItem.equals(Material.GOLD_INGOT)) {
            materialName = "Gold";
        }
        if (costItem.equals(Material.DIAMOND)) {
            materialName = "Diamonds";
        }
        if (costItem.equals(Material.EMERALD)) {
            materialName = "Emeralds";
        }

        if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + name) || event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + name)) {
            if (player.getInventory().containsAtLeast(new ItemStack(costItem), cost)) {
                addItem(player, item);
                player.playSound(player.getLocation(), Sound.NOTE_PIANO,1, 2);
                player.sendMessage(ChatColor.GREEN + "You Purchased " + ChatColor.GOLD + name + ChatColor.GREEN + "!");
                player.getInventory().removeItem(new ItemStack(costItem, cost));
            } else {
                player.sendMessage(ChatColor.RED + "Not Enough " + materialName + "!");
            }
        }
    }

    public static void attemptSwordPurchase(Player player, String name, ItemStack item, Material costItem, int cost, InventoryClickEvent event) {
        String materialName = "";
        if (costItem.equals(Material.IRON_INGOT)) {
            materialName = "Iron";
        }
        if (costItem.equals(Material.GOLD_INGOT)) {
            materialName = "Gold";
        }
        if (costItem.equals(Material.DIAMOND)) {
            materialName = "Diamonds";
        }
        if (costItem.equals(Material.EMERALD)) {
            materialName = "Emeralds";
        }
        int swordSlot = 55;

        if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + name) || event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + name)) {
            if (player.getInventory().containsAtLeast(new ItemStack(costItem), cost)) {
                Inventory inv = player.getInventory();
                for (int i = 0; i < 36; i++) {
                    if (inv.getItem(i) != null) {
                        if (inv.getItem(i).getType() == Material.WOOD_SWORD) {
                            swordSlot = i;
                        }
                    }
                }
                if (swordSlot == 55) {
                    addItem(player, item);
                } else {
                    player.getInventory().setItem(swordSlot, item);
                }
                player.playSound(player.getLocation(), Sound.NOTE_PIANO,1, 2);
                player.sendMessage(ChatColor.GREEN + "You Purchased " + ChatColor.GOLD + name + ChatColor.GREEN + "!");
                player.getInventory().removeItem(new ItemStack(costItem, cost));
            } else {
                player.sendMessage(ChatColor.RED + "Not Enough " + materialName + "!");
            }
        }
    }

    public static void attemptArmorPurchase(Player player, String name, ItemStack item, Material costItem, int cost, InventoryClickEvent event) {
        String materialName = "";
        if (costItem.equals(Material.IRON_INGOT)) {
            materialName = "Iron";
        }
        if (costItem.equals(Material.GOLD_INGOT)) {
            materialName = "Gold";
        }
        if (costItem.equals(Material.DIAMOND)) {
            materialName = "Diamonds";
        }
        if (costItem.equals(Material.EMERALD)) {
            materialName = "Emeralds";
        }

        if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + name)) {
            if (player.getInventory().containsAtLeast(new ItemStack(costItem), cost)) {
                if (name.equalsIgnoreCase("Permanent Chainmail Armor")) {
                    BedwarsInventory.setPlayerArmor(player, Material.CHAINMAIL_BOOTS);
                }
                if (name.equalsIgnoreCase("Permanent Iron Armor")) {
                    BedwarsInventory.setPlayerArmor(player, Material.IRON_BOOTS);
                }
                if (name.equalsIgnoreCase("Permanent Diamond Armor")) {
                    BedwarsInventory.setPlayerArmor(player, Material.DIAMOND_BOOTS);
                }
                player.playSound(player.getLocation(), Sound.NOTE_PIANO,1, 2);
                player.sendMessage(ChatColor.GREEN + "You Purchased " + ChatColor.GOLD + name + ChatColor.GREEN + "!");
                player.getInventory().removeItem(new ItemStack(costItem, cost));
            } else {
                player.sendMessage(ChatColor.RED + "Not Enough " + materialName + "!");
            }
        }
    }

    public static void attemptShearPurchase(Player player, String name, ItemStack item, Material costItem, int cost, InventoryClickEvent event) {
        String materialName = "";
        if (costItem.equals(Material.IRON_INGOT)) {
            materialName = "Iron";
        }
        if (costItem.equals(Material.GOLD_INGOT)) {
            materialName = "Gold";
        }
        if (costItem.equals(Material.DIAMOND)) {
            materialName = "Diamonds";
        }
        if (costItem.equals(Material.EMERALD)) {
            materialName = "Emeralds";
        }

        item = new ItemStack(Material.SHEARS);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.spigot().setUnbreakable(true);
        itemMeta.setDisplayName(ChatColor.GOLD + "Permanent Shears");
        item.setItemMeta(itemMeta);

        if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + name)) {
            if (player.getInventory().containsAtLeast(new ItemStack(costItem), cost)) {
                addItem(player, item);
                BedwarsInventory.PlayerShears.put(player, true);
                player.playSound(player.getLocation(), Sound.NOTE_PIANO,1, 2);
                player.sendMessage(ChatColor.GREEN + "You Purchased " + ChatColor.GOLD + name + ChatColor.GREEN + "!");
                player.getInventory().removeItem(new ItemStack(costItem, cost));
            } else {
                player.sendMessage(ChatColor.RED + "Not Enough " + materialName + "!");
            }
        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + name)) {
            if (BedwarsInventory.PlayerShears.get(player)) {
                player.sendMessage(ChatColor.RED + "Already Purchased!");
                return;
            }
            player.sendMessage(ChatColor.RED + "Not Enough Iron!");

        }
    }

    public static void attemptPickaxePurchase(Player player, InventoryClickEvent event) {

        int pickaxeSlot = 55;

        if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Wood Pickaxe (Efficiency I)")) {
            if (player.getInventory().containsAtLeast(new ItemStack(Material.IRON_INGOT), 10)) {

                ItemStack item = new ItemStack(Material.WOOD_PICKAXE);
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.spigot().setUnbreakable(true);
                itemMeta.addEnchant(Enchantment.DIG_SPEED, 1, true);
                itemMeta.setDisplayName(ChatColor.GOLD + "Wood Pickaxe (Efficiency I)");
                item.setItemMeta(itemMeta);

                addItem(player, item);
                BedwarsInventory.PlayerPickaxe.put(player, Material.WOOD_PICKAXE);
                player.playSound(player.getLocation(), Sound.NOTE_PIANO,1, 2);
                player.sendMessage(ChatColor.GREEN + "You Purchased " + ChatColor.GOLD + "Wood Pickaxe (Efficiency I)" + ChatColor.GREEN + "!");
                player.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 10));
            } else {
                player.sendMessage(ChatColor.RED + "Not Enough " + "Iron" + "!");
            }
        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Iron Pickaxe (Efficiency I)")) {
            if (player.getInventory().containsAtLeast(new ItemStack(Material.IRON_INGOT), 10)) {
                ItemStack item = new ItemStack(Material.IRON_PICKAXE);
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.spigot().setUnbreakable(true);
                itemMeta.addEnchant(Enchantment.DIG_SPEED, 1, true);
                itemMeta.setDisplayName(ChatColor.GOLD + "Iron Pickaxe (Efficiency I)");
                item.setItemMeta(itemMeta);

                for (int i = 0; i < 36; i++) {
                    if (player.getInventory().getItem(i) != null) {
                        if (player.getInventory().getItem(i).getType() == Material.WOOD_PICKAXE) {
                            pickaxeSlot = i;
                        }
                    }
                }
                player.getInventory().setItem(pickaxeSlot, item);
                BedwarsInventory.PlayerPickaxe.put(player, Material.IRON_PICKAXE);
                player.playSound(player.getLocation(), Sound.NOTE_PIANO,1, 2);
                player.sendMessage(ChatColor.GREEN + "You Purchased " + ChatColor.GOLD + "Iron Pickaxe (Efficiency I)" + ChatColor.GREEN + "!");
                player.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 10));
            } else {
                player.sendMessage(ChatColor.RED + "Not Enough " + "Iron" + "!");
            }
        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Gold Pickaxe (Efficiency III, Sharpness II)")) {
            if (player.getInventory().containsAtLeast(new ItemStack(Material.GOLD_INGOT), 3)) {
                ItemStack item = new ItemStack(Material.GOLD_PICKAXE);
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.spigot().setUnbreakable(true);
                itemMeta.addEnchant(Enchantment.DIG_SPEED, 3, true);
                itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 2, true);
                itemMeta.setDisplayName(ChatColor.GOLD + "Gold Pickaxe (Efficiency III, Sharpness II)");
                item.setItemMeta(itemMeta);

                for (int i = 0; i < 36; i++) {
                    if (player.getInventory().getItem(i) != null) {
                        if (player.getInventory().getItem(i).getType() == Material.IRON_PICKAXE) {
                            pickaxeSlot = i;
                        }
                    }
                }
                player.getInventory().setItem(pickaxeSlot, item);
                BedwarsInventory.PlayerPickaxe.put(player, Material.GOLD_PICKAXE);
                player.playSound(player.getLocation(), Sound.NOTE_PIANO,1, 2);
                player.sendMessage(ChatColor.GREEN + "You Purchased " + ChatColor.GOLD + "Gold Pickaxe (Efficiency III, Sharpness II)" + ChatColor.GREEN + "!");
                player.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 3));
            } else {
                player.sendMessage(ChatColor.RED + "Not Enough " + "Gold" + "!");
            }
        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Diamond Pickaxe (Efficiency III)")) {
            if (player.getInventory().containsAtLeast(new ItemStack(Material.GOLD_INGOT), 6)) {
                ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.spigot().setUnbreakable(true);
                itemMeta.addEnchant(Enchantment.DIG_SPEED, 3, true);
                itemMeta.setDisplayName(ChatColor.GOLD + "Diamond Pickaxe (Efficiency III)");
                item.setItemMeta(itemMeta);

                for (int i = 0; i < 36; i++) {
                    if (player.getInventory().getItem(i) != null) {
                        if (player.getInventory().getItem(i).getType() == Material.GOLD_PICKAXE) {
                            pickaxeSlot = i;
                        }
                    }
                }
                player.getInventory().setItem(pickaxeSlot, item);
                BedwarsInventory.PlayerPickaxe.put(player, Material.DIAMOND_PICKAXE);
                player.playSound(player.getLocation(), Sound.NOTE_PIANO,1, 2);
                player.sendMessage(ChatColor.GREEN + "You Purchased " + ChatColor.GOLD + "Diamond Pickaxe (Efficiency III)" + ChatColor.GREEN + "!");
                player.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 6));
            } else {
                player.sendMessage(ChatColor.RED + "Not Enough " + "Gold" + "!");
            }
        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Diamond Pickaxe (Efficiency III)")) {
            player.sendMessage(ChatColor.RED + "Already Purchased!");
        }
    }

    public static void attemptAddItemToQuickBuy(Player player, InventoryClickEvent event) {
        if ((event.getSlot() >= 19 && event.getSlot() <= 25) || (event.getSlot() >= 28 && event.getSlot() <= 34) || (event.getSlot() >= 37 && event.getSlot() <= 43)) {
            if (event.getInventory().getName().equalsIgnoreCase(ChatColor.GOLD + "Game Shop - Quick Buy")) {
                String name = event.getInventory().getItem(4).getItemMeta().getDisplayName().replace(ChatColor.GREEN + "", "").replace(ChatColor.RED + "", "").replace(ChatColor.GOLD + "", "");
                if (!name.replace("Pickaxe", "").equalsIgnoreCase(name)) name = "Pickaxe";
                if (!name.replace("Axe", "").equalsIgnoreCase(name)) name = "Axe";
                BedwarsInventory.saveQuickBuy(player, event.getSlot(), name);
                System.out.println(name);
                MainShop.openShopKeeper(player);
                return;
            }
        }
        if (event.getSlot() == 53) {
            MainShop.openShopKeeper(player);
        }
    }

    public static void attemptPurchaseEveryItem(Player player, InventoryClickEvent event) {
        if (event.getClick().equals(ClickType.SHIFT_LEFT) || event.getClick().equals(ClickType.SHIFT_RIGHT)) {
            if ((event.getSlot() >= 19 && event.getSlot() <= 25) || (event.getSlot() >= 28 && event.getSlot() <= 34) || (event.getSlot() >= 37 && event.getSlot() <= 43)) {
                if (event.getInventory().getName().equalsIgnoreCase(ChatColor.GOLD + "Game Shop")) {
                    BedwarsInventory.saveQuickBuy(player, event.getSlot(), "Quick Buy");
                    MainShop.updateMainShop(player);
                    return;
                }
                MainShop.addItemToQuickBuyMenu(player, event.getCurrentItem());
            }
            return;
        }
        ItemStack kbStick = new ItemStack(Material.STICK);
        ItemMeta kbStickMeta = kbStick.getItemMeta();
        kbStickMeta.setDisplayName(ChatColor.GOLD + "Stick (Knockback I)");
        kbStickMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);
        kbStick.setItemMeta(kbStickMeta);

        ItemStack powerBow = new ItemStack(Material.BOW);
        ItemMeta powerBowMeta = powerBow.getItemMeta();
        powerBowMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        powerBow.setItemMeta(powerBowMeta);

        ItemStack punchBow = new ItemStack(Material.BOW);
        ItemMeta punchBowMeta = punchBow.getItemMeta();
        punchBowMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        punchBowMeta.addEnchant(Enchantment.ARROW_KNOCKBACK, 1, true);
        punchBow.setItemMeta(punchBowMeta);

        ItemStack speedPotion = new ItemStack(Material.POTION, 1, (byte) 8195);
        PotionMeta speedPotionMeta = (PotionMeta) speedPotion.getItemMeta();
        speedPotionMeta.setDisplayName(ChatColor.GOLD + "Speed II Potion (45 Seconds)");
        speedPotionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 45*20, 1, true, false), true);
        speedPotion.setItemMeta(speedPotionMeta);

        ItemStack jumpPotion = new ItemStack(Material.POTION, 1, (byte) 8235);
        PotionMeta jumpPotionMeta = (PotionMeta) jumpPotion.getItemMeta();
        jumpPotionMeta.setDisplayName(ChatColor.GOLD + "Jump V Potion (45 Seconds)");
        jumpPotionMeta.addCustomEffect(new PotionEffect(PotionEffectType.JUMP, 45*20, 4, true, false), true);
        jumpPotion.setItemMeta(jumpPotionMeta);

        ItemStack invisiblePotion = new ItemStack(Material.POTION, 1, (byte) 8206);
        PotionMeta invisiblePotionMeta = (PotionMeta) invisiblePotion.getItemMeta();
        invisiblePotionMeta.setDisplayName(ChatColor.GOLD + "Invisibility Potion (30 Seconds)");
        invisiblePotionMeta.addCustomEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 30*20, 0, true, false), true);
        invisiblePotion.setItemMeta(invisiblePotionMeta);

        ItemStack dreamDefender = new ItemStack(Material.MONSTER_EGG);
        ItemMeta dreamDefenderMeta = dreamDefender.getItemMeta();
        dreamDefenderMeta.setDisplayName(ChatColor.GOLD + "Dream Defender");
        dreamDefender.setItemMeta(dreamDefenderMeta);

        ItemStack bedBug = new ItemStack(Material.SNOW_BALL);
        ItemMeta bedBugMeta = bedBug.getItemMeta();
        bedBugMeta.setDisplayName(ChatColor.GOLD + "Bedbug");
        bedBug.setItemMeta(bedBugMeta);

        ItemStack bridgeEgg = new ItemStack(Material.EGG);
        ItemMeta bridgeEggMeta = bridgeEgg.getItemMeta();
        bridgeEggMeta.setDisplayName(ChatColor.GOLD + "Bridge Egg");
        bridgeEgg.setItemMeta(bridgeEggMeta);

        attemptPurchase(player, "Wool", new ItemStack(Material.WOOL, 16, BedwarsPlayer.getByteColor(player)), Material.IRON_INGOT, 4, event);
        attemptPurchase(player, "Hardened Clay", new ItemStack(Material.STAINED_CLAY, 16, BedwarsPlayer.getByteColor(player)), Material.IRON_INGOT, 12, event);
        attemptPurchase(player, "Blast-Proof Glass", new ItemStack(Material.STAINED_GLASS, 4, BedwarsPlayer.getByteColor(player)), Material.IRON_INGOT, 12, event);
        attemptPurchase(player, "End Stone", new ItemStack(Material.ENDER_STONE, 12), Material.IRON_INGOT, 24, event);
        attemptPurchase(player, "Ladder", new ItemStack(Material.LADDER, 16), Material.IRON_INGOT, 4, event);
        attemptPurchase(player, "Wood", new ItemStack(Material.WOOD, 16), Material.GOLD_INGOT, 4, event);
        attemptPurchase(player, "Obsidian", new ItemStack(Material.OBSIDIAN, 4), Material.EMERALD, 4, event);

        attemptSwordPurchase(player, "Stone Sword", new ItemStack(Material.STONE_SWORD), Material.IRON_INGOT, 10, event);
        attemptSwordPurchase(player, "Iron Sword", new ItemStack(Material.IRON_SWORD), Material.GOLD_INGOT, 7, event);
        attemptSwordPurchase(player, "Diamond Sword", new ItemStack(Material.DIAMOND_SWORD), Material.EMERALD, 4, event);
        attemptPurchase(player, "Stick (Knockback I)", kbStick, Material.GOLD_INGOT, 5, event);

        attemptArmorPurchase(player, "Permanent Chainmail Armor", new ItemStack(Material.CHAINMAIL_BOOTS), Material.IRON_INGOT, 40, event);
        attemptArmorPurchase(player, "Permanent Iron Armor", new ItemStack(Material.IRON_BOOTS), Material.GOLD_INGOT, 12, event);
        attemptArmorPurchase(player, "Permanent Diamond Armor", new ItemStack(Material.DIAMOND_SWORD), Material.EMERALD, 6, event);

        attemptShearPurchase(player, "Permanent Shears", new ItemStack(Material.SHEARS), Material.IRON_INGOT, 20, event);
        attemptPickaxePurchase(player, event);
        attemptAxePurchase(player, event);

        attemptPurchase(player, "Arrow", new ItemStack(Material.ARROW, 8), Material.GOLD_INGOT, 2, event);
        attemptPurchase(player, "Bow", new ItemStack(Material.BOW), Material.GOLD_INGOT, 12, event);
        attemptPurchase(player, "Bow (Power I)", powerBow, Material.GOLD_INGOT, 24, event);
        attemptPurchase(player, "Bow (Power I, Punch I)", punchBow, Material.EMERALD, 6, event);

        attemptPurchase(player, "Speed II Potion (45 Seconds)", speedPotion, Material.EMERALD, 1, event);
        attemptPurchase(player, "Jump V Potion (45 Seconds)", jumpPotion, Material.EMERALD, 1, event);
        attemptPurchase(player, "Invisibility Potion (30 Seconds)", invisiblePotion, Material.EMERALD, 2, event);

        attemptPurchase(player, "Golden Apple", new ItemStack(Material.GOLDEN_APPLE), Material.GOLD_INGOT, 3, event);
        attemptPurchase(player, "BedBug", bedBug, Material.IRON_INGOT, 40, event);
        attemptPurchase(player, "Dream Defender", dreamDefender, Material.IRON_INGOT, 120, event);
        attemptPurchase(player, "Fireball", new ItemStack(Material.FIREBALL), Material.IRON_INGOT, 40, event);
        attemptPurchase(player, "TNT", new ItemStack(Material.TNT), Material.GOLD_INGOT, 4, event);
        attemptPurchase(player, "Ender Pearl", new ItemStack(Material.ENDER_PEARL), Material.EMERALD, 4, event);
        attemptPurchase(player, "Water Bucket", new ItemStack(Material.WATER_BUCKET), Material.GOLD_INGOT, 3, event);
        attemptPurchase(player, "Bridge Egg", new ItemStack(Material.AIR), Material.EMERALD, 0, event);
        attemptPurchase(player, "Magic Milk", new ItemStack(Material.MILK_BUCKET), Material.GOLD_INGOT, 4, event);
        attemptPurchase(player, "Compact Pop-Up Tower", new ItemStack(Material.CHEST), Material.IRON_INGOT, 24, event);
    }



    public static void attemptAxePurchase(Player player, InventoryClickEvent event) {

        int axeSlot = 55;

        if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Wood Axe (Efficiency I)")) {
            if (player.getInventory().containsAtLeast(new ItemStack(Material.IRON_INGOT), 10)) {

                ItemStack item = new ItemStack(Material.WOOD_AXE);
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.spigot().setUnbreakable(true);
                itemMeta.addEnchant(Enchantment.DIG_SPEED, 1, true);
                itemMeta.setDisplayName(ChatColor.GOLD + "Wood Axe (Efficiency I)");
                item.setItemMeta(itemMeta);

                addItem(player, item);
                BedwarsInventory.PlayerAxe.put(player, Material.WOOD_AXE);
                player.playSound(player.getLocation(), Sound.NOTE_PIANO,1, 2);
                player.sendMessage(ChatColor.GREEN + "You Purchased " + ChatColor.GOLD + "Wood Axe (Efficiency I)" + ChatColor.GREEN + "!");
                player.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 10));
            } else {
                player.sendMessage(ChatColor.RED + "Not Enough " + "Iron" + "!");
            }
        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Stone Axe (Efficiency I)")) {
            if (player.getInventory().containsAtLeast(new ItemStack(Material.IRON_INGOT), 10)) {
                ItemStack item = new ItemStack(Material.STONE_AXE);
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.spigot().setUnbreakable(true);
                itemMeta.addEnchant(Enchantment.DIG_SPEED, 1, true);
                itemMeta.setDisplayName(ChatColor.GOLD + "Stone Axe (Efficiency I)");
                item.setItemMeta(itemMeta);

                for (int i = 0; i < 36; i++) {
                    if (player.getInventory().getItem(i) != null) {
                        if (player.getInventory().getItem(i).getType() == Material.WOOD_AXE) {
                            axeSlot = i;
                        }
                    }
                }
                player.getInventory().setItem(axeSlot, item);
                BedwarsInventory.PlayerAxe.put(player, Material.STONE_AXE);
                player.playSound(player.getLocation(), Sound.NOTE_PIANO,1, 2);
                player.sendMessage(ChatColor.GREEN + "You Purchased " + ChatColor.GOLD + "Stone Axe (Efficiency I)" + ChatColor.GREEN + "!");
                player.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 10));
            } else {
                player.sendMessage(ChatColor.RED + "Not Enough " + "Iron" + "!");
            }
        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Iron Axe (Efficiency II)")) {
            if (player.getInventory().containsAtLeast(new ItemStack(Material.GOLD_INGOT), 3)) {
                ItemStack item = new ItemStack(Material.IRON_AXE);
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.spigot().setUnbreakable(true);
                itemMeta.addEnchant(Enchantment.DIG_SPEED, 2, true);
                itemMeta.setDisplayName(ChatColor.GOLD + "Iron Axe (Efficiency II)");
                item.setItemMeta(itemMeta);

                for (int i = 0; i < 36; i++) {
                    if (player.getInventory().getItem(i) != null) {
                        if (player.getInventory().getItem(i).getType() == Material.STONE_AXE) {
                            axeSlot = i;
                        }
                    }
                }
                player.getInventory().setItem(axeSlot, item);
                BedwarsInventory.PlayerAxe.put(player, Material.IRON_AXE);
                player.playSound(player.getLocation(), Sound.NOTE_PIANO,1, 2);
                player.sendMessage(ChatColor.GREEN + "You Purchased " + ChatColor.GOLD + "Iron Axe (Efficiency II)" + ChatColor.GREEN + "!");
                player.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 3));
            } else {
                player.sendMessage(ChatColor.RED + "Not Enough " + "Gold" + "!");
            }
        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Diamond Axe (Efficiency III)")) {
            if (player.getInventory().containsAtLeast(new ItemStack(Material.GOLD_INGOT), 6)) {
                ItemStack item = new ItemStack(Material.DIAMOND_AXE);
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.spigot().setUnbreakable(true);
                itemMeta.addEnchant(Enchantment.DIG_SPEED, 3, true);
                itemMeta.setDisplayName(ChatColor.GOLD + "Diamond Axe (Efficiency III)");
                item.setItemMeta(itemMeta);

                for (int i = 0; i < 36; i++) {
                    if (player.getInventory().getItem(i) != null) {
                        if (player.getInventory().getItem(i).getType() == Material.IRON_AXE) {
                            axeSlot = i;
                        }
                    }
                }
                player.getInventory().setItem(axeSlot, item);
                BedwarsInventory.PlayerAxe.put(player, Material.DIAMOND_AXE);
                player.playSound(player.getLocation(), Sound.NOTE_PIANO,1, 2);
                player.sendMessage(ChatColor.GREEN + "You Purchased " + ChatColor.GOLD + "Diamond Axe (Efficiency III)" + ChatColor.GREEN + "!");
                player.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 6));
            } else {
                player.sendMessage(ChatColor.RED + "Not Enough " + "Gold" + "!");
            }
        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Diamond Axe (Efficiency III)")) {
            player.sendMessage(ChatColor.RED + "Already Purchased!");
        }
    }


    public static void changePage(Player player, String page, InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getItemMeta() == null) return;
        if (event.getCurrentItem().getItemMeta().getDisplayName() == null) return;
        if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.YELLOW + page)) {
            if (page.equalsIgnoreCase("Quick Buy")) {
                MainShop.openShopKeeper(player);
            }
            if (page.equalsIgnoreCase("Blocks")) {
                MainShop.openShopKeeperBlocks(player);
            }
            if (page.equalsIgnoreCase("Melee")) {
                MainShop.openShopKeeperMelee(player);
            }
            if (page.equalsIgnoreCase("Armor")) {
                MainShop.openShopKeeperArmor(player);
            }
            if (page.equalsIgnoreCase("Tools")) {
                MainShop.openShopKeeperTools(player);
            }
            if (page.equalsIgnoreCase("Projectiles")) {
                MainShop.openShopKeeperProjectiles(player);
            }
            if (page.equalsIgnoreCase("Potions")) {
                MainShop.openShopKeeperPotions(player);
            }
            if (page.equalsIgnoreCase("Utilities")) {
                MainShop.openShopKeeperUtilities(player);
            }
        }
    }




}
