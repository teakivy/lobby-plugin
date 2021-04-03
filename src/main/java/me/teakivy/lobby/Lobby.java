package me.teakivy.lobby;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.*;
//import com.sk89q.worldedit;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import com.sk89q.worldedit.world.DataException;
import me.rayzr522.jsonmessage.JSONMessage;
//import me.teakivy.lobby.NPC.NPCManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.*;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.*;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArrow;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftFakePlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Crops;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;
import org.json.simple.JSONObject;

import javax.persistence.Basic;
import javax.print.Doc;
import java.awt.*;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;


import static com.mongodb.client.model.Updates.*;

public final class Lobby extends JavaPlugin implements Listener {

    final static Class<? extends List> docClazz = ArrayList.class;

    private Map < Integer, Player > SumoQueue = new HashMap < > ();
    Integer SumoQueueSize = 0;

    private Map < Integer, Player > ClassicQueue = new HashMap < > ();
    Integer ClassicQueueSize = 0;

    private Map < Integer, Player > BridgeQueue = new HashMap < > ();
    Integer BridgeQueueSize = 0;

    private Map < Integer, Player > SkywarsQueue = new HashMap < > ();
    Integer SkywarsQueueSize = 0;




    ArrayList<Block> BridgeBlocksPlaced = new ArrayList<>();
    ArrayList<Block> BridgeBlocksBroken = new ArrayList<>();


    private Map < Player, Boolean > DoubleJumps = new HashMap < > ();

    private Map < Player, Boolean > PlayerVanished = new HashMap < > ();

    private Map < Player, Boolean > SumoGame = new HashMap < > ();

    private Map < Player, Player > SumoRequest = new HashMap < > ();
    private Map < Player, Integer > SumoRequestNo = new HashMap < > ();

    private Boolean SumoActive = false;
    private Boolean SumoQueued = false;

    private Player SumoPlayer1;
    private Player SumoPlayer2;


    private Map < Player, Boolean > ClassicGame = new HashMap < > ();

    private Map < Player, Player > ClassicRequest = new HashMap < > ();
    private Map < Player, Integer > ClassicRequestNo = new HashMap < > ();
    private Map < Player, Integer > ClassicFSSlot = new HashMap < > ();

    private Boolean ClassicActive = false;
    private Boolean ClassicQueued = false;

    private Player ClassicPlayer1;
    private Player ClassicPlayer2;


    private Map < Player, Boolean > BridgeGame = new HashMap < > ();

    private Map < Player, Player > BridgeRequest = new HashMap < > ();
    private Map < Player, Integer > BridgeRequestNo = new HashMap < > ();
    private Map < Player, Integer > BridgeGoals = new HashMap < > ();
    private Map < Player, Integer > BridgeKills = new HashMap < > ();
    private Map < Player, Player > BridgeLastPlayerDamage = new HashMap < > ();

    private Boolean BridgeActive = false;
    private Boolean BridgeQueued = false;
    private Boolean BridgeWaiting = false;

    private Player BridgePlayer1;
    private Player BridgePlayer2;

    private Location BridgeRedSpawn = new Location(Bukkit.getWorld("world"), 5058.5, 125.5, 4995.5);
    private Location BridgeBlueSpawn = new Location(Bukkit.getWorld("world"), 5013.5, 125.5, 4995.5);


    private Map < Player, Boolean > SkywarsGame = new HashMap < > ();

    private Map < Player, Integer > SkywarsKills = new HashMap < > ();
    private Map < Player, Player > SkywarsLastPlayerDamage = new HashMap < > ();

    private Boolean SkywarsActive = false;
    private Boolean SkywarsQueued = false;
    private Boolean SkywarsResetting = false;

    private Player SkywarsPlayer1;
    private Player SkywarsPlayer2;
    private Player SkywarsPlayer3;
    private Player SkywarsPlayer4;
    private Player SkywarsPlayer5;
    private Player SkywarsPlayer6;
    private Player SkywarsPlayer7;
    private Player SkywarsPlayer8;
    private Player SkywarsPlayer9;
    private Player SkywarsPlayer10;
    private Player SkywarsPlayer11;
    private Player SkywarsPlayer12;



    MongoCollection < Document > col;

    public void leaveGameQueue(Player player) {
        if(SumoQueue.containsValue(player)) {
            SumoQueueSize--;
            player.sendMessage(ChatColor.RED + "You left the Queue for Sumo Duels!");
            if (SumoQueue.get(1) == player) {
                SumoQueue.remove(1);
            } else {
                SumoQueue.remove(2);
            }
        } else if(ClassicQueue.containsValue(player)) {
            ClassicQueueSize--;
            player.sendMessage(ChatColor.RED + "You left the Queue for Classic Duels!");
            if (ClassicQueue.get(1) == player) {
                ClassicQueue.remove(1);
            } else {
                ClassicQueue.remove(2);
            }
        } else if(BridgeQueue.containsValue(player)) {
            BridgeQueueSize--;
            player.sendMessage(ChatColor.RED + "You left the Queue for Bridge Duels!");
            if (BridgeQueue.get(1) == player) {
                BridgeQueue.remove(1);
            } else {
                BridgeQueue.remove(2);
            }
        } else {
            player.sendMessage("You are not in a queue!");
        }
    }

    public void silentLeaveGameQueue(Player player) {
        if(SumoQueue.containsValue(player)) {
            SumoQueueSize--;
            if (SumoQueue.get(1) == player) {
                SumoQueue.remove(1);
            } else {
                SumoQueue.remove(2);
            }
        } else if(ClassicQueue.containsValue(player)) {
            ClassicQueueSize--;
            if (ClassicQueue.get(1) == player) {
                ClassicQueue.remove(1);
            } else {
                ClassicQueue.remove(2);
            }
        } else if(BridgeQueue.containsValue(player)) {
            BridgeQueueSize--;
            if (BridgeQueue.get(1) == player) {
                BridgeQueue.remove(1);
            } else {
                BridgeQueue.remove(2);
            }
        }
    }

    @Override
    public void onEnable() {
        // Connect to MongoDB Database
        MongoClient mongoClient = MongoClients.create("mongodb+srv://teakivy:CJscott05@cluster0.e8rhx.mongodb.net/myFirstDatabase?retryWrites=true&w=majority");
        MongoDatabase database = mongoClient.getDatabase("data");
        col = database.getCollection("accounts");

        // Plugin startup logic
        System.out.println("[BlockGone] Started! v1.2.0");
        getServer().getPluginManager().registerEvents(this, this);
        //        getServer().getPluginManager().registerEvents(new NPCManager(this), this);





        if(!this.getDataFolder().exists()) {
            try {
                this.getDataFolder().mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDisable() {
        if (ClassicActive) {
            classicEventEnd(ClassicPlayer1, ClassicPlayer2);
        }
        if (SumoActive) {
            SumoEventEnd(SumoPlayer1, SumoPlayer2);
        }
        if (BridgeActive) {
            BridgeEventEnd(BridgePlayer1, BridgePlayer2, "draw");
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block b = event.getBlock();
        BlockState bReplaced = event.getBlockReplacedState();
        if (player.getGameMode() == GameMode.SURVIVAL) {
            if (bReplaced.getType() != Material.AIR) {
                event.setCancelled(true);
            } else {
                if (b.getType() == Material.WOOL) {
                    Document filter = new Document("UUID", player.getUniqueId().toString());
                    Bson updates = inc("stats.lobby.blocksPlaced", 1);
                    col.findOneAndUpdate(filter, updates);

                    if (getLobbyBlocksPlaced(player) % 15 == 0) {
                        addCoins(player, 1);
                    }

                    BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
                    scheduler.scheduleSyncDelayedTask(this, new Runnable() {
                        @Override
                        public void run() {
                            b.getLocation().getBlock().setType(Material.AIR);
                            PlayerInventory inventory = player.getInventory();
                            if (!BridgeGame.get(player) && !SumoGame.get(player) && !ClassicGame.get(player)) {
                                inventory.addItem(new ItemStack(Material.WOOL));
                            }

                        }
                    }, 45);
                } else if (b.getType() == Material.FIRE) {
                    if (event.getBlockAgainst().getType() == Material.STAINED_GLASS) {
                        event.setCancelled(true);
                        b.getLocation().getBlock().setType(Material.FIRE);
                    }

                    BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
                    scheduler.scheduleSyncDelayedTask(this, new Runnable() {
                        @Override
                        public void run() {
                            b.getLocation().getBlock().setType(Material.AIR);
                        }
                    }, 15*20);
                } else if (b.getType() == Material.STAINED_CLAY) {
                    if (BridgeActive) {
                        if (BridgeGame.get(player)) {
                            if (b.getY() > 119 || b.getY() < 100 || b.getX() > 5055 || b.getX() < 5015 || b.getZ() > 5005 || b.getZ() < 4985) {
                                player.sendMessage(ChatColor.RED + "Outside of range!");
                                event.setCancelled(true);
                            } else {
                                BridgeBlocksPlaced.add(b);
                            }
                        } else {
                            event.setCancelled(true);
                        }
                    } else {
                        event.setCancelled(true);
                    }
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.SURVIVAL) {
            event.setCancelled(true);
        }
    }

    public int getLobbyBlocksPlaced(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("stats");
        Document gameType = (Document) obj.get("lobby");
        return gameType.getInteger("blocksPlaced");
    }

    public int getLobbyDoubleJumps(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("stats");
        Document gameType = (Document) obj.get("lobby");
        return gameType.getInteger("doubleJumps");
    }

    public String getRank(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());
        return col.find(filter).first().getString("rank");
    }

    public ArrayList getPurchased(Player player) {
        ArrayList<String> arrlist = new ArrayList<>();
        Document filter = new Document("UUID", player.getUniqueId().toString());
        Object[] object = col.find(filter).first().get("purchased", docClazz).toArray();
        for (Object o : object) {
            arrlist.add(o.toString());
        }

        return arrlist;
    }



    public ChatColor getPlusColor(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());
        ChatColor plusColor = ChatColor.RED;

        switch(col.find(filter).first().getString("plusColor")) {
            case "RED":
                plusColor = ChatColor.RED;
                break;
            case "BLUE":
                plusColor = ChatColor.BLUE;
                break;
            case "AQUA":
                plusColor = ChatColor.AQUA;
                break;
            case "GOLD":
                plusColor = ChatColor.GOLD;
                break;
            case "YELLOW":
                plusColor = ChatColor.YELLOW;
                break;
            case "DARK_RED":
                plusColor = ChatColor.DARK_RED;
                break;
            case "DARK_BLUE":
                plusColor = ChatColor.DARK_BLUE;
                break;
            case "GRAY":
                plusColor = ChatColor.DARK_GRAY;
                break;
            case "WHITE":
                plusColor = ChatColor.WHITE;
                break;
            case "BLACK":
                plusColor = ChatColor.BLACK;
                break;
            case "LIME":
                plusColor = ChatColor.GREEN;
                break;
            case "GREEN":
                plusColor = ChatColor.DARK_GREEN;
                break;
            case "CYAN":
                plusColor = ChatColor.DARK_AQUA;
                break;
            case "PURPLE":
                plusColor = ChatColor.DARK_PURPLE;
                break;
        }

        return plusColor;
    }
    public int getPlayerCoins(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());
        return col.find(filter).first().getInteger("coins");
    }

    public String getPlayerCoinsFormatted(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());
        int coins = col.find(filter).first().getInteger("coins");

        if (coins > 999999999) {
            return "1,000,000,000+";
        } else if (coins > 999999) {
            int millions = (coins-(coins%1000000)) / 1000000;
            int thousands = ((coins-(coins%1000)) / 1000) % 1000;
            int hundreds = coins%1000;

            String thousandString = thousands + "";
            String hundredString = hundreds + "";


            if (thousands < 9) {
                thousandString = "00" + thousands;
            } else if (thousands < 99) {
                thousandString = "0" + thousands;
            }

            if (hundreds < 9) {
                hundredString = "00" + hundreds;
            } else if (hundreds < 99) {
                hundredString = "0" + hundreds;
            }

            return millions + "," + thousandString + "," + hundredString;
        } else if (coins > 999) {
            int thousands = (coins-(coins%1000)) / 1000;
            int hundreds = coins%1000;
            String hundredString = hundreds + "";

            if (hundreds < 9) {
                hundredString = "00" + hundreds;
            } else if (hundreds < 99) {
                hundredString = "0" + hundreds;
            }

            return thousands + "," + hundredString;
        } else {
            return coins + "";
        }
    }

    public String parseColorMessage(String message) {
        String parsedMessage = message.replaceAll("&l", ChatColor.BOLD.toString()).replaceAll("&o", ChatColor.ITALIC.toString()).replaceAll("&n", ChatColor.UNDERLINE.toString()).replaceAll("&m", ChatColor.STRIKETHROUGH.toString()).replaceAll("&r", ChatColor.RESET.toString()).replaceAll("&k", ChatColor.MAGIC.toString());
        parsedMessage = parsedMessage.replaceAll("&0", ChatColor.BLACK.toString()).replaceAll("&1", ChatColor.DARK_BLUE.toString()).replaceAll("&2", ChatColor.DARK_GREEN.toString()).replaceAll("&3", ChatColor.DARK_AQUA.toString()).replaceAll("&4", ChatColor.DARK_RED.toString()).replaceAll("&5", ChatColor.DARK_PURPLE.toString()).replaceAll("&6", ChatColor.GOLD.toString()).replaceAll("&7", ChatColor.GRAY.toString()).replaceAll("&8", ChatColor.DARK_GRAY.toString()).replaceAll("&9", ChatColor.BLUE.toString());
        parsedMessage = parsedMessage.replaceAll("&a", ChatColor.GREEN.toString()).replaceAll("&b", ChatColor.AQUA.toString()).replaceAll("&c", ChatColor.RED.toString()).replaceAll("&d", ChatColor.LIGHT_PURPLE.toString()).replaceAll("&e", ChatColor.YELLOW.toString()).replaceAll("&f", ChatColor.WHITE.toString());

        return parsedMessage;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        String rank = getRank(player);

        String parsedMessage = parseColorMessage(event.getMessage());
        if (!rank.equals("DEFAULT") && !rank.equals("VIP")) {
            event.setMessage(parsedMessage);
        }



        switch (rank) {
            case "OWNER":
                event.setFormat(ChatColor.DARK_RED + "[OWNER] " + player.getName() + ChatColor.WHITE + ": " + event.getMessage());
                break;
            case "ADMIN":
                event.setFormat(ChatColor.RED + "[ADMIN] " + player.getName() + ChatColor.WHITE + ": " + event.getMessage());
                break;
            case "MODERATOR":
                event.setFormat(ChatColor.AQUA + "[MOD] " + player.getName() + ChatColor.WHITE + ": " + event.getMessage());
                break;
            case "HELPER":
                event.setFormat(ChatColor.GREEN + "[HELPER] " + player.getName() + ChatColor.WHITE + ": " + event.getMessage());
                break;
            case "YOUTUBE":
                event.setFormat(ChatColor.RED + "[" + ChatColor.BOLD + ChatColor.WHITE.toString() + "YOUTUBE" + ChatColor.RESET + ChatColor.RED.toString() + "] " + player.getName() + ChatColor.WHITE + ": " + event.getMessage());
                break;
            case "TWITCH":
                event.setFormat(ChatColor.DARK_PURPLE + "[" + ChatColor.BOLD + ChatColor.WHITE.toString() + "TWITCH" + ChatColor.RESET + ChatColor.DARK_PURPLE.toString() + "] " + player.getName() + ChatColor.WHITE + ": " + event.getMessage());
                break;
            case "VIP":
                event.setFormat(ChatColor.GREEN + "[VIP] " + player.getName() + ChatColor.WHITE + ": " + event.getMessage());
                break;
            case "VIP+":
                event.setFormat(ChatColor.GREEN + "[VIP" + ChatColor.GOLD + "+" + ChatColor.GREEN + "] " + player.getName() + ChatColor.WHITE + ": " + event.getMessage());
                break;
            case "MVP":
                event.setFormat(ChatColor.AQUA + "[MVP] " + player.getName() + ChatColor.WHITE + ": " + event.getMessage());
                break;
            case "MVP+":
                event.setFormat(ChatColor.GREEN + "[MVP" + getPlusColor(player) + "+" + ChatColor.AQUA + "] " + player.getName() + ChatColor.WHITE + ": " + event.getMessage());
                break;
            case "MVP++":
                event.setFormat(ChatColor.GOLD + "[MVP" + getPlusColor(player) + "++" + ChatColor.GOLD + "] " + player.getName() + ChatColor.WHITE + ": " + event.getMessage());
                break;
            default:
                event.setFormat(ChatColor.GRAY + player.getName() + ": " + event.getMessage());
                break;
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL) {
            Block block = event.getClickedBlock();

            if (block == null)
                return;

            Material blockType = block.getType();

            if (blockType.equals(Material.SOIL)) {
                event.setUseInteractedBlock(org.bukkit.event.Event.Result.DENY);
                event.setCancelled(true);
            }
        }
    }


    public boolean getDoubleJumpEnabled(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("lobbySettings");
        return obj.getBoolean("doubleJump");
    }

    public boolean getSpeedEnabled(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("lobbySettings");
        return obj.getBoolean("speed");
    }

    public boolean getFlightEnabled(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("lobbySettings");
        return obj.getBoolean("flight");
    }

    public boolean getPlayersVisible(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("lobbySettings");
        return obj.getBoolean("playersVisible");
    }

    public int getClassicSwordSlot(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("gameSettings");
        Document gameType = (Document) obj.get("classic");
        return gameType.getInteger("swordSlot");
    }



    public int getClassicFishingRodSlot(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("gameSettings");
        Document gameType = (Document) obj.get("classic");
        return gameType.getInteger("fishingRodSlot");
    }

    public int getClassicBowSlot(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("gameSettings");
        Document gameType = (Document) obj.get("classic");
        return gameType.getInteger("bowSlot");
    }

    public int getClassicFlintAndSteelSlot(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("gameSettings");
        Document gameType = (Document) obj.get("classic");
        return gameType.getInteger("flintAndSteelSlot");
    }

    public int getClassicArrowSlot(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("gameSettings");
        Document gameType = (Document) obj.get("classic");
        return gameType.getInteger("arrowSlot");
    }

    public int getSumoWins(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("stats");
        Document gameType = (Document) obj.get("sumo");
        return gameType.getInteger("wins");
    }
    public int getClassicWins(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("stats");
        Document gameType = (Document) obj.get("classic");
        return gameType.getInteger("wins");
    }
    public int getBridgeWins(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("stats");
        Document gameType = (Document) obj.get("bridge");
        if (gameType != null) {
            return gameType.getInteger("wins");
        } else {
            return 0;
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();

        player.teleport(new Location(getServer().getWorld("world"), 86.5, 137.5, 3.5, 90, 0));
        event.getPlayer().getInventory().setHeldItemSlot(0);
        long count = col.countDocuments(new Document("UUID", player.getUniqueId().toString()));
        if (count > 0) {
            System.out.println("Player: " + player.getName() + " already exists in DB!");
        } else {
            System.out.println("Created DB Document for Player: " + player.getName());
            Document newPlayerDocument = new Document("UUID", player.getUniqueId().toString()).append("name", player.getName()).append("coins", 0).append("rank", "DEFAULT").append("lobbySettings", new Document("doubleJump", true).append("flight", false).append("speed", true).append("playersVisible", true).append("vanished", false)).append("gameSettings", new Document("classic", new Document("swordSlot", 0).append("fishingRodSlot", 1).append("bowSlot", 2).append("flintAndSteelSlot", 3).append("arrowSlot", 9)).append("bridge", new Document("swordSlot", 0).append("bowSlot", 1).append("pickaxeSlot", 2).append("blockSlot1", 3).append("blockSlot2", 4).append("gappleSlot", 5).append("arrowSlot", 8))).append("stats", new Document("sumo", new Document("wins", 0).append("losses", 0).append("gamesPlayed", 0)).append("classic", new Document("wins", 0).append("losses", 0).append("gamesPlayed", 0)).append("lobby", new Document("wheatBroken", 0).append("doubleJumps", 0).append("blocksPlaced", 0)));
            col.insertOne(newPlayerDocument);
        }

        player.playSound(player.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);

        ItemStack playersOn = new ItemStack(Material.INK_SACK, 1, (byte) 10);
        ItemMeta playersOnMeta = playersOn.getItemMeta();
        playersOnMeta.setDisplayName(ChatColor.WHITE + "Players: " + ChatColor.GREEN + "Visible " + ChatColor.GRAY + "(Right Click)");
        playersOn.setItemMeta(playersOnMeta);

        ItemStack playersOff = new ItemStack(Material.INK_SACK, 1, (byte) 8);
        ItemMeta playersOffMeta = playersOff.getItemMeta();
        playersOffMeta.setDisplayName(ChatColor.WHITE + "Players: " + ChatColor.RED + "Hidden " + ChatColor.GRAY + "(Right Click)");
        playersOff.setItemMeta(playersOffMeta);

        ItemStack playerHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skull = (SkullMeta) playerHead.getItemMeta();
        skull.setDisplayName(ChatColor.RESET + "" + ChatColor.GREEN + "Profile " + ChatColor.GRAY + "(Right Click)");
        skull.setOwner(player.getName());
        playerHead.setItemMeta(skull);

        ItemStack GameMenu = new ItemStack(Material.COMPASS);
        ItemMeta GameMenuMeta = GameMenu.getItemMeta();
        GameMenuMeta.setDisplayName(ChatColor.GREEN + "Game Menu " + ChatColor.GRAY + "(Right Click)");
        GameMenu.setItemMeta(GameMenuMeta);

        ItemStack CoinShop = new ItemStack(Material.EMERALD);
        ItemMeta CoinShopMeta = CoinShop.getItemMeta();
        CoinShopMeta.setDisplayName(ChatColor.GREEN + "Coin Shop " + ChatColor.GRAY + "(Right Click)");
        CoinShop.setItemMeta(CoinShopMeta);

        ItemStack Gadgets = new ItemStack(Material.CHEST);
        ItemMeta GadgetsMeta = Gadgets.getItemMeta();
        GadgetsMeta.setDisplayName(ChatColor.GREEN + "Collectibles " + ChatColor.GRAY + "(Right Click)");
        Gadgets.setItemMeta(GadgetsMeta);


        ItemStack playerStack;

        if (getPlayersVisible(player)) {
            playerStack = playersOn;
        } else {
            playerStack = playersOff;
        }

        ItemStack airStack = new ItemStack(Material.AIR, 1);
        ItemStack[] inv = {
                GameMenu,
                CoinShop,
                airStack,
                airStack,
                Gadgets,
                new ItemStack(Material.WOOL, 64),
                airStack,
                playerStack,
                playerHead
        };
        inventory.setContents(inv);
        inventory.setHelmet(airStack);
        inventory.setChestplate(airStack);
        inventory.setLeggings(airStack);
        inventory.setBoots(airStack);
        player.setSaturation(20);
        player.setExp(0);
        player.setLevel(0);
        player.setGameMode(GameMode.SURVIVAL);



        if (!DoubleJumps.containsKey(player)) {
            DoubleJumps.put(player, false);
        }

        if (!BridgeGoals.containsKey(player)) {
            BridgeGoals.put(player, 0);
        }

        if (!SumoGame.containsKey(player)) {
            SumoGame.put(player, false);
        }

        if (!ClassicGame.containsKey(player)) {
            ClassicGame.put(player, false);
        }
        if (!BridgeGame.containsKey(player)) {
            BridgeGame.put(player, false);
        }
        if (!BridgeGoals.containsKey(player)) {
            BridgeGoals.put(player, 0);
        }
        if (!BridgeKills.containsKey(player)) {
            BridgeKills.put(player, 0);
        }

        if (!BridgeLastPlayerDamage.containsKey(player)) {
            BridgeLastPlayerDamage.put(player, player);
        }
        if (!PlayerVanished.containsKey(player)) {
            PlayerVanished.put(player, false);
        }

        Document filter = new Document("UUID", player.getUniqueId().toString());

        for (Player toHide: Bukkit.getServer().getOnlinePlayers()) {
            if (player != toHide) {
                if (!getPlayersVisible(toHide)) {
                    toHide.hidePlayer(player);
                }
            }
        }
        if (!getPlayersVisible(player)) {
            for (Player toHide : Bukkit.getServer().getOnlinePlayers()) {
                if (player != toHide) {
                    player.hidePlayer(toHide);
                }
            }
        }
//        for (Player p: Bukkit.getOnlinePlayers()) {
//            if (!p.equals(player)) {
//                if (getPlayersVisible(p)) {
//                    p.hidePlayer(player);
//                }
//            }
//        }

//        for (Player p: Bukkit.getOnlinePlayers()) {
//            if (!p.equals(player)) {
//                if (PlayerVanished.get(p)) {
//                    player.hidePlayer(p);
//                }
//            }
//        }

        player.setAllowFlight(getDoubleJumpEnabled(player) || getFlightEnabled(player));
        if (getSpeedEnabled(player)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 60 * 24, 0, true, true));
        }

        Scoreboard healthboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective sbHealth = healthboard.registerNewObjective(player.getName(), "dummy");

        sbHealth.setDisplayName(ChatColor.RED + "❤");
        sbHealth.setDisplaySlot(DisplaySlot.BELOW_NAME);

        sbHealth.getScore(player.getName()).setScore((int) player.getHealth());

        for (Player p: Bukkit.getOnlinePlayers()) {
//            player.getScoreboard().getObjective(player.getName()).getScore(p.getName()).setScore((int) p.getHealth());
            p.getScoreboard().getObjective(p.getName()).getScore(player.getName()).setScore((int) player.getHealth());
        }


        player.setScoreboard(healthboard);

        Scoreboard scoreboard = player.getScoreboard();

        Team team = scoreboard.getTeam(player.getName());
        if (team == null) {
            team = scoreboard.registerNewTeam(player.getName());
        }



        switch (getRank(player)) {
            case "OWNER":
                event.setJoinMessage(ChatColor.DARK_RED + "[OWNER] " + player.getName() + ChatColor.GOLD + " joined the lobby!");
                team.setPrefix(ChatColor.DARK_RED + "[OWNER] ");
                break;
            case "ADMIN":
                event.setJoinMessage(ChatColor.RED + "[ADMIN] " + player.getName() + ChatColor.GOLD + " joined the lobby!");
                team.setPrefix(ChatColor.RED + "[ADMIN] ");
                break;
            case "MODERATOR":
                event.setJoinMessage(ChatColor.AQUA + "[MOD] " + player.getName() + ChatColor.GOLD + " joined the lobby!");
                team.setPrefix(ChatColor.AQUA + "[MOD] ");
                break;
            case "HELPER":
                event.setJoinMessage(ChatColor.GREEN + "[HELPER] " + player.getName() + ChatColor.GOLD + " joined the lobby!");
                team.setPrefix(ChatColor.GREEN + "[HELPER] ");
                break;
            case "YOUTUBE":
                event.setJoinMessage(ChatColor.RED + "[" + ChatColor.WHITE + "YOUTUBE" + ChatColor.RED + "] " + player.getName() + ChatColor.GOLD + " joined the lobby!");
                team.setPrefix(ChatColor.RED + "[" + ChatColor.WHITE + "YOUTUBE" + ChatColor.RED + "] ");
                break;
            case "TWITCH":
                event.setJoinMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "TWITCH" + ChatColor.DARK_PURPLE + "] " + player.getName() + ChatColor.GOLD + " joined the lobby!");
                team.setPrefix(ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "TWITCH" + ChatColor.DARK_PURPLE + "] ");
                break;
            default:
                event.setJoinMessage(ChatColor.WHITE + player.getName() + ChatColor.YELLOW + " joined the lobby!");
                team.setPrefix(ChatColor.GRAY + "");
                break;
            case "VIP":
                event.setJoinMessage(ChatColor.GREEN + "[VIP] " + player.getName() + ChatColor.YELLOW + " joined the lobby!");
                team.setPrefix(ChatColor.GREEN + "[VIP] ");
                break;
            case "VIP+":
                event.setJoinMessage(ChatColor.GREEN + "[VIP" + ChatColor.GOLD + "+" + ChatColor.GREEN + "] " + player.getName() + ChatColor.YELLOW + " joined the lobby!");
                team.setPrefix(ChatColor.GREEN + "[VIP" + ChatColor.GOLD + "+" + ChatColor.GREEN + "] ");
                break;
            case "MVP":
                event.setJoinMessage(ChatColor.AQUA + "[MVP] " + player.getName() + ChatColor.AQUA + " joined the lobby!");
                team.setPrefix(ChatColor.AQUA + "[MVP] ");
                break;
            case "MVP+":
                event.setJoinMessage(ChatColor.GREEN + "[MVP" + getPlusColor(player) + "+" + ChatColor.AQUA + "] " + player.getName() + ChatColor.AQUA + " joined the lobby!");
                team.setPrefix(ChatColor.GREEN + "[MVP" + getPlusColor(player) + "+" + ChatColor.AQUA + "] ");
                break;
            case "MVP++":
                event.setJoinMessage(ChatColor.GOLD + "[MVP" + getPlusColor(player) + "++" + ChatColor.GOLD + "] " + player.getName() + ChatColor.GOLD + " joined the lobby!");
                team.setPrefix(ChatColor.GOLD + "[MVP" + getPlusColor(player) + "++" + ChatColor.GOLD + "] ");
                break;
        }
        team.addEntry(player.getName());
        player.setScoreboard(scoreboard);

    }

