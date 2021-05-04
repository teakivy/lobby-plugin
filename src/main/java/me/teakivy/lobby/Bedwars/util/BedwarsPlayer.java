package me.teakivy.lobby.Bedwars.util;

import com.mongodb.client.MongoCollection;
import me.rayzr522.jsonmessage.JSONMessage;
import me.teakivy.lobby.Bedwars.Bedwars;
import me.teakivy.lobby.Main;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Bed;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.mongodb.client.model.Updates.inc;

public class BedwarsPlayer {
    static MongoCollection< Document > col = me.teakivy.lobby.Main.getPlugin(Main.class).col;

    public static Map<Player, Color > PlayerColor = new HashMap< >();
    public static ArrayList<Player> PlayerRespawning = new ArrayList<>();

    public static Location BlueSpawnLocation = new Location(Bukkit.getWorld("world"), 20134.5, 96.5, 20023.5, 0, 0);
    public static Location RedSpawnLocation = new Location(Bukkit.getWorld("world"), 20082.5, 96.5, 20023.5, 0, 0);
    public static Location SpectatorSpawnLocation = new Location(Bukkit.getWorld("world"), 20105.5, 149.5, 20111.5, 180, 0);

    public static Plugin lobby = Main.getPlugin(Main.class);


    public static void playerDeath(Player player, Player killer) {
        BukkitScheduler scheduler = Bukkit.getScheduler();

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

        if (BedwarsInventory.PlayerPickaxe.get(player).equals(Material.IRON_PICKAXE)) {
            BedwarsInventory.PlayerPickaxe.put(player, Material.WOOD_PICKAXE);
        }
        if (BedwarsInventory.PlayerPickaxe.get(player).equals(Material.GOLD_PICKAXE)) {
            BedwarsInventory.PlayerPickaxe.put(player, Material.IRON_PICKAXE);
        }
        if (BedwarsInventory.PlayerPickaxe.get(player).equals(Material.DIAMOND_PICKAXE)) {
            BedwarsInventory.PlayerPickaxe.put(player, Material.GOLD_PICKAXE);
        }

        if (BedwarsInventory.PlayerAxe.get(player).equals(Material.STONE_AXE)) {
            BedwarsInventory.PlayerAxe.put(player, Material.WOOD_AXE);
        }
        if (BedwarsInventory.PlayerAxe.get(player).equals(Material.IRON_AXE)) {
            BedwarsInventory.PlayerAxe.put(player, Material.STONE_AXE);
        }
        if (BedwarsInventory.PlayerAxe.get(player).equals(Material.DIAMOND_AXE)) {
            BedwarsInventory.PlayerAxe.put(player, Material.IRON_AXE);
        }

//        for (int i = 80; i < 84; i++) {
//            if (inv.getItem(i) != null) {
//                if (inv.getItem(i).getType() == Material.IRON_INGOT) {
//                    ironAmount = ironAmount + inv.getItem(i).getAmount();
//                }
//                if (inv.getItem(i).getType() == Material.GOLD_INGOT) {
//                    goldAmount = goldAmount + inv.getItem(i).getAmount();
//                }
//                if (inv.getItem(i).getType() == Material.DIAMOND) {
//                    diamondAmount = diamondAmount + inv.getItem(i).getAmount();
//                }
//                if (inv.getItem(i).getType() == Material.EMERALD) {
//                    emeraldAmount = emeraldAmount + inv.getItem(i).getAmount();
//                }
//            }
//        }

        if (ironAmount > 0) killer.getInventory().addItem(new ItemStack(Material.IRON_INGOT, ironAmount));
        if (goldAmount > 0) killer.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, goldAmount));
        if (diamondAmount > 0) killer.getInventory().addItem(new ItemStack(Material.DIAMOND, diamondAmount));
        if (emeraldAmount > 0) killer.getInventory().addItem(new ItemStack(Material.EMERALD, emeraldAmount));

        setSpectator(player);



        JSONMessage.create(ChatColor.BOLD + ChatColor.RED.toString() + "YOU DIED!").title(1, 18, 1, player);
        JSONMessage.create(ChatColor.YELLOW + "You will respawn in " + ChatColor.GREEN + "5" + ChatColor.YELLOW + " seconds!").subtitle(player);
        player.playSound(player.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);
        player.playSound(player.getLocation(), Sound.HURT_FLESH, 1, 1);

        scheduler.scheduleSyncDelayedTask(lobby, () -> {
            JSONMessage.create(ChatColor.BOLD + ChatColor.RED.toString() + "YOU DIED!").title(1, 18, 1, player);
            JSONMessage.create(ChatColor.YELLOW + "You will respawn in " + ChatColor.YELLOW + "4" + ChatColor.YELLOW + " seconds!").subtitle(player);
            player.playSound(player.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);
        }, 20);
        scheduler.scheduleSyncDelayedTask(lobby, () -> {
            JSONMessage.create(ChatColor.BOLD + ChatColor.RED.toString() + "YOU DIED!").title(1, 18, 1, player);
            JSONMessage.create(ChatColor.YELLOW + "You will respawn in " + ChatColor.GOLD + "3" + ChatColor.YELLOW + " seconds!").subtitle(player);
            player.playSound(player.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);
        }, 2 * 20);
        scheduler.scheduleSyncDelayedTask(lobby, () -> {
            JSONMessage.create(ChatColor.BOLD + ChatColor.RED.toString() + "YOU DIED!").title(1, 18, 1, player);
            JSONMessage.create(ChatColor.YELLOW + "You will respawn in " + ChatColor.RED + "2" + ChatColor.YELLOW + " seconds!").subtitle(player);
            player.playSound(player.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);
        }, 3 * 20);
        scheduler.scheduleSyncDelayedTask(lobby, () -> {
            JSONMessage.create(ChatColor.BOLD + ChatColor.RED.toString() + "YOU DIED!").title(1, 18, 1, player);
            JSONMessage.create(ChatColor.YELLOW + "You will respawn in " + ChatColor.DARK_RED + "1" + ChatColor.YELLOW + " seconds!").subtitle(player);
            player.playSound(player.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);
        }, 4 * 20);
        scheduler.scheduleSyncDelayedTask(lobby, () -> {
            JSONMessage.create(ChatColor.BOLD + ChatColor.GREEN.toString() + "RESPAWNED!").title(1, 18, 1, player);
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
            respawnPlayer(player);
        }, 5 * 20);


    }

    public static void respawnPlayer(Player player) {
        player.setHealth(20);
        player.setGameMode(GameMode.SURVIVAL);

        if (PlayerColor.get(player).equals(Color.RED)) {
            player.teleport(RedSpawnLocation);
            BedwarsInventory.setBedwarsInv(player, Color.RED);
        }
        if (PlayerColor.get(player).equals(Color.BLUE)) {
            player.teleport(BlueSpawnLocation);
            BedwarsInventory.setBedwarsInv(player, Color.BLUE);
        }
    }

    public static Color getColor(Player player) {
        if (!PlayerColor.containsKey(player)) {
            return null;
        }
        return PlayerColor.get(player);
    }

    public static Player getOpponent(Player player) {
        if (getColor(player) == Color.RED) {
            return Bedwars.BedwarsPlayer2;
        }
        if (getColor(player) == Color.BLUE) {
            return Bedwars.BedwarsPlayer1;
        }
        return null;
    }

    public static void setColor(Player player, Color color) {
        PlayerColor.put(player, color);
    }

    public static boolean hasColor (Player player) {
        return PlayerColor.containsKey(player);
    }

    public static boolean hasBed(Player player) {
        return Bedwars.HasBed.get(player);
    }

    public static void removeColor (Player player) {
        PlayerColor.remove(player);
    }

    public static boolean isPlayerRespawning(Player player) {
        return PlayerRespawning.contains(player);
    }

    public static void endGame(Player loser) {
        Player winner = loser;
        if (loser == Bedwars.BedwarsPlayer1) {
            winner = Bedwars.BedwarsPlayer2;
        }
        if (loser == Bedwars.BedwarsPlayer2) {
            winner = Bedwars.BedwarsPlayer1;
        }

        Bedwars.BedwarsEventEnd(winner, loser, "normal");
    }

    public static void bedBreak(Player breaker) {
        Color color = getColor(getOpponent(breaker));
        ChatColor bedColor = ChatColor.GOLD;
        String bedName = "";
        Player opponent = getOpponent(breaker);
        if (color == Color.RED) {
            bedColor = ChatColor.RED;
            bedName = "Red";
        }
        if (color == Color.BLUE) {
            bedColor = ChatColor.BLUE;
            bedName = "Blue";
        }
        Document filter = new Document("UUID", breaker.getUniqueId().toString());
        Bson updates = inc("stats.bedwars.bedsBroken", 1);
        col.findOneAndUpdate(filter, updates);

        Document Killerfilter = new Document("UUID", BedwarsPlayer.getOpponent(breaker).getUniqueId().toString());
        Bson Killerupdates = inc("stats.bedwars.bedsLost", 1);
        col.findOneAndUpdate(Killerfilter, Killerupdates);

        sendMessage("  ");
        sendMessage(ChatColor.WHITE + ChatColor.BOLD.toString() + "BED DESTRUCTION > " + ChatColor.RESET + bedColor + bedName + " Bed" + ChatColor.GRAY + " was destroyed by " + getChatColor(breaker) + breaker.getName() + ChatColor.GRAY + "!");
        sendMessage("  ");
        breaker.playSound(breaker.getLocation(), Sound.ENDERDRAGON_GROWL, .7F, 1);
        opponent.playSound(opponent.getLocation(), Sound.WITHER_DEATH, .7F, 1);
    }

    public static ChatColor getChatColor(Player player) {
        Color color = getColor(player);
        ChatColor bedColor = ChatColor.GOLD;
        if (color == Color.RED) {
            bedColor = ChatColor.RED;
        }
        if (color == Color.BLUE) {
            bedColor = ChatColor.BLUE;
        }
        return bedColor;
    }

    public static byte getByteColor(Player player) {
        Color color = getColor(player);
        byte bedColor = 4;
        if (color == Color.RED) {
            bedColor = 14;
        }
        if (color == Color.BLUE) {
            bedColor = 11;
        }
        return bedColor;
    }


    public static void playSound(Sound sound, float pitch, float volume) {
        Bedwars.BedwarsPlayer1.playSound(Bedwars.BedwarsPlayer1.getLocation(), sound, pitch, volume);
        Bedwars.BedwarsPlayer2.playSound(Bedwars.BedwarsPlayer2.getLocation(), sound, pitch, volume);
    }

    public static Location getSpawn(Player player) {
        if (!PlayerColor.containsKey(player)) return null;
        if (PlayerColor.get(player).equals(Color.RED)) {
            return RedSpawnLocation;
        }
        if (PlayerColor.get(player).equals(Color.BLUE)) {
            return BlueSpawnLocation;
        }
        return null;
    }

    public static void sendMessage(String message) {
        Bedwars.BedwarsPlayer1.sendMessage(message);
        Bedwars.BedwarsPlayer2.sendMessage(message);
    }

    public static void setSpectator(Player player) {
        player.setGameMode(GameMode.SPECTATOR);
        player.setHealth(20);
        player.teleport(SpectatorSpawnLocation);
    }

    public static Location getSpawnLocation(Player player) {
        if (PlayerColor.get(player).equals(Color.RED)) {
            return RedSpawnLocation;
        }
        if (PlayerColor.get(player).equals(Color.BLUE)) {
            return BlueSpawnLocation;
        }
        return null;
    }
}