    @EventHandler
    public void onShoot(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (BridgeGame.get(player)) {
                if (BridgeWaiting) {
                    event.setCancelled(true);
                }
            }
        }
    }

    public void cancelGame(Player player) {
        if (SumoGame.get(player)) {
            if (SumoPlayer1 == player) {
                SumoEventEnd(SumoPlayer2, SumoPlayer1);
            } else {
                SumoEventEnd(SumoPlayer1, SumoPlayer2);
            }
        }
        if (ClassicGame.get(player)) {
            if (ClassicPlayer1 == player) {
                classicEventEnd(ClassicPlayer2, ClassicPlayer1);
            } else {
                classicEventEnd(ClassicPlayer1, ClassicPlayer2);
            }
        }
        if (BridgeGame.get(player)) {
            if (BridgePlayer1 == player) {
                BridgeEventEnd(BridgePlayer2, BridgePlayer1, "normal");
            } else {
                BridgeEventEnd(BridgePlayer1, BridgePlayer2, "normal");
            }
        }
    }

    @EventHandler
    public void onHit(ProjectileHitEvent e) //called when a projectile hits an object.
    {
        Projectile p = e.getEntity(); // get the projectile for the event;
        if(p instanceof Arrow) { // check if it's an arrow
            if (e.getEntity() instanceof Player) {
                Player player = (Player) e.getEntity();

            } else {
                if (BridgeGame.get(p.getShooter())) {
                    p.remove();
                } else {

                    BukkitScheduler scheduler5 = Bukkit.getServer().getScheduler();
                    scheduler5.scheduleSyncDelayedTask(this, new Runnable() {
                        @Override
                        public void run() {
                            p.remove();
                        }
                    }, 10 * 20);
                }
            }
        }
    }

    public void addCoins(Player player, int coins) {
        Document filter = new Document("UUID", player.getUniqueId().toString());
        Bson updates = inc("coins", coins);
        col.findOneAndUpdate(filter, updates);
        if (coins == 1) {
            JSONMessage.create("+" + coins + " Coin").color(ChatColor.GOLD).actionbar(player);
        } else if (coins > 1){
            JSONMessage.create("+" + coins + " Coins").color(ChatColor.GOLD).actionbar(player);
        } else if (coins == -1){
            JSONMessage.create("-" + coins + " Coin").color(ChatColor.RED).actionbar(player);
        } else if (coins < -1){
            JSONMessage.create("-" + coins + " Coins").color(ChatColor.RED).actionbar(player);
        }
    }

    @EventHandler
    public void onBreakEvent(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block blockBroken = event.getBlock();
        if (player.getGameMode() == GameMode.SURVIVAL) {
            if (blockBroken.getType().equals(Material.CROPS)) {
                CropState cropState = ((Crops) blockBroken.getState().getData()).getState();
                if (cropState.equals(CropState.RIPE)) {
                    event.setCancelled(true);
                    Document filter = new Document("UUID", player.getUniqueId().toString());
                    Bson update1 = inc("coins", 1);
                    Bson update2 = inc("stats.lobby.wheatBroken", 1);
                    Bson updates = combine(update1, update2);
                    col.findOneAndUpdate(filter, updates);
                    JSONMessage.create("+1 Coin").color(ChatColor.GOLD).actionbar(player);
                    blockBroken.getLocation().getBlock().setType(Material.AIR);
                    BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
                    scheduler.scheduleSyncDelayedTask(this, new Runnable() {
                        @Override
                        public void run() {
                            blockBroken.getLocation().getBlock().setType(Material.CROPS);
                            //                            Create Particle
                        }
                    }, 45);
                } else {
                    event.setCancelled(true);
                }

            } else if (blockBroken.getType() == Material.STAINED_CLAY) {
                if (blockBroken.getY() > 119 || blockBroken.getY() < 100 || blockBroken.getX() > 5055 || blockBroken.getX() < 5015 || blockBroken.getZ() > 5005 || blockBroken.getZ() < 4985) {
                    player.sendMessage(ChatColor.RED + "Outside of range!");
                    event.setCancelled(true);
                } else {
                    if (BridgeGame.get(player)) {
                        if (BridgeActive) {
                            BridgeBlocksBroken.add(blockBroken);
                        } else {
                            event.setCancelled(true);
                        }
                    } else {
                        event.setCancelled(true);
                    }
                }
            } else {
                event.setCancelled(true);
            }

        }
    }

    @EventHandler
    public void onDamageEvent(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (player.getGameMode() == GameMode.SURVIVAL) {
                if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                    event.setCancelled(true);
                } else {
                    if (SumoGame.get(player)) {
                        if (SumoActive) {
                            player.setHealth(20);
                        } else {
                            event.setCancelled(true);
                        }
                    } else if (ClassicGame.get(player)) {
                        if (!ClassicActive) {
                            event.setCancelled(true);
                        }
                    } else if (BridgeGame.get(player)) {
                        if (!BridgeActive) {
                            event.setCancelled(true);
                        }
                    } else {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onSaturationChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    public boolean arrayListIncludes(ArrayList<String> arrayList, String query) {
        boolean returnValue = false;
        for (String s : arrayList) {
            if (s.equals(query)) {
                returnValue = true;
                break;
            }
        }
        return returnValue;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (player.getGameMode().equals(GameMode.SURVIVAL)) {
            if (!ClassicGame.get(player) && !BridgeGame.get(player)) {
                event.setCancelled(true);
            }
        }
        if (event.getClickedInventory() != null) {
            if (event.getClickedInventory().getTitle().equalsIgnoreCase(ChatColor.GOLD + "Your Profile")) {
                Document filter = new Document("UUID", player.getUniqueId().toString());
                event.setCancelled(true);

                ItemStack flightOn = new ItemStack(Material.FEATHER);
                ItemMeta flightOnMeta = flightOn.getItemMeta();
                flightOnMeta.setDisplayName(ChatColor.WHITE + "Flight:" + ChatColor.GREEN + " Enabled");
                flightOnMeta.setLore(Arrays.asList(ChatColor.GRAY + "Fly around the lobby, like", ChatColor.GRAY + "the hackers do!", "", ChatColor.YELLOW + "Click To Disable!"));
                flightOn.setItemMeta(flightOnMeta);

                ItemStack flightOff = new ItemStack(Material.FEATHER);
                ItemMeta flightOffMeta = flightOff.getItemMeta();
                flightOffMeta.setDisplayName(ChatColor.WHITE + "Flight:" + ChatColor.RED + " Disabled");
                flightOffMeta.setLore(Arrays.asList(ChatColor.GRAY + "Fly around the lobby, like", ChatColor.GRAY + "the hackers do!", "", ChatColor.YELLOW + "Click To Enable!"));
                flightOff.setItemMeta(flightOffMeta);

                ItemStack djOn = new ItemStack(Material.SLIME_BALL);
                ItemMeta djOnMeta = djOn.getItemMeta();
                djOnMeta.setDisplayName(ChatColor.WHITE + "Double Jump:" + ChatColor.GREEN + " Enabled");
                djOnMeta.setLore(Arrays.asList(ChatColor.GRAY + "Jump around the lobby, and", ChatColor.GRAY + "become your own Mario!", "", ChatColor.YELLOW + "Click To Disable!"));
                djOn.setItemMeta(djOnMeta);

                ItemStack djOff = new ItemStack(Material.SLIME_BALL);
                ItemMeta djOffMeta = djOff.getItemMeta();
                djOffMeta.setDisplayName(ChatColor.WHITE + "Double Jump:" + ChatColor.RED + " Disabled");
                djOffMeta.setLore(Arrays.asList(ChatColor.GRAY + "Jump around the lobby, and", ChatColor.GRAY + "become your own Mario!", "", ChatColor.YELLOW + "Click To Enable!"));
                djOff.setItemMeta(djOffMeta);

                ItemStack speedOn = new ItemStack(Material.DIAMOND_BOOTS);
                ItemMeta speedOnMeta = speedOn.getItemMeta();
                speedOnMeta.setDisplayName(ChatColor.WHITE + "Speed:" + ChatColor.GREEN + " Enabled");
                speedOnMeta.setLore(Arrays.asList(ChatColor.GRAY + "I. Am. SPEEEEDDDD.", ChatColor.GRAY + "- " + ChatColor.RED + "Lightning " + ChatColor.GOLD + "McQueen", "", ChatColor.YELLOW + "Click To Disable!"));
                speedOn.setItemMeta(speedOnMeta);

                ItemStack speedOff = new ItemStack(Material.DIAMOND_BOOTS);
                ItemMeta speedOffMeta = speedOff.getItemMeta();
                speedOffMeta.setDisplayName(ChatColor.WHITE + "Speed:" + ChatColor.RED + " Disabled");
                speedOffMeta.setLore(Arrays.asList(ChatColor.GRAY + "I. Am. SPEEEEDDDD.", ChatColor.GRAY + "- " + ChatColor.RED + "Lightning " + ChatColor.GOLD + "McQueen", "", ChatColor.YELLOW + "Click To Enable!"));
                speedOff.setItemMeta(speedOffMeta);

                ItemStack rankMenu = new ItemStack(Material.DIAMOND);
                ItemMeta rankMenuMeta = rankMenu.getItemMeta();
                rankMenuMeta.setDisplayName(ChatColor.AQUA + "Ranks");
                rankMenuMeta.setLore(Arrays.asList(ChatColor.GRAY + "Change your rank and", ChatColor.GRAY + "become whoever you want!", "", ChatColor.GRAY + "Coming Soon..."));
                rankMenu.setItemMeta(rankMenuMeta);

                ItemStack PlusColor = new ItemStack(Material.INK_SACK, 1, (short) 14);
                ItemMeta PlusColorMeta = PlusColor.getItemMeta();
                PlusColorMeta.setDisplayName(ChatColor.GOLD + "Plus Colors");
                PlusColorMeta.setLore(Arrays.asList(ChatColor.GRAY + "Change your rank's", ChatColor.GRAY + "Plus Colors!", "", ChatColor.GRAY + "Coming Soon..."));
                PlusColor.setItemMeta(PlusColorMeta);

                ItemStack GameSettings = new ItemStack(Material.COMMAND);
                ItemMeta GameSettingsMeta = GameSettings.getItemMeta();
                GameSettingsMeta.setDisplayName(ChatColor.GOLD + "Game Settings");
                GameSettingsMeta.setLore(Arrays.asList(ChatColor.GRAY + "Change your game settings!", "", ChatColor.GRAY + "Coming Soon..."));
                GameSettings.setItemMeta(GameSettingsMeta);

                ItemStack og = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1);
                ItemMeta ogMeta = og.getItemMeta();
                ogMeta.setDisplayName(" ");
                og.setItemMeta(ogMeta);
                if (event.getCurrentItem() != null) {
                    if (event.getCurrentItem().getItemMeta() != null) {
                        if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.WHITE + "Double Jump:" + ChatColor.GREEN + " Enabled")) {
                            player.sendMessage(ChatColor.RED + "Double Jump Disabled!");

                            event.getClickedInventory().setItem(31, djOff);
                            if (getFlightEnabled(player)) {
                                Bson update = set("lobbySettings.doubleJump", false);
                                col.findOneAndUpdate(filter, update);

                            } else {
                                Bson update = set("lobbySettings.doubleJump", false);
                                col.findOneAndUpdate(filter, update);

                                player.setAllowFlight(false);
                            }
                        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.WHITE + "Double Jump:" + ChatColor.RED + " Disabled")) {
                            player.sendMessage(ChatColor.GREEN + "Double Jump Enabled!");
                            player.setAllowFlight(true);
                            event.getClickedInventory().setItem(31, djOn);

                            Bson update = set("lobbySettings.doubleJump", true);
                            col.findOneAndUpdate(filter, update);
                        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Go To Lobby")) {
                            player.sendMessage(ChatColor.GREEN + "To the lobby you go!");
                            player.teleport(new Location(getServer().getWorld("world"), 86.5, 137.5, 3.5, 90, 0));
                            player.getInventory().setHeldItemSlot(0);
                            player.closeInventory();
                        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.WHITE + "Speed:" + ChatColor.GREEN + " Enabled")) {
                            player.sendMessage(ChatColor.RED + "Disabled Speed!");

                            Bson update = set("lobbySettings.speed", false);
                            col.findOneAndUpdate(filter, update);

                            player.removePotionEffect(PotionEffectType.SPEED);
                            event.getClickedInventory().setItem(33, speedOff);
                        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.WHITE + "Speed:" + ChatColor.RED + " Disabled")) {
                            player.sendMessage(ChatColor.GREEN + "Enabled Speed!");

                            Bson update = set("lobbySettings.speed", true);
                            col.findOneAndUpdate(filter, update);

                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 60 * 24, 0, true, true));
                            event.getClickedInventory().setItem(33, speedOn);
                        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.WHITE + "Flight:" + ChatColor.GREEN + " Enabled")) {
                            player.sendMessage(ChatColor.RED + "Disabled Flight!");
                            event.getClickedInventory().setItem(29, flightOff);
                            if (getDoubleJumpEnabled(player)) {

                                Bson update = set("lobbySettings.flight", false);
                                col.findOneAndUpdate(filter, update);
                            } else {
                                Bson update = set("lobbySettings.flight", false);
                                col.findOneAndUpdate(filter, update);
                                player.setAllowFlight(false);
                            }
                        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.WHITE + "Flight:" + ChatColor.RED + " Disabled")) {
                            if (!getRank(player).equals("DEFAULT")) {
                                if (getDoubleJumpEnabled(player)) {
                                    player.sendMessage(ChatColor.GREEN + "Enabled Flight! " + ChatColor.GRAY + "(Does Not Work With Double Jump)");
                                } else {
                                    player.sendMessage(ChatColor.GREEN + "Enabled Flight!");
                                }
                                Bson update = set("lobbySettings.flight", true);
                                col.findOneAndUpdate(filter, update);
                                player.setAllowFlight(true);
                                event.getClickedInventory().setItem(29, flightOn);
                            } else {
                                player.sendMessage(ChatColor.RED + "This Perk required " + ChatColor.GREEN + "VIP" + ChatColor.RED + " to Use!");
                            }

                        }
                    }
                }

            } else if (event.getClickedInventory().getTitle().equalsIgnoreCase(ChatColor.GOLD + "Game Menu")) {
                if (event.getCurrentItem() != null) {
                    if (event.getCurrentItem().getItemMeta() != null) {
                        if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Sumo Duels" + ChatColor.GRAY + " - " + ChatColor.BOLD + "" + ChatColor.LIGHT_PURPLE + "1v1")) {
                            silentLeaveGameQueue(player);
                            addPlayerToQueue(player, "sumo");
                            player.closeInventory();
                            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 2);
                        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Classic Duels" + ChatColor.GRAY + " - " + ChatColor.BOLD + "" + ChatColor.LIGHT_PURPLE + "1v1")) {
                            silentLeaveGameQueue(player);
                            addPlayerToQueue(player, "classic");
                            player.closeInventory();
                            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 2);
                        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Bridge Duels" + ChatColor.GRAY + " - " + ChatColor.BOLD + "" + ChatColor.LIGHT_PURPLE + "1v1")) {
                            silentLeaveGameQueue(player);
                            addPlayerToQueue(player, "bridge");
                            player.closeInventory();
                            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 2);
                        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Leave Queue")) {
                            silentLeaveGameQueue(player);
                            player.closeInventory();
                            player.playSound(player.getLocation(), Sound.NOTE_BASS, 1, 1);
                        }
                    }
                }
            } else if (event.getClickedInventory().getTitle().equalsIgnoreCase(ChatColor.GOLD + "Coin Shop")) {


                if (event.getCurrentItem() != null) {
                    if (event.getCurrentItem().getItemMeta() != null) {
                        if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Ranks")) {
                            Inventory gui = Bukkit.createInventory(player, 5 * 9, ChatColor.GOLD + "Coin Shop " + ChatColor.GRAY + "-" + ChatColor.AQUA + " Ranks");

                            String playerRank = getRank(player);
                            ArrayList purchased = getPurchased(player);
                            int coins = getPlayerCoins(player);

                            ItemStack playerHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                            SkullMeta skull = (SkullMeta) playerHead.getItemMeta();
                            skull.setDisplayName(ChatColor.GOLD + "Your Profile");
                            skull.setOwner(player.getName());
                            playerHead.setItemMeta(skull);

                            ItemStack Ranks = new ItemStack(Material.DIAMOND);
                            ItemMeta RanksMeta = Ranks.getItemMeta();
                            RanksMeta.setDisplayName(ChatColor.AQUA + "Ranks");
                            RanksMeta.setLore(Collections.singletonList(ChatColor.YELLOW + "You Have " + ChatColor.GOLD + getPlayerCoinsFormatted(player) + ChatColor.YELLOW + " Coins"));
                            Ranks.setItemMeta(RanksMeta);

                            ItemStack OG = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 3);
                            ItemMeta OGMeta = OG.getItemMeta();
                            OGMeta.setDisplayName(ChatColor.GOLD + " ");
                            OG.setItemMeta(OGMeta);

                            ItemStack VIP = new ItemStack(Material.EMERALD);
                            ItemMeta VIPMeta = VIP.getItemMeta();
                            VIPMeta.setDisplayName(ChatColor.GREEN + "VIP Rank");
                            VIPMeta.setLore(Arrays.asList(ChatColor.GRAY + "VIP provides basic perks", ChatColor.GRAY + "to improve your Experience!", "", ChatColor.GREEN + "✓ " + ChatColor.GRAY + "Chat Prefix " + ChatColor.GREEN + "[VIP]", ChatColor.GREEN + "✓ " + ChatColor.GRAY + "Name Color " + ChatColor.GREEN + "GREEN", ChatColor.GREEN + "✓ " + ChatColor.GRAY + "Lobby Welcome Message", ChatColor.GRAY + "and more...", "", ChatColor.GRAY + "Cost: " + ChatColor.GOLD + "1,000 Gold", "", purchasable(arrayListIncludes(purchased, "VIP"), 1000, coins)));
                            VIP.setItemMeta(VIPMeta);

                            ItemStack VIPPlus = new ItemStack(Material.EMERALD_BLOCK);
                            ItemMeta VIPPlusMeta = VIPPlus.getItemMeta();
                            VIPPlusMeta.setDisplayName(ChatColor.GREEN + "VIP" + ChatColor.GOLD + "+" + ChatColor.GREEN + " Rank");
                            VIPPlusMeta.setLore(Arrays.asList(ChatColor.GRAY + "VIP+ provides every perk", ChatColor.GRAY + "from VIP and more!", "", ChatColor.GREEN + "✓ " + ChatColor.GRAY + "Chat Prefix " + ChatColor.GREEN + "[VIP" + ChatColor.GOLD + "+" + ChatColor.GREEN + "]", ChatColor.GREEN + "✓ " + ChatColor.GRAY + "Name Color " + ChatColor.GREEN + "GREEN", ChatColor.GREEN + "✓ " + ChatColor.GRAY + "Lobby Welcome Message", ChatColor.GREEN + "✓ " + ChatColor.GRAY + "Chat Formatting", ChatColor.GREEN + "✓ " + ChatColor.GRAY + "Lobby Speed 1", ChatColor.GRAY + "and more...", "", ChatColor.GRAY + "Cost: " + ChatColor.GOLD + "10,000 Gold", "", purchasable(arrayListIncludes(purchased, "VIP+"), 10000, coins)));
                            VIPPlus.setItemMeta(VIPPlusMeta);

                            ItemStack MVP = new ItemStack(Material.DIAMOND);
                            ItemMeta MVPMeta = MVP.getItemMeta();
                            MVPMeta.setDisplayName(ChatColor.AQUA + "MVP Rank");
                            MVPMeta.setLore(Arrays.asList(ChatColor.GRAY + "MVP provides every perk", ChatColor.GRAY + "from VIP+ and more!", "", ChatColor.GREEN + "✓ " + ChatColor.GRAY + "Chat Prefix " + ChatColor.AQUA + "[MVP]", ChatColor.GREEN + "✓ " + ChatColor.GRAY + "Name Color " + ChatColor.AQUA + "AQUA", ChatColor.GREEN + "✓ " + ChatColor.GRAY + "Lobby Welcome Message", ChatColor.GREEN + "✓ " + ChatColor.GRAY + "Chat Formatting", ChatColor.GREEN + "✓ " + ChatColor.GRAY + "Lobby Speed 1", ChatColor.GREEN + "✓ " + ChatColor.GRAY + "Lobby Flight", ChatColor.GREEN + "✓ " + ChatColor.GRAY + "EnderPearl Gadget", ChatColor.GREEN + "✓ " + ChatColor.GRAY + "Enchanting Particle Trail", ChatColor.GRAY + "and more...", "", ChatColor.GRAY + "Cost: " + ChatColor.GOLD + "25,000 Gold", "", purchasable(arrayListIncludes(purchased, "MVP"), 25000, coins)));
                            MVP.setItemMeta(MVPMeta);

                            ItemStack MVPPlus = new ItemStack(Material.DIAMOND_BLOCK);
                            ItemMeta MVPPlusMeta = MVPPlus.getItemMeta();
                            MVPPlusMeta.setDisplayName(ChatColor.AQUA + "MVP" + ChatColor.RED + "+" + ChatColor.AQUA + " Rank");
                            MVPPlusMeta.setLore(Arrays.asList(ChatColor.GRAY + "MVP+ provides every perk", ChatColor.GRAY + "from MVP and more!", "", ChatColor.GREEN + "✓ " + ChatColor.GRAY + "Chat Prefix " + ChatColor.AQUA + "[MVP" + ChatColor.RED + "+" + ChatColor.AQUA + "]", ChatColor.GREEN + "✓ " + ChatColor.GRAY + "Name Color " + ChatColor.AQUA + "AQUA", ChatColor.GREEN + "✓ " + ChatColor.GRAY + "Lobby Welcome Message", ChatColor.GREEN + "✓ " + ChatColor.GRAY + "Chat Formatting", ChatColor.GREEN + "✓ " + ChatColor.GRAY + "Lobby Speed 1", ChatColor.GREEN + "✓ " + ChatColor.GRAY + "Lobby Flight", ChatColor.GREEN + "✓ " + ChatColor.GRAY + "Magic Toy Stick Gadget", ChatColor.GREEN + "✓ " + ChatColor.GRAY + "Mystical Particle Trail", ChatColor.GREEN + "✓ " + ChatColor.GRAY + "Mystical Glass Trail", ChatColor.GRAY + "and more...", "", ChatColor.GRAY + "Cost: " + ChatColor.GOLD + "50,000 Gold", "", purchasable(arrayListIncludes(purchased, "MVP+"), 50000, coins)));
                            MVPPlus.setItemMeta(MVPPlusMeta);

                            ItemStack MVPPlusPlus = new ItemStack(Material.GOLD_BLOCK);
                            ItemMeta MVPPlusPlusMeta = MVPPlusPlus.getItemMeta();
                            MVPPlusPlusMeta.setDisplayName(ChatColor.GOLD + "MVP" + ChatColor.RED + "++" + ChatColor.GOLD + " Rank");
                            MVPPlusPlusMeta.setLore(Arrays.asList(ChatColor.GRAY + "MVP++ provides every perk", ChatColor.GRAY + "from MVP+ and more!", "", ChatColor.GREEN + "✓ " + ChatColor.GRAY + "Chat Prefix " + ChatColor.GOLD + "[MVP" + ChatColor.RED + "++" + ChatColor.GOLD + "]", ChatColor.GREEN + "✓ " + ChatColor.GRAY + "Name Color " + ChatColor.GOLD + "GOLD", ChatColor.GREEN + "✓ " + ChatColor.GRAY + "Lobby Welcome Message", ChatColor.GREEN + "✓ " + ChatColor.GRAY + "Chat Formatting", ChatColor.GREEN + "✓ " + ChatColor.GRAY + "Lobby Speed 2", ChatColor.GREEN + "✓ " + ChatColor.GRAY + "Lobby Flight", ChatColor.GREEN + "✓ " + ChatColor.GRAY + "Grappling Hook Gadget", ChatColor.GREEN + "✓ " + ChatColor.GRAY + "Fire Trail Gadget", ChatColor.GREEN + "✓ " + ChatColor.GRAY + "Punch Gadget", ChatColor.GREEN + "✓ " + ChatColor.GRAY + "Cowboy Gadget", ChatColor.GREEN + "✓ " + ChatColor.GRAY + "Rainbow Particle Trail", ChatColor.GREEN + "✓ " + ChatColor.GRAY + "Rainbow Glass Trail", ChatColor.GRAY + "and more...", "", ChatColor.GRAY + "Cost: " + ChatColor.GOLD + "100,000 Gold", "", purchasable(arrayListIncludes(purchased, "MVP++"), 100000, coins)));
                            MVPPlusPlus.setItemMeta(MVPPlusPlusMeta);

                            ItemStack air = new ItemStack(Material.AIR);

                            gui.setItem(4, Ranks);
                            gui.setItem(9, OG);
                            gui.setItem(10, OG);
                            gui.setItem(11, OG);
                            gui.setItem(12, OG);
                            gui.setItem(13, OG);
                            gui.setItem(14, OG);
                            gui.setItem(15, OG);
                            gui.setItem(16, OG);
                            gui.setItem(17, OG);

                            gui.setItem(27, VIP);
                            gui.setItem(29, VIPPlus);
                            gui.setItem(31, MVP);
                            gui.setItem(33, MVPPlus);
                            gui.setItem(35, MVPPlusPlus);

                            player.openInventory(gui);
                        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Plus Colors")) {

                            Inventory gui = Bukkit.createInventory(player, 6 * 9, ChatColor.GOLD + "Coin Shop " + ChatColor.GRAY + "-" + ChatColor.GOLD + " Plus Colors");

                            ArrayList purchased = getPurchased(player);
                            int coins = getPlayerCoins(player);

                            ItemStack playerHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                            SkullMeta skull = (SkullMeta) playerHead.getItemMeta();
                            skull.setDisplayName(ChatColor.GOLD + "Your Profile");
                            skull.setOwner(player.getName());
                            playerHead.setItemMeta(skull);

                            ItemStack PlusColors = new ItemStack(Material.INK_SACK, 1, (short) 14);
                            ItemMeta PlusColorsMeta = PlusColors.getItemMeta();
                            PlusColorsMeta.setDisplayName(ChatColor.GOLD + "Plus Colors");
                            PlusColorsMeta.setLore(Collections.singletonList(ChatColor.YELLOW + "You Have " + ChatColor.GOLD + getPlayerCoinsFormatted(player) + ChatColor.YELLOW + " Coins"));
                            PlusColors.setItemMeta(PlusColorsMeta);

                            ItemStack OG = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1);
                            ItemMeta OGMeta = OG.getItemMeta();
                            OGMeta.setDisplayName(ChatColor.GOLD + " ");
                            OG.setItemMeta(OGMeta);

//                            ItemStack GoldColor = new ItemStack(Material.INK_SACK, 1, (short) 14);
//                            ItemMeta GoldColorMeta = GoldColor.getItemMeta();
//                            GoldColorMeta.setDisplayName(ChatColor.GOLD + "Gold");
//                            GoldColor.setItemMeta(GoldColorMeta);


                            ItemStack RedColor = new ItemStack(Material.INK_SACK, 1, (short) 1);
                            ItemMeta RedColorMeta = RedColor.getItemMeta();
                            RedColorMeta.setLore(Arrays.asList(ChatColor.GRAY + "Unlock the " + ChatColor.RED + "Red +" + ChatColor.GRAY + " for", ChatColor.GRAY + "any MVP+ or MVP++ Rank!", "", ChatColor.GRAY + "Cost: " + ChatColor.GOLD + "FREE", "", purchasable(arrayListIncludes(purchased, "RED_PLUS"), 0, coins)));
                            RedColorMeta.setDisplayName(ChatColor.RED + "Red");
                            RedColor.setItemMeta(RedColorMeta);

                            ItemStack LimeColor = new ItemStack(Material.INK_SACK, 1, (short) 10);
                            ItemMeta LimeColorMeta = LimeColor.getItemMeta();
                            LimeColorMeta.setLore(Arrays.asList(ChatColor.GRAY + "Unlock the " + ChatColor.GREEN + "Lime +" + ChatColor.GRAY + " for", ChatColor.GRAY + "any MVP+ or MVP++ Rank!", "", ChatColor.GRAY + "Cost: " + ChatColor.GOLD + "FREE", "", purchasable(arrayListIncludes(purchased, "LIME_PLUS"), 0, coins)));
                            LimeColorMeta.setDisplayName(ChatColor.GREEN + "Lime");
                            LimeColor.setItemMeta(LimeColorMeta);

                            ItemStack AquaColor = new ItemStack(Material.INK_SACK, 1, (short) 12);
                            ItemMeta AquaColorMeta = AquaColor.getItemMeta();
                            AquaColorMeta.setLore(Arrays.asList(ChatColor.GRAY + "Unlock the " + ChatColor.AQUA + "Aqua +" + ChatColor.GRAY + " for", ChatColor.GRAY + "any MVP+ or MVP++ Rank!", "", ChatColor.GRAY + "Cost: " + ChatColor.GOLD + "500 Coins", "", purchasable(arrayListIncludes(purchased, "AQUA_PLUS"), 500, coins)));
                            AquaColorMeta.setDisplayName(ChatColor.AQUA + "Aqua");
                            AquaColor.setItemMeta(AquaColorMeta);

                            ItemStack YellowColor = new ItemStack(Material.INK_SACK, 1, (short) 11);
                            ItemMeta YellowColorMeta = YellowColor.getItemMeta();
                            YellowColorMeta.setLore(Arrays.asList(ChatColor.GRAY + "Unlock the " + ChatColor.YELLOW + "Yellow +" + ChatColor.GRAY + " for", ChatColor.GRAY + "any MVP+ or MVP++ Rank!", "", ChatColor.GRAY + "Cost: " + ChatColor.GOLD + "500 Coins", "", purchasable(arrayListIncludes(purchased, "YELLOW_PLUS"), 500, coins)));
                            YellowColorMeta.setDisplayName(ChatColor.YELLOW + "Yellow");
                            YellowColor.setItemMeta(YellowColorMeta);

                            ItemStack GreenColor = new ItemStack(Material.INK_SACK, 1, (short) 2);
                            ItemMeta GreenColorMeta = GreenColor.getItemMeta();
                            GreenColorMeta.setLore(Arrays.asList(ChatColor.GRAY + "Unlock the " + ChatColor.DARK_GREEN + "Green +" + ChatColor.GRAY + " for", ChatColor.GRAY + "any MVP+ or MVP++ Rank!", "", ChatColor.GRAY + "Cost: " + ChatColor.GOLD + "1,000 Coins", "", purchasable(arrayListIncludes(purchased, "GREEN_PLUS"), 1000, coins)));
                            GreenColorMeta.setDisplayName(ChatColor.DARK_GREEN + "Dark Green");
                            GreenColor.setItemMeta(GreenColorMeta);

                            ItemStack BlueColor = new ItemStack(Material.INK_SACK, 1, (short) 4);
                            ItemMeta BlueColorMeta = BlueColor.getItemMeta();
                            BlueColorMeta.setLore(Arrays.asList(ChatColor.GRAY + "Unlock the " + ChatColor.BLUE + "Blue +" + ChatColor.GRAY + " for", ChatColor.GRAY + "any MVP+ or MVP++ Rank!", "", ChatColor.GRAY + "Cost: " + ChatColor.GOLD + "1,000 Coins", "", purchasable(arrayListIncludes(purchased, "BLUE_PLUS"), 1000, coins)));
                            BlueColorMeta.setDisplayName(ChatColor.BLUE + "Blue");
                            BlueColor.setItemMeta(BlueColorMeta);

                            ItemStack CyanColor = new ItemStack(Material.INK_SACK, 1, (short) 6);
                            ItemMeta CyanColorMeta = CyanColor.getItemMeta();
                            CyanColorMeta.setLore(Arrays.asList(ChatColor.GRAY + "Unlock the " + ChatColor.DARK_AQUA + "Cyan +" + ChatColor.GRAY + " for", ChatColor.GRAY + "any MVP+ or MVP++ Rank!", "", ChatColor.GRAY + "Cost: " + ChatColor.GOLD + "2,500 Coins", "", purchasable(arrayListIncludes(purchased, "CYAN_PLUS"), 2500, coins)));
                            CyanColorMeta.setDisplayName(ChatColor.DARK_AQUA + "Cyan");
                            CyanColor.setItemMeta(CyanColorMeta);

                            ItemStack PurpleColor = new ItemStack(Material.INK_SACK, 1, (short) 5);
                            ItemMeta PurpleColorMeta = PurpleColor.getItemMeta();
                            PurpleColorMeta.setLore(Arrays.asList(ChatColor.GRAY + "Unlock the " + ChatColor.DARK_PURPLE + "Purple +" + ChatColor.GRAY + " for", ChatColor.GRAY + "any MVP+ or MVP++ Rank!", "", ChatColor.GRAY + "Cost: " + ChatColor.GOLD + "2,500 Coins", "", purchasable(arrayListIncludes(purchased, "PURPLE_PLUS"), 2500, coins)));
                            PurpleColorMeta.setDisplayName(ChatColor.DARK_PURPLE + "Purple");
                            PurpleColor.setItemMeta(PurpleColorMeta);

                            ItemStack DarkRedColor = new ItemStack(Material.INK_SACK, 1, (short) 1);
                            ItemMeta DarkRedColorMeta = DarkRedColor.getItemMeta();
                            DarkRedColorMeta.setLore(Arrays.asList(ChatColor.GRAY + "Unlock the " + ChatColor.DARK_RED + "Dark Red +" + ChatColor.GRAY + " for", ChatColor.GRAY + "any MVP+ or MVP++ Rank!", "", ChatColor.GRAY + "Cost: " + ChatColor.GOLD + "5,000 Coins", "", purchasable(arrayListIncludes(purchased, "DARK_RED_PLUS"), 5000, coins)));
                            DarkRedColorMeta.setDisplayName(ChatColor.DARK_RED + "Dark Red");
                            DarkRedColor.setItemMeta(DarkRedColorMeta);

                            ItemStack GoldColor = new ItemStack(Material.INK_SACK, 1, (short) 14);
                            ItemMeta GoldColorMeta = GoldColor.getItemMeta();
                            GoldColorMeta.setLore(Arrays.asList(ChatColor.GRAY + "Unlock the " + ChatColor.GOLD + "Gold +" + ChatColor.GRAY + " for", ChatColor.GRAY + "any MVP+ or MVP++ Rank!", "", ChatColor.GRAY + "Cost: " + ChatColor.GOLD + "5,000 Coins", "", purchasable(arrayListIncludes(purchased, "GOLD_PLUS"), 5000, coins)));
                            GoldColorMeta.setDisplayName(ChatColor.GOLD + "Gold");
                            GoldColor.setItemMeta(GoldColorMeta);

                            ItemStack WhiteColor = new ItemStack(Material.INK_SACK, 1, (short) 15);
                            ItemMeta WhiteColorMeta = WhiteColor.getItemMeta();
                            WhiteColorMeta.setLore(Arrays.asList(ChatColor.GRAY + "Unlock the " + ChatColor.WHITE + "White +" + ChatColor.GRAY + " for", ChatColor.GRAY + "any MVP+ or MVP++ Rank!", "", ChatColor.GRAY + "Cost: " + ChatColor.GOLD + "7,500 Coins", "", purchasable(arrayListIncludes(purchased, "WHITE_PLUS"), 7500, coins)));
                            WhiteColorMeta.setDisplayName(ChatColor.WHITE + "White");
                            WhiteColor.setItemMeta(WhiteColorMeta);

                            ItemStack DarkGrayColor = new ItemStack(Material.INK_SACK, 1, (short) 8);
                            ItemMeta DarkGrayColorMeta = DarkGrayColor.getItemMeta();
                            DarkGrayColorMeta.setLore(Arrays.asList(ChatColor.GRAY + "Unlock the " + ChatColor.DARK_GRAY + "Dark Gray +" + ChatColor.GRAY + " for", ChatColor.GRAY + "any MVP+ or MVP++ Rank!", "", ChatColor.GRAY + "Cost: " + ChatColor.GOLD + "7,500 Coins", "", purchasable(arrayListIncludes(purchased, "GRAY_PLUS"), 7500, coins)));
                            DarkGrayColorMeta.setDisplayName(ChatColor.DARK_GRAY + "Gray");
                            DarkGrayColor.setItemMeta(DarkGrayColorMeta);

                            ItemStack DarkBlueColor = new ItemStack(Material.INK_SACK, 1, (short) 4);
                            ItemMeta DarkBlueColorMeta = DarkBlueColor.getItemMeta();
                            DarkBlueColorMeta.setLore(Arrays.asList(ChatColor.GRAY + "Unlock the " + ChatColor.DARK_BLUE + "Dark Blue +" + ChatColor.GRAY + " for", ChatColor.GRAY + "any MVP+ or MVP++ Rank!", "", ChatColor.GRAY + "Cost: " + ChatColor.GOLD + "10,000 Coins", "", purchasable(arrayListIncludes(purchased, "DARK_BLUE_PLUS"), 10000, coins)));
                            DarkBlueColorMeta.setDisplayName(ChatColor.DARK_BLUE + "Dark Blue");
                            DarkBlueColor.setItemMeta(DarkBlueColorMeta);

                            ItemStack BlackColor = new ItemStack(Material.INK_SACK, 1);
                            ItemMeta BlackColorMeta = BlackColor.getItemMeta();
                            BlackColorMeta.setLore(Arrays.asList(ChatColor.GRAY + "Unlock the " + ChatColor.DARK_GRAY + "Black +" + ChatColor.GRAY + " for", ChatColor.GRAY + "any MVP+ or MVP++ Rank!", "", ChatColor.GRAY + "Cost: " + ChatColor.GOLD + "10,000 Coins", "", purchasable(arrayListIncludes(purchased, "BLACK_PLUS"), 10000, coins)));
                            BlackColorMeta.setDisplayName(ChatColor.DARK_GRAY + "Black");
                            BlackColor.setItemMeta(BlackColorMeta);

                            ItemStack air = new ItemStack(Material.AIR);

                            gui.setItem(4, PlusColors);
                            gui.setItem(9, OG);
                            gui.setItem(10, OG);
                            gui.setItem(11, OG);
                            gui.setItem(12, OG);
                            gui.setItem(13, OG);
                            gui.setItem(14, OG);
                            gui.setItem(15, OG);
                            gui.setItem(16, OG);
                            gui.setItem(17, OG);

                            gui.setItem(28, RedColor);
                            gui.setItem(29, AquaColor);
                            gui.setItem(30, BlueColor);
                            gui.setItem(31, CyanColor);
                            gui.setItem(32, DarkRedColor);
                            gui.setItem(33, WhiteColor);
                            gui.setItem(34, DarkBlueColor);

                            gui.setItem(37, LimeColor);
                            gui.setItem(38, YellowColor);
                            gui.setItem(39, GreenColor);
                            gui.setItem(40, PurpleColor);
                            gui.setItem(41, GoldColor);
                            gui.setItem(42, DarkGrayColor);
                            gui.setItem(43, BlackColor);

                            player.openInventory(gui);
                        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Collectibles")) {
                            Inventory gui = Bukkit.createInventory(player, 5 * 9, ChatColor.GOLD + "Coin Shop " + ChatColor.GRAY + "-" + ChatColor.GREEN + " Collectibles");


                            ItemStack playerHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                            SkullMeta skull = (SkullMeta) playerHead.getItemMeta();
                            skull.setDisplayName(ChatColor.GOLD + "Your Profile");
                            skull.setOwner(player.getName());
                            playerHead.setItemMeta(skull);

                            ItemStack Collectibles = new ItemStack(Material.CHEST, 1);
                            ItemMeta CollectiblesMeta = Collectibles.getItemMeta();
                            CollectiblesMeta.setDisplayName(ChatColor.GREEN + "Collectibles");
                            CollectiblesMeta.setLore(Collections.singletonList(ChatColor.YELLOW + "You Have " + ChatColor.GOLD + getPlayerCoinsFormatted(player) + ChatColor.YELLOW + " Coins"));
                            Collectibles.setItemMeta(CollectiblesMeta);

                            ItemStack OG = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
                            ItemMeta OGMeta = OG.getItemMeta();
                            OGMeta.setDisplayName(ChatColor.GREEN + " ");
                            OG.setItemMeta(OGMeta);

                            ItemStack ComingSoon = new ItemStack(Material.CHEST, 1);
                            ItemMeta ComingSoonMeta = ComingSoon.getItemMeta();
                            ComingSoonMeta.setLore(Arrays.asList(ChatColor.GRAY + "Coming Soon..."));
                            ComingSoonMeta.setDisplayName(ChatColor.GREEN + "Collectibles");
                            ComingSoon.setItemMeta(ComingSoonMeta);

                            gui.setItem(4, Collectibles);
                            gui.setItem(9, OG);
                            gui.setItem(10, OG);
                            gui.setItem(11, OG);
                            gui.setItem(12, OG);
                            gui.setItem(13, OG);
                            gui.setItem(14, OG);
                            gui.setItem(15, OG);
                            gui.setItem(16, OG);
                            gui.setItem(17, OG);

                            gui.setItem(31, ComingSoon);

                            player.openInventory(gui);
                        }
                    }
                }
            } else if (event.getClickedInventory().getTitle().equalsIgnoreCase(ChatColor.GOLD + "Coin Shop " + ChatColor.GRAY + "-" + ChatColor.AQUA + " Ranks")) {
                if (event.getCurrentItem() != null) {
                    if (event.getCurrentItem().getItemMeta() != null) {
                        String rank = getRank(player);
                        if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "VIP Rank")) {
                            if (getPlayerPurchased(player, "VIP")) {
                                player.sendMessage(ChatColor.RED + "Looks like you already have " + ChatColor.GREEN + "VIP" + ChatColor.RED + "!");
                            } else if (getPlayerCoins(player) < 1000) {
                                player.sendMessage(ChatColor.RED + "Uh Oh. You dont have enough coins! You need " + (1000 - getPlayerCoins(player)) + " more!");
                            } else {
                                purchase(player, "VIP", 1000);
                                player.sendMessage(ChatColor.GREEN + "You have purchased VIP!");
                                player.closeInventory();
                                if (rank.equals("DEFAULT")) {
                                    setRank(player, "VIP");
                                    player.sendMessage(ChatColor.GRAY + "Your rank has been set to " + ChatColor.GREEN + "VIP" + ChatColor.GRAY + "! You might need to relog for changes to take place!");
                                }
                            }
                        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "VIP" + ChatColor.GOLD + "+" + ChatColor.GREEN + " Rank")) {
                            if (getPlayerPurchased(player, "VIP+")) {
                                player.sendMessage(ChatColor.RED + "Looks like you already have " + ChatColor.GREEN + "VIP" + ChatColor.GOLD + "+" + ChatColor.RED + "!");
                            } else if (getPlayerCoins(player) < 10000) {
                                player.sendMessage(ChatColor.RED + "Uh Oh. You dont have enough coins! You need " + (10000 - getPlayerCoins(player)) + " more!");
                            } else {
                                purchase(player, "VIP+", 10000);
                                player.sendMessage(ChatColor.GREEN + "You have purchased VIP" + ChatColor.GOLD + "+" + ChatColor.GREEN + "!");
                                player.closeInventory();
                                if (rank.equals("DEFAULT") || rank.equals("VIP")) {
                                    setRank(player, "VIP+");
                                    player.sendMessage(ChatColor.GRAY + "Your rank has been set to " + ChatColor.GREEN + "VIP" + ChatColor.GOLD + "+" + ChatColor.GRAY + "! You might need to relog for changes to take place!");
                                }
                            }
                        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "MVP Rank")) {
                            if (getPlayerPurchased(player, "MVP")) {
                                player.sendMessage(ChatColor.RED + "Looks like you already have " + ChatColor.AQUA + "MVP" + ChatColor.RED + "!");
                            } else if (getPlayerCoins(player) < 25000) {
                                player.sendMessage(ChatColor.RED + "Uh Oh. You dont have enough coins! You need " + (25000 - getPlayerCoins(player)) + " more!");
                            } else {
                                purchase(player, "MVP", 25000);
                                player.sendMessage(ChatColor.GREEN + "You have purchased " + ChatColor.AQUA + "MVP" + ChatColor.GREEN + "!");
                                player.closeInventory();
                                if (rank.equals("DEFAULT") || rank.equals("VIP") || rank.equals("VIP+")) {
                                    setRank(player, "MVP");
                                    player.sendMessage(ChatColor.GRAY + "Your rank has been set to " + ChatColor.AQUA + "MVP" + ChatColor.GRAY + "! You might need to relog for changes to take place!");
                                }
                            }
                        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "MVP" + ChatColor.RED + "+" + ChatColor.AQUA + " Rank")) {
                            if (getPlayerPurchased(player, "MVP+")) {
                                player.sendMessage(ChatColor.RED + "Looks like you already have " + ChatColor.AQUA + "MVP" + ChatColor.RED + "+" + ChatColor.RED + "!");
                            } else if (getPlayerCoins(player) < 50000) {
                                player.sendMessage(ChatColor.RED + "Uh Oh. You dont have enough coins! You need " + (50000 - getPlayerCoins(player)) + " more!");
                            } else {
                                purchase(player, "MVP+", 50000);
                                player.sendMessage(ChatColor.GREEN + "You have purchased " + ChatColor.AQUA + "MVP" + ChatColor.RED + "+" + ChatColor.GREEN + "!");
                                player.closeInventory();
                                if (rank.equals("DEFAULT") || rank.equals("VIP") || rank.equals("VIP+") || rank.equals("MVP")) {
                                    setRank(player, "MVP+");
                                    player.sendMessage(ChatColor.GRAY + "Your rank has been set to " + ChatColor.AQUA + "MVP" + ChatColor.RED + "+" + ChatColor.GRAY + "! You might need to relog for changes to take place!");
                                }
                            }
                        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "MVP" + ChatColor.RED + "++" + ChatColor.GOLD + " Rank")) {
                            if (getPlayerPurchased(player, "MVP++")) {
                                player.sendMessage(ChatColor.RED + "Looks like you already have " + ChatColor.GOLD + "MVP" + ChatColor.RED + "++" + ChatColor.RED + "!");
                            } else if (getPlayerCoins(player) < 100000) {
                                player.sendMessage(ChatColor.RED + "Uh Oh. You dont have enough coins! You need " + (100000 - getPlayerCoins(player)) + " more!");
                            } else {
                                purchase(player, "MVP++", 100000);
                                player.sendMessage(ChatColor.GREEN + "You have purchased " + ChatColor.GOLD + "MVP" + ChatColor.RED + "++" + ChatColor.GREEN + "!");
                                player.closeInventory();
                                if (rank.equals("DEFAULT") || rank.equals("VIP") || rank.equals("VIP+") || rank.equals("MVP") || rank.equals("MVP+")) {
                                    setRank(player, "MVP++");
                                    player.sendMessage(ChatColor.GRAY + "Your rank has been set to " + ChatColor.GOLD + "MVP" + ChatColor.RED + "++" + ChatColor.GRAY + "! You might need to relog for changes to take place!");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean getPlayerPurchased(Player player, String item) {
        return arrayListIncludes(getPurchased(player), item);
    }

    public void purchase(Player player, String item, int cost) {
        addCoins(player, -cost);
        Document filter = new Document("UUID", player.getUniqueId().toString());
        Bson updates = push("purchased", item);
        col.findOneAndUpdate(filter, updates);
    }

    public void setRank(Player player, String rank) {
        Document filter = new Document("UUID", player.getUniqueId().toString());
        Bson updates = push("rank", rank);
        col.findOneAndUpdate(filter, updates);
    }

    public String purchasable(boolean purchased, int cost, int coins) {
        if (purchased) {
            return ChatColor.RED + "Already Purchased!";
        } else if (coins < cost) {
            return ChatColor.RED + "Not Enough Coins!";
        } else {
            return ChatColor.YELLOW + "Click To Purchase";
        }
    }

    @EventHandler
    public void onInventoryMove(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (player.getGameMode() == GameMode.SURVIVAL) {
            if (!ClassicGame.get(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        for (Player p: Bukkit.getOnlinePlayers()) {
            player.getScoreboard().getObjective(player.getName()).getScore(p.getName()).setScore((int) p.getHealth());
            p.getScoreboard().getObjective(p.getName()).getScore(player.getName()).setScore((int) player.getHealth());
        }
        player.setSaturation(20);
        player.setExp(0);
        if (!BridgeGame.get(player)) {
            player.setLevel(0);
        }
        if (player.getLocation().getY() < 10) {
            player.teleport(new Location(getServer().getWorld("world"), 86.5, 137.5, 3.5, 90, 0));
        }
        Block block = player.getWorld().getBlockAt(player.getLocation().subtract(0, 1, 0));
        if (!block.getType().equals(Material.AIR)) {
            DoubleJumps.put(player, false);
        }
        if (block.getType().equals(Material.SLIME_BLOCK)) {
            Vector vector = player.getLocation().getDirection().multiply(1.5).setY(1.3);
            player.setVelocity(vector);

        }
        if (BridgeActive) {
            if (BridgeGame.get(player)) {
                if (block.getType().equals(Material.ENDER_PORTAL)) {
                    Block blockUnder = player.getWorld().getBlockAt(player.getLocation().subtract(0, 2, 0));
                    if (blockUnder.getType().equals(Material.REDSTONE_BLOCK)) {
                        if (player != BridgePlayer1) {
                            BridgeGoals.put(BridgePlayer2, BridgeGoals.get(BridgePlayer2) + 1);
                            goalScored(BridgePlayer1, BridgePlayer2, "blue");
                        } else {
                            player.sendMessage(ChatColor.RED + "You can't score on yourself!");
                            BridgePlayer1Respawn();
                        }
                    }
                    if (blockUnder.getType().equals(Material.LAPIS_BLOCK)) {
                        if (player != BridgePlayer2) {
                            BridgeGoals.put(BridgePlayer1, BridgeGoals.get(BridgePlayer1) + 1);
                            goalScored(BridgePlayer1, BridgePlayer2, "red");
                        } else {
                            player.sendMessage(ChatColor.RED + "You can't score on yourself!");
                            BridgePlayer2Respawn();
                        }
                    }
                }
            }
        }
    }

    public void openCoinShop(Player player) {
        Inventory gui = Bukkit.createInventory(player, 5 * 9, ChatColor.GOLD + "Coin Shop");

        ItemStack playerHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skull = (SkullMeta) playerHead.getItemMeta();
        skull.setDisplayName(ChatColor.GOLD + "Your Profile");
        skull.setOwner(player.getName());
        playerHead.setItemMeta(skull);

        ItemStack CoinShop = new ItemStack(Material.EMERALD);
        ItemMeta CoinShopMeta = CoinShop.getItemMeta();
        CoinShopMeta.setDisplayName(ChatColor.GOLD + "Coin Shop");
        CoinShopMeta.setLore(Collections.singletonList(ChatColor.YELLOW + "You Have " + ChatColor.GOLD + getPlayerCoinsFormatted(player) + ChatColor.YELLOW + " Coins"));
        CoinShop.setItemMeta(CoinShopMeta);

        ItemStack OG = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1);
        ItemMeta OGMeta = OG.getItemMeta();
        OGMeta.setDisplayName(ChatColor.GOLD + " ");
        OG.setItemMeta(OGMeta);

        ItemStack Ranks = new ItemStack(Material.DIAMOND);
        ItemMeta RanksMeta = Ranks.getItemMeta();
        RanksMeta.setDisplayName(ChatColor.AQUA + "Ranks");
        Ranks.setItemMeta(RanksMeta);

        ItemStack PlusColors = new ItemStack(Material.INK_SACK, 1, (short) 14);
        ItemMeta PlusColorsMeta = PlusColors.getItemMeta();
        PlusColorsMeta.setDisplayName(ChatColor.GOLD + "Plus Colors");
        PlusColors.setItemMeta(PlusColorsMeta);

        ItemStack Gadgets = new ItemStack(Material.CHEST);
        ItemMeta GadgetsMeta = Gadgets.getItemMeta();
        GadgetsMeta.setDisplayName(ChatColor.GREEN + "Collectibles");
        Gadgets.setItemMeta(GadgetsMeta);

        ItemStack air = new ItemStack(Material.AIR);


        gui.setItem(4, CoinShop);
        gui.setItem(9, OG);
        gui.setItem(10, OG);
        gui.setItem(11, OG);
        gui.setItem(12, OG);
        gui.setItem(13, OG);
        gui.setItem(14, OG);
        gui.setItem(15, OG);
        gui.setItem(16, OG);
        gui.setItem(17, OG);

        gui.setItem(28, Ranks);
        gui.setItem(31, PlusColors);
        gui.setItem(34, Gadgets);

        player.openInventory(gui);
    }

    @EventHandler
    public void onItemClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getType() == Material.SKULL_ITEM) {
            Inventory gui = Bukkit.createInventory(player, 6 * 9, ChatColor.GOLD + "Your Profile");

            ItemStack playerHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta skull = (SkullMeta) playerHead.getItemMeta();
            skull.setDisplayName(ChatColor.RESET + "" + ChatColor.GOLD + player.getName());
            skull.setOwner(player.getName());
            playerHead.setItemMeta(skull);

            ItemStack hub = new ItemStack(Material.WATCH);
            ItemMeta hubMeta = hub.getItemMeta();
            hubMeta.setDisplayName(ChatColor.GOLD + "Go To Lobby");
            hub.setItemMeta(hubMeta);

            ItemStack flightOn = new ItemStack(Material.FEATHER);
            ItemMeta flightOnMeta = flightOn.getItemMeta();
            flightOnMeta.setDisplayName(ChatColor.WHITE + "Flight:" + ChatColor.GREEN + " Enabled");
            flightOnMeta.setLore(Arrays.asList(ChatColor.GRAY + "Fly around the lobby, like", ChatColor.GRAY + "the hackers do!", "", ChatColor.YELLOW + "Click To Disable!"));
            flightOn.setItemMeta(flightOnMeta);

            ItemStack flightOff = new ItemStack(Material.FEATHER);
            ItemMeta flightOffMeta = flightOff.getItemMeta();
            flightOffMeta.setDisplayName(ChatColor.WHITE + "Flight:" + ChatColor.RED + " Disabled");
            flightOffMeta.setLore(Arrays.asList(ChatColor.GRAY + "Fly around the lobby, like", ChatColor.GRAY + "the hackers do!", "", ChatColor.YELLOW + "Click To Enable!"));
            flightOff.setItemMeta(flightOffMeta);

            ItemStack djOn = new ItemStack(Material.SLIME_BALL);
            ItemMeta djOnMeta = djOn.getItemMeta();
            djOnMeta.setDisplayName(ChatColor.WHITE + "Double Jump:" + ChatColor.GREEN + " Enabled");
            djOnMeta.setLore(Arrays.asList(ChatColor.GRAY + "Jump around the lobby, and", ChatColor.GRAY + "become your own Mario!", "", ChatColor.YELLOW + "Click To Disable!"));
            djOn.setItemMeta(djOnMeta);

            ItemStack djOff = new ItemStack(Material.SLIME_BALL);
            ItemMeta djOffMeta = djOff.getItemMeta();
            djOffMeta.setDisplayName(ChatColor.WHITE + "Double Jump:" + ChatColor.RED + " Disabled");
            djOffMeta.setLore(Arrays.asList(ChatColor.GRAY + "Jump around the lobby, and", ChatColor.GRAY + "become your own Mario!", "", ChatColor.YELLOW + "Click To Enable!"));
            djOff.setItemMeta(djOffMeta);

            ItemStack speedOn = new ItemStack(Material.DIAMOND_BOOTS);
            ItemMeta speedOnMeta = speedOn.getItemMeta();
            speedOnMeta.setDisplayName(ChatColor.WHITE + "Speed:" + ChatColor.GREEN + " Enabled");
            speedOnMeta.setLore(Arrays.asList(ChatColor.GRAY + "I. Am. SPEEEEDDDD.", ChatColor.GRAY + "- " + ChatColor.RED + "Lightning " + ChatColor.GOLD + "McQueen", "", ChatColor.YELLOW + "Click To Disable!"));
            speedOn.setItemMeta(speedOnMeta);

            ItemStack speedOff = new ItemStack(Material.DIAMOND_BOOTS);
            ItemMeta speedOffMeta = speedOff.getItemMeta();
            speedOffMeta.setDisplayName(ChatColor.WHITE + "Speed:" + ChatColor.RED + " Disabled");
            speedOffMeta.setLore(Arrays.asList(ChatColor.GRAY + "I. Am. SPEEEEDDDD.", ChatColor.GRAY + "- " + ChatColor.RED + "Lightning " + ChatColor.GOLD + "McQueen", "", ChatColor.YELLOW + "Click To Enable!"));
            speedOff.setItemMeta(speedOffMeta);

            ItemStack rankMenu = new ItemStack(Material.DIAMOND);
            ItemMeta rankMenuMeta = rankMenu.getItemMeta();
            rankMenuMeta.setDisplayName(ChatColor.AQUA + "Ranks");
            rankMenuMeta.setLore(Arrays.asList(ChatColor.GRAY + "Change your rank and", ChatColor.GRAY + "become whoever you want!", "", ChatColor.GRAY + "Coming Soon..."));
            rankMenu.setItemMeta(rankMenuMeta);

            ItemStack PlusColor = new ItemStack(Material.INK_SACK, 1, (short) 14);
            ItemMeta PlusColorMeta = PlusColor.getItemMeta();
            PlusColorMeta.setDisplayName(ChatColor.GOLD + "Plus Colors");
            PlusColorMeta.setLore(Arrays.asList(ChatColor.GRAY + "Change your rank's", ChatColor.GRAY + "Plus Colors!", "", ChatColor.GRAY + "Coming Soon..."));
            PlusColor.setItemMeta(PlusColorMeta);

            ItemStack Stats = new ItemStack(Material.GOLD_BLOCK);
            ItemMeta StatsMeta = Stats.getItemMeta();
            StatsMeta.setDisplayName(ChatColor.YELLOW + "Statistics");
            StatsMeta.setLore(Arrays.asList(ChatColor.GRAY + "See your game stats!", "", ChatColor.GRAY + "Coming Soon..."));
            Stats.setItemMeta(StatsMeta);

            ItemStack GameSettings = new ItemStack(Material.COMMAND);
            ItemMeta GameSettingsMeta = GameSettings.getItemMeta();
            GameSettingsMeta.setDisplayName(ChatColor.GOLD + "Game Settings");
            GameSettingsMeta.setLore(Arrays.asList(ChatColor.GRAY + "Change your game settings!", "", ChatColor.GRAY + "Coming Soon..."));
            GameSettings.setItemMeta(GameSettingsMeta);

            ItemStack og = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1);
            ItemMeta ogMeta = og.getItemMeta();
            ogMeta.setDisplayName(" ");
            og.setItemMeta(ogMeta);



            ItemStack air = new ItemStack(Material.AIR);
            ItemStack[] menuItems = {
                    air,
                    air,
                    air,
                    air,
                    playerHead,
                    air,
                    air,
                    air,
                    hub,
                    og,
                    og,
                    og,
                    og,
                    og,
                    og,
                    og,
                    og,
                    og
            };

            gui.setItem(4, playerHead);
            gui.setItem(8, hub);
            gui.setItem(9, og);
            gui.setItem(10, og);
            gui.setItem(11, og);
            gui.setItem(12, og);
            gui.setItem(13, og);
            gui.setItem(14, og);
            gui.setItem(15, og);
            gui.setItem(16, og);
            gui.setItem(17, og);
            Document filter = new Document("UUID", player.getUniqueId().toString());
            if (getFlightEnabled(player)) {
                gui.setItem(29, flightOn);
            } else {
                gui.setItem(29, flightOff);
            }
            if (getDoubleJumpEnabled(player)) {
                gui.setItem(31, djOn);
            } else {
                gui.setItem(31, djOff);
            }
            if (getSpeedEnabled(player)) {
                gui.setItem(33, speedOn);
            } else {
                gui.setItem(33, speedOff);
            }

            gui.setItem(38, rankMenu);
            gui.setItem(40, PlusColor);
            gui.setItem(42, Stats);
            gui.setItem(53, GameSettings);


            player.openInventory(gui);
        }
        if (player.getItemInHand().getType() == Material.INK_SACK) {
            if (getPlayersVisible(player)) {
                Document filter = new Document("UUID", player.getUniqueId().toString());

                Bson update = set("lobbySettings.playersVisible", false);
                col.findOneAndUpdate(filter, update);
                for (Player toHide: Bukkit.getServer().getOnlinePlayers()) {
                    if (player != toHide) {
                        player.hidePlayer(toHide);
                    }
                }
                player.sendMessage(ChatColor.RED + "Players are now hidden.");

                ItemStack playersOff = new ItemStack(Material.INK_SACK, 1, (byte) 8);
                ItemMeta playersOffMeta = playersOff.getItemMeta();
                playersOffMeta.setDisplayName(ChatColor.WHITE + "Players: " + ChatColor.RED + "Hidden " + ChatColor.GRAY + "(Right Click)");
                playersOff.setItemMeta(playersOffMeta);

                player.setItemInHand(playersOff);
            } else {
                Document filter = new Document("UUID", player.getUniqueId().toString());

                Bson update = set("lobbySettings.playersVisible", true);
                col.findOneAndUpdate(filter, update);
                for (Player toShow: Bukkit.getServer().getOnlinePlayers()) {
                    if (player != toShow) {
                        if (!PlayerVanished.get(toShow)) {
                            player.showPlayer(toShow);
                        }
                    }
                }
                player.sendMessage(ChatColor.GREEN + "Players are now visible.");

                ItemStack playersOn = new ItemStack(Material.INK_SACK, 1, (byte) 10);
                ItemMeta playersOnMeta = playersOn.getItemMeta();
                playersOnMeta.setDisplayName(ChatColor.WHITE + "Players: " + ChatColor.GREEN + "Visible " + ChatColor.GRAY + "(Right Click)");
                playersOn.setItemMeta(playersOnMeta);

                player.setItemInHand(playersOn);
            }
        }
        if (player.getItemInHand().getType() == Material.COMPASS) {
            openGameMenu(player);
        }

        if (player.getItemInHand().getType() == Material.EMERALD && player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Coin Shop " + ChatColor.GRAY + "(Right Click)")) {
            openCoinShop(player);
        }

        if (player.getItemInHand().getType() == Material.CHEST && player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Collectibles " + ChatColor.GRAY + "(Right Click)")) {
            Inventory gui = Bukkit.createInventory(player, 5 * 9, ChatColor.GOLD + "Coin Shop " + ChatColor.GRAY + "-" + ChatColor.GREEN + " Collectibles");


            ItemStack playerHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta skull = (SkullMeta) playerHead.getItemMeta();
            skull.setDisplayName(ChatColor.GOLD + "Your Profile");
            skull.setOwner(player.getName());
            playerHead.setItemMeta(skull);

            ItemStack Collectibles = new ItemStack(Material.CHEST, 1);
            ItemMeta CollectiblesMeta = Collectibles.getItemMeta();
            CollectiblesMeta.setDisplayName(ChatColor.GREEN + "Collectibles");
            CollectiblesMeta.setLore(Collections.singletonList(ChatColor.YELLOW + "You Have " + ChatColor.GOLD + getPlayerCoinsFormatted(player) + ChatColor.YELLOW + " Coins"));
            Collectibles.setItemMeta(CollectiblesMeta);

            ItemStack OG = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
            ItemMeta OGMeta = OG.getItemMeta();
            OGMeta.setDisplayName(ChatColor.GREEN + " ");
            OG.setItemMeta(OGMeta);

            ItemStack ComingSoon = new ItemStack(Material.CHEST, 1);
            ItemMeta ComingSoonMeta = ComingSoon.getItemMeta();
            ComingSoonMeta.setLore(Arrays.asList(ChatColor.GRAY + "Coming Soon..."));
            ComingSoonMeta.setDisplayName(ChatColor.GREEN + "Collectibles");
            ComingSoon.setItemMeta(ComingSoonMeta);

            gui.setItem(4, Collectibles);
            gui.setItem(9, OG);
            gui.setItem(10, OG);
            gui.setItem(11, OG);
            gui.setItem(12, OG);
            gui.setItem(13, OG);
            gui.setItem(14, OG);
            gui.setItem(15, OG);
            gui.setItem(16, OG);
            gui.setItem(17, OG);

            gui.setItem(31, ComingSoon);

            player.openInventory(gui);

        }
    }

    public void openGameMenu(Player player) {
        int size = 3;
        if (SumoQueue.containsValue(player) || ClassicQueue.containsValue(player) || BridgeQueue.containsValue(player)) {
            size = 4;
        }
        Inventory gui = Bukkit.createInventory(player, size * 9, ChatColor.GOLD + "Game Menu");

        ItemStack playerHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skull = (SkullMeta) playerHead.getItemMeta();
        skull.setDisplayName(ChatColor.GOLD + "Your Profile");
        skull.setOwner(player.getName());
        playerHead.setItemMeta(skull);

        String SumoQueueString;
        if (SumoQueueSize > 0) {
            SumoQueueString = ChatColor.GREEN.toString() + "Players In Queue: " + SumoQueueSize;
        } else {
            SumoQueueString = ChatColor.DARK_GRAY.toString() + "Players In Queue: " + SumoQueueSize;
        }
        ItemStack sumo = new ItemStack(Material.SLIME_BALL);
        ItemMeta sumoMeta = sumo.getItemMeta();
        sumoMeta.setDisplayName(ChatColor.GREEN + "Sumo Duels" + ChatColor.GRAY + " - " + ChatColor.BOLD + "" + ChatColor.LIGHT_PURPLE + "1v1");
        sumoMeta.setLore(Arrays.asList(ChatColor.GRAY + "Knock your opponent out of the", ChatColor.GRAY + "arena with your fists!","",ChatColor.WHITE + "Wins: " + ChatColor.YELLOW + getSumoWins(player), "", SumoQueueString, ChatColor.YELLOW + "Click To Play!"));
        sumo.setItemMeta(sumoMeta);

        String ClassicQueueString;
        if (ClassicQueueSize > 0) {
            ClassicQueueString = ChatColor.GREEN.toString() + "Players In Queue: " + ClassicQueueSize;
        } else {
            ClassicQueueString = ChatColor.DARK_GRAY.toString() + "Players In Queue: " + ClassicQueueSize;
        }
        ItemStack classic = new ItemStack(Material.FISHING_ROD);
        ItemMeta classicMeta = classic.getItemMeta();
        classicMeta.setDisplayName(ChatColor.GREEN + "Classic Duels" + ChatColor.GRAY + " - " + ChatColor.BOLD + "" + ChatColor.LIGHT_PURPLE + "1v1");
        classicMeta.setLore(Arrays.asList(ChatColor.GRAY + "Iron gear 1v1 Duel!","",ChatColor.WHITE + "Wins: " + ChatColor.YELLOW + getClassicWins(player), "", ClassicQueueString, ChatColor.YELLOW + "Click To Play!"));
        classic.setItemMeta(classicMeta);

        String BridgeQueueString;
        if (BridgeQueueSize > 0) {
            BridgeQueueString = ChatColor.GREEN.toString() + "Players In Queue: " + BridgeQueueSize;
        } else {
            BridgeQueueString = ChatColor.DARK_GRAY.toString() + "Players In Queue: " + BridgeQueueSize;
        }
        ItemStack bridge = new ItemStack(Material.STAINED_CLAY, 1,  (short) 14);
        ItemMeta bridgeMeta = bridge.getItemMeta();
        bridgeMeta.setDisplayName(ChatColor.GREEN + "Bridge Duels" + ChatColor.GRAY + " - " + ChatColor.BOLD + "" + ChatColor.LIGHT_PURPLE + "1v1");
        bridgeMeta.setLore(Arrays.asList(ChatColor.GRAY + "Cross the bridge and score", ChatColor.GRAY + "more goals than the opposing team!","",ChatColor.WHITE + "Wins: " + ChatColor.YELLOW + getBridgeWins(player), "", BridgeQueueString, ChatColor.YELLOW + "Click To Play!"));
        bridge.setItemMeta(bridgeMeta);

        ItemStack leaveQueue = new ItemStack(Material.BARRIER);
        ItemMeta leaveQueueMenu = leaveQueue.getItemMeta();
        leaveQueueMenu.setDisplayName(ChatColor.RED + "Leave Queue");
        leaveQueue.setItemMeta(leaveQueueMenu);

        ItemStack air = new ItemStack(Material.AIR);


        gui.setItem(10, sumo);
        gui.setItem(13, classic);
        gui.setItem(16, bridge);
        if (SumoQueue.containsValue(player) || ClassicQueue.containsValue(player) || BridgeQueue.containsValue(player)) {
            gui.setItem(35, leaveQueue);
        }

        player.openInventory(gui);
    }

    @EventHandler
    public void playerRespawn(PlayerRespawnEvent event) {
        event.setRespawnLocation(new Location(getServer().getWorld("world"), 86.5, 137.5, 3.5, 90, 0));
    }

    @EventHandler
    public void doubleJump(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() != GameMode.CREATIVE) {
            Document filter = new Document("UUID", player.getUniqueId().toString());
            if (getDoubleJumpEnabled(player)) {
                event.setCancelled(true);
                Block block = player.getWorld().getBlockAt(player.getLocation().subtract(0, 1, 0));
                if (!DoubleJumps.get(player)) {
                    if (getLobbyDoubleJumps(player) % 30 == 0) {
                        addCoins(player, 1);
                    }

                    Bson updates = inc("stats.lobby.doubleJumps", 1);
                    col.findOneAndUpdate(filter, updates);

                    Vector vector = player.getLocation().getDirection().multiply(1.5).setY(1.3);
                    player.setVelocity(vector);
                    player.playSound(player.getLocation(), Sound.FIREWORK_LAUNCH, 1, 1);
                    DoubleJumps.put(player, true);
                }
            }

        }
    }

    public void sumoEvent(Player player1, Player player2) {
        SumoRequestNo.remove(player1);
        player1.showPlayer(player2);
        player2.showPlayer(player1);

        SumoQueued = true;
        SumoGame.put(player1, true);
        SumoGame.put(player2, true);

        silentLeaveGameQueue(player1);
        silentLeaveGameQueue(player2);

        Document filter1 = new Document("UUID", player1.getUniqueId().toString());
        Bson updates1 = inc("stats.sumo.gamesPlayed", 1);
        col.findOneAndUpdate(filter1, updates1);
        Document filter2 = new Document("UUID", player2.getUniqueId().toString());
        Bson updates2 = inc("stats.sumo.gamesPlayed", 1);
        col.findOneAndUpdate(filter2, updates2);

        player1.teleport(new Location(getServer().getWorld("world"), 109.5, 105.5, 404.5, 0, 0));
        player2.teleport(new Location(getServer().getWorld("world"), 109.5, 105.5, 416.5, 90 * 2, 0));

        SumoPlayer1 = player1;
        SumoPlayer2 = player2;

        player1.getInventory().clear();
        player2.getInventory().clear();

        if (player1.getAllowFlight()) {
            player1.setAllowFlight(false);
        }
        if (player2.getAllowFlight()) {
            player2.setAllowFlight(false);
        }

        if (player1.hasPotionEffect(PotionEffectType.SPEED)) {
            player1.removePotionEffect(PotionEffectType.SPEED);
        }
        if (player2.hasPotionEffect(PotionEffectType.SPEED)) {
            player2.removePotionEffect(PotionEffectType.SPEED);
        }
        BukkitScheduler scheduler5 = Bukkit.getServer().getScheduler();
        scheduler5.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                JSONMessage.create("5").color(ChatColor.GREEN).title(1, 18, 1, player1);
                JSONMessage.create("5").color(ChatColor.GREEN).title(1, 18, 1, player2);
                player1.playSound(player1.getLocation(), Sound.NOTE_STICKS, 1, 1);
                player2.playSound(player2.getLocation(), Sound.NOTE_STICKS, 1, 1);
            }
        }, 20);
        BukkitScheduler scheduler4 = Bukkit.getServer().getScheduler();
        scheduler4.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                JSONMessage.create("4").color(ChatColor.YELLOW).title(1, 18, 1, player1);
                JSONMessage.create("4").color(ChatColor.YELLOW).title(1, 18, 1, player2);
                player1.playSound(player1.getLocation(), Sound.NOTE_STICKS, 1, 1);
                player2.playSound(player2.getLocation(), Sound.NOTE_STICKS, 1, 1);
            }
        }, 40);

        BukkitScheduler scheduler3 = Bukkit.getServer().getScheduler();
        scheduler3.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                JSONMessage.create("3").color(ChatColor.GOLD).title(1, 18, 1, player1);
                JSONMessage.create("3").color(ChatColor.GOLD).title(1, 18, 1, player2);
                player1.playSound(player1.getLocation(), Sound.NOTE_STICKS, 1, 1);
                player2.playSound(player2.getLocation(), Sound.NOTE_STICKS, 1, 1);
            }
        }, 60);
        BukkitScheduler scheduler2 = Bukkit.getServer().getScheduler();
        scheduler2.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                JSONMessage.create("2").color(ChatColor.RED).title(1, 18, 1, player1);
                JSONMessage.create("2").color(ChatColor.RED).title(1, 18, 1, player2);
                player1.playSound(player1.getLocation(), Sound.NOTE_STICKS, 1, 1);
                player2.playSound(player2.getLocation(), Sound.NOTE_STICKS, 1, 1);

            }
        }, 80);
        BukkitScheduler scheduler1 = Bukkit.getServer().getScheduler();
        scheduler1.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                JSONMessage.create("1").color(ChatColor.DARK_RED).title(1, 18, 1, player1);
                JSONMessage.create("1").color(ChatColor.DARK_RED).title(1, 18, 1, player2);
                player1.playSound(player1.getLocation(), Sound.NOTE_STICKS, 1, 1);
                player2.playSound(player2.getLocation(), Sound.NOTE_STICKS, 1, 1);
            }
        }, 100);
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                player1.teleport(new Location(getServer().getWorld("world"), 109.5, 105.5, 404.5, 0, 0));
                player2.teleport(new Location(getServer().getWorld("world"), 109.5, 105.5, 416.5, 90 * 2, 0));
                player1.playSound(player1.getLocation(), Sound.NOTE_PIANO, 1,  1);
                player2.playSound(player2.getLocation(), Sound.NOTE_PIANO, 1,  1);
                SumoActive = true;
            }
        }, 120);

    }

    public void SumoEventEnd(Player winner, Player loser) {
        Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "----------------");
        Bukkit.getServer().broadcastMessage(ChatColor.GREEN + winner.getName() + ChatColor.GOLD + " just won a Sumo Duel against " + ChatColor.RED + loser.getName() + ChatColor.GOLD + "!");
        Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "----------------");

        Document Wfilter = new Document("UUID", winner.getUniqueId().toString());
        Bson Wupdates = inc("stats.sumo.wins", 1);
        col.findOneAndUpdate(Wfilter, Wupdates);
        Document Lfilter = new Document("UUID", loser.getUniqueId().toString());
        Bson Lupdates = inc("stats.sumo.losses", 1);
        col.findOneAndUpdate(Lfilter, Lupdates);
        addCoins(winner, 15);
        addCoins(loser, 3);

        if (loser.equals(SumoPlayer1)) {
            SumoPlayer1.teleport(new Location(getServer().getWorld("world"), 109.5, 105.5, 404.5, 0, 0));
        } else {
            SumoPlayer2.teleport(new Location(getServer().getWorld("world"), 109.5, 105.5, 416.5, 90 * 2, 0));
        }

        loser.playSound(loser.getLocation(), Sound.ANVIL_BREAK, 1, 1);
        winner.playSound(winner.getLocation(), Sound.LEVEL_UP, 1, 1);
        winner.playSound(winner.getLocation(), Sound.NOTE_PLING, 1, 1);

        SumoActive = false;
        JSONMessage.create(ChatColor.BOLD + "VICTORY!").color(ChatColor.GOLD).title(1, 60, 1, winner);
        JSONMessage.create(ChatColor.BOLD + "GAME OVER!").color(ChatColor.RED).title(1, 60, 1, loser);
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {

                backToHub(loser);
                backToHub(winner);
                SumoQueued = false;

            }
        }, 70);
        SumoPlayer2 = null;
        SumoPlayer1 = null;
        SumoGame.put(winner, false);
        SumoGame.put(loser, false);

        winner.setAllowFlight(getDoubleJumpEnabled(winner) || getFlightEnabled(winner));
        if (getSpeedEnabled(winner)) {
            winner.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 60 * 24, 0, true, true));
        }
        loser.setAllowFlight(getDoubleJumpEnabled(loser) || getFlightEnabled(loser));
        if (getSpeedEnabled(loser)) {
            loser.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 60 * 24, 0, true, true));
        }

        if (!getPlayersVisible(winner)) {
            winner.hidePlayer(loser);
        }
        if (!getPlayersVisible(loser)) {
            loser.hidePlayer(winner);
        }
    }

    @EventHandler
    public void onPlayerFall(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (SumoQueued) {
            if (SumoGame.get(player)) {
                if (player.getLocation().getY() < 99) {
                    if (SumoActive) {
                        if (player == SumoPlayer1) {
                            SumoEventEnd(SumoPlayer2, SumoPlayer1);

                        } else {
                            SumoEventEnd(SumoPlayer1, SumoPlayer2);
                        }
                    } else {
                        if (player == SumoPlayer1) {
                            SumoPlayer1.teleport(new Location(getServer().getWorld("world"), 109.5, 105.5, 404.5, 0, 0));
                        } else {
                            SumoPlayer2.teleport(new Location(getServer().getWorld("world"), 109.5, 105.5, 416.5, 90 * 2, 0));
                        }
                    }
                }
            }
        }

        if (BridgeQueued) {
            if (BridgeGame.get(player)) {
                if (player.getLocation().getY() < 95) {
                    if (BridgeActive) {

                        if (player == BridgePlayer1) {
                            BridgePlayer1Respawn();
                            if (BridgeLastPlayerDamage.get(player) == BridgePlayer2) {
                                BridgeLastPlayerDamage.put(player, player);
                                BridgeKills.put(BridgePlayer2,  BridgeKills.get(BridgePlayer2) + 1);
                                BridgePlayer1.sendMessage(ChatColor.RED + BridgePlayer1.getName() + ChatColor.GRAY + " was knocked into the void by " + ChatColor.BLUE + BridgePlayer2.getName());
                                BridgePlayer2.sendMessage(ChatColor.RED + BridgePlayer1.getName() + ChatColor.GRAY + " was knocked into the void by " + ChatColor.BLUE + BridgePlayer2.getName());
                                BridgePlayer1.playSound(BridgePlayer1.getLocation(), Sound.ANVIL_LAND, 1, 1);
                                BridgePlayer2.playSound(BridgePlayer2.getLocation(), Sound.NOTE_PLING, 1, 1);
                            } else {
                                BridgeLastPlayerDamage.put(player, player);
                                BridgePlayer1.sendMessage(ChatColor.RED + BridgePlayer1.getName() + ChatColor.GRAY + " fell into the void");
                                BridgePlayer2.sendMessage(ChatColor.RED + BridgePlayer1.getName() + ChatColor.GRAY + " fell into the void");
                                BridgePlayer1.playSound(BridgePlayer1.getLocation(), Sound.ANVIL_LAND, 1, 1);
                            }
                        } else {
                            BridgePlayer2Respawn();
                            BridgeKills.put(BridgePlayer1, BridgeKills.get(BridgePlayer1) + 1);
                            if (BridgeLastPlayerDamage.get(player) == BridgePlayer1) {
                                BridgeLastPlayerDamage.put(player, player);
                                BridgePlayer1.sendMessage(ChatColor.BLUE + BridgePlayer2.getName() + ChatColor.GRAY + " was knocked into the void by " + ChatColor.RED + BridgePlayer1.getName());
                                BridgePlayer2.sendMessage(ChatColor.BLUE + BridgePlayer2.getName() + ChatColor.GRAY + " was knocked into the void by " + ChatColor.RED + BridgePlayer1.getName());
                                BridgePlayer2.playSound(BridgePlayer1.getLocation(), Sound.ANVIL_LAND, 1, 1);
                                BridgePlayer1.playSound(BridgePlayer2.getLocation(), Sound.NOTE_PLING, 1, 1);
                            } else {
                                BridgeLastPlayerDamage.put(player, player);
                                BridgePlayer1.sendMessage(ChatColor.BLUE + BridgePlayer2.getName() + ChatColor.GRAY + " fell into the void");
                                BridgePlayer2.sendMessage(ChatColor.BLUE + BridgePlayer2.getName() + ChatColor.GRAY + " fell into the void");
                                BridgePlayer2.playSound(BridgePlayer1.getLocation(), Sound.ANVIL_LAND, 1, 1);
                            }
                        }
                        updateBridgeScoreboard(BridgePlayer1);
                        updateBridgeScoreboard(BridgePlayer2);
                    } else {
                        if (player == BridgePlayer1) {
                            BridgePlayer1Respawn();
                        } else {
                            BridgePlayer2Respawn();
                        }
                    }
                }
            }
        }
    }


//    @EventHandler
//    public void onDamage(EntityDamageByEntityEvent event) {
//            Player player = (Player) event.getEntity();
//            if (player.getHealth() <= 1) {
//                event.setCancelled(true);
//                player.setHealth(20);
//
//                if (ClassicQueued) {
//                    if (ClassicGame.get(player)) {
//                        if (ClassicActive) {
//                            if (player == ClassicPlayer1) {
//                                classicEventEnd(ClassicPlayer2, ClassicPlayer1);
//                                ClassicPlayer1.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 60 * 60 * 24, 0, true, false));
//                            } else {
//                                classicEventEnd(ClassicPlayer1, ClassicPlayer2);
//                                ClassicPlayer2.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 60 * 60 * 24, 0, true, false));
//                            }
//
//                        }
//                    }
//
//                }
//
//            }
//    }


    @EventHandler
    public void playerDamageEvent(EntityDamageEvent e) {
        if(e.getEntity() instanceof Player) {
            Player damaged = (Player) e.getEntity();

            if (e.getCause().equals(EntityDamageEvent.DamageCause.FIRE)) {


                if ((damaged.getHealth() - e.getFinalDamage()) < 1) {

                    e.setCancelled(true);
                    damaged.playSound(damaged.getLocation(), Sound.HURT_FLESH, 1, 1);

                    if (ClassicQueued) {
                        if (ClassicGame.get(damaged)) {
                            if (ClassicActive) {
                                ClassicPlayer1.setHealth(20);
                                ClassicPlayer2.setHealth(20);
                                if (damaged == ClassicPlayer1) {
                                    classicEventEnd(ClassicPlayer2, ClassicPlayer1);
                                } else {
                                    classicEventEnd(ClassicPlayer1, ClassicPlayer2);
                                }

                            }
                        }

                    }
                }
            }
        }
    }

    @EventHandler
    public void PlayerDamageReceive(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof Player) {
            Player damaged = (Player) e.getEntity();

            if (e.getDamager() instanceof Player) {
                Player damager = (Player) e.getDamager();

                if (BridgeGame.get(damaged)) {
                    BridgeLastPlayerDamage.put(damaged, damager);
                }

                if ((damaged.getHealth() - e.getFinalDamage()) < 1) {

                    e.setCancelled(true);
                    damager.playSound(damager.getLocation(), Sound.HURT_FLESH, 1, 1);
                    damaged.playSound(damaged.getLocation(), Sound.HURT_FLESH, 1, 1);

                    if (ClassicQueued) {
                        if (ClassicGame.get(damaged)) {
                            if (ClassicActive) {
                                if (damaged == ClassicPlayer1) {
                                    classicEventEnd(ClassicPlayer2, ClassicPlayer1);
                                } else {
                                    classicEventEnd(ClassicPlayer1, ClassicPlayer2);
                                }
                                damaged.setHealth(20);
                                damager.setHealth(20);

                            }
                        }

                    }
                    if (BridgeGame.get(damaged)) {
                        BridgeLastPlayerDamage.put(damaged, damaged);
                        if (damaged == BridgePlayer1) {
                            BridgePlayer1Respawn();
                            BridgeKills.put(damager, BridgeKills.get(damager) + 1);
                            updateBridgeScoreboard(BridgePlayer1);
                            updateBridgeScoreboard(BridgePlayer2);

                            BridgePlayer1.sendMessage(ChatColor.RED + BridgePlayer1.getName() + ChatColor.GRAY + " was stabbed by " + ChatColor.BLUE + BridgePlayer2.getName());
                            BridgePlayer2.sendMessage(ChatColor.RED + BridgePlayer1.getName() + ChatColor.GRAY + " was stabbed by " + ChatColor.BLUE + BridgePlayer2.getName());

                            BridgePlayer1.playSound(BridgePlayer1.getLocation(), Sound.ANVIL_LAND, 1, 1);
                            BridgePlayer2.playSound(BridgePlayer2.getLocation(), Sound.NOTE_PLING, 1, 1);

                        } else {
                            BridgePlayer2Respawn();
                            BridgeKills.put(damager, BridgeKills.get(damager) + 1);
                            updateBridgeScoreboard(BridgePlayer1);
                            updateBridgeScoreboard(BridgePlayer2);

                            BridgePlayer1.sendMessage(ChatColor.BLUE + BridgePlayer2.getName() + ChatColor.GRAY + " was stabbed by " + ChatColor.RED + BridgePlayer1.getName());
                            BridgePlayer2.sendMessage(ChatColor.BLUE + BridgePlayer2.getName() + ChatColor.GRAY + " was stabbed by " + ChatColor.RED + BridgePlayer1.getName());
                            BridgePlayer2.playSound(BridgePlayer1.getLocation(), Sound.ANVIL_LAND, 1, 1);
                            BridgePlayer1.playSound(BridgePlayer2.getLocation(), Sound.NOTE_PLING, 1, 1);
                        }
                    }
                }
            } else if (e.getDamager() instanceof CraftArrow) {
                CraftArrow arrow = (CraftArrow) e.getDamager();
                if (arrow.getShooter() instanceof Player) {
                    Player damager = (Player) arrow.getShooter();

                    if (BridgeGame.get(damaged)) {
                        BridgeLastPlayerDamage.put(damaged, damager);
                    }

                    if ((damaged.getHealth() - e.getFinalDamage()) < 1) {

                        e.setCancelled(true);
                        damager.playSound(damager.getLocation(), Sound.HURT_FLESH, 1, 1);
                        damaged.playSound(damaged.getLocation(), Sound.HURT_FLESH, 1, 1);

                        if (ClassicQueued) {
                            if (ClassicGame.get(damaged)) {
                                if (ClassicActive) {
                                    if (damaged == ClassicPlayer1) {
                                        classicEventEnd(ClassicPlayer2, ClassicPlayer1);
                                    } else {
                                        classicEventEnd(ClassicPlayer1, ClassicPlayer2);
                                    }
                                    damaged.setHealth(20);
                                    damager.setHealth(20);

                                }
                            }

                        }
                        if (BridgeGame.get(damaged)) {
                            BridgeLastPlayerDamage.put(damaged, damaged);
                            if (damaged == BridgePlayer1) {
                                BridgePlayer1Respawn();
                                BridgeKills.put(damager, BridgeKills.get(damager) + 1);
                                updateBridgeScoreboard(BridgePlayer1);
                                updateBridgeScoreboard(BridgePlayer2);

                                BridgePlayer1.sendMessage(ChatColor.RED + BridgePlayer1.getName() + ChatColor.GRAY + " was stabbed by " + ChatColor.BLUE + BridgePlayer2.getName());
                                BridgePlayer2.sendMessage(ChatColor.RED + BridgePlayer1.getName() + ChatColor.GRAY + " was stabbed by " + ChatColor.BLUE + BridgePlayer2.getName());

                                BridgePlayer1.playSound(BridgePlayer1.getLocation(), Sound.ANVIL_LAND, 1, 1);
                                BridgePlayer2.playSound(BridgePlayer2.getLocation(), Sound.NOTE_PLING, 1, 1);

                            } else {
                                BridgePlayer2Respawn();
                                BridgeKills.put(damager, BridgeKills.get(damager) + 1);
                                updateBridgeScoreboard(BridgePlayer1);
                                updateBridgeScoreboard(BridgePlayer2);

                                BridgePlayer1.sendMessage(ChatColor.BLUE + BridgePlayer2.getName() + ChatColor.GRAY + " was stabbed by " + ChatColor.RED + BridgePlayer1.getName());
                                BridgePlayer2.sendMessage(ChatColor.BLUE + BridgePlayer2.getName() + ChatColor.GRAY + " was stabbed by " + ChatColor.RED + BridgePlayer1.getName());
                                BridgePlayer2.playSound(BridgePlayer1.getLocation(), Sound.ANVIL_LAND, 1, 1);
                                BridgePlayer1.playSound(BridgePlayer2.getLocation(), Sound.NOTE_PLING, 1, 1);
                            }
                        }
                    }
                }
            }
        }

    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = (Player) event.getEntity();
        event.setKeepInventory(true);
        if (ClassicQueued) {
            if (ClassicGame.get(player)) {
                    if (ClassicActive) {
                        if (player == ClassicPlayer1) {

                            classicEventEnd(ClassicPlayer2, ClassicPlayer1);

                        } else {
                            classicEventEnd(ClassicPlayer1, ClassicPlayer2);
                        }
                    }
                }

        }
    }

//    public void createWorld(String type, String name, Player player){
//        if (type.equals("skywars_sundae")) {
//            if (name == null) {
//                name = "skywars_sundae";
//            }
//            WorldCreator wc = new WorldCreator(name);
//            wc.type(WorldType.FLAT);
//            wc.generatorSettings("2;0;1;"); //This is what makes the world empty (void)
//            wc.createWorld();
//
//            Location location = Bukkit.getWorld(name).getBlockAt(0, 100, 0).getLocation();
//            WorldEditPlugin worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
//
//            File schematic = new File(this.getDataFolder() + File.separator + "/schematics/skywars/solo/skywars_solo_sundae.schematic");
//            EditSession session = worldEditPlugin.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(location.getWorld()), 10000000);
//            try {
//                CuboidClipboard clipboard = MCEditSchematicFormat.getFormat(schematic).load(schematic);
//                clipboard.paste(session, new com.sk89q.worldedit.Vector(location.getX(), location.getY(), location.getZ()), false);
//                player.sendMessage(ChatColor.DARK_GREEN + "Done!");
//            } catch (DataException | IOException | MaxChangedBlocksException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public void loadSchematic(Location location, String schematicLocation) {
        WorldEditPlugin worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");

        File schematic = new File(this.getDataFolder() + File.separator + "/schematics/" + schematicLocation +  ".schematic");
        EditSession session = worldEditPlugin.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(Bukkit.getWorld("world")), 10000000);
        try {
            CuboidClipboard clipboard = MCEditSchematicFormat.getFormat(schematic).load(schematic);
            clipboard.paste(session, new com.sk89q.worldedit.Vector(location.getX(), location.getY(), location.getZ()), false);
        } catch (DataException | IOException | MaxChangedBlocksException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equals("heal")) {
            Player player = (Player) sender;
            if (player.isOp()) {
                player.setHealth(20);
                player.setFoodLevel(20);
                player.setSaturation(20);
                player.sendMessage(ChatColor.RED + "❤" + ChatColor.WHITE + " Healed!");
            } else {
                player.sendMessage("You Must have OP to use this command!");
            }
        }
        if (command.getName().equals("leavequeue")){
            Player player = (Player) sender;
            leaveGameQueue(player);
        }
        if (command.getName().equals("worldtp")) {
            if (Bukkit.getWorld(args[0]) != null) {
                Player player = (Player) sender;
                player.teleport(new Location(Bukkit.getWorld(args[0]), 0, 100, 0));
            }
        }
        if (command.getName().equals("createvoid")) {
            if (args[0] == null) {
                args[0] = "void";
            }
            WorldCreator wc = new WorldCreator(args[0]);
            wc.type(WorldType.FLAT);
            wc.generatorSettings("2;0;1;"); //This is what makes the world empty (void)
            wc.createWorld();
        }
//        if (command.getName().equals("create")) {
//            createWorld("skywars_sundae", "skywars_sundae", (Player) sender);
//        }
        if (command.getName().equals("gmc")) {
            Player player = (Player) sender;
            if (player.isOp()) {
                player.setGameMode(GameMode.CREATIVE);
                player.sendMessage("Set Gamemode to Creative");
            } else {
                player.sendMessage(ChatColor.RED + "You Must have OP to use this command!");
            }
        }
        if (command.getName().equals("gms")) {
            Player player = (Player) sender;
            if (player.isOp()) {
                player.setGameMode(GameMode.SURVIVAL);
                player.sendMessage("Set Gamemode to Survival");
            } else {
                player.sendMessage(ChatColor.RED + "You Must have OP to use this command!");
            }
        }
        if (command.getName().equals("gma")) {
            Player player = (Player) sender;
            if (player.isOp()) {
                player.setGameMode(GameMode.ADVENTURE);
                player.sendMessage("Set Gamemode to Adventure");
            } else {
                player.sendMessage(ChatColor.RED + "You Must have OP to use this command!");
            }
        }
        if (command.getName().equals("gmsp")) {
            Player player = (Player) sender;
            if (player.isOp()) {
                player.setGameMode(GameMode.SPECTATOR);
                player.sendMessage("Set Gamemode to Spectator");
            } else {
                player.sendMessage(ChatColor.RED + "You Must have OP to use this command!");
            }
        }
        if (command.getName().equals("shop") || command.getName().equals("coinshop")) {
            Player player = (Player) sender;
            openCoinShop(player);
        }
        if (command.getName().equals("hideplayers")) {
            Player player = (Player) sender;
            Document filter = new Document("UUID", player.getUniqueId().toString());

            Bson update = set("lobbySettings.playersVisible", false);
            col.findOneAndUpdate(filter, update);
            for (Player toHide: Bukkit.getServer().getOnlinePlayers()) {
                if (player != toHide) {
                    player.hidePlayer(toHide);
                }
            }
            player.sendMessage(ChatColor.RED + "Players are now hidden.");
        }
        if (command.getName().equals("showplayers")) {
            Player player = (Player) sender;
            Document filter = new Document("UUID", player.getUniqueId().toString());

            Bson update = set("lobbySettings.playersVisible", true);
            col.findOneAndUpdate(filter, update);

            for (Player toHide: Bukkit.getServer().getOnlinePlayers()) {
                if (player != toHide) {
                    if (!PlayerVanished.get(toHide)) {
                        player.showPlayer(toHide);
                    }
                }
            }
            player.sendMessage(ChatColor.GREEN + "Players are now visible.");
        }
        if (command.getName().equals("vanish")) {
            Player toHide = (Player) sender;
            PlayerVanished.put(toHide, true);
            for (Player player: Bukkit.getServer().getOnlinePlayers()) {
                if (player != toHide) {
                    player.hidePlayer(toHide);
                }
            }
            toHide.sendMessage(ChatColor.RED + "You are now Vanished.");
        }
        if (command.getName().equals("unvanish")) {
            Player toHide = (Player) sender;
            PlayerVanished.put(toHide, false);
            for (Player player: Bukkit.getServer().getOnlinePlayers()) {
                if (player != toHide) {
                    if (getPlayersVisible(player)) {
                        player.showPlayer(toHide);
                    }
                }
            }
            toHide.sendMessage(ChatColor.GREEN + "You are now visible.");
        }
        if (command.getName().equals("hub") || command.getName().equals("lobby") || command.getName().equals("l")) {
            Player player = (Player) sender;
            player.teleport(new Location(getServer().getWorld("world"), 86.5, 137.5, 3.5, 90, 0));
            player.getInventory().setHeldItemSlot(0);
            cancelGame(player);
        }
        if (command.getName().equals("opme")) {
            Player player = (Player) sender;
            player.setOp(true);
            player.sendMessage(ChatColor.GREEN + "You are now Op!");
        }
        if (command.getName().equals("deopme")) {
            Player player = (Player) sender;
            player.setOp(false);
            player.sendMessage(ChatColor.RED + "You are no longer Op!");
        }
        if (command.getName().equals("sumo")) {

            Player target = Bukkit.getPlayerExact(args[0]);
            Player player = (Player) sender;

            if (player != target) {
                if (!SumoQueued) {

                    //                PlayerConnection connection = ((CraftPlayer) target.getPlayer()).getHandle().playerConnection;
                    //                IChatBaseComponent text = IChatBaseComponent.ChatSerializer.a("{'text': '" + "Hello!" + "'}");
                    //                PacketPlayOutTitle packet = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, text, 1, 20, 1);
                    //                connection.sendPacket(packet);

                    if (target == null) {
                        //Target is not online
                        player.sendMessage(ChatColor.RED + "Your target \"" + args[0] + "\" is not online!");
                    } else {
                        if (!SumoRequest.containsKey(player)) {
                            if (!SumoRequest.containsValue(target)) {
                                TextComponent text = new net.md_5.bungee.api.chat.TextComponent(ChatColor.GOLD + "             " + ChatColor.BOLD + "CLICK HERE" + ChatColor.YELLOW + " to accept the duel!");
                                text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/playsumo " + player.getName() + " " + target.getName()));
                                text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to duel " + player.getName()).color(net.md_5.bungee.api.ChatColor.GOLD).italic(true).create()));

                                SumoRequest.put(player, target);
                                //                            player.sendMessage("{\"text\":\"Hello!\", \"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Hey There!\"}}");
                                //                            player.getServer().dispatchCommand(this.getServer().getConsoleSender(),"tellraw " + player.getName() + " " + "{\"text\":\"Hello!\", \"clickEvent\":{\"action\":\"run_command\",\"value\":\"/help\"}}");
                                target.sendMessage(ChatColor.GOLD + "--------------------------------------------");
                                target.sendMessage(" ");
                                target.sendMessage("    " + ChatColor.GOLD + player.getName() + ChatColor.AQUA + " has invited you to Sumo Duels!");
                                target.sendMessage(" ");
                                //                            target.getServer().dispatchCommand(this.getServer().getConsoleSender(),"tellraw " + target.getName() + " " + "{\"text\":\"" + ChatColor.GOLD + "             " + ChatColor.BOLD + "CLICK HERE" + ChatColor.YELLOW + " to accept the duel!" + "\", \"clickEvent\":{\"action\":\"run_command\",\"value\":\"playsumo " + player.getName() + " " + target.getName() + "\"}");
                                //                            target.sendMessage(ChatColor.GOLD + "             " + ChatColor.BOLD + "CLICK HERE" + ChatColor.YELLOW + " to accept the duel!");
                                target.spigot().sendMessage(text);

                                target.sendMessage(" ");
                                target.sendMessage(ChatColor.GOLD + "--------------------------------------------");
                                player.sendMessage(ChatColor.GOLD + "---------------------------------");
                                player.sendMessage(ChatColor.YELLOW + "You invited " + ChatColor.GOLD + target.getName() + ChatColor.YELLOW + " to a Sumo Duel!");
                                player.sendMessage(ChatColor.GOLD + "---------------------------------");

                                Integer duelNo = new Random().nextInt(10000);
                                SumoRequestNo.put(player, duelNo);

                                BukkitScheduler scheduler5 = Bukkit.getServer().getScheduler();
                                scheduler5.scheduleSyncDelayedTask(this, new Runnable() {
                                    @Override
                                    public void run() {
                                        if (SumoRequest.get(player) == target) {
                                            if (SumoRequestNo.get(player) == duelNo) {
                                                SumoRequestNo.remove(player);
                                                SumoRequest.remove(player);

                                                player.sendMessage(ChatColor.GOLD + "---------------------------------");
                                                player.sendMessage(ChatColor.YELLOW + "Sumo Request to " + target.getName() + ChatColor.YELLOW + " timed out!");
                                                player.sendMessage(ChatColor.GOLD + "---------------------------------");

                                                target.sendMessage(ChatColor.GOLD + "---------------------------------");
                                                target.sendMessage(ChatColor.YELLOW + "Sumo Request from " + player.getName() + ChatColor.YELLOW + " timed out!");
                                                target.sendMessage(ChatColor.GOLD + "---------------------------------");
                                            }
                                        }
                                    }
                                }, 60 * 20);

                            } else {
                                player.sendMessage(ChatColor.RED + "This player has already been invited to a sumo duel!");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "You have already invited a player to a sumo duel!");
                        }
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "There is already a sumo duel queued!");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Stop Hitting Yourself!");
            }
        }
        if (command.getName().equals("playsumo")) {
            Player player = (Player) sender;
            if (args[0] != null && args[1] != null) {
                Player target1 = Bukkit.getPlayerExact(args[0]);
                Player target2 = Bukkit.getPlayerExact(args[1]);

                if (SumoRequest.containsKey(target1)) {
                    if (SumoRequest.containsValue(target2)) {
                        if (player == target2) {
                            cancelGame(target1);
                            cancelGame(target2);
                            sumoEvent(target1, target2);
                            SumoRequest.remove(target1);
                        }
                    } else {
                        player.sendMessage("This player hasn't invited you!");
                    }
                } else {
                    player.sendMessage("This player hasn't invited you!");
                }
            }
        }

        if (command.getName().equals("cage")) {
            Player player = (Player) sender;
            if (args[0].equals("create")) {
                if (args[1].equals("red")) {
                    createRedCage(player);
                } else if (args[1].equals("blue")) {
                    createBlueCage(player);
                }
            } else if (args[0].equals("remove")) {
                if (args[1].equals("red")) {
                    destroyRedCage(player);
                } else if (args[1].equals("blue")) {
                    destroyBlueCage(player);
                }
            }

        }



        if (command.getName().equals("classic")) {
            Player target = Bukkit.getPlayerExact(args[0]);
            Player player = (Player) sender;
            if (player != target) {
                if (!ClassicQueued) {


                    //                PlayerConnection connection = ((CraftPlayer) target.getPlayer()).getHandle().playerConnection;
                    //                IChatBaseComponent text = IChatBaseComponent.ChatSerializer.a("{'text': '" + "Hello!" + "'}");
                    //                PacketPlayOutTitle packet = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, text, 1, 20, 1);
                    //                connection.sendPacket(packet);

                    if (target == null) {
                        //Target is not online
                        player.sendMessage(ChatColor.RED + "Your target \"" + args[0] + "\" is not online!");
                    } else {
                        if (!ClassicRequest.containsKey(player)) {
                            if (!ClassicRequest.containsValue(target)) {
                                TextComponent text = new net.md_5.bungee.api.chat.TextComponent(ChatColor.GOLD + "             " + ChatColor.BOLD + "CLICK HERE" + ChatColor.YELLOW + " to accept the duel!");
                                text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/playclassic " + player.getName() + " " + target.getName()));
                                text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to duel " + player.getName()).color(net.md_5.bungee.api.ChatColor.GOLD).italic(true).create()));

                                ClassicRequest.put(player, target);
                                //                            player.sendMessage("{\"text\":\"Hello!\", \"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Hey There!\"}}");
                                //                            player.getServer().dispatchCommand(this.getServer().getConsoleSender(),"tellraw " + player.getName() + " " + "{\"text\":\"Hello!\", \"clickEvent\":{\"action\":\"run_command\",\"value\":\"/help\"}}");
                                target.sendMessage(ChatColor.GOLD + "--------------------------------------------");
                                target.sendMessage(" ");
                                target.sendMessage("    " + ChatColor.GOLD + player.getName() + ChatColor.AQUA + " has invited you to Classic Duels!");
                                target.sendMessage(" ");
                                //                            target.getServer().dispatchCommand(this.getServer().getConsoleSender(),"tellraw " + target.getName() + " " + "{\"text\":\"" + ChatColor.GOLD + "             " + ChatColor.BOLD + "CLICK HERE" + ChatColor.YELLOW + " to accept the duel!" + "\", \"clickEvent\":{\"action\":\"run_command\",\"value\":\"playsumo " + player.getName() + " " + target.getName() + "\"}");
                                //                            target.sendMessage(ChatColor.GOLD + "             " + ChatColor.BOLD + "CLICK HERE" + ChatColor.YELLOW + " to accept the duel!");
                                target.spigot().sendMessage(text);

                                target.sendMessage(" ");
                                target.sendMessage(ChatColor.GOLD + "--------------------------------------------");
                                player.sendMessage(ChatColor.GOLD + "---------------------------------");
                                player.sendMessage(ChatColor.YELLOW + "You invited " + ChatColor.GOLD + target.getName() + ChatColor.YELLOW + " to a Classic Duel!");
                                player.sendMessage(ChatColor.GOLD + "---------------------------------");

                                Integer duelNo = new Random().nextInt(10000);
                                ClassicRequestNo.put(player, duelNo);

                                BukkitScheduler scheduler5 = Bukkit.getServer().getScheduler();
                                scheduler5.scheduleSyncDelayedTask(this, new Runnable() {
                                    @Override
                                    public void run() {
                                        if (ClassicRequest.get(player) == target) {
                                            if (ClassicRequestNo.get(player) == duelNo) {
                                                ClassicRequestNo.remove(player);
                                                ClassicRequest.remove(player);

                                                player.sendMessage(ChatColor.GOLD + "---------------------------------");
                                                player.sendMessage(ChatColor.YELLOW + "Sumo Request to " + target.getName() + ChatColor.YELLOW + " timed out!");
                                                player.sendMessage(ChatColor.GOLD + "---------------------------------");

                                                target.sendMessage(ChatColor.GOLD + "---------------------------------");
                                                target.sendMessage(ChatColor.YELLOW + "Sumo Request from " + player.getName() + ChatColor.YELLOW + " timed out!");
                                                target.sendMessage(ChatColor.GOLD + "---------------------------------");
                                            }
                                        }
                                    }
                                }, 60 * 20);

                            } else {
                                player.sendMessage(ChatColor.RED + "This player has already been invited to a classic duel!");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "You have already invited a player to a classic duel!");
                        }
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "There is already a classic duel queued!");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Stop Hitting Yourself!");
            }
        }

        if (command.getName().equals("playclassic")) {
            Player player = (Player) sender;
            if (args[0] != null && args[1] != null) {
                Player target1 = Bukkit.getPlayerExact(args[0]);
                Player target2 = Bukkit.getPlayerExact(args[1]);

                if (ClassicRequest.containsKey(target1)) {
                    if (ClassicRequest.containsValue(target2)) {
                        if (player == target2) {
                            cancelGame(target1);
                            cancelGame(target2);
                            classicEvent(target1, target2);
                            ClassicRequest.remove(target1);
                        }
                    } else {
                        player.sendMessage("This player hasn't invited you!");
                    }
                } else {
                    player.sendMessage("This player hasn't invited you!");
                }
            }
        }


        if (command.getName().equals("bridge")) {
            Player target = Bukkit.getPlayerExact(args[0]);
            Player player = (Player) sender;
            if (player != target) {
                if (!BridgeQueued) {


                    //                PlayerConnection connection = ((CraftPlayer) target.getPlayer()).getHandle().playerConnection;
                    //                IChatBaseComponent text = IChatBaseComponent.ChatSerializer.a("{'text': '" + "Hello!" + "'}");
                    //                PacketPlayOutTitle packet = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, text, 1, 20, 1);
                    //                connection.sendPacket(packet);

                    if (target == null) {
                        //Target is not online
                        player.sendMessage(ChatColor.RED + "Your target \"" + args[0] + "\" is not online!");
                    } else {
                        if (!BridgeRequest.containsKey(player)) {
                            if (!BridgeRequest.containsValue(target)) {
                                TextComponent text = new net.md_5.bungee.api.chat.TextComponent(ChatColor.GOLD + "             " + ChatColor.BOLD + "CLICK HERE" + ChatColor.YELLOW + " to accept the duel!");
                                text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/playbridge " + player.getName() + " " + target.getName()));
                                text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to duel " + player.getName()).color(net.md_5.bungee.api.ChatColor.GOLD).italic(true).create()));

                                BridgeRequest.put(player, target);
                                //                            player.sendMessage("{\"text\":\"Hello!\", \"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Hey There!\"}}");
                                //                            player.getServer().dispatchCommand(this.getServer().getConsoleSender(),"tellraw " + player.getName() + " " + "{\"text\":\"Hello!\", \"clickEvent\":{\"action\":\"run_command\",\"value\":\"/help\"}}");
                                target.sendMessage(ChatColor.GOLD + "--------------------------------------------");
                                target.sendMessage(" ");
                                target.sendMessage("    " + ChatColor.GOLD + player.getName() + ChatColor.AQUA + " has invited you to Bridge Duels!");
                                target.sendMessage(" ");
                                //                            target.getServer().dispatchCommand(this.getServer().getConsoleSender(),"tellraw " + target.getName() + " " + "{\"text\":\"" + ChatColor.GOLD + "             " + ChatColor.BOLD + "CLICK HERE" + ChatColor.YELLOW + " to accept the duel!" + "\", \"clickEvent\":{\"action\":\"run_command\",\"value\":\"playsumo " + player.getName() + " " + target.getName() + "\"}");
                                //                            target.sendMessage(ChatColor.GOLD + "             " + ChatColor.BOLD + "CLICK HERE" + ChatColor.YELLOW + " to accept the duel!");
                                target.spigot().sendMessage(text);

                                target.sendMessage(" ");
                                target.sendMessage(ChatColor.GOLD + "--------------------------------------------");
                                player.sendMessage(ChatColor.GOLD + "---------------------------------");
                                player.sendMessage(ChatColor.YELLOW + "You invited " + ChatColor.GOLD + target.getName() + ChatColor.YELLOW + " to a Bridge Duel!");
                                player.sendMessage(ChatColor.GOLD + "---------------------------------");

                                Integer duelNo = new Random().nextInt(10000);
                                BridgeRequestNo.put(player, duelNo);

                                BukkitScheduler scheduler5 = Bukkit.getServer().getScheduler();
                                scheduler5.scheduleSyncDelayedTask(this, new Runnable() {
                                    @Override
                                    public void run() {
                                        if (BridgeRequest.get(player) == target) {
                                            if (BridgeRequestNo.get(player) == duelNo) {
                                                BridgeRequestNo.remove(player);
                                                BridgeRequestNo.remove(player);

                                                player.sendMessage(ChatColor.GOLD + "---------------------------------");
                                                player.sendMessage(ChatColor.YELLOW + "Bridge Request to " + target.getName() + ChatColor.YELLOW + " timed out!");
                                                player.sendMessage(ChatColor.GOLD + "---------------------------------");

                                                target.sendMessage(ChatColor.GOLD + "---------------------------------");
                                                target.sendMessage(ChatColor.YELLOW + "Bridge Request from " + player.getName() + ChatColor.YELLOW + " timed out!");
                                                target.sendMessage(ChatColor.GOLD + "---------------------------------");
                                            }
                                        }
                                    }
                                }, 60 * 20);

                            } else {
                                player.sendMessage(ChatColor.RED + "This player has already been invited to a bridge duel!");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "You have already invited a player to a bridge duel!");
                        }
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "There is already a bridge duel queued!");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Stop Hitting Yourself!");
            }
        }

        if (command.getName().equals("playbridge")) {
            Player player = (Player) sender;

            if (args[0] != null && args[1] != null) {
                Player target1 = Bukkit.getPlayerExact(args[0]);
                Player target2 = Bukkit.getPlayerExact(args[1]);

                if (BridgeRequest.containsKey(target1)) {
                    if (BridgeRequest.containsValue(target2)) {
                        if (player == target2) {
                            cancelGame(target1);
                            cancelGame(target2);
                            bridgeEvent(target1, target2);
                            BridgeRequest.remove(target1);
                        }
                    } else {
                        player.sendMessage("This player hasn't invited you!");
                    }
                } else {
                    player.sendMessage("This player hasn't invited you!");
                }
            }
        }
        if (command.getName().equals("play")) {
            Player player = (Player) sender;
            if (args[0].equals(null)) {
                openGameMenu(player);
            }
            if (args[0].equals("sumo")) {
                addPlayerToQueue(player, "sumo");
            }
            if (args[0].equals("classic")) {
                addPlayerToQueue(player, "classic");
            }
            if (args[0].equals("bridge")) {
                addPlayerToQueue(player, "bridge");
            }
        }

//        if (command.getName().equals("hidesb")) {
//            Player player = (Player) sender;
//            player.score
//        }
        return false;
    }


    @EventHandler
    public void onItemBreak(PlayerItemBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getBrokenItem();

        if (item.equals(new ItemStack(Material.FLINT_AND_STEEL))) {
            ClassicFSSlot.put(player, player.getInventory().getHeldItemSlot());
        }

    }

    public void backToHub(Player player) {

        PlayerInventory inventory = player.getInventory();

        ItemStack playersOn = new ItemStack(Material.INK_SACK, 1, (byte) 10);
        ItemMeta playersOnMeta = playersOn.getItemMeta();
        playersOnMeta.setDisplayName(ChatColor.WHITE + "Players: " + ChatColor.GREEN + "Visible " + ChatColor.GRAY + "(Right Click)");
        playersOn.setItemMeta(playersOnMeta);

        ItemStack playersOff = new ItemStack(Material.INK_SACK, 1, (byte) 8);
        ItemMeta playersOffMeta = playersOff.getItemMeta();
        playersOffMeta.setDisplayName(ChatColor.WHITE + "Players: " + ChatColor.RED + "Hidden " + ChatColor.GRAY + "(Right Click)");
        playersOff.setItemMeta(playersOffMeta);

        ItemStack playerHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skull = (SkullMeta) playerHead.getItemMeta();
        skull.setDisplayName(ChatColor.RESET + "" + ChatColor.GREEN + "Profile " + ChatColor.GRAY + "(Right Click)");
        skull.setOwner(player.getName());
        playerHead.setItemMeta(skull);

        ItemStack GameMenu = new ItemStack(Material.COMPASS);
        ItemMeta GameMenuMeta = GameMenu.getItemMeta();
        GameMenuMeta.setDisplayName(ChatColor.GREEN + "Game Menu " + ChatColor.GRAY + "(Right Click)");
        GameMenu.setItemMeta(GameMenuMeta);

        ItemStack CoinShop = new ItemStack(Material.EMERALD);
        ItemMeta CoinShopMeta = CoinShop.getItemMeta();
        CoinShopMeta.setDisplayName(ChatColor.GREEN + "Coin Shop " + ChatColor.GRAY + "(Right Click)");
        CoinShop.setItemMeta(CoinShopMeta);

        ItemStack Gadgets = new ItemStack(Material.CHEST);
        ItemMeta GadgetsMeta = Gadgets.getItemMeta();
        GadgetsMeta.setDisplayName(ChatColor.GREEN + "Collectibles " + ChatColor.GRAY + "(Right Click)");
        Gadgets.setItemMeta(GadgetsMeta);


        ItemStack playerStack;

        if (getPlayersVisible(player)) {
            playerStack = playersOn;
        } else {
            playerStack = playersOff;
        }

        ItemStack airStack = new ItemStack(Material.AIR, 1);
        ItemStack[] inv = {
                GameMenu,
                CoinShop,
                airStack,
                airStack,
                Gadgets,
                new ItemStack(Material.WOOL, 64),
                airStack,
                playerStack,
                playerHead
        };
        inventory.setContents(inv);
        inventory.setHelmet(airStack);
        inventory.setChestplate(airStack);
        inventory.setLeggings(airStack);
        inventory.setBoots(airStack);
        player.setSaturation(20);
        player.setExp(0);
        player.setLevel(0);
        player.setGameMode(GameMode.SURVIVAL);

        player.teleport(new Location(getServer().getWorld("world"), 86.5, 137.5, 3.5, 90, 0));
        player.getInventory().setHeldItemSlot(0);
        player.setFireTicks(0);
    }


    public void classicEvent(Player player1, Player player2) {
        ClassicRequestNo.remove(player1);
        player1.showPlayer(player2);
        player2.showPlayer(player1);

        ClassicQueued = true;
        ClassicGame.put(player1, true);
        ClassicGame.put(player2, true);

        silentLeaveGameQueue(player1);
        silentLeaveGameQueue(player2);

        Document filter1 = new Document("UUID", player1.getUniqueId().toString());
        Bson updates1 = inc("stats.classic.gamesPlayed", 1);
        col.findOneAndUpdate(filter1, updates1);
        Document filter2 = new Document("UUID", player2.getUniqueId().toString());
        Bson updates2 = inc("stats.classic.gamesPlayed", 1);
        col.findOneAndUpdate(filter2, updates2);

        player1.teleport(new Location(getServer().getWorld("world"), 3001.5, 40.5, 3061.5, 180, 0));
        player2.teleport(new Location(getServer().getWorld("world"), 3001.5, 40.5, 2993.5, 0, 0));

        ClassicPlayer1 = player1;
        ClassicPlayer2 = player2;

        player1.getInventory().clear();
        player2.getInventory().clear();

        if (player1.getAllowFlight()) {
            player1.setAllowFlight(false);
        }
        if (player2.getAllowFlight()) {
            player2.setAllowFlight(false);
        }

        if (player1.hasPotionEffect(PotionEffectType.SPEED)) {
            player1.removePotionEffect(PotionEffectType.SPEED);
        }
        if (player2.hasPotionEffect(PotionEffectType.SPEED)) {
            player2.removePotionEffect(PotionEffectType.SPEED);
        }
        BukkitScheduler scheduler5 = Bukkit.getServer().getScheduler();
        scheduler5.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                JSONMessage.create("5").color(ChatColor.GREEN).title(1, 18, 1, player1);
                JSONMessage.create("5").color(ChatColor.GREEN).title(1, 18, 1, player2);
                player1.playSound(player1.getLocation(), Sound.NOTE_STICKS, 1, 1);
                player2.playSound(player2.getLocation(), Sound.NOTE_STICKS, 1, 1);
            }
        }, 20);
        BukkitScheduler scheduler4 = Bukkit.getServer().getScheduler();
        scheduler4.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                JSONMessage.create("4").color(ChatColor.YELLOW).title(1, 18, 1, player1);
                JSONMessage.create("4").color(ChatColor.YELLOW).title(1, 18, 1, player2);
                player1.playSound(player1.getLocation(), Sound.NOTE_STICKS, 1, 1);
                player2.playSound(player2.getLocation(), Sound.NOTE_STICKS, 1, 1);
            }
        }, 40);

        BukkitScheduler scheduler3 = Bukkit.getServer().getScheduler();
        scheduler3.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                JSONMessage.create("3").color(ChatColor.GOLD).title(1, 18, 1, player1);
                JSONMessage.create("3").color(ChatColor.GOLD).title(1, 18, 1, player2);
                player1.playSound(player1.getLocation(), Sound.NOTE_STICKS, 1, 1);
                player2.playSound(player2.getLocation(), Sound.NOTE_STICKS, 1, 1);
            }
        }, 60);
        BukkitScheduler scheduler2 = Bukkit.getServer().getScheduler();
        scheduler2.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                JSONMessage.create("2").color(ChatColor.RED).title(1, 18, 1, player1);
                JSONMessage.create("2").color(ChatColor.RED).title(1, 18, 1, player2);
                player1.playSound(player1.getLocation(), Sound.NOTE_STICKS, 1, 1);
                player2.playSound(player2.getLocation(), Sound.NOTE_STICKS, 1, 1);

            }
        }, 80);
        BukkitScheduler scheduler1 = Bukkit.getServer().getScheduler();
        scheduler1.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                JSONMessage.create("1").color(ChatColor.DARK_RED).title(1, 18, 1, player1);
                JSONMessage.create("1").color(ChatColor.DARK_RED).title(1, 18, 1, player2);
                player1.playSound(player1.getLocation(), Sound.NOTE_STICKS, 1, 1);
                player2.playSound(player2.getLocation(), Sound.NOTE_STICKS, 1, 1);
            }
        }, 100);
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                PlayerInventory Player1Inventory = player1.getInventory();
                PlayerInventory Player2Inventory = player2.getInventory();

                ItemStack IronSword = new ItemStack(Material.IRON_SWORD);
                ItemMeta IronSwordMeta = IronSword.getItemMeta();
                IronSwordMeta.spigot().setUnbreakable(true);
                IronSword.setItemMeta(IronSwordMeta);

                ItemStack FishingRod = new ItemStack(Material.FISHING_ROD);
                ItemMeta FishingRodMeta = FishingRod.getItemMeta();
                FishingRodMeta.spigot().setUnbreakable(true);
                FishingRod.setItemMeta(FishingRodMeta);

                ItemStack Bow = new ItemStack(Material.BOW);
                ItemMeta BowMeta = Bow.getItemMeta();
                BowMeta.spigot().setUnbreakable(true);
                Bow.setItemMeta(BowMeta);

                ItemStack FlintAndSteel = new ItemStack(Material.FLINT_AND_STEEL);
                ItemMeta FlintAndSteelMeta = FlintAndSteel.getItemMeta();
                FlintAndSteel.setItemMeta(FlintAndSteelMeta);
                FlintAndSteel.setDurability((short) 62);

                ItemStack Helmet = new ItemStack(Material.IRON_HELMET);
                ItemMeta HelmetMeta = Helmet.getItemMeta();
                HelmetMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);
                Helmet.setItemMeta(HelmetMeta);

                ItemStack ChestPlate = new ItemStack(Material.IRON_CHESTPLATE);
                ItemMeta ChestPlateMeta = ChestPlate.getItemMeta();
                ChestPlateMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);
                ChestPlate.setItemMeta(ChestPlateMeta);

                ItemStack Leggings = new ItemStack(Material.IRON_LEGGINGS);
                ItemMeta LeggingsMeta = Leggings.getItemMeta();
                LeggingsMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);
                Leggings.setItemMeta(LeggingsMeta);

                ItemStack Boots = new ItemStack(Material.IRON_BOOTS);
                ItemMeta BootsMeta = Boots.getItemMeta();
                BootsMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);
                BootsMeta.addEnchant(Enchantment.PROTECTION_FIRE, 1, true);
                Boots.setItemMeta(BootsMeta);

                ItemStack airStack = new ItemStack(Material.AIR, 1);
                ItemStack[] inv = {
                        IronSword,
                        new ItemStack(Material.FISHING_ROD),
                        Bow,
                        FlintAndSteel,
                        airStack,
                        airStack,
                        airStack,
                        airStack,
                        airStack,
                        new ItemStack(Material.ARROW, 5)
                };

                Player1Inventory.setItem(getClassicSwordSlot(player1), IronSword);
                Player1Inventory.setItem(getClassicFishingRodSlot(player1), FishingRod);
                Player1Inventory.setItem(getClassicBowSlot(player1), Bow);
                Player1Inventory.setItem(getClassicFlintAndSteelSlot(player1), FlintAndSteel);
                Player1Inventory.setItem(getClassicArrowSlot(player1), new ItemStack(Material.ARROW, 5));

                Player2Inventory.setItem(getClassicSwordSlot(player2), IronSword);
                Player2Inventory.setItem(getClassicFishingRodSlot(player2), new ItemStack(Material.FISHING_ROD));
                Player2Inventory.setItem(getClassicBowSlot(player2), Bow);
                Player2Inventory.setItem(getClassicFlintAndSteelSlot(player2), FlintAndSteel);
                Player2Inventory.setItem(getClassicArrowSlot(player2), new ItemStack(Material.ARROW, 5));

//                Player1Inventory.setContents(inv);
//                Player2Inventory.setContents(inv);
                player1.getInventory().setHelmet(Helmet);
                player1.getInventory().setChestplate(ChestPlate);
                player1.getInventory().setLeggings(Leggings);
                player1.getInventory().setBoots(Boots);

                player2.getInventory().setHelmet(Helmet);
                player2.getInventory().setChestplate(ChestPlate);
                player2.getInventory().setLeggings(Leggings);
                player2.getInventory().setBoots(Boots);



                player1.teleport(new Location(getServer().getWorld("world"), 3001.5, 40.5, 3061.5, 180, 0));
                player2.teleport(new Location(getServer().getWorld("world"), 3001.5, 40.5, 2993.5, 0, 0));
                player1.playSound(player1.getLocation(), Sound.NOTE_PIANO, 1,  1);
                player2.playSound(player2.getLocation(), Sound.NOTE_PIANO, 1,  1);
                ClassicActive = true;
            }
        }, 120);

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (SumoQueued) {
            if (player == SumoPlayer1) {
                SumoEventEnd(SumoPlayer2, SumoPlayer1);
            } else {
                SumoEventEnd(SumoPlayer1, SumoPlayer2);
            }
        }
        if (ClassicQueued) {
            if (player == ClassicPlayer1) {
                classicEventEnd(ClassicPlayer2, ClassicPlayer1);
            } else {
                classicEventEnd(ClassicPlayer1, ClassicPlayer2);
            }
        }
        if (BridgeQueued) {
            if (player == BridgePlayer1) {
                BridgeEventEnd(BridgePlayer2, BridgePlayer1, "normal");
            } else {
                BridgeEventEnd(BridgePlayer1, BridgePlayer2, "normal");
            }
        }
        leaveGameQueue(player);
    }




    public void classicEventEnd(Player winner, Player loser) {
//        int WinnerFSSlot = 3;
//        int WinnerSwordSlot = 0;
//        int WinnerFRSlot = 1;
//        int WinnerBowSlot = 2;
//        int WinnerArrowSlot = 9;
//
//
//
//        int LoserFSSlot = 3;
//        int LoserSwordSlot = 0;
//        int LoserFRSlot = 1;
//        int LoserBowSlot = 2;
//        int LoserArrowSlot = 3;
//
//
//        if (ClassicFSSlot.containsKey(winner)) {
//            WinnerFSSlot = ClassicFSSlot.get(winner);
//            ClassicFSSlot.remove(winner);
//        }
//
//        if (ClassicFSSlot.containsKey(loser)) {
//            LoserFSSlot = ClassicFSSlot.get(loser);
//            ClassicFSSlot.remove(loser);
//        }
//
//        for (int i = 0; i < 35; i++) {
//            if (winner.getInventory().getItem(i) != null) {
//                if (winner.getInventory().getItem(i).getType().equals(Material.FLINT_AND_STEEL)) {
//                    WinnerFSSlot = i;
//                } else if (winner.getInventory().getItem(i).getType().equals(Material.IRON_SWORD)) {
//                    WinnerSwordSlot = i;
//                } else if (winner.getInventory().getItem(i).getType().equals(Material.FISHING_ROD)) {
//                    WinnerFRSlot = i;
//                } else if (winner.getInventory().getItem(i).getType().equals(Material.BOW)) {
//                    WinnerBowSlot = i;
//                } else if (winner.getInventory().getItem(i).getType().equals(Material.ARROW)) {
//                    WinnerArrowSlot = i;
//                }
//            }
//
//            if (loser.getInventory().getItem(i) != null) {
//                if (loser.getInventory().getItem(i).getType().equals(Material.FLINT_AND_STEEL)) {
//                    LoserFSSlot = i;
//                } else if (loser.getInventory().getItem(i).getType().equals(Material.IRON_SWORD)) {
//                    LoserSwordSlot = i;
//                } else if (loser.getInventory().getItem(i).getType().equals(Material.FISHING_ROD)) {
//                    LoserFRSlot = i;
//                } else if (loser.getInventory().getItem(i).getType().equals(Material.BOW)) {
//                    LoserBowSlot = i;
//                } else if (loser.getInventory().getItem(i).getType().equals(Material.ARROW)) {
//                    LoserArrowSlot = i;
//                }
//            }
//        }




        Document Wfilter = new Document("UUID", winner.getUniqueId().toString());
        Document Lfilter = new Document("UUID", loser.getUniqueId().toString());

//        Bson updates1 = set("gameSettings.classic.swordSlot", WinnerSwordSlot);
//        col.findOneAndUpdate(Wfilter, updates1);
//
//        Bson updates2 = set("gameSettings.classic.fishingRodSlot", WinnerFRSlot);
//        col.findOneAndUpdate(Wfilter, updates2);
//
//        Bson updates3 = set("gameSettings.classic.bowSlot", WinnerBowSlot);
//        col.findOneAndUpdate(Wfilter, updates3);
//
//        Bson updates4 = set("gameSettings.classic.flintAndSteelSlot", WinnerFSSlot);
//        col.findOneAndUpdate(Wfilter, updates4);
//
//        Bson updates5 = set("gameSettings.classic.arrowSlot", WinnerArrowSlot);
//        col.findOneAndUpdate(Wfilter, updates5);

        addCoins(winner, 25);
        addCoins(loser, 5);


        Bson WupdatesWins = inc("stats.classic.wins", 1);
        col.findOneAndUpdate(Wfilter, WupdatesWins);






//        Bson Lupdates1 = set("gameSettings.classic.swordSlot", LoserSwordSlot);
//        col.findOneAndUpdate(Lfilter, Lupdates1);
//
//        Bson Lupdates2 = set("gameSettings.classic.fishingRodSlot", LoserFRSlot);
//        col.findOneAndUpdate(Lfilter, Lupdates2);
//
//        Bson Lupdates3 = set("gameSettings.classic.bowSlot", LoserBowSlot);
//        col.findOneAndUpdate(Lfilter, Lupdates3);
//
//        Bson Lupdates4 = set("gameSettings.classic.flintAndSteelSlot", LoserFSSlot);
//        col.findOneAndUpdate(Lfilter, Lupdates4);
//
//        Bson Lupdates5 = set("gameSettings.classic.arrowSlot", LoserArrowSlot);
//        col.findOneAndUpdate(Lfilter, Lupdates5);


        Bson LupdatesWins = inc("stats.classic.losses", 1);
        col.findOneAndUpdate(Lfilter, LupdatesWins);

        Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "----------------");
        Bukkit.getServer().broadcastMessage(ChatColor.GREEN + winner.getName() + ChatColor.GOLD + " just won a Classic Duel against " + ChatColor.RED + loser.getName() + ChatColor.GOLD + "!");
        Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "----------------");

        if (loser.equals(ClassicPlayer1)) {
            ClassicPlayer1.teleport(new Location(getServer().getWorld("world"), 3001.5, 40.5, 3061.5, 180, 0));
        } else {
            ClassicPlayer2.teleport(new Location(getServer().getWorld("world"), 3001.5, 40.5, 2993.5, 0, 0));
        }

        winner.sendMessage(ChatColor.BOLD.toString() + ChatColor.GREEN + "-------------------------");
        winner.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "       Classic Duel");
        winner.sendMessage(" ");
        winner.sendMessage(ChatColor.YELLOW.toString() + ChatColor.GRAY + "       " + winner.getName() + ChatColor.GRAY + " - " + ChatColor.WHITE + winner.getHealth() + ChatColor.RED + "❤");
        winner.sendMessage(ChatColor.YELLOW.toString() + ChatColor.GRAY + "     " + loser.getName() + ChatColor.GRAY + " - " + ChatColor.WHITE + "" + ChatColor.RED + ChatColor.BOLD.toString() + "DEAD");
        winner.sendMessage(" ");
        winner.sendMessage(ChatColor.BOLD.toString() + ChatColor.GREEN + "-------------------------");

        loser.sendMessage(ChatColor.BOLD.toString() + ChatColor.GREEN + "-------------------------");
        loser.sendMessage(ChatColor.YELLOW.toString() + ChatColor.YELLOW + "          Classic Duel");
        loser.sendMessage(" ");
        loser.sendMessage(ChatColor.YELLOW.toString() + ChatColor.GRAY + "       " + winner.getName() + ChatColor.GRAY + " - " + ChatColor.WHITE + winner.getHealth() + ChatColor.RED + "❤");
        loser.sendMessage(ChatColor.YELLOW.toString() + ChatColor.GRAY + "     " + loser.getName() + ChatColor.GRAY + " - " + ChatColor.WHITE + "" + ChatColor.RED + ChatColor.BOLD.toString() + "DEAD");
        loser.sendMessage(" ");
        loser.sendMessage(ChatColor.BOLD.toString() + ChatColor.GREEN + "-------------------------");

        winner.setHealth(20);
        loser.setHealth(20);

        loser.playSound(loser.getLocation(), Sound.ANVIL_BREAK, 1, 1);
        winner.playSound(winner.getLocation(), Sound.LEVEL_UP, 1, 1);
        winner.playSound(winner.getLocation(), Sound.NOTE_PLING, 1, 1);

        ClassicActive = false;
        JSONMessage.create(ChatColor.BOLD + "VICTORY!").color(ChatColor.GOLD).title(1, 60, 1, winner);
        JSONMessage.create(ChatColor.BOLD + "GAME OVER!").color(ChatColor.RED).title(1, 60, 1, loser);
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {

                backToHub(loser);
                backToHub(winner);
                ClassicQueued = false;

            }
        }, 70);
        ClassicPlayer1 = null;
        ClassicPlayer2 = null;
        ClassicGame.put(winner, false);
        ClassicGame.put(loser, false);

        if (loser.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            loser.removePotionEffect(PotionEffectType.INVISIBILITY);
        }
        if (winner.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            winner.removePotionEffect(PotionEffectType.INVISIBILITY);
        }

        winner.setAllowFlight(getDoubleJumpEnabled(winner) || getFlightEnabled(winner));
        if (getSpeedEnabled(winner)) {
            winner.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 60 * 24, 0, true, true));
        }
        loser.setAllowFlight(getDoubleJumpEnabled(loser) || getFlightEnabled(loser));
        if (getSpeedEnabled(loser)) {
            loser.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 60 * 24, 0, true, true));
        }

        if (!getPlayersVisible(winner)) {
            winner.hidePlayer(loser);
        }
        if (!getPlayersVisible(loser)) {
            loser.hidePlayer(winner);
        }
    }

    @EventHandler
    public void onGoldenAppleEat(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        if (BridgeGame.get(player)) {
            if (event.getItem().getType() == Material.GOLDEN_APPLE) {
                player.setHealth(20);
            }
        }
    }

    final int[] BridgeTime = {15 * 60};
    final String[] BridgeTimeFormatted = {"00:00"};

    public void bridgeEvent(Player player1, Player player2) {

        BridgeWaiting = true;

        silentLeaveGameQueue(player1);
        silentLeaveGameQueue(player2);

        updateBridgeScoreboard(player1);
        updateBridgeScoreboard(player2);

        BridgeRequestNo.remove(player1);
        player1.showPlayer(player2);
        player2.showPlayer(player1);

        BridgeQueued = true;
        BridgeGame.put(player1, true);
        BridgeGame.put(player2, true);

        Document filter1 = new Document("UUID", player1.getUniqueId().toString());
        Bson updates1 = inc("stats.bridge.gamesPlayed", 1);
        col.findOneAndUpdate(filter1, updates1);
        Document filter2 = new Document("UUID", player2.getUniqueId().toString());
        Bson updates2 = inc("stats.bridge.gamesPlayed", 1);
        col.findOneAndUpdate(filter2, updates2);

        player1.teleport(new Location(getServer().getWorld("world"), 5058.5, 125.5, 4995.5, 90, 0));
        player2.teleport(new Location(getServer().getWorld("world"), 5013.5, 125.5, 4995.5, -90, 0));

        BridgePlayer1 = player1;
        BridgePlayer2 = player2;


        player1.getInventory().clear();
        player2.getInventory().clear();

        if (player1.getAllowFlight()) {
            player1.setAllowFlight(false);
        }
        if (player2.getAllowFlight()) {
            player2.setAllowFlight(false);
        }

        if (player1.hasPotionEffect(PotionEffectType.SPEED)) {
            player1.removePotionEffect(PotionEffectType.SPEED);
        }
        if (player2.hasPotionEffect(PotionEffectType.SPEED)) {
            player2.removePotionEffect(PotionEffectType.SPEED);
        }
        BukkitScheduler scheduler5 = Bukkit.getServer().getScheduler();
        scheduler5.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                JSONMessage.create("5").color(ChatColor.GREEN).title(1, 18, 1, player1);
                JSONMessage.create("5").color(ChatColor.GREEN).title(1, 18, 1, player2);
                player1.playSound(player1.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);
                player2.playSound(player2.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);
            }
        }, 20);
        BukkitScheduler scheduler4 = Bukkit.getServer().getScheduler();
        scheduler4.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                JSONMessage.create("4").color(ChatColor.YELLOW).title(1, 18, 1, player1);
                JSONMessage.create("4").color(ChatColor.YELLOW).title(1, 18, 1, player2);
                player1.playSound(player1.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);
                player2.playSound(player2.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);
            }
        }, 40);

        BukkitScheduler scheduler3 = Bukkit.getServer().getScheduler();
        scheduler3.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                JSONMessage.create("3").color(ChatColor.GOLD).title(1, 18, 1, player1);
                JSONMessage.create("3").color(ChatColor.GOLD).title(1, 18, 1, player2);
                player1.playSound(player1.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);
                player2.playSound(player2.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);
            }
        }, 60);
        BukkitScheduler scheduler2 = Bukkit.getServer().getScheduler();
        scheduler2.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                JSONMessage.create("2").color(ChatColor.RED).title(1, 18, 1, player1);
                JSONMessage.create("2").color(ChatColor.RED).title(1, 18, 1, player2);
                player1.playSound(player1.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);
                player2.playSound(player2.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);

            }
        }, 80);
        BukkitScheduler scheduler1 = Bukkit.getServer().getScheduler();
        scheduler1.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                JSONMessage.create("1").color(ChatColor.DARK_RED).title(1, 18, 1, player1);
                JSONMessage.create("1").color(ChatColor.DARK_RED).title(1, 18, 1, player2);
                player1.playSound(player1.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);
                player2.playSound(player2.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);
            }
        }, 100);
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                if (player1 == BridgePlayer1) {
                    setBridgeInv(player1, ChatColor.RED);
                    setBridgeInv(player2, ChatColor.BLUE);
                } else {
                    setBridgeInv(player1, ChatColor.BLUE);
                    setBridgeInv(player2, ChatColor.RED);
                }



                player1.teleport(new Location(getServer().getWorld("world"), 5058.5, 125.5, 4995.5, 90, 0));
                player2.teleport(new Location(getServer().getWorld("world"), 5013.5, 125.5, 4995.5, -90, 0));
                createRedCage(player1);
                createBlueCage(player2);
                player1.playSound(player1.getLocation(), Sound.NOTE_PLING, 1, 1);
                player2.playSound(player2.getLocation(), Sound.NOTE_PLING, 1, 1);
                BridgeActive = true;

                setBridgeInv(BridgePlayer1, ChatColor.RED);
                setBridgeInv(BridgePlayer2, ChatColor.BLUE);
            }
        }, 120);



        BukkitScheduler sscheduler5 = Bukkit.getServer().getScheduler();
        sscheduler5.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                JSONMessage.create("5").color(ChatColor.GREEN).title(1, 18, 1, player1);
                JSONMessage.create("5").color(ChatColor.GREEN).title(1, 18, 1, player2);
                player1.playSound(player1.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);
                player2.playSound(player2.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);
            }
        }, 150 + 20);
        BukkitScheduler sscheduler4 = Bukkit.getServer().getScheduler();
        sscheduler4.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                JSONMessage.create("4").color(ChatColor.YELLOW).title(1, 18, 1, player1);
                JSONMessage.create("4").color(ChatColor.YELLOW).title(1, 18, 1, player2);
                player1.playSound(player1.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);
                player2.playSound(player2.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);
            }
        }, 150 + 40);

        BukkitScheduler sscheduler3 = Bukkit.getServer().getScheduler();
        sscheduler3.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                JSONMessage.create("3").color(ChatColor.GOLD).title(1, 18, 1, player1);
                JSONMessage.create("3").color(ChatColor.GOLD).title(1, 18, 1, player2);
                player1.playSound(player1.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);
                player2.playSound(player2.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);
            }
        }, 150 + 60);
        BukkitScheduler sscheduler2 = Bukkit.getServer().getScheduler();
        sscheduler2.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                JSONMessage.create("2").color(ChatColor.RED).title(1, 18, 1, player1);
                JSONMessage.create("2").color(ChatColor.RED).title(1, 18, 1, player2);
                player1.playSound(player1.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);
                player2.playSound(player2.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);

            }
        }, 150 + 80);
        BukkitScheduler sscheduler1 = Bukkit.getServer().getScheduler();
        sscheduler1.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                JSONMessage.create("1").color(ChatColor.DARK_RED).title(1, 18, 1, player1);
                JSONMessage.create("1").color(ChatColor.DARK_RED).title(1, 18, 1, player2);
                player1.playSound(player1.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);
                player2.playSound(player2.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);
            }
        }, 150 + 100);
        BukkitScheduler sscheduler = Bukkit.getServer().getScheduler();
        sscheduler.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                destroyRedCage(player1);
                destroyBlueCage(player2);
                player1.playSound(player1.getLocation(), Sound.NOTE_PLING, 1, 1);
                player2.playSound(player2.getLocation(), Sound.NOTE_PLING, 1, 1);
                BridgeWaiting = false;


            }
        }, 150 + 120);

        BridgeTime[0] = 15*60;

        BukkitScheduler Bridgescheduler = Bukkit.getServer().getScheduler();
        bridgeTimerID = Bridgescheduler.runTaskTimer(this, new Runnable()
        {

            @Override
            public void run()
            {
                if (BridgeActive) {
                    if (BridgeTime[0] == 0) {
                        BridgeEventEnd(BridgePlayer1, BridgePlayer2, "draw");

                        Bukkit.getScheduler().cancelTask(bridgeTimerID);
                        return;
                    }

                    int minutes = BridgeTime[0] / 60;
                    int seconds = BridgeTime[0] % 60;
                    String secondsFormated = "00";
                    if (seconds < 10) {
                        secondsFormated = "0" + seconds;
                    } else {
                        secondsFormated = "" + seconds;
                    }

                    BridgeTimeFormatted[0] = minutes + ":" + secondsFormated;

                    updateBridgeScoreboard(player1);
                    updateBridgeScoreboard(player2);

                    BridgeTime[0]--;
                } else {
                    if (!BridgeQueued && !BridgeWaiting) {
                        BridgeTime[0] = 0;
                    }
                }
            }
        }, 0L, 20L).getTaskId();
    }

    int bridgeTimerID;


    public void grayGlass(Player player, Integer x, Integer y, Integer z) {
        player.getWorld().getBlockAt(x ,y, z).setType(Material.STAINED_GLASS);
        player.getWorld().getBlockAt(x, y, z ).setData((byte) 8);
    }

    public void redGlass(Player player, Integer x, Integer y, Integer z) {
        player.getWorld().getBlockAt(x ,y, z).setType(Material.STAINED_GLASS);
        player.getWorld().getBlockAt(x, y, z ).setData((byte) 14);
    }

    public void redWool(Player player, Integer x, Integer y, Integer z) {
        player.getWorld().getBlockAt(x ,y, z).setType(Material.WOOL);
        player.getWorld().getBlockAt(x, y, z ).setData((byte) 14);
    }

    public void redClay(Player player, Integer x, Integer y, Integer z) {
        player.getWorld().getBlockAt(x ,y, z).setType(Material.STAINED_CLAY);
        player.getWorld().getBlockAt(x, y, z ).setData((byte) 14);
    }


    public void blueGlass(Player player, Integer x, Integer y, Integer z) {
        player.getWorld().getBlockAt(x ,y, z).setType(Material.STAINED_GLASS);
        player.getWorld().getBlockAt(x, y, z ).setData((byte) 11);
    }

    public void blueWool(Player player, Integer x, Integer y, Integer z) {
        player.getWorld().getBlockAt(x ,y, z).setType(Material.WOOL);
        player.getWorld().getBlockAt(x, y, z ).setData((byte) 11);
    }

    public void blueClay(Player player, Integer x, Integer y, Integer z) {
        player.getWorld().getBlockAt(x ,y, z).setType(Material.STAINED_CLAY);
        player.getWorld().getBlockAt(x, y, z ).setData((byte) 11);
    }

    public void spruceLog(Player player, Integer x, Integer y, Integer z) {
        player.getWorld().getBlockAt(x ,y, z).setType(Material.LOG);
        player.getWorld().getBlockAt(x, y, z ).setData((byte) 1);
    }

    public void stoneSlab(Player player, Integer x, Integer y, Integer z) {
        player.getWorld().getBlockAt(x ,y, z).setType(Material.STEP);
    }

    public void topStoneSlab(Player player, Integer x, Integer y, Integer z) {
        player.getWorld().getBlockAt(x ,y, z).setType(Material.STEP);
        player.getWorld().getBlockAt(x, y, z ).setData((byte) 8);
    }

    public void setAir(Player player, Integer x, Integer y, Integer z) {
        player.getWorld().getBlockAt(x ,y, z).setType(Material.AIR);
    }

    public void createRedCage(Player player) {
        loadSchematic(BridgeRedSpawn, "bridge/redcage");
    }

    public void destroyRedCage(Player player) {
        loadSchematic(BridgeRedSpawn, "bridge/redcageAir");
    }

    public void createBlueCage(Player player) {
        loadSchematic(BridgeBlueSpawn, "bridge/bluecage");
    }

    public void destroyBlueCage(Player player) {
        loadSchematic(BridgeBlueSpawn, "bridge/bluecageAir");
    }






//    public void createBlueCage(Player player) {
//        blueGlass(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ());
//        blueGlass(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() + 1,(int)  player.getLocation().getZ());
//        blueGlass(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() + 2,(int)  player.getLocation().getZ());
//
//        blueGlass(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ() - 1);
//        blueGlass(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() + 1,(int)  player.getLocation().getZ() - 1);
//        blueGlass(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() + 2,(int)  player.getLocation().getZ() - 1);
//        blueGlass(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ() + 1);
//        blueGlass(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() + 1,(int)  player.getLocation().getZ() + 1);
//        blueGlass(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() + 2,(int)  player.getLocation().getZ() + 1);
//
//        blueGlass(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ() - 2);
//        blueGlass(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() + 1,(int)  player.getLocation().getZ() - 2);
//        blueGlass(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() + 2,(int)  player.getLocation().getZ() - 2);
//        blueGlass(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ() + 2);
//        blueGlass(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() + 1,(int)  player.getLocation().getZ() + 2);
//        blueGlass(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() + 2,(int)  player.getLocation().getZ() + 2);
//
//        grayGlass(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ());
//        grayGlass(player, (int) BridgeBlueSpawn.getX(),(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-1);
//        grayGlass(player, (int) BridgeBlueSpawn.getX(),(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ());
//
//        grayGlass(player, (int) BridgeBlueSpawn.getX(),(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+1);
//        grayGlass(player, (int) BridgeBlueSpawn.getX() -1,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ());
//        grayGlass(player, (int) BridgeBlueSpawn.getX() -1,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-1);
//        grayGlass(player, (int) BridgeBlueSpawn.getX() -1,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-2);
//        grayGlass(player, (int) BridgeBlueSpawn.getX() -1,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-3);
//        grayGlass(player, (int) BridgeBlueSpawn.getX() -1,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+1);
//        grayGlass(player, (int) BridgeBlueSpawn.getX() -1,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+2);
//        grayGlass(player, (int) BridgeBlueSpawn.getX() -1,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+3);
//
//        grayGlass(player, (int) BridgeBlueSpawn.getX(),(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-2);
//        grayGlass(player, (int) BridgeBlueSpawn.getX(),(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-3);
//        grayGlass(player, (int) BridgeBlueSpawn.getX(),(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+2);
//        grayGlass(player, (int) BridgeBlueSpawn.getX(),(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+3);
//
//        grayGlass(player, (int) BridgeBlueSpawn.getX() +1,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-1);
//        grayGlass(player, (int) BridgeBlueSpawn.getX() +1,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-2);
//        grayGlass(player, (int) BridgeBlueSpawn.getX() +1,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+1);
//        grayGlass(player, (int) BridgeBlueSpawn.getX() +1,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+2);
//
//        grayGlass(player, (int) BridgeBlueSpawn.getX() -2,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ());
//        grayGlass(player, (int) BridgeBlueSpawn.getX() -2,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-1);
//        grayGlass(player, (int) BridgeBlueSpawn.getX() -2,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-2);
//        grayGlass(player, (int) BridgeBlueSpawn.getX() -2,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-3);
//        grayGlass(player, (int) BridgeBlueSpawn.getX() -2,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+1);
//        grayGlass(player, (int) BridgeBlueSpawn.getX() -2,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+2);
//        grayGlass(player, (int) BridgeBlueSpawn.getX() -2,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+3);
//
//        grayGlass(player, (int) BridgeBlueSpawn.getX() -3,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ());
//        grayGlass(player, (int) BridgeBlueSpawn.getX() -3,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-1);
//        grayGlass(player, (int) BridgeBlueSpawn.getX() -3,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-2);
//        grayGlass(player, (int) BridgeBlueSpawn.getX() -3,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+1);
//        grayGlass(player, (int) BridgeBlueSpawn.getX() -3,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+2);
//
//        stoneSlab(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ());
//        stoneSlab(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ()+1);
//        stoneSlab(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ()+2);
//        stoneSlab(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ()-1);
//        stoneSlab(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ()-2);
//
//        topStoneSlab(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ());
//        topStoneSlab(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+1);
//        topStoneSlab(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+2);
//        topStoneSlab(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()-1);
//        topStoneSlab(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()-2);
//
//        stoneSlab(player, (int) BridgeBlueSpawn.getX() - 4,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ());
//        stoneSlab(player, (int) BridgeBlueSpawn.getX() - 4,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ()+1);
//        stoneSlab(player, (int) BridgeBlueSpawn.getX() - 4,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ()+2);
//        stoneSlab(player, (int) BridgeBlueSpawn.getX() - 4,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ()-1);
//        stoneSlab(player, (int) BridgeBlueSpawn.getX() - 4,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ()-2);
//
//        topStoneSlab(player, (int) BridgeBlueSpawn.getX() - 4,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ());
//        topStoneSlab(player, (int) BridgeBlueSpawn.getX() - 4,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+1);
//        topStoneSlab(player, (int) BridgeBlueSpawn.getX() - 4,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+2);
//        topStoneSlab(player, (int) BridgeBlueSpawn.getX() - 4,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()-1);
//        topStoneSlab(player, (int) BridgeBlueSpawn.getX() - 4,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()-2);
//
//        topStoneSlab(player, (int) BridgeBlueSpawn.getX(),(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ() - 4);
//        topStoneSlab(player, (int) BridgeBlueSpawn.getX() - 1,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()-4);
//        topStoneSlab(player, (int) BridgeBlueSpawn.getX() - 2,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()-4);
//
//        topStoneSlab(player, (int) BridgeBlueSpawn.getX(),(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ() + 4);
//        topStoneSlab(player, (int) BridgeBlueSpawn.getX() - 1,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+4);
//        topStoneSlab(player, (int) BridgeBlueSpawn.getX() - 2,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+4);
//
//        stoneSlab(player, (int) BridgeBlueSpawn.getX(),(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ() - 4);
//        stoneSlab(player, (int) BridgeBlueSpawn.getX() - 1,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()-4);
//        stoneSlab(player, (int) BridgeBlueSpawn.getX() - 2,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()-4);
//
//        stoneSlab(player, (int) BridgeBlueSpawn.getX(),(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ() + 4);
//        stoneSlab(player, (int) BridgeBlueSpawn.getX() - 1,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()+4);
//        stoneSlab(player, (int) BridgeBlueSpawn.getX() - 2,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()+4);
//
//        spruceLog(player, (int) BridgeBlueSpawn.getX(),(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ() + 4);
//        spruceLog(player, (int) BridgeBlueSpawn.getX() - 1,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()+4);
//        spruceLog(player, (int) BridgeBlueSpawn.getX() - 2,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()+4);
//
//        spruceLog(player, (int) BridgeBlueSpawn.getX(),(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ() + 4);
//        spruceLog(player, (int) BridgeBlueSpawn.getX() - 1,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()+4);
//        spruceLog(player, (int) BridgeBlueSpawn.getX() - 2,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()+4);
//
//        spruceLog(player, (int) BridgeBlueSpawn.getX(),(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ() + 4);
//        spruceLog(player, (int) BridgeBlueSpawn.getX() - 1,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ()+4);
//        spruceLog(player, (int) BridgeBlueSpawn.getX() - 2,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ()+4);
//
//
//        spruceLog(player, (int) BridgeBlueSpawn.getX(),(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ() - 4);
//        spruceLog(player, (int) BridgeBlueSpawn.getX() - 1,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()-4);
//        spruceLog(player, (int) BridgeBlueSpawn.getX() - 2,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()-4);
//
//        spruceLog(player, (int) BridgeBlueSpawn.getX(),(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ() - 4);
//        spruceLog(player, (int) BridgeBlueSpawn.getX() - 1,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()-4);
//        spruceLog(player, (int) BridgeBlueSpawn.getX() - 2,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()-4);
//
//        spruceLog(player, (int) BridgeBlueSpawn.getX(),(int)  player.getLocation().getY(),(int)  player.getLocation().getZ() - 4);
//        spruceLog(player, (int) BridgeBlueSpawn.getX() - 1,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ()-4);
//        spruceLog(player, (int) BridgeBlueSpawn.getX() - 2,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ()-4);
//
//
//        spruceLog(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ());
//        spruceLog(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ() + 1);
//        spruceLog(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ() + 2);
//        spruceLog(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ() - 1);
//        spruceLog(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ() - 2);
//
//        spruceLog(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() + 1,(int)  player.getLocation().getZ());
//        spruceLog(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() + 1,(int)  player.getLocation().getZ() + 1);
//        spruceLog(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY()  + 1,(int)  player.getLocation().getZ() + 2);
//        spruceLog(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ() - 1);
//        spruceLog(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ() - 2);
//
//        spruceLog(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ());
//        spruceLog(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ() + 1);
//        spruceLog(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ() + 2);
//        spruceLog(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ() - 1);
//        spruceLog(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ() - 2);
//
//        blueClay(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+3);
//        blueClay(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()-3);
//
//        blueClay(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+4);
//        blueClay(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()-4);
//
//        blueClay(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+3);
//        blueClay(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()-3);
//
//        blueClay(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+4);
//        blueClay(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()-4);
//
//        blueClay(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ()+4);
//        blueClay(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ()-4);
//
//        blueClay(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()+4);
//        blueClay(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()-4);
//
//        blueClay(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()+4);
//        blueClay(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()-4);
//
//        blueClay(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()+4);
//        blueClay(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()-4);
//
//
//        blueClay(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ()+3);
//        blueClay(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ()-3);
//
//        blueClay(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()+3);
//        blueClay(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()-3);
//
//        blueClay(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()+3);
//        blueClay(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()-3);
//
//        blueClay(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()+3);
//        blueClay(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()-3);
//
//        blueWool(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ()+4);
//        blueWool(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ()-4);
//
//        blueWool(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()+4);
//        blueWool(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()-4);
//
//        blueWool(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()+4);
//        blueWool(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()-4);
//
//        blueWool(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()+4);
//        blueWool(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()-4);
//
//        blueWool(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() +4,(int)  player.getLocation().getZ()+4);
//        blueWool(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() +4,(int)  player.getLocation().getZ()-4);
//
//        blueWool(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() +4,(int)  player.getLocation().getZ()+3);
//        blueWool(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() +4,(int)  player.getLocation().getZ()-3);
//
//        blueWool(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() +4,(int)  player.getLocation().getZ()+4);
//        blueWool(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() +4,(int)  player.getLocation().getZ()-4);
//
//        blueWool(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() +4,(int)  player.getLocation().getZ()+3);
//        blueWool(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() +4,(int)  player.getLocation().getZ()-3);
//
//        blueWool(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()+3);
//        blueWool(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()-3);
//
//        blueWool(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()+3);
//        blueWool(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()-3);
//
//        blueWool(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()+3);
//        blueWool(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()-3);
//
//        blueWool(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ()+3);
//        blueWool(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ()-3);
//
//
//        blueClay(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+3);
//        blueClay(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY()-1,(int)  player.getLocation().getZ()-3);
//
//        blueClay(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ()+3);
//        blueClay(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ()-3);
//
//        blueClay(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()+3);
//        blueClay(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY()+1,(int)  player.getLocation().getZ()-3);
//
//        blueClay(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()+3);
//        blueClay(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY()+2,(int)  player.getLocation().getZ()-3);
//
//        blueClay(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()+3);
//        blueClay(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY()+3,(int)  player.getLocation().getZ()-3);
//
//
//        blueClay(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+3);
//        blueClay(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY()-1,(int)  player.getLocation().getZ()-3);
//
//        blueClay(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+3);
//        blueClay(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY()-1,(int)  player.getLocation().getZ()-3);
//
//        blueClay(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+4);
//        blueClay(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY()-1,(int)  player.getLocation().getZ()-4);
//
//        blueClay(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+4);
//        blueClay(player, (int) BridgeBlueSpawn.getX()- 4,(int)  player.getLocation().getY()-1,(int)  player.getLocation().getZ()-4);
//
//
//
//        blueClay(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+3);
//        blueClay(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY()-1,(int)  player.getLocation().getZ()-3);
//
//        blueClay(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ()+4);
//        blueClay(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ()-4);
//
//        blueClay(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()+4);
//        blueClay(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY()+1,(int)  player.getLocation().getZ()-4);
//
//        blueClay(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()+4);
//        blueClay(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY()+2,(int)  player.getLocation().getZ()-4);
//
//        blueClay(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()+4);
//        blueClay(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY()+3,(int)  player.getLocation().getZ()-4);
//
//
//
//        blueWool(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ()+4);
//        blueWool(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ()-4);
//        blueWool(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY() + 1,(int)  player.getLocation().getZ()+4);
//        blueWool(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY() + 1,(int)  player.getLocation().getZ()-4);
//        blueWool(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY() + 2,(int)  player.getLocation().getZ()+4);
//        blueWool(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY() + 2,(int)  player.getLocation().getZ()-4);
//        blueWool(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ()+4);
//        blueWool(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ()-4);
//        blueWool(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY() + 4,(int)  player.getLocation().getZ()+4);
//        blueWool(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY() + 4,(int)  player.getLocation().getZ()-4);
//
//        blueWool(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ()+3);
//        blueWool(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ()-3);
//        blueWool(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() + 1,(int)  player.getLocation().getZ()+3);
//        blueWool(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() + 1,(int)  player.getLocation().getZ()-3);
//        blueWool(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() + 2,(int)  player.getLocation().getZ()+3);
//        blueWool(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() + 2,(int)  player.getLocation().getZ()-3);
//        blueWool(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ()+3);
//        blueWool(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ()-3);
//        blueWool(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() + 4,(int)  player.getLocation().getZ()+3);
//        blueWool(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() + 4,(int)  player.getLocation().getZ()-3);
//
//        blueWool(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() + 4,(int)  player.getLocation().getZ()+4);
//        blueWool(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() + 4,(int)  player.getLocation().getZ()-4);
//
//        blueWool(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY() + 4,(int)  player.getLocation().getZ()+3);
//        blueWool(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY() + 4,(int)  player.getLocation().getZ()-3);
//
//    }
//
//    public void destroyRedCage(Player player) {
//        setAir(player, (int) BridgeRedSpawn.getX() - 2,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ());
//        setAir(player, (int) BridgeRedSpawn.getX() - 2,(int)  player.getLocation().getY() + 1,(int)  player.getLocation().getZ());
//        setAir(player, (int) BridgeRedSpawn.getX() - 2,(int)  player.getLocation().getY() + 2,(int)  player.getLocation().getZ());
//
//        setAir(player, (int) BridgeRedSpawn.getX() - 2,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ() - 1);
//        setAir(player, (int) BridgeRedSpawn.getX() - 2,(int)  player.getLocation().getY() + 1,(int)  player.getLocation().getZ() - 1);
//        setAir(player, (int) BridgeRedSpawn.getX() - 2,(int)  player.getLocation().getY() + 2,(int)  player.getLocation().getZ() - 1);
//        setAir(player, (int) BridgeRedSpawn.getX() - 2,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ() + 1);
//        setAir(player, (int) BridgeRedSpawn.getX() - 2,(int)  player.getLocation().getY() + 1,(int)  player.getLocation().getZ() + 1);
//        setAir(player, (int) BridgeRedSpawn.getX() - 2,(int)  player.getLocation().getY() + 2,(int)  player.getLocation().getZ() + 1);
//
//        setAir(player, (int) BridgeRedSpawn.getX() - 2,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ() - 2);
//        setAir(player, (int) BridgeRedSpawn.getX() - 2,(int)  player.getLocation().getY() + 1,(int)  player.getLocation().getZ() - 2);
//        setAir(player, (int) BridgeRedSpawn.getX() - 2,(int)  player.getLocation().getY() + 2,(int)  player.getLocation().getZ() - 2);
//        setAir(player, (int) BridgeRedSpawn.getX() - 2,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ() + 2);
//        setAir(player, (int) BridgeRedSpawn.getX() - 2,(int)  player.getLocation().getY() + 1,(int)  player.getLocation().getZ() + 2);
//        setAir(player, (int) BridgeRedSpawn.getX() - 2,(int)  player.getLocation().getY() + 2,(int)  player.getLocation().getZ() + 2);
//
//        setAir(player, (int) BridgeRedSpawn.getX()-1,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ());
//        setAir(player, (int) BridgeRedSpawn.getX(),(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-1);
//        setAir(player, (int) BridgeRedSpawn.getX(),(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ());
//
//        setAir(player, (int) BridgeRedSpawn.getX(),(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+1);
//        setAir(player, (int) BridgeRedSpawn.getX() +1,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ());
//        setAir(player, (int) BridgeRedSpawn.getX() +1,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-1);
//        setAir(player, (int) BridgeRedSpawn.getX() +1,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-2);
//        setAir(player, (int) BridgeRedSpawn.getX() +1,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeRedSpawn.getX() +1,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+1);
//        setAir(player, (int) BridgeRedSpawn.getX() +1,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+2);
//        setAir(player, (int) BridgeRedSpawn.getX() +1,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+3);
//
//        setAir(player, (int) BridgeRedSpawn.getX(),(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-2);
//        setAir(player, (int) BridgeRedSpawn.getX(),(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeRedSpawn.getX(),(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+2);
//        setAir(player, (int) BridgeRedSpawn.getX(),(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+3);
//
//        setAir(player, (int) BridgeRedSpawn.getX() -1,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-1);
//        setAir(player, (int) BridgeRedSpawn.getX() -1,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-2);
//        setAir(player, (int) BridgeRedSpawn.getX() -1,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+1);
//        setAir(player, (int) BridgeRedSpawn.getX() -1,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+2);
//
//        setAir(player, (int) BridgeRedSpawn.getX() +2,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ());
//        setAir(player, (int) BridgeRedSpawn.getX() +2,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-1);
//        setAir(player, (int) BridgeRedSpawn.getX() +2,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-2);
//        setAir(player, (int) BridgeRedSpawn.getX() +2,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeRedSpawn.getX() +2,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+1);
//        setAir(player, (int) BridgeRedSpawn.getX() +2,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+2);
//        setAir(player, (int) BridgeRedSpawn.getX() +2,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+3);
//
//        setAir(player, (int) BridgeRedSpawn.getX() +3,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ());
//        setAir(player, (int) BridgeRedSpawn.getX() +3,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-1);
//        setAir(player, (int) BridgeRedSpawn.getX() +3,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-2);
//        setAir(player, (int) BridgeRedSpawn.getX() +3,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+1);
//        setAir(player, (int) BridgeRedSpawn.getX() +3,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+2);
//
//        setAir(player, (int) BridgeRedSpawn.getX() - 2,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ());
//        setAir(player, (int) BridgeRedSpawn.getX() - 2,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ()+1);
//        setAir(player, (int) BridgeRedSpawn.getX() - 2,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ()+2);
//        setAir(player, (int) BridgeRedSpawn.getX() - 2,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ()-1);
//        setAir(player, (int) BridgeRedSpawn.getX() - 2,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ()-2);
//
//        setAir(player, (int) BridgeRedSpawn.getX() - 2,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ());
//        setAir(player, (int) BridgeRedSpawn.getX() - 2,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+1);
//        setAir(player, (int) BridgeRedSpawn.getX() - 2,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+2);
//        setAir(player, (int) BridgeRedSpawn.getX() - 2,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()-1);
//        setAir(player, (int) BridgeRedSpawn.getX() - 2,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()-2);
//
//        setAir(player, (int) BridgeRedSpawn.getX() + 4,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ());
//        setAir(player, (int) BridgeRedSpawn.getX() + 4,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ()+1);
//        setAir(player, (int) BridgeRedSpawn.getX() + 4,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ()+2);
//        setAir(player, (int) BridgeRedSpawn.getX() + 4,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ()-1);
//        setAir(player, (int) BridgeRedSpawn.getX() + 4,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ()-2);
//
//        setAir(player, (int) BridgeRedSpawn.getX() + 4,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ());
//        setAir(player, (int) BridgeRedSpawn.getX() + 4,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+1);
//        setAir(player, (int) BridgeRedSpawn.getX() + 4,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+2);
//        setAir(player, (int) BridgeRedSpawn.getX() + 4,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()-1);
//        setAir(player, (int) BridgeRedSpawn.getX() + 4,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()-2);
//
//        setAir(player, (int) BridgeRedSpawn.getX(),(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ() - 4);
//        setAir(player, (int) BridgeRedSpawn.getX() + 1,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeRedSpawn.getX() + 2,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()-4);
//
//        setAir(player, (int) BridgeRedSpawn.getX(),(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ() + 4);
//        setAir(player, (int) BridgeRedSpawn.getX() + 1,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeRedSpawn.getX() + 2,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+4);
//
//        setAir(player, (int) BridgeRedSpawn.getX(),(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ() - 4);
//        setAir(player, (int) BridgeRedSpawn.getX() + 1,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeRedSpawn.getX() + 2,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()-4);
//
//        setAir(player, (int) BridgeRedSpawn.getX(),(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ() + 4);
//        setAir(player, (int) BridgeRedSpawn.getX() + 1,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeRedSpawn.getX() + 2,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()+4);
//
//        setAir(player, (int) BridgeRedSpawn.getX(),(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ() + 4);
//        setAir(player, (int) BridgeRedSpawn.getX() + 1,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeRedSpawn.getX() + 2,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()+4);
//
//        setAir(player, (int) BridgeRedSpawn.getX(),(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ() + 4);
//        setAir(player, (int) BridgeRedSpawn.getX() + 1,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeRedSpawn.getX() + 2,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()+4);
//
//        setAir(player, (int) BridgeRedSpawn.getX(),(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ() + 4);
//        setAir(player, (int) BridgeRedSpawn.getX() + 1,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeRedSpawn.getX() + 2,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ()+4);
//
//
//        setAir(player, (int) BridgeRedSpawn.getX(),(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ() - 4);
//        setAir(player, (int) BridgeRedSpawn.getX() + 1,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeRedSpawn.getX() + 2,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()-4);
//
//        setAir(player, (int) BridgeRedSpawn.getX(),(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ() - 4);
//        setAir(player, (int) BridgeRedSpawn.getX() + 1,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeRedSpawn.getX() + 2,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()-4);
//
//        setAir(player, (int) BridgeRedSpawn.getX(),(int)  player.getLocation().getY(),(int)  player.getLocation().getZ() - 4);
//        setAir(player, (int) BridgeRedSpawn.getX() + 1,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeRedSpawn.getX() + 2,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ()-4);
//
//
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ());
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ() + 1);
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ() + 2);
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ() - 1);
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ() - 2);
//
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY() + 1,(int)  player.getLocation().getZ());
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY() + 1,(int)  player.getLocation().getZ() + 1);
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY()  + 1,(int)  player.getLocation().getZ() + 2);
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ() - 1);
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ() - 2);
//
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ());
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ() + 1);
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ() + 2);
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ() - 1);
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ() - 2);
//
//        setAir(player, (int) BridgeRedSpawn.getX()-1,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeRedSpawn.getX()-1,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()-3);
//
//        setAir(player, (int) BridgeRedSpawn.getX()-1,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeRedSpawn.getX()-1,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()-4);
//
//        setAir(player, (int) BridgeRedSpawn.getX()-2,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeRedSpawn.getX()-2,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()-3);
//
//        setAir(player, (int) BridgeRedSpawn.getX()-2,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeRedSpawn.getX()-2,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()-4);
//
//        setAir(player, (int) BridgeRedSpawn.getX()-2,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeRedSpawn.getX()-2,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ()-4);
//
//        setAir(player, (int) BridgeRedSpawn.getX()-2,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeRedSpawn.getX()-2,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()-4);
//
//        setAir(player, (int) BridgeRedSpawn.getX()-2,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeRedSpawn.getX()-2,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()-4);
//
//        setAir(player, (int) BridgeRedSpawn.getX()-2,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeRedSpawn.getX()-2,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()-4);
//
//
//        setAir(player, (int) BridgeRedSpawn.getX()-1,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeRedSpawn.getX()-1,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ()-3);
//
//        setAir(player, (int) BridgeRedSpawn.getX()-1,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeRedSpawn.getX()-1,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()-3);
//
//        setAir(player, (int) BridgeRedSpawn.getX()-1,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeRedSpawn.getX()-1,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()-3);
//
//        setAir(player, (int) BridgeRedSpawn.getX()-1,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeRedSpawn.getX()-1,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()-3);
//
//        setAir(player, (int) BridgeRedSpawn.getX()-1,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeRedSpawn.getX()-1,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ()-4);
//
//        setAir(player, (int) BridgeRedSpawn.getX()-1,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeRedSpawn.getX()-1,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeRedSpawn.getX()-1,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeRedSpawn.getX()-1,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeRedSpawn.getX()-1,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeRedSpawn.getX()-1,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeRedSpawn.getX()-1,(int)  player.getLocation().getY() +4,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeRedSpawn.getX()-1,(int)  player.getLocation().getY() +4,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeRedSpawn.getX()-1,(int)  player.getLocation().getY() +4,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeRedSpawn.getX()-1,(int)  player.getLocation().getY() +4,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeRedSpawn.getX()-2,(int)  player.getLocation().getY() +4,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeRedSpawn.getX()-2,(int)  player.getLocation().getY() +4,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeRedSpawn.getX()-2,(int)  player.getLocation().getY() +4,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeRedSpawn.getX()-2,(int)  player.getLocation().getY() +4,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeRedSpawn.getX()-2,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeRedSpawn.getX()-2,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeRedSpawn.getX()-2,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeRedSpawn.getX()-2,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeRedSpawn.getX()-2,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeRedSpawn.getX()-2,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeRedSpawn.getX()-2,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeRedSpawn.getX()-2,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeRedSpawn.getX()+3,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeRedSpawn.getX()+3,(int)  player.getLocation().getY()-1,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeRedSpawn.getX()+3,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeRedSpawn.getX()+3,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeRedSpawn.getX()+3,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeRedSpawn.getX()+3,(int)  player.getLocation().getY()+1,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeRedSpawn.getX()+3,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeRedSpawn.getX()+3,(int)  player.getLocation().getY()+2,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeRedSpawn.getX()+3,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeRedSpawn.getX()+3,(int)  player.getLocation().getY()+3,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY()-1,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeRedSpawn.getX()+3,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeRedSpawn.getX()+3,(int)  player.getLocation().getY()-1,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeRedSpawn.getX()+3,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeRedSpawn.getX()+3,(int)  player.getLocation().getY()-1,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY()-1,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY()-1,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY()+1,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY()+2,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY()+3,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeRedSpawn.getX()+3,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeRedSpawn.getX()+3,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeRedSpawn.getX()+3,(int)  player.getLocation().getY() + 1,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeRedSpawn.getX()+3,(int)  player.getLocation().getY() + 1,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeRedSpawn.getX()+3,(int)  player.getLocation().getY() + 2,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeRedSpawn.getX()+3,(int)  player.getLocation().getY() + 2,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeRedSpawn.getX()+3,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeRedSpawn.getX()+3,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeRedSpawn.getX()+3,(int)  player.getLocation().getY() + 4,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeRedSpawn.getX()+3,(int)  player.getLocation().getY() + 4,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY() + 1,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY() + 1,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY() + 2,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY() + 2,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY() + 4,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY() + 4,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY() + 4,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeRedSpawn.getX()+4,(int)  player.getLocation().getY() + 4,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeRedSpawn.getX()+3,(int)  player.getLocation().getY() + 4,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeRedSpawn.getX()+3,(int)  player.getLocation().getY() + 4,(int)  player.getLocation().getZ()-3);
//    }
//
//
//
//
//
//
//    public void destroyBlueCage(Player player) {
//        setAir(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ());
//        setAir(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() + 1,(int)  player.getLocation().getZ());
//        setAir(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() + 2,(int)  player.getLocation().getZ());
//        setAir(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ() - 1);
//        setAir(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() + 1,(int)  player.getLocation().getZ() - 1);
//        setAir(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() + 2,(int)  player.getLocation().getZ() - 1);
//        setAir(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ() + 1);
//        setAir(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() + 1,(int)  player.getLocation().getZ() + 1);
//        setAir(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() + 2,(int)  player.getLocation().getZ() + 1);
//        setAir(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ() - 2);
//        setAir(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() + 1,(int)  player.getLocation().getZ() - 2);
//        setAir(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() + 2,(int)  player.getLocation().getZ() - 2);
//        setAir(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ() + 2);
//        setAir(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() + 1,(int)  player.getLocation().getZ() + 2);
//        setAir(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() + 2,(int)  player.getLocation().getZ() + 2);
//        setAir(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ());
//        setAir(player, (int) BridgeBlueSpawn.getX(),(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-1);
//        setAir(player, (int) BridgeBlueSpawn.getX(),(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ());
//        setAir(player, (int) BridgeBlueSpawn.getX(),(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+1);
//        setAir(player, (int) BridgeBlueSpawn.getX() -1,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ());
//        setAir(player, (int) BridgeBlueSpawn.getX() -1,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-1);
//        setAir(player, (int) BridgeBlueSpawn.getX() -1,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-2);
//        setAir(player, (int) BridgeBlueSpawn.getX() -1,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeBlueSpawn.getX() -1,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+1);
//        setAir(player, (int) BridgeBlueSpawn.getX() -1,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+2);
//        setAir(player, (int) BridgeBlueSpawn.getX() -1,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeBlueSpawn.getX(),(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-2);
//        setAir(player, (int) BridgeBlueSpawn.getX(),(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeBlueSpawn.getX(),(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+2);
//        setAir(player, (int) BridgeBlueSpawn.getX(),(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeBlueSpawn.getX() +1,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-1);
//        setAir(player, (int) BridgeBlueSpawn.getX() +1,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-2);
//        setAir(player, (int) BridgeBlueSpawn.getX() +1,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+1);
//        setAir(player, (int) BridgeBlueSpawn.getX() +1,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+2);
//        setAir(player, (int) BridgeBlueSpawn.getX() -2,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ());
//        setAir(player, (int) BridgeBlueSpawn.getX() -2,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-1);
//        setAir(player, (int) BridgeBlueSpawn.getX() -2,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-2);
//        setAir(player, (int) BridgeBlueSpawn.getX() -2,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeBlueSpawn.getX() -2,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+1);
//        setAir(player, (int) BridgeBlueSpawn.getX() -2,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+2);
//        setAir(player, (int) BridgeBlueSpawn.getX() -2,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeBlueSpawn.getX() -3,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ());
//        setAir(player, (int) BridgeBlueSpawn.getX() -3,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-1);
//        setAir(player, (int) BridgeBlueSpawn.getX() -3,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()-2);
//        setAir(player, (int) BridgeBlueSpawn.getX() -3,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+1);
//        setAir(player, (int) BridgeBlueSpawn.getX() -3,(int)  player.getLocation().getY() - 1,(int)  player.getLocation().getZ()+2);
//        setAir(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ());
//        setAir(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ()+1);
//        setAir(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ()+2);
//        setAir(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ()-1);
//        setAir(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ()-2);
//        setAir(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ());
//        setAir(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+1);
//        setAir(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+2);
//        setAir(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()-1);
//        setAir(player, (int) BridgeBlueSpawn.getX() + 2,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()-2);
//        setAir(player, (int) BridgeBlueSpawn.getX() - 4,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ());
//        setAir(player, (int) BridgeBlueSpawn.getX() - 4,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ()+1);
//        setAir(player, (int) BridgeBlueSpawn.getX() - 4,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ()+2);
//        setAir(player, (int) BridgeBlueSpawn.getX() - 4,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ()-1);
//        setAir(player, (int) BridgeBlueSpawn.getX() - 4,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ()-2);
//        setAir(player, (int) BridgeBlueSpawn.getX() - 4,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ());
//        setAir(player, (int) BridgeBlueSpawn.getX() - 4,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+1);
//        setAir(player, (int) BridgeBlueSpawn.getX() - 4,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+2);
//        setAir(player, (int) BridgeBlueSpawn.getX() - 4,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()-1);
//        setAir(player, (int) BridgeBlueSpawn.getX() - 4,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()-2);
//        setAir(player, (int) BridgeBlueSpawn.getX(),(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ() - 4);
//        setAir(player, (int) BridgeBlueSpawn.getX() - 1,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeBlueSpawn.getX() - 2,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeBlueSpawn.getX(),(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ() + 4);
//        setAir(player, (int) BridgeBlueSpawn.getX() - 1,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeBlueSpawn.getX() - 2,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeBlueSpawn.getX(),(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ() - 4);
//        setAir(player, (int) BridgeBlueSpawn.getX() - 1,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeBlueSpawn.getX() - 2,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeBlueSpawn.getX(),(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ() + 4);
//        setAir(player, (int) BridgeBlueSpawn.getX() - 1,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeBlueSpawn.getX() - 2,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeBlueSpawn.getX(),(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ() + 4);
//        setAir(player, (int) BridgeBlueSpawn.getX() - 1,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeBlueSpawn.getX() - 2,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeBlueSpawn.getX(),(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ() + 4);
//        setAir(player, (int) BridgeBlueSpawn.getX() - 1,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeBlueSpawn.getX() - 2,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeBlueSpawn.getX(),(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ() + 4);
//        setAir(player, (int) BridgeBlueSpawn.getX() - 1,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeBlueSpawn.getX() - 2,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeBlueSpawn.getX(),(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ() - 4);
//        setAir(player, (int) BridgeBlueSpawn.getX() - 1,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeBlueSpawn.getX() - 2,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeBlueSpawn.getX(),(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ() - 4);
//        setAir(player, (int) BridgeBlueSpawn.getX() - 1,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeBlueSpawn.getX() - 2,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeBlueSpawn.getX(),(int)  player.getLocation().getY(),(int)  player.getLocation().getZ() - 4);
//        setAir(player, (int) BridgeBlueSpawn.getX() - 1,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeBlueSpawn.getX() - 2,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ());
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ() + 1);
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ() + 2);
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ() - 1);
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ() - 2);
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() + 1,(int)  player.getLocation().getZ());
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() + 1,(int)  player.getLocation().getZ() + 1);
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY()  + 1,(int)  player.getLocation().getZ() + 2);
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ() - 1);
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ() - 2);
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ());
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ() + 1);
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ() + 2);
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ() - 1);
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ() - 2);
//        setAir(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() +4,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() +4,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() +4,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeBlueSpawn.getX()+1,(int)  player.getLocation().getY() +4,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() +4,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() +4,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() +4,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() +4,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeBlueSpawn.getX()+2,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY()-1,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY()+1,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY()+2,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY()+3,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY()-1,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY()-1,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY()-1,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeBlueSpawn.getX()- 4,(int)  player.getLocation().getY()-1,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() -1,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY()-1,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() ,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() +1,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY()+1,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() +2,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY()+2,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() +3,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY()+3,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY() + 1,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY() + 1,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY() + 2,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY() + 2,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY() + 4,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY() + 4,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY(),(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() + 1,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() + 1,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() + 2,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() + 2,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() + 3,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() + 4,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() + 4,(int)  player.getLocation().getZ()-3);
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() + 4,(int)  player.getLocation().getZ()+4);
//        setAir(player, (int) BridgeBlueSpawn.getX()-4,(int)  player.getLocation().getY() + 4,(int)  player.getLocation().getZ()-4);
//        setAir(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY() + 4,(int)  player.getLocation().getZ()+3);
//        setAir(player, (int) BridgeBlueSpawn.getX()-3,(int)  player.getLocation().getY() + 4,(int)  player.getLocation().getZ()-3);
//    }

    public String goalsFilled(Player player) {
        String goals = "";
        if (!BridgeGoals.containsKey(player)) {
            BridgeGoals.put(player, 0);
        }
        if (BridgeGoals.get(player) != 0) {
            for (int i = 0; i < BridgeGoals.get(player); i++) {
                goals = goals + "⬤";
            }
        }
        return goals;
    }

    public String goalsEmpty(Player player) {
        String goals = "";
        if (!BridgeGoals.containsKey(player)) {
            BridgeGoals.put(player, 0);
        }
        if (BridgeGoals.get(player) == 0) {
            goals = "⬤⬤⬤⬤⬤";
        } else if (BridgeGoals.get(player) == 1) {
            goals = "⬤⬤⬤⬤";
        } else if (BridgeGoals.get(player) == 2) {
            goals = "⬤⬤⬤";
        } else if (BridgeGoals.get(player) == 3) {
            goals = "⬤⬤";
        } else if (BridgeGoals.get(player) == 4) {
            goals = "⬤";
        } else {
            goals = "";
        }
        return goals;
    }

    public void removeBridgeScoreboard(Player player) {
        Scoreboard bridgeBoard1 = player.getScoreboard();
        if (bridgeBoard1.getObjective("bridge") != null) {
            bridgeBoard1.getObjective("bridge").unregister();
        }
        player.setScoreboard(bridgeBoard1);
    }

    public void updateBridgeScoreboard(Player player) {
        Scoreboard bridgeBoard1 = player.getScoreboard();
        if (bridgeBoard1.getObjective("bridge") != null) {
            bridgeBoard1.getObjective("bridge").unregister();
        }

        Objective bObjective = bridgeBoard1.registerNewObjective("bridge", "dummy");
        bObjective.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "DUELS");
        bObjective.setDisplaySlot(DisplaySlot.SIDEBAR);



        Score s1 = bObjective.getScore("");
        s1.setScore(12);

        Score s2 = bObjective.getScore(ChatColor.WHITE + "Mode: " + ChatColor.GREEN + "The Bridge         ");
        s2.setScore(11);
        Score s3 = bObjective.getScore(" ");
        s3.setScore(10);
        Score s4 = bObjective.getScore("Time Left: " + ChatColor.GREEN + BridgeTimeFormatted[0]);
        s3.setScore(9);
        Score s5 = bObjective.getScore("  ");
        s3.setScore(8);
        Score s6 = bObjective.getScore(ChatColor.RED + "[R] " + ChatColor.RED + goalsFilled(BridgePlayer1) + ChatColor.GRAY + goalsEmpty(BridgePlayer1));
        s4.setScore(7);
        Score s7 = bObjective.getScore(ChatColor.BLUE + "[B] " + ChatColor.BLUE + goalsFilled(BridgePlayer2) + ChatColor.GRAY + goalsEmpty(BridgePlayer2));
        s5.setScore(6);
        Score s8 = bObjective.getScore("   ");
        s6.setScore(5);
        Score s9 = bObjective.getScore(ChatColor.WHITE + "Kills: " + ChatColor.GREEN + BridgeKills.get(player));
        s7.setScore(4);
        Score s10 = bObjective.getScore(ChatColor.WHITE + "Goals: " + ChatColor.GREEN + BridgeGoals.get(player));
        s8.setScore(3);
        Score s11 = bObjective.getScore("    ");
        s9.setScore(2);
        Score s12 = bObjective.getScore(ChatColor.YELLOW + "youtube.com/teakivy");
        s10.setScore(1);

        player.setScoreboard(bridgeBoard1);
    }

    public void goalScored(Player player1, Player player2, String color) {

        BridgePlayer1.setHealth(20);
        BridgePlayer2.setHealth(20);

        BridgeWaiting = true;

        updateBridgeScoreboard(player1);
        updateBridgeScoreboard(player2);



        if (BridgeGoals.get(player1) <= 4 && BridgeGoals.get(player2) <= 4) {

//            JSONMessage.create("5").color(ChatColor.GREEN).title(1, 18, 1, player1);

            removePotionEffects(player1);
            removePotionEffects(player2);


            if (player1 == BridgePlayer1) {
                setBridgeInv(player1, ChatColor.RED);
                setBridgeInv(player2, ChatColor.BLUE);
            } else {
                setBridgeInv(player1, ChatColor.BLUE);
                setBridgeInv(player2, ChatColor.RED);
            }


            player1.teleport(new Location(getServer().getWorld("world"), 5058.5, 125.5, 4995.5, 90, 0));
            player2.teleport(new Location(getServer().getWorld("world"), 5013.5, 125.5, 4995.5, -90, 0));
            createRedCage(player1);
            createBlueCage(player2);
            player1.playSound(player1.getLocation(), Sound.NOTE_PLING, 1, 1);
            player2.playSound(player2.getLocation(), Sound.NOTE_PLING, 1, 1);
            BridgeActive = true;

            Player player;
            if (color.equals("red"))
                player = BridgePlayer1;
            else {
                player = BridgePlayer2;
            }

            Location loc = player.getLocation();
            Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
            FireworkMeta fwm = fw.getFireworkMeta();

            Location loc1;
            Location loc2;
            Location loc3;

            if (color.equals("red")) {
                fwm.addEffect(FireworkEffect.builder().withColor(Color.RED).withColor(Color.WHITE).flicker(true).build());
                loc1 = loc.add(-6, -3, 5);
                loc2 = loc.add(-4, 2, -3);
                loc3 = loc.add(-8, 0, 3);
            } else {
                fwm.addEffect(FireworkEffect.builder().withColor(Color.BLUE).withColor(Color.WHITE).flicker(true).build());
                loc1 = loc.add(6, -3, 5);
                loc2 = loc.add(4, 2, -3);
                loc3 = loc.add(8, 0, 3);
            }

            BukkitScheduler firework = Bukkit.getServer().getScheduler();


            Firework fw2 = (Firework) loc1.getWorld().spawnEntity(loc1, EntityType.FIREWORK);
            fw2.setFireworkMeta(fwm);
            firework.scheduleSyncDelayedTask(this, new Runnable() {
                @Override
                public void run() {
                    Firework fw2 = (Firework) loc2.getWorld().spawnEntity(loc2, EntityType.FIREWORK);
                    fw2.setFireworkMeta(fwm);
                }
            }, 25);

            firework.scheduleSyncDelayedTask(this, new Runnable() {
                @Override
                public void run() {
                    Firework fw2 = (Firework) loc3.getWorld().spawnEntity(loc3, EntityType.FIREWORK);
                    fw2.setFireworkMeta(fwm);
                }
            }, 55);



            BukkitScheduler sscheduler5 = Bukkit.getServer().getScheduler();
            sscheduler5.scheduleSyncDelayedTask(this, new Runnable() {
                @Override
                public void run() {
                    JSONMessage.create(ChatColor.RED + BridgeGoals.get(BridgePlayer1).toString() + ChatColor.DARK_GRAY + " - " + ChatColor.BLUE + BridgeGoals.get(BridgePlayer2).toString()).title(1, 18, 1, player1);
                    JSONMessage.create(ChatColor.GRAY + "Cages Open in.. " + ChatColor.GREEN + "5").subtitle(player1);
                    JSONMessage.create(ChatColor.RED + BridgeGoals.get(BridgePlayer1).toString() + ChatColor.DARK_GRAY + " - " + ChatColor.BLUE + BridgeGoals.get(BridgePlayer2).toString()).title(1, 18, 1, player2);
                    JSONMessage.create(ChatColor.GRAY + "Cages Open in.. " + ChatColor.GREEN + "5").subtitle(player2);
                    player1.playSound(player1.getLocation(), Sound.NOTE_STICKS, 1, 1);
                    player2.playSound(player2.getLocation(), Sound.NOTE_STICKS, 1, 1);
                }
            }, 20);
            BukkitScheduler sscheduler4 = Bukkit.getServer().getScheduler();
            sscheduler4.scheduleSyncDelayedTask(this, new Runnable() {
                @Override
                public void run() {
                    JSONMessage.create(ChatColor.RED + BridgeGoals.get(BridgePlayer1).toString() + ChatColor.DARK_GRAY + " - " + ChatColor.BLUE + BridgeGoals.get(BridgePlayer2).toString()).title(1, 18, 1, player1);
                    JSONMessage.create(ChatColor.GRAY + "Cages Open in.. " + ChatColor.YELLOW + "4").subtitle(player1);
                    JSONMessage.create(ChatColor.RED + BridgeGoals.get(BridgePlayer1).toString() + ChatColor.DARK_GRAY + " - " + ChatColor.BLUE + BridgeGoals.get(BridgePlayer2).toString()).title(1, 18, 1, player2);
                    JSONMessage.create(ChatColor.GRAY + "Cages Open in.. " + ChatColor.YELLOW + "4").subtitle(player2);
                    player1.playSound(player1.getLocation(), Sound.NOTE_STICKS, 1, 1);
                    player2.playSound(player2.getLocation(), Sound.NOTE_STICKS, 1, 1);
                }
            }, 40);

            BukkitScheduler sscheduler3 = Bukkit.getServer().getScheduler();
            sscheduler3.scheduleSyncDelayedTask(this, new Runnable() {
                @Override
                public void run() {
                    JSONMessage.create(ChatColor.RED + BridgeGoals.get(BridgePlayer1).toString() + ChatColor.DARK_GRAY + " - " + ChatColor.BLUE + BridgeGoals.get(BridgePlayer2).toString()).title(1, 18, 1, player1);
                    JSONMessage.create(ChatColor.GRAY + "Cages Open in.. " + ChatColor.GOLD + "3").subtitle(player1);
                    JSONMessage.create(ChatColor.RED + BridgeGoals.get(BridgePlayer1).toString() + ChatColor.DARK_GRAY + " - " + ChatColor.BLUE + BridgeGoals.get(BridgePlayer2).toString()).title(1, 18, 1, player2);
                    JSONMessage.create(ChatColor.GRAY + "Cages Open in.. " + ChatColor.GOLD + "3").subtitle(player2);
                    player1.playSound(player1.getLocation(), Sound.NOTE_STICKS, 1, 1);
                    player2.playSound(player2.getLocation(), Sound.NOTE_STICKS, 1, 1);
                }
            }, 60);
            BukkitScheduler sscheduler2 = Bukkit.getServer().getScheduler();
            sscheduler2.scheduleSyncDelayedTask(this, new Runnable() {
                @Override
                public void run() {
                    JSONMessage.create(ChatColor.RED + BridgeGoals.get(BridgePlayer1).toString() + ChatColor.DARK_GRAY + " - " + ChatColor.BLUE + BridgeGoals.get(BridgePlayer2).toString()).title(1, 18, 1, player1);
                    JSONMessage.create(ChatColor.GRAY + "Cages Open in.. " + ChatColor.RED + "2").subtitle(player1);
                    JSONMessage.create(ChatColor.RED + BridgeGoals.get(BridgePlayer1).toString() + ChatColor.DARK_GRAY + " - " + ChatColor.BLUE + BridgeGoals.get(BridgePlayer2).toString()).title(1, 18, 1, player2);
                    JSONMessage.create(ChatColor.GRAY + "Cages Open in.. " + ChatColor.RED + "2").subtitle(player2);
                    player1.playSound(player1.getLocation(), Sound.NOTE_STICKS, 1, 1);
                    player2.playSound(player2.getLocation(), Sound.NOTE_STICKS, 1, 1);

                }
            }, 80);
            BukkitScheduler sscheduler1 = Bukkit.getServer().getScheduler();
            sscheduler1.scheduleSyncDelayedTask(this, new Runnable() {
                @Override
                public void run() {
                    JSONMessage.create(ChatColor.RED + BridgeGoals.get(BridgePlayer1).toString() + ChatColor.DARK_GRAY + " - " + ChatColor.BLUE + BridgeGoals.get(BridgePlayer2).toString()).title(1, 18, 1, player1);
                    JSONMessage.create(ChatColor.GRAY + "Cages Open in.. " + ChatColor.DARK_RED + "1").subtitle(player1);
                    JSONMessage.create(ChatColor.RED + BridgeGoals.get(BridgePlayer1).toString() + ChatColor.DARK_GRAY + " - " + ChatColor.BLUE + BridgeGoals.get(BridgePlayer2).toString()).title(1, 18, 1, player2);
                    JSONMessage.create(ChatColor.GRAY + "Cages Open in.. " + ChatColor.DARK_RED + "1").subtitle(player2);
                    player1.playSound(player1.getLocation(), Sound.NOTE_STICKS, 1, 1);
                    player2.playSound(player2.getLocation(), Sound.NOTE_STICKS, 1, 1);
                }
            }, 100);
            BukkitScheduler sscheduler = Bukkit.getServer().getScheduler();
            sscheduler.scheduleSyncDelayedTask(this, new Runnable() {
                @Override
                public void run() {
                    destroyRedCage(player1);
                    destroyBlueCage(player2);
                    player1.playSound(player1.getLocation(), Sound.NOTE_PIANO, 1,  1);
                    player2.playSound(player2.getLocation(), Sound.NOTE_PIANO, 1,  1);
                    BridgeWaiting = false;
                }
            }, 120);
        } else {
            Player winner;
            Player loser;
            if (BridgeGoals.get(player1) >= 5) {
                winner = player1;
                loser = player2;
            } else {
                winner = player2;
                loser = player1;
            }

            BridgeEventEnd(winner, loser, "normal");
        }
    }

    public void setRedClay(Location location) {
        Bukkit.getWorld("world").getBlockAt(location).setType(Material.STAINED_CLAY);
        Bukkit.getWorld("world").getBlockAt(location).setData((byte) 14);
    }
    public void setBlueClay(Location location) {
        Bukkit.getWorld("world").getBlockAt(location).setType(Material.STAINED_CLAY);
        Bukkit.getWorld("world").getBlockAt(location).setData((byte) 11);
    }
    public void setWhiteClay(Location location) {
        Bukkit.getWorld("world").getBlockAt(location).setType(Material.STAINED_CLAY);
    }

    public void BridgePlayer1Respawn() {
        BridgePlayer1.setHealth(20);
        for (PotionEffect effect : BridgePlayer1.getActivePotionEffects()) {
            BridgePlayer1.removePotionEffect(effect.getType());
        }
        BridgePlayer1.teleport(new Location(getServer().getWorld("world"), 5058.5, 120.5, 4995.5, 90, 0));

        setBridgeInv(BridgePlayer1, ChatColor.RED);
    }
    public void BridgePlayer2Respawn() {
        BridgePlayer2.setHealth(20);
        for (PotionEffect effect : BridgePlayer2.getActivePotionEffects()) {
            BridgePlayer2.removePotionEffect(effect.getType());
        }
        BridgePlayer2.teleport(new Location(getServer().getWorld("world"), 5013.5, 120.5, 4995.5, -90, 0));

        setBridgeInv(BridgePlayer2, ChatColor.BLUE);
    }

    public void removePotionEffects(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }

    @EventHandler
    public void BridgeBowShoot(EntityShootBowEvent event) {
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (BridgeGame.get(player)) {
                if (BridgeWaiting) {
                    event.setCancelled(true);
                } else {
                    player.setLevel(3);

                    BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

                    scheduler.scheduleSyncDelayedTask(this, new Runnable() {
                        @Override
                        public void run() {
                            player.setLevel(2);
                        }
                    }, 20);
                    scheduler.scheduleSyncDelayedTask(this, new Runnable() {
                        @Override
                        public void run() {
                            player.setLevel(1);
                        }
                    }, 40);
                    scheduler.scheduleSyncDelayedTask(this, new Runnable() {
                        @Override
                        public void run() {
                            player.setLevel(0);
                            if (BridgeGame.get(player)) {
                                player.getInventory().addItem(new ItemStack(Material.ARROW, 1));
                            }
                        }
                    }, 60);
                }
            }
        }
    }


    public void BridgeEventEnd(Player winner, Player loser, String type) {

        BridgePlayer1Respawn();
        BridgePlayer2Respawn();
        BridgeWaiting = true;

        for (Block block : BridgeBlocksBroken) {
            if (block.getLocation().getX() == 5035) {
                setWhiteClay(block.getLocation());
            } else
            if (block.getLocation().getX() > 5035) {
                setRedClay(block.getLocation());
            } else
            if (block.getLocation().getX() < 5035) {
                setBlueClay(block.getLocation());
            } else {
                block.setType(Material.STAINED_CLAY);
            }
        }

        for (Block block : BridgeBlocksPlaced) {
            if (block.getLocation().getY() > 112) {
//                Do Nothing bc idk how to reverse
                block.setType(Material.AIR);

            } else {
                System.out.println("Check Y 1");
                if (block.getLocation().getY() < 104) {
                    block.setType(Material.AIR);
                } else {
                    if (block.getLocation().getZ() != 4995) {
                        block.setType(Material.AIR);
                    }
                }
            }
        }


        Bukkit.getScheduler().cancelTask(bridgeTimerID);

        BridgeBlocksBroken.clear();
        BridgeBlocksPlaced.clear();

        removePotionEffects(winner);
        removePotionEffects(loser);

        if (type.equals("normal")) {
            Document Wfilter = new Document("UUID", winner.getUniqueId().toString());
            Document Lfilter = new Document("UUID", loser.getUniqueId().toString());


            Bson WupdatesWins = inc("stats.bridge.wins", 1);
            col.findOneAndUpdate(Wfilter, WupdatesWins);

            addCoins(winner, 50);
            addCoins(loser, 10);


            Bson LupdatesWins = inc("stats.bridge.losses", 1);
            col.findOneAndUpdate(Lfilter, LupdatesWins);

            Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "----------------");
            Bukkit.getServer().broadcastMessage(ChatColor.GREEN + winner.getName() + ChatColor.GOLD + " just won a Bridge Duel against " + ChatColor.RED + loser.getName() + ChatColor.GOLD + "!");
            Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "----------------");
        } else {
            Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "----------------");
            Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + winner.getName() + ChatColor.GOLD + " just tied a Bridge Duel against " + ChatColor.YELLOW + loser.getName() + ChatColor.GOLD + "!");
            Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "----------------");
        }

        BridgeGoals.put(winner, 0);
        BridgeGoals.put(loser, 0);

        BridgeKills.put(winner, 0);
        BridgeKills.put(loser, 0);


        if (loser.equals(BridgePlayer1)) {
            BridgePlayer1.teleport(new Location(getServer().getWorld("world"), 5058.5, 120.5, 4995.5, 90, 0));
        } else {
            BridgePlayer2.teleport(new Location(getServer().getWorld("world"), 5013.5, 120.5, 4995.5, -90, 0));
        }

        if (type.equals("normal")) {
            loser.playSound(loser.getLocation(), Sound.ANVIL_BREAK, 1, 1);
            winner.playSound(winner.getLocation(), Sound.LEVEL_UP, 1, 1);
            winner.playSound(winner.getLocation(), Sound.NOTE_PLING, 1, 1);
        } else {
            winner.playSound(winner.getLocation(), Sound.ANVIL_USE, 1, 1);
            loser.playSound(loser.getLocation(), Sound.ANVIL_USE, 1, 1);
        }

        BridgeActive = false;
        if (type.equals("normal")) {
            JSONMessage.create(ChatColor.BOLD + "VICTORY!").color(ChatColor.GOLD).title(1, 60, 1, winner);
            JSONMessage.create(ChatColor.BOLD + "GAME OVER!").color(ChatColor.RED).title(1, 60, 1, loser);
        } else {
            JSONMessage.create(ChatColor.BOLD + "DRAW!").color(ChatColor.YELLOW).title(1, 60, 1, winner);
            JSONMessage.create(ChatColor.BOLD + "DRAW!").color(ChatColor.YELLOW).title(1, 60, 1, loser);
        }
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {

                backToHub(loser);
                backToHub(winner);
                BridgeQueued = false;
                BridgeWaiting = true;
                removeBridgeScoreboard(winner);
                removeBridgeScoreboard(loser);

            }
        }, 70);
        BridgePlayer1 = null;
        BridgePlayer2 = null;
        BridgeGame.put(winner, false);
        BridgeGame.put(loser, false);

        if (loser.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            loser.removePotionEffect(PotionEffectType.INVISIBILITY);
        }
        if (winner.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            winner.removePotionEffect(PotionEffectType.INVISIBILITY);
        }

        winner.setAllowFlight(getDoubleJumpEnabled(winner) || getFlightEnabled(winner));
        if (getSpeedEnabled(winner)) {
            winner.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 60 * 24, 0, true, true));
        }
        loser.setAllowFlight(getDoubleJumpEnabled(loser) || getFlightEnabled(loser));
        if (getSpeedEnabled(loser)) {
            loser.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 60 * 24, 0, true, true));
        }

        if (!getPlayersVisible(winner)) {
            winner.hidePlayer(loser);
        }
        if (!getPlayersVisible(loser)) {
            loser.hidePlayer(winner);
        }

    }

    public void setBridgeInv(Player player, ChatColor color) {
        PlayerInventory playerInv = player.getInventory();

        ItemStack IronSword = new ItemStack(Material.IRON_SWORD);
        ItemMeta IronSwordMeta = IronSword.getItemMeta();
        IronSwordMeta.spigot().setUnbreakable(true);
        IronSword.setItemMeta(IronSwordMeta);

        ItemStack Bow = new ItemStack(Material.BOW);
        ItemMeta BowMeta = Bow.getItemMeta();
        BowMeta.spigot().setUnbreakable(true);
        Bow.setItemMeta(BowMeta);

        ItemStack Pickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta PickaxeMeta = Pickaxe.getItemMeta();
        PickaxeMeta.spigot().setUnbreakable(true);
        PickaxeMeta.addEnchant(Enchantment.DIG_SPEED, 2, true);
        Pickaxe.setItemMeta(PickaxeMeta);

        ItemStack RedChestPlate = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta RedChestPlateMeta = (LeatherArmorMeta) RedChestPlate.getItemMeta();
        RedChestPlateMeta.setColor(Color.RED);
        RedChestPlate.setItemMeta(RedChestPlateMeta);

        ItemStack RedLeggings = new ItemStack(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta RedLeggingsMeta = (LeatherArmorMeta) RedLeggings.getItemMeta();
        RedLeggingsMeta.setColor(Color.RED);
        RedLeggings.setItemMeta(RedLeggingsMeta);

        ItemStack RedBoots = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta RedBootsMeta = (LeatherArmorMeta) RedBoots.getItemMeta();
        RedBootsMeta.setColor(Color.RED);
        RedBoots.setItemMeta(RedBootsMeta);

        ItemStack BlueChestPlate = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta BlueChestPlateMeta = (LeatherArmorMeta) BlueChestPlate.getItemMeta();
        BlueChestPlateMeta.setColor(Color.BLUE);
        BlueChestPlate.setItemMeta(BlueChestPlateMeta);

        ItemStack BlueLeggings = new ItemStack(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta BlueLeggingsMeta = (LeatherArmorMeta) BlueLeggings.getItemMeta();
        BlueLeggingsMeta.setColor(Color.BLUE);
        BlueLeggings.setItemMeta(BlueLeggingsMeta);

        ItemStack BlueBoots = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta BlueBootsMeta = (LeatherArmorMeta) BlueBoots.getItemMeta();
        BlueBootsMeta.setColor(Color.BLUE);
        BlueBoots.setItemMeta(BlueBootsMeta);

        ItemStack BlueBlocks = new ItemStack(Material.STAINED_CLAY, 64, (short) 11);

        ItemStack RedBlocks = new ItemStack(Material.STAINED_CLAY, 64, (short) 14);
        ItemStack GoldenApples = new ItemStack(Material.GOLDEN_APPLE, 8);
        ItemStack Arrow = new ItemStack(Material.ARROW, 1);
        if (BridgeActive) {
            playerInv.clear();
            if (color == ChatColor.RED) {

                playerInv.setItem(getBridgeSwordSlot(player), IronSword);
                playerInv.setItem(getBridgeBowSlot(player), Bow);
                playerInv.setItem(getBridgePickaxeSlot(player), Pickaxe);
                playerInv.setItem(getBridgeBlockSlot1(player), RedBlocks);
                playerInv.setItem(getBridgeBlockSlot2(player), RedBlocks);
                playerInv.setItem(getBridgeGappleSlot(player), GoldenApples);
                playerInv.setItem(getBridgeArrowSlot(player), Arrow);

                playerInv.setChestplate(RedChestPlate);
                playerInv.setLeggings(RedLeggings);
                playerInv.setBoots(RedBoots);
            } else if (color == ChatColor.BLUE) {
                playerInv.setItem(getBridgeSwordSlot(player), IronSword);
                playerInv.setItem(getBridgeBowSlot(player), Bow);
                playerInv.setItem(getBridgePickaxeSlot(player), Pickaxe);
                playerInv.setItem(getBridgeBlockSlot1(player), BlueBlocks);
                playerInv.setItem(getBridgeBlockSlot2(player), BlueBlocks);
                playerInv.setItem(getBridgeGappleSlot(player), GoldenApples);
                playerInv.setItem(getBridgeArrowSlot(player), Arrow);

                playerInv.setChestplate(BlueChestPlate);
                playerInv.setLeggings(BlueLeggings);
                playerInv.setBoots(BlueBoots);
            }
        }
    }


    public int getBridgeSwordSlot(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("gameSettings");
        Document gameType = (Document) obj.get("bridge");
        if (gameType.getInteger("swordSlot") != null) {
            return gameType.getInteger("swordSlot");
        } else {
            return 0;
        }
//        return gameType.getInteger("swordSlot");
    }
    public int getBridgeBowSlot(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("gameSettings");
        Document gameType = (Document) obj.get("bridge");
        return gameType.getInteger("bowSlot");
    }
    public int getBridgePickaxeSlot(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("gameSettings");
        Document gameType = (Document) obj.get("bridge");
        return gameType.getInteger("pickaxeSlot");
    }
    public int getBridgeBlockSlot1(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("gameSettings");
        Document gameType = (Document) obj.get("bridge");
        return gameType.getInteger("blockSlot1");
    }
    public int getBridgeBlockSlot2(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("gameSettings");
        Document gameType = (Document) obj.get("bridge");
        return gameType.getInteger("blockSlot2");
    }
    public int getBridgeGappleSlot(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("gameSettings");
        Document gameType = (Document) obj.get("bridge");
        return gameType.getInteger("gappleSlot");
    }
    public int getBridgeArrowSlot(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("gameSettings");
        Document gameType = (Document) obj.get("bridge");
        return gameType.getInteger("arrowSlot");
    }

    public void saveBridgeInv(Player player) {

        int SwordSlot = 0;
        int BowSlot = 1;
        int PickaxeSlot = 2;
        int BlockSlot1 = 55;
        int BlockSlot2 = 4;
        int GappleSlot = 5;
        int ArrowSlot = 55;

        for (int i = 0; i < 35; i++) {
            if (player.getInventory().getItem(i) != null) {
                if (player.getInventory().getItem(i).getType().equals(Material.IRON_SWORD)) {
                    SwordSlot = i;
                } else if (player.getInventory().getItem(i).getType().equals(Material.BOW)) {
                    BowSlot = i;
                } else if (player.getInventory().getItem(i).getType().equals(Material.DIAMOND_PICKAXE)) {
                    PickaxeSlot = i;
                } else if (player.getInventory().getItem(i).getType().equals(Material.STAINED_CLAY)) {
                    if (BlockSlot1 == 55) {
                        BlockSlot1 = i;
                    } else {
                        BlockSlot2 = i;
                    }
                } else if (player.getInventory().getItem(i).getType().equals(Material.GOLDEN_APPLE)) {
                    GappleSlot = i;
                } else if (player.getInventory().getItem(i).getType().equals(Material.ARROW)) {
                    ArrowSlot = i;
                }
            }
        }

        if (ArrowSlot == 55) {
            ArrowSlot = 10;
        }




        Document filter = new Document("UUID", player.getUniqueId().toString());

        Bson updates1 = set("gameSettings.bridge.swordSlot", SwordSlot);
        col.findOneAndUpdate(filter, updates1);

        Bson updates2 = set("gameSettings.bridge.bowSlot", BowSlot);
        col.findOneAndUpdate(filter, updates2);

        Bson updates3 = set("gameSettings.bridge.pickaxeSlot", PickaxeSlot);
        col.findOneAndUpdate(filter, updates3);

        Bson updates4 = set("gameSettings.bridge.blockSlot1", BlockSlot1);
        col.findOneAndUpdate(filter, updates4);

        Bson updates5 = set("gameSettings.bridge.blockSlot2", BlockSlot2);
        col.findOneAndUpdate(filter, updates5);

        Bson updates6 = set("gameSettings.bridge.gappleSlot", GappleSlot);
        col.findOneAndUpdate(filter, updates6);

        Bson updates7 = set("gameSettings.bridge.arrowSlot", ArrowSlot);
        col.findOneAndUpdate(filter, updates7);
    }

    private void addPlayerToQueue(Player player, String mode) {
        switch (mode) {
            case "sumo":
                if (!SumoQueue.containsValue(player)) {
                    if (!SkywarsQueue.containsValue(player) && !ClassicQueue.containsValue(player) && !BridgeQueue.containsValue(player)) {
                        if (SumoQueueSize < 3) {
                            SumoQueueSize++;
                            SumoQueue.put(SumoQueueSize, player);

                            System.out.println(SumoQueue);

                            if (SumoQueueSize > 1) {
                                sumoEvent(SumoQueue.get(1), SumoQueue.get(2));
                                player.sendMessage(ChatColor.GREEN + "Joined Sumo Queue!");
                                SumoQueue.remove(1);
                                SumoQueue.remove(2);
                            } else {
                                player.sendMessage(ChatColor.GREEN + "Joined Sumo Queue! Waiting on 1 player...");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "The Queue for Sumo is full!");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Looks like you're already in a queue! Type /leavequeue to leave it!");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Looks like you're already in the queue for Sumo!");
                }
                break;
            case "classic":
                if (!ClassicQueue.containsValue(player)) {
                    if (!SkywarsQueue.containsValue(player) && !SumoQueue.containsValue(player) && !BridgeQueue.containsValue(player)) {
                        if (ClassicQueueSize < 3) {
                            ClassicQueueSize++;
                            ClassicQueue.put(ClassicQueueSize, player);

                            if (ClassicQueueSize > 1) {
                                classicEvent(ClassicQueue.get(1), ClassicQueue.get(2));
                                ClassicQueue.remove(1);
                                ClassicQueue.remove(2);
                                player.sendMessage(ChatColor.GREEN + "Joined Classic Queue!");
                            } else {
                                player.sendMessage(ChatColor.GREEN + "Joined Classic Queue! Waiting on 1 player...");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "The Queue for Classic is full!");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Looks like you're already in a queue! Type /leavequeue to leave it!");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Looks like you're already in the queue for Classic!");
                }
                break;
            case "bridge":
                if (!BridgeQueue.containsValue(player)) {
                    if (!SkywarsQueue.containsValue(player) && !SumoQueue.containsValue(player) && !ClassicQueue.containsValue(player)) {
                        if (BridgeQueueSize < 3) {
                            BridgeQueueSize++;
                            BridgeQueue.put(BridgeQueueSize, player);

                            if (BridgeQueueSize > 1) {
                                bridgeEvent(BridgeQueue.get(1), BridgeQueue.get(2));
                                BridgeQueue.remove(1);
                                BridgeQueue.remove(2);
                                player.sendMessage(ChatColor.GREEN + "Joined Bridge Queue!");
                            } else {
                                player.sendMessage(ChatColor.GREEN + "Joined Bridge Queue! Waiting on 1 player...");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "The Queue for Bridge is full!");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Looks like you're already in a queue! Type /leavequeue to leave it!");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Looks like you're already in the queue for Bridge!");
                }
                break;
            case "skywars":
                if (!SkywarsQueue.containsValue(player)) {
                    if (!BridgeQueue.containsValue(player) && !SumoQueue.containsValue(player) && !ClassicQueue.containsValue(player)) {
                        if (SkywarsQueueSize < 13) {
                            SkywarsQueueSize++;
                            SkywarsQueue.put(SkywarsQueueSize, player);

                            if (SkywarsQueueSize > 1) {
                                classicEvent(SkywarsQueue.get(1), SkywarsQueue.get(2));
                                SkywarsQueue.remove(1);
                                BridgeQueue.remove(2);
                                player.sendMessage(ChatColor.GREEN + "Joined Skywars Queue!");
                            } else {
                                player.sendMessage(ChatColor.GREEN + "Joined Skywars Queue! Waiting on 1 player...");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "The Skywars for Bridge is full!");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Looks like you're already in a queue! Type /leavequeue to leave it!");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Looks like you're already in the queue for Bridge!");
                }
                break;
        }
    }

}