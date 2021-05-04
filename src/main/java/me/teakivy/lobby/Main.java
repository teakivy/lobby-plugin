package me.teakivy.lobby;

import com.comphenix.protocol.ProtocolLibrary;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
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
import me.teakivy.lobby.Bedwars.Bedwars;
import me.teakivy.lobby.Bedwars.Events.*;
import me.teakivy.lobby.Bedwars.Events.Fireball;
import me.teakivy.lobby.Bedwars.Inventory.InventoryHelper;
import me.teakivy.lobby.Bedwars.menus.MainShop;
import me.teakivy.lobby.Bedwars.util.BedwarsInventory;
import me.teakivy.lobby.Bedwars.util.ShopKeeper;
import me.teakivy.lobby.Events.ClickNPC;
import me.teakivy.lobby.Gadgets.*;
import me.teakivy.lobby.utils.*;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.*;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.WorldType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArrow;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Crops;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.*;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

import javax.print.Doc;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;


import static com.mongodb.client.model.Updates.*;

public final class Main extends JavaPlugin implements Listener {

    public static DataManager data;

    final static Class<? extends List> docClazz = ArrayList.class;

    public Map < Integer, Player > SumoQueue = new HashMap < > ();
    Integer SumoQueueSize = 0;

    public Map < Integer, Player > ClassicQueue = new HashMap < > ();
    Integer ClassicQueueSize = 0;

    public Map < Integer, Player > BridgeQueue = new HashMap < > ();
    Integer BridgeQueueSize = 0;

    public Map < Integer, Player > BedwarsQueue = new HashMap < > ();
    Integer BedwarsQueueSize = 0;




    ArrayList<Block> BridgeBlocksPlaced = new ArrayList<>();
    ArrayList<Block> BridgeBlocksBroken = new ArrayList<>();


    public Map < Player, Boolean > DoubleJumps = new HashMap < > ();
    public Map < Player, Player > DuelMenuRequests = new HashMap < > ();

    public Map < Player, Boolean > PlayerVanished = new HashMap < > ();

    public Map < Player, Boolean > SumoGame = new HashMap < > ();

    public Map < Player, Player > SumoRequest = new HashMap < > ();
    public Map < Player, Integer > SumoRequestNo = new HashMap < > ();

    public Boolean SumoActive = false;
    public Boolean SumoQueued = false;

    public Player SumoPlayer1;
    public Player SumoPlayer2;


    public Map < Player, Boolean > ClassicGame = new HashMap < > ();

    public Map < Player, Player > ClassicRequest = new HashMap < > ();
    public Map < Player, Integer > ClassicRequestNo = new HashMap < > ();
    public Map < Player, Integer > ClassicFSSlot = new HashMap < > ();

    public Boolean ClassicActive = false;
    public Boolean ClassicQueued = false;

    public Player ClassicPlayer1;
    public Player ClassicPlayer2;


    public Map < Player, Boolean > BridgeGame = new HashMap < > ();

    public Map < Player, Player > BridgeRequest = new HashMap < > ();
    public Map < Player, Integer > BridgeRequestNo = new HashMap < > ();
    public Map < Player, Integer > BridgeGoals = new HashMap < > ();
    public Map < Player, Integer > BridgeKills = new HashMap < > ();
    public Map < Player, Player > BridgeLastPlayerDamage = new HashMap < > ();

    public Boolean BridgeActive = false;
    public Boolean BridgeQueued = false;
    public Boolean BridgeWaiting = false;

    public Player BridgePlayer1;
    public Player BridgePlayer2;

    public Location BridgeRedSpawn = new Location(Bukkit.getWorld("world"), 5058.5, 125.5, 4995.5);
    public Location BridgeBlueSpawn = new Location(Bukkit.getWorld("world"), 5013.5, 125.5, 4995.5);






    public MongoCollection < Document > col;
    MongoCollection < Document > col2;

    public static FileConfiguration getData() {
        return data.getConfig();
    }

    public static void saveData() {
        data.saveConfig();
    }

    public void loadNPCs() {
        GameProfile DuelsSelector = new GameProfile(UUID.fromString("2c2e5356-cd23-4756-98d2-c2c552773376"), "DuelsSelector");
        DuelsSelector.getProperties().put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTU5ODc1Mzg2OTcxOSwKICAicHJvZmlsZUlkIiA6ICIzM2ViZDMyYmIzMzk0YWQ5YWM2NzBjOTZjNTQ5YmE3ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJEYW5ub0JhbmFubm9YRCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83ZTA2Y2ZjM2Q0M2RiMTJkYTJkYmI2MDI5M2YwNDgyMTQ5NGIwMDI3YjI3NzE1NDczZjFjMTRkYmY1MzBlOGE5IgogICAgfQogIH0KfQ==", "pDo5+R3ZKg+99q/9W+AiPTY3yQlPuHad6siEZr90pxyZ3DnBu/DadzTW5oyFq47B+aiyWtLxeH70WOPUevALfukWQy5KUehk8I2ENeXunONfy7yPC0J3/8NvWtFsxwwhEMD9kawC7uBsza++WEfysSX6rNuMoVp5+23tWj7YLMnBVUvVl2EYMi8CEm6Hf0pcaM+LsTxoFyj4IkyVUIyA1RRS8WeoScwI+Air+nYKZrNMExByY9JKzBM5ACqE2M1ykCSfxzlU9z+70gAlUnfttBqvmbLKs/VqMXfeETDX0/tNJKlJRib2yrkU5NJSrS/GQNKuZ533S4r+7niuKSLXiUjmA05yG28v549YD2Lode0aqd3ZbZZIecLBvwbv67p+2dreHL67793yuCDhIyYqNNthKwS1xHQiurNB/lduXyJjR836j9uN/KQ/lYORf21wM81mtDay8chMwXZ0qz5mjFwndS6YYOiKVd8k2aVoGlIuMUAFlQyrbuEhrLqLn3PeSr50afkp1YkfaMbrZ3NzIgslePnHLzRI65ppjFkbkbbHbXY4AmHnPZl+WGPwzO+cJRotwQVGggdcFt75N+VfZTzNubSytEwB4xmqEiyD7mI+E+/ELps5T6XXnoc+JZp4YQcQ0sTYnLmXaYp2txCdTJTZ2rVO6GL6HGM2Xwven/k="));
        NPC.loadNPC(new Location(Bukkit.getWorld("world"), 74.5, 136, 12.5, -135, 0), DuelsSelector);
    }

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
    @EventHandler(
            priority = EventPriority.HIGHEST,
            ignoreCancelled = true
    )
    public void WeatherChangeEvent(WeatherChangeEvent event) {
        if (event.toWeatherState()) {
            event.setCancelled(true);
            event.getWorld().setWeatherDuration(0);
            event.getWorld().setThundering(false);
        }
    }


    @Override
    public void onEnable() {
        data = new DataManager(this);
        this.getServer().getPluginManager().registerEvents(new ClickNPC(), this);
        this.getServer().getPluginManager().registerEvents(new GrappleHook(), this);
        this.getServer().getPluginManager().registerEvents(new GrappleGun(), this);
        this.getServer().getPluginManager().registerEvents(new MagicToyStick(), this);
        this.getServer().getPluginManager().registerEvents(new KittyCannon(), this);
        this.getServer().getPluginManager().registerEvents(new RainbowWool(), this);
        this.getServer().getPluginManager().registerEvents(new Wool(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        this.getServer().getPluginManager().registerEvents(new Blocks(), this);
        this.getServer().getPluginManager().registerEvents(new Fall(), this);
        this.getServer().getPluginManager().registerEvents(new BedwarsInventory(), this);
        this.getServer().getPluginManager().registerEvents(new ShopKeeper(), this);
        this.getServer().getPluginManager().registerEvents(new Fireball(), this);
        this.getServer().getPluginManager().registerEvents(new BedBug(), this);
        this.getServer().getPluginManager().registerEvents(new ItemMoves(), this);
        this.getServer().getPluginManager().registerEvents(new Potions(), this);

        int interval = 5; //How often the footprints should appear
        Bukkit.getScheduler().runTaskTimer(this, new Step(), interval, interval); //Start running a Timer

        // Connect to MongoDB Database
        MongoClient mongoClient = MongoClients.create("mongodb+srv://teakivy:CJscott05@cluster0.e8rhx.mongodb.net/myFirstDatabase?retryWrites=true&w=majority");
        MongoDatabase database = mongoClient.getDatabase("data");
        col = database.getCollection("accounts");
        col2 = database.getCollection("accounts2");

        // Plugin startup logic
        System.out.println("[Lobby] Started! v1.3.0");
        getServer().getPluginManager().registerEvents(this, this);
        //        getServer().getPluginManager().registerEvents(new NPCManager(this), this);

//
//        for (Document document : col.find()) {
////            Document filter = new Document("UUID", document.getString("UUID"));
////
////            ItemStack QuickBuy = MainShop.createItem(Material.STAINED_GLASS_PANE, ChatColor.GOLD + "Quick Buy", new String[]{ChatColor.GRAY + "Shift click an item to", ChatColor.GRAY + "add it to your Quick Buy"}, 1, (byte) 14);
////            BedwarsInventory.saveQuickBuy(filter, 19, "Wool");
////            BedwarsInventory.saveQuickBuy(filter, 20, "Stone Sword");
////            BedwarsInventory.saveQuickBuy(filter, 21, "Permanent Chainmail Armor");
////            BedwarsInventory.saveQuickBuy(filter, 22, "Quick Buy");
////            BedwarsInventory.saveQuickBuy(filter, 23, "Bow");
////            BedwarsInventory.saveQuickBuy(filter, 24, "Speed II Potion (45 Seconds)");
////            BedwarsInventory.saveQuickBuy(filter, 25, "TNT");
////            BedwarsInventory.saveQuickBuy(filter, 28, "Wood");
////            BedwarsInventory.saveQuickBuy(filter, 29, "Iron Sword");
////            BedwarsInventory.saveQuickBuy(filter, 30, "Permanent Iron Armor");
////            BedwarsInventory.saveQuickBuy(filter, 31, "Permanent Shears");
////            BedwarsInventory.saveQuickBuy(filter, 32, "Arrow");
////            BedwarsInventory.saveQuickBuy(filter, 33, "Invisibility Potion (30 Seconds)");
////            BedwarsInventory.saveQuickBuy(filter, 34, "Water Bucket");
////
////            BedwarsInventory.saveQuickBuy(filter, 37, "Quick Buy");
////            BedwarsInventory.saveQuickBuy(filter, 38, "Quick Buy");
////            BedwarsInventory.saveQuickBuy(filter, 39, "Quick Buy");
////            BedwarsInventory.saveQuickBuy(filter, 40, "Quick Buy");
////            BedwarsInventory.saveQuickBuy(filter, 41, "Quick Buy");
////            BedwarsInventory.saveQuickBuy(filter, 42, "Quick Buy");
////            BedwarsInventory.saveQuickBuy(filter, 43, "Quick Buy");
//
//
//
//////            Document newPlayerDocument = new Document("UUID", document.getString("UUID")).append("name", document.getString("name")).append("coins", document.getInteger("coins")).append("rank", document.getString("rank")).append("plusColor", "RED").append("purchased", new ArrayList<String>()).append("lobbySettings", new Document("doubleJump", ((Document)document.get("lobbySettings")).getBoolean("doubleJump")).append("flight", ((Document)document.get("lobbySettings")).getBoolean("flight")).append("speed", ((Document)document.get("lobbySettings")).getBoolean("speed")).append("playersVisible", ((Document)document.get("lobbySettings")).getBoolean("playersVisible")).append("vanished", false).append("currentGadget", "WOOL")).append("gameSettings", new Document("classic", new Document("swordSlot", 0).append("fishingRodSlot", 1).append("bowSlot", 2).append("flintAndSteelSlot", 3).append("arrowSlot", 9)).append("bridge", new Document("swordSlot", 0).append("bowSlot", 1).append("pickaxeSlot", 2).append("blockSlot1", 3).append("blockSlot2", 4).append("gappleSlot", 5).append("arrowSlot", 8))).append("stats", new Document("sumo", new Document("wins", ((Document)((Document)document.get("stats")).get("sumo")).getInteger("wins")).append("losses", ((Document)((Document)document.get("stats")).get("sumo")).getInteger("losses")).append("gamesPlayed", ((Document)((Document)document.get("stats")).get("sumo")).getInteger("gamesPlayed"))).append("classic", new Document("wins", ((Document)((Document)document.get("stats")).get("classic")).getInteger("wins")).append("losses", ((Document)((Document)document.get("stats")).get("classic")).getInteger("losses")).append("gamesPlayed", ((Document)((Document)document.get("stats")).get("classic")).getInteger("gamesPlayed"))).append("bridge", new Document("wins", ((Document)((Document)document.get("stats")).get("bridge")).getInteger("wins")).append("losses", ((Document)((Document)document.get("stats")).get("bridge")).getInteger("losses")).append("gamesPlayed", ((Document)((Document)document.get("stats")).get("bridge")).getInteger("gamesPlayed")).append("goals", ((Document)((Document)document.get("stats")).get("bridge")).getInteger("wins")*5).append("kills", 0)).append("lobby", new Document("wheatBroken", ((Document)((Document)document.get("stats")).get("lobby")).getInteger("wheatBroken")).append("doubleJumps", ((Document)((Document)document.get("stats")).get("lobby")).getInteger("doubleJumps")).append("blocksPlaced", ((Document)((Document)document.get("stats")).get("lobby")).getInteger("blocksPlaced"))));
//////            col.insertOne(newPlayerDocument);
//            Document filter = new Document("UUID", document.getString("UUID"));
//////            Bson updates = set("losses", "RED_PLUS");
//////            col.findOneAndUpdate(filter, updates);
////
////
//            Document obj = (Document) col.find(filter).first().get("stats");
//            Document gameType = (Document) obj.get("bedwars");
////            Object losses = gameType.get("losses");
////            Object wins = gameType.get("wins");
//            String wins = null;
//
//            if (wins == null){
//                Bson LupdatesWins = set("stats.bedwars.losses", 0);
//                col.findOneAndUpdate(filter, LupdatesWins);
//            }
//
//            if (wins == null){
//                Bson LupdatesWins = set("stats.bedwars.wins", 0);
//                col.findOneAndUpdate(filter, LupdatesWins);
//            }
//            if (wins == null){
//                Bson LupdatesWins = set("stats.bedwars.gamesPlayed", 0);
//                col.findOneAndUpdate(filter, LupdatesWins);
//            }
//            if (wins == null){
//                Bson LupdatesWins = set("stats.bedwars.bedsBroken", 0);
//                col.findOneAndUpdate(filter, LupdatesWins);
//            }
//            if (wins == null){
//                Bson LupdatesWins = set("stats.bedwars.bedsLost", 0);
//                col.findOneAndUpdate(filter, LupdatesWins);
//            }
//            if (wins == null){
//                Bson LupdatesWins = set("stats.bedwars.kills", 0);
//                col.findOneAndUpdate(filter, LupdatesWins);
//            }
//            if (wins == null){
//                Bson LupdatesWins = set("stats.bedwars.finalKills", 0);
//                col.findOneAndUpdate(filter, LupdatesWins);
//            }
//            if (wins == null){
//                Bson LupdatesWins = set("stats.bedwars.deaths", 0);
//                col.findOneAndUpdate(filter, LupdatesWins);
//            }
//            if (wins == null){
//                Bson LupdatesWins = set("stats.bedwars.finalDeaths", 0);
//                col.findOneAndUpdate(filter, LupdatesWins);
//            }
//            System.out.println("Completed " + document.getString("name"));
//        }


        if(!this.getDataFolder().exists()) {
            try {
                this.getDataFolder().mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                PacketReader reader = new PacketReader();
                reader.inject(player);
            }
        }

        loadNPCs();




    }

    @Override
    public void onDisable() {
        if (ClassicActive) {
            classicEventEnd(ClassicPlayer1, ClassicPlayer2, "draw");
        }
        if (SumoActive) {
            SumoEventEnd(SumoPlayer1, SumoPlayer2, "draw");
        }
        if (BridgeActive) {
            BridgeEventEnd(BridgePlayer1, BridgePlayer2, "draw");
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            PacketReader reader = new PacketReader();
            reader.uninject(player);
            Scoreboard sb = player.getScoreboard();
            sb.getTeam(Ranks.getRankPriority(player) + player.getName().substring(0, 0) + player.getName().substring(1)).unregister();
//            sb.getObjective(player.getUniqueId().toString()).unregister();
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (inBedwars(player)) return;
        Block b = event.getBlock();
        BlockState bReplaced = event.getBlockReplacedState();
        if (player.getGameMode() == GameMode.SURVIVAL) {
            if (bReplaced.getType() != Material.AIR) {
                event.setCancelled(true);
            } else {
                if (b.getType() == Material.WOOL) {
                    return;
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
        if (inBedwars(player)) return;
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

//    public int getLobbyDoubleJumps(Player player) {
//        Document filter = new Document("UUID", player.getUniqueId().toString());
//
//        Document obj = (Document) col.find(filter).first().get("stats");
//        Document gameType = (Document) obj.get("lobby");
//        return gameType.getInteger("doubleJumps");
//    }

    public String getRank(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());
        return col.find(filter).first().getString("rank");
    }

    public ArrayList<String> getPurchased(Player player) {
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
                event.setFormat(ChatColor.DARK_RED + "[OWNER] " + player.getDisplayName() + ChatColor.WHITE + ": " + event.getMessage());
                break;
            case "ADMIN":
                event.setFormat(ChatColor.RED + "[ADMIN] " + player.getDisplayName() + ChatColor.WHITE + ": " + event.getMessage());
                break;
            case "MODERATOR":
                event.setFormat(ChatColor.AQUA + "[MOD] " + player.getDisplayName() + ChatColor.WHITE + ": " + event.getMessage());
                break;
            case "HELPER":
                event.setFormat(ChatColor.GREEN + "[HELPER] " + player.getDisplayName() + ChatColor.WHITE + ": " + event.getMessage());
                break;
            case "YOUTUBE":
                event.setFormat(ChatColor.RED + "[" + ChatColor.BOLD + ChatColor.WHITE.toString() + "YOUTUBE" + ChatColor.RESET + ChatColor.RED.toString() + "] " + player.getDisplayName() + ChatColor.WHITE + ": " + event.getMessage());
                break;
            case "TWITCH":
                event.setFormat(ChatColor.DARK_PURPLE + "[" + ChatColor.BOLD + ChatColor.WHITE.toString() + "TWITCH" + ChatColor.RESET + ChatColor.DARK_PURPLE.toString() + "] " + player.getDisplayName() + ChatColor.WHITE + ": " + event.getMessage());
                break;
            case "VIP":
                event.setFormat(ChatColor.GREEN + "[VIP] " + player.getDisplayName() + ChatColor.WHITE + ": " + event.getMessage());
                break;
            case "VIP+":
                event.setFormat(ChatColor.GREEN + "[VIP" + ChatColor.GOLD + "+" + ChatColor.GREEN + "] " + player.getDisplayName() + ChatColor.WHITE + ": " + event.getMessage());
                break;
            case "MVP":
                event.setFormat(ChatColor.AQUA + "[MVP] " + player.getDisplayName() + ChatColor.WHITE + ": " + event.getMessage());
                break;
            case "MVP+":
                event.setFormat(ChatColor.AQUA + "[MVP" + getPlusColor(player) + "+" + ChatColor.AQUA + "] " + player.getDisplayName() + ChatColor.WHITE + ": " + event.getMessage());
                break;
            case "MVP++":
                event.setFormat(ChatColor.GOLD + "[MVP" + getPlusColor(player) + "++" + ChatColor.GOLD + "] " + player.getDisplayName() + ChatColor.WHITE + ": " + event.getMessage());
                break;
            default:
                event.setFormat(ChatColor.GRAY + player.getDisplayName() + ": " + event.getMessage());
                break;
            case "DUCK":
                event.setFormat(ChatColor.YELLOW + "[" + ChatColor.GOLD + "DUCK" + ChatColor.YELLOW + "] " + player.getDisplayName() + ChatColor.WHITE + ": " + event.getMessage());
        }
    }

    public String rankedName(Player player) {
        String rank = getRank(player);

        switch (rank) {
            case "OWNER":
                return ChatColor.DARK_RED + "[OWNER] " + player.getName();
            case "ADMIN":
                return ChatColor.RED + "[ADMIN] " + player.getName();
            case "MODERATOR":
                return ChatColor.AQUA + "[MOD] " + player.getName();
            case "HELPER":
                return ChatColor.GREEN + "[HELPER] " + player.getName();
            case "YOUTUBE":
                return ChatColor.RED + "[" + ChatColor.BOLD + ChatColor.WHITE.toString() + "YOUTUBE" + ChatColor.RESET + ChatColor.RED.toString() + "] " + player.getName();
            case "TWITCH":
                return ChatColor.DARK_PURPLE + "[" + ChatColor.BOLD + ChatColor.WHITE.toString() + "TWITCH" + ChatColor.RESET + ChatColor.DARK_PURPLE.toString() + "] " + player.getName();
            case "VIP":
                return ChatColor.GREEN + "[VIP] " + player.getName();
            case "VIP+":
                return ChatColor.GREEN + "[VIP" + ChatColor.GOLD + "+" + ChatColor.GREEN + "] " + player.getName();
            case "MVP":
                return ChatColor.AQUA + "[MVP] " + player.getName();
            case "MVP+":
                return ChatColor.AQUA + "[MVP" + getPlusColor(player) + "+" + ChatColor.AQUA + "] " + player.getName();
            case "MVP++":
                return ChatColor.GOLD + "[MVP" + getPlusColor(player) + "++" + ChatColor.GOLD + "] " + player.getName();
            default:
                return ChatColor.GRAY + player.getName();
            case "DUCK":
                return ChatColor.YELLOW + "[" + ChatColor.GOLD + "DUCK" + ChatColor.YELLOW + "] " + player.getName();
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

    public int getClassicSwordSlot(Player player, Document obj) {
        Document gameType = (Document) obj.get("classic");
        return gameType.getInteger("swordSlot");
    }



    public int getClassicFishingRodSlot(Player player, Document obj) {
        Document gameType = (Document) obj.get("classic");
        return gameType.getInteger("fishingRodSlot");
    }

    public int getClassicBowSlot(Player player, Document obj) {
        Document gameType = (Document) obj.get("classic");
        return gameType.getInteger("bowSlot");
    }

    public int getClassicFlintAndSteelSlot(Player player, Document obj) {
        Document gameType = (Document) obj.get("classic");
        return gameType.getInteger("flintAndSteelSlot");
    }

    public int getClassicArrowSlot(Player player, Document obj) {
        Document gameType = (Document) obj.get("classic");
        return gameType.getInteger("arrowSlot");
    }

    public int getSumoWins(Player player, Document obj) {
        Document gameType = (Document) obj.get("sumo");
        return gameType.getInteger("wins");
    }

    public int getSumoLosses(Player player, Document obj) {
        Document gameType = (Document) obj.get("sumo");
        return gameType.getInteger("losses");
    }
    public int getSumoGames(Player player, Document obj) {
        Document gameType = (Document) obj.get("sumo");
        return gameType.getInteger("gamesPlayed");
    }

    //    public int getQ
    public int getClassicWins(Player player, Document obj) {
        Document gameType = (Document) obj.get("classic");
        return gameType.getInteger("wins");
    }
    public int getClassicLosses(Player player, Document obj) {
        Document gameType = (Document) obj.get("classic");
        return gameType.getInteger("losses");
    }
    public int getClassicGames(Player player, Document obj) {
        Document gameType = (Document) obj.get("classic");
        return gameType.getInteger("gamesPlayed");
    }
    public int getBridgeWins(Player player, Document obj) {
        Document gameType = (Document) obj.get("bridge");
        if (gameType != null) {
            return gameType.getInteger("wins");
        } else {
            return 0;
        }
    }
    public int getBridgeLosses(Player player, Document obj) {
        Document gameType = (Document) obj.get("bridge");
        return gameType.getInteger("losses");
    }
    public int getBridgeGames(Player player, Document obj) {
        Document gameType = (Document) obj.get("bridge");
        return gameType.getInteger("gamesPlayed");
    }
    public int getBridgeGoals(Player player, Document obj) {
        Document gameType = (Document) obj.get("bridge");
        return gameType.getInteger("goals");
    }
    public int getBridgeKills(Player player, Document obj) {
        Document gameType = (Document) obj.get("bridge");
        return gameType.getInteger("kills");
    }

    public int getLobbyWheat(Player player, Document obj) {
        Document gameType = (Document) obj.get("lobby");
        return gameType.getInteger("wheatBroken");
    }
    public int getLobbyDoubleJumps(Player player, Document obj) {
        Document gameType = (Document) obj.get("lobby");
        return gameType.getInteger("doubleJumps");
    }
    public int getLobbyBlocks(Player player, Document obj) {
        Document gameType = (Document) obj.get("lobby");
        return gameType.getInteger("blocksPlaced");
    }
    public int getBedwarsWins(Player player, Document obj) {
        Document gameType = (Document) obj.get("bedwars");
        return gameType.getInteger("wins");
    }
    public int getBedwarsLosses(Player player, Document obj) {
        Document gameType = (Document) obj.get("bedwars");
        return gameType.getInteger("losses");
    }
    public int getBedwarsGames(Player player, Document obj) {
        Document gameType = (Document) obj.get("bedwars");
        return gameType.getInteger("gamesPlayed");
    }
    public int getBedwarsBedsBroken(Player player, Document obj) {
        Document gameType = (Document) obj.get("bedwars");
        return gameType.getInteger("bedsBroken");
    }
    public int getBedwarsBedsLost(Player player, Document obj) {
        Document gameType = (Document) obj.get("bedwars");
        return gameType.getInteger("bedsLost");
    }
    public int getBedwarsKills(Player player, Document obj) {
        Document gameType = (Document) obj.get("bedwars");
        return gameType.getInteger("kills");
    }
    public int getBedwarsFinalKills(Player player, Document obj) {
        Document gameType = (Document) obj.get("bedwars");
        return gameType.getInteger("finalKills");
    }
    public int getBedwarsDeaths(Player player, Document obj) {
        Document gameType = (Document) obj.get("bedwars");
        return gameType.getInteger("deaths");
    }
    public int getBedwarsFinalDeaths(Player player, Document obj) {
        Document gameType = (Document) obj.get("bedwars");
        return gameType.getInteger("finalDeaths");
    }



    public int getSumoWins(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("stats");
        Document gameType = (Document) obj.get("sumo");
        return gameType.getInteger("wins");
    }

    public int getSumoLosses(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("stats");
        Document gameType = (Document) obj.get("sumo");
        return gameType.getInteger("losses");
    }
    public int getSumoGames(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("stats");
        Document gameType = (Document) obj.get("sumo");
        return gameType.getInteger("gamesPlayed");
    }

//    public int getQ
    public int getClassicWins(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("stats");
        Document gameType = (Document) obj.get("classic");
        return gameType.getInteger("wins");
    }
    public int getClassicLosses(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("stats");
        Document gameType = (Document) obj.get("classic");
        return gameType.getInteger("losses");
    }
    public int getClassicGames(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("stats");
        Document gameType = (Document) obj.get("classic");
        return gameType.getInteger("gamesPlayed");
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
    public int getBridgeLosses(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("stats");
        Document gameType = (Document) obj.get("bridge");
        return gameType.getInteger("losses");
    }
    public int getBridgeGames(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("stats");
        Document gameType = (Document) obj.get("bridge");
        return gameType.getInteger("gamesPlayed");
    }
    public int getBridgeGoals(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("stats");
        Document gameType = (Document) obj.get("bridge");
        return gameType.getInteger("goals");
    }
    public int getBridgeKills(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("stats");
        Document gameType = (Document) obj.get("bridge");
        return gameType.getInteger("kills");
    }

    public int getBedwarsWins(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("stats");
        Document gameType = (Document) obj.get("bedwars");
        return gameType.getInteger("wins");
    }

    public int getLobbyWheat(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("stats");
        Document gameType = (Document) obj.get("lobby");
        return gameType.getInteger("wheatBroken");
    }
    public int getLobbyDoubleJumps(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("stats");
        Document gameType = (Document) obj.get("lobby");
        return gameType.getInteger("doubleJumps");
    }
    public int getLobbyBlocks(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("stats");
        Document gameType = (Document) obj.get("lobby");
        return gameType.getInteger("blocksPlaced");
    }

    public String getPlayerUUID(String name) {
        long count = col.countDocuments(new Document("name", name));
        if (count > 0) {
            Document filter = new Document("name", name);
            return col.find(filter).first().getString("UUID");
        } else {
            return null;
        }
    }

    public String getPlayerName(String UUID) {
        long count = col.countDocuments(new Document("UUID", UUID));
        if (count > 0) {
            Document filter = new Document("UUID", UUID);
            return col.find(filter).first().getString("name");
        } else {
            return null;
        }
    }

    public String getPlayerName(UUID UUID) {
        return getPlayerName(UUID.toString());
    }



    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        System.out.println(((CraftPlayer) player).getHandle().playerConnection.networkManager.getRawAddress());



        Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
        player.setScoreboard(sb);
//        if (sb.getObjective(player.getUniqueId().toString()) != null) {
//            sb.getObjective(player.getUniqueId().toString()).unregister();
//        }


        PlayerInventory inventory = player.getInventory();

        player.teleport(new Location(getServer().getWorld("world"), 86.5, 137.5, 3.5, 90, 0));
        event.getPlayer().getInventory().setHeldItemSlot(0);
        long count = col.countDocuments(new Document("UUID", player.getUniqueId().toString()));
        if (count > 0) {
            System.out.println("Player: " + player.getName() + " already exists in DB!");
        } else {
            System.out.println("Created DB Document for Player: " + player.getName());
            Document newPlayerDocument = new Document("UUID", player.getUniqueId().toString()).append("name", player.getName()).append("coins", 0).append("rank", "DEFAULT").append("plusColor", "RED").append("purchased", new ArrayList<String>().add("RED_PLUS")).append("lobbySettings", new Document("doubleJump", true).append("flight", false).append("speed", true).append("playersVisible", true).append("vanished", false).append("currentGadget", "WOOL")).append("gameSettings", new Document("classic", new Document("swordSlot", 0).append("fishingRodSlot", 1).append("bowSlot", 2).append("flintAndSteelSlot", 3).append("arrowSlot", 9)).append("bridge", new Document("swordSlot", 0).append("bowSlot", 1).append("pickaxeSlot", 2).append("blockSlot1", 3).append("blockSlot2", 4).append("gappleSlot", 5).append("arrowSlot", 8)).append("bedwars", new Document("quickBuy", new Document("19", "STAINED_GLASS_PANE:14 1 name:Wool").append("20", "STAINED_GLASS_PANE:14 1 name:Stone_Sword").append("21", "STAINED_GLASS_PANE:14 1 name:Permanent_Chainmail_Armor").append("22", "STAINED_GLASS_PANE:14 1 name:Quick_Buy").append("23", "STAINED_GLASS_PANE:14 1 name:Bow").append("24", "STAINED_GLASS_PANE:14 1 name:Speed_II_Potion_(45_Seconds)").append("25", "STAINED_GLASS_PANE:14 1 name:TNT").append("28", "STAINED_GLASS_PANE:14 1 name:Wood").append("29", "STAINED_GLASS_PANE:14 1 name:Iron_Sword").append("30", "STAINED_GLASS_PANE:14 1 name:Permanent_Iron_Armor").append("31", "STAINED_GLASS_PANE:14 1 name:Permanent_Shears").append("32", "STAINED_GLASS_PANE:14 1 name:Arrow").append("33", "STAINED_GLASS_PANE:14 1 name:Invisibility_Potion_(30_Seconds)").append("34", "STAINED_GLASS_PANE:14 1 name:Water_Bucket").append("37", "STAINED_GLASS_PANE:14 1 name:Quick_Buy").append("38", "STAINED_GLASS_PANE:14 1 name:Quick_Buy").append("39", "STAINED_GLASS_PANE:14 1 name:Quick_Buy").append("40", "STAINED_GLASS_PANE:14 1 name:Quick_Buy").append("41", "STAINED_GLASS_PANE:14 1 name:Quick_Buy").append("42", "STAINED_GLASS_PANE:14 1 name:Quick_Buy").append("43", "STAINED_GLASS_PANE:14 1 name:Quick_Buy")))).append("stats", new Document("sumo", new Document("wins", 0).append("losses", 0).append("gamesPlayed", 0)).append("classic", new Document("wins", 0).append("losses", 0).append("gamesPlayed", 0)).append("bridge", new Document("wins", 0).append("losses", 0).append("gamesPlayed", 0).append("goals", 0).append("kills", 0)).append("lobby", new Document("wheatBroken", 0).append("doubleJumps", 0).append("blocksPlaced", 0)));
            col.insertOne(newPlayerDocument);
            Document filter = new Document("UUID", player.getUniqueId());
            Bson updates = push("purchased", "RED_PLUS");
            col.findOneAndUpdate(filter, updates);
        }

        if (sb.getTeam(Ranks.getRankPriority(player) + player.getName().substring(0, 0) + player.getName().substring(1) ) != null) {
            sb.getTeam(Ranks.getRankPriority(player) + player.getName().substring(0, 0) + player.getName().substring(1)).unregister();
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
                Wool.woolItem(),
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

        if (!Bedwars.BedwarsGame.containsKey(player)) {
            Bedwars.BedwarsGame.put(player, false);
        }
        if (!Bedwars.BedwarsKills.containsKey(player)) {
            Bedwars.BedwarsKills.put(player, 0);
        }
        if (!Bedwars.BedwarsLastPlayerDamage.containsKey(player)) {
            Bedwars.BedwarsLastPlayerDamage.put(player, player);
        }


        if (!BridgeLastPlayerDamage.containsKey(player)) {
            BridgeLastPlayerDamage.put(player, player);
        }
        if (!PlayerVanished.containsKey(player)) {
            PlayerVanished.put(player, false);
        }


        for (Player toHide: Bukkit.getServer().getOnlinePlayers()) {
            if (!SumoGame.get(toHide) && !ClassicGame.get(toHide) && !BridgeGame.get(toHide)) {
                Scoreboards.updateLobbyScoreboard(toHide);
            }
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

        Scoreboard healthboard = player.getScoreboard();
        Objective sbHealth;
        if (healthboard.getObjective("playerHealth") == null) {
            sbHealth = healthboard.registerNewObjective("playerHealth", "dummy");
            sbHealth.setDisplayName(ChatColor.RED + "❤");
            sbHealth.setDisplaySlot(DisplaySlot.BELOW_NAME);
        } else {
            sbHealth = healthboard.getObjective("playerHealth");
        }




        sbHealth.getScore(player.getName()).setScore((int) player.getHealth());

        for (Player p: Bukkit.getOnlinePlayers()) {
//            player.getScoreboard().getObjective(player.getUniqueId().toString()).getScore(p.getName()).setScore((int) p.getHealth());
            if (!p.equals(player)) {
                p.getScoreboard().getObjective("playerHealth").getScore(player.getName()).setScore((int) player.getHealth());
            }
        }

        if (NPC.getNPCs() != null && !NPC.getNPCs().isEmpty()) {
            NPC.addJoinPacket(player);
        }
        GameProfile ProfileSelector = new GameProfile(UUID.randomUUID(), "ProfileSelector");
        String[] skin = NPC.getSkin(player.getName());
        ProfileSelector.getProperties().put("textures", new Property("textures", skin[0], skin[1]));
        NPC.loadPlayerNPC(new Location(Bukkit.getWorld("world"), 74.5, 136, -5.5, -45, 0), ProfileSelector, player);

        PacketReader reader = new PacketReader();
        reader.inject(player);

        String playerRank = getRank(player);


        player.setScoreboard(healthboard);


        for (Player p : Bukkit.getOnlinePlayers()) {
                Scoreboard scoreboard = p.getScoreboard();

                Team team = scoreboard.getTeam(Ranks.getRankPriority(player) + player.getName().substring(0, 0) + player.getName().substring(1));
                if (team == null) {
                    team = scoreboard.registerNewTeam(Ranks.getRankPriority(player) + player.getName().substring(0, 0) + player.getName().substring(1));
                }

                switch (playerRank) {
                    case "OWNER":
                        team.setPrefix(ChatColor.DARK_RED + "[OWNER] ");
                        break;
                    case "ADMIN":
                        team.setPrefix(ChatColor.RED + "[ADMIN] ");
                        break;
                    case "MODERATOR":
                        team.setPrefix(ChatColor.AQUA + "[MOD] ");
                        break;
                    case "HELPER":
                        team.setPrefix(ChatColor.GREEN + "[HELPER] ");
                        break;
                    case "YOUTUBE":
                        team.setPrefix(ChatColor.RED + "[" + ChatColor.WHITE + "YOUTUBE" + ChatColor.RED + "] ");
                        break;
                    case "TWITCH":
                        team.setPrefix(ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "TWITCH" + ChatColor.DARK_PURPLE + "] ");
                        break;
                    default:
                        team.setPrefix(ChatColor.GRAY + "");
                        break;
                    case "VIP":
                        team.setPrefix(ChatColor.GREEN + "[VIP] ");
                        break;
                    case "VIP+":
                        team.setPrefix(ChatColor.GREEN + "[VIP" + ChatColor.GOLD + "+" + ChatColor.GREEN + "] ");
                        break;
                    case "MVP":
                        team.setPrefix(ChatColor.AQUA + "[MVP] ");
                        break;
                    case "MVP+":
                        team.setPrefix(ChatColor.AQUA + "[MVP" + getPlusColor(player) + "+" + ChatColor.AQUA + "] ");
                        break;
                    case "MVP++":
                        team.setPrefix(ChatColor.GOLD + "[MVP" + getPlusColor(player) + "++" + ChatColor.GOLD + "] ");
                        break;
                    case "DUCK":
                        team.setPrefix(ChatColor.YELLOW + "[" + ChatColor.GOLD + "DUCK" + ChatColor.YELLOW + "] ");
                        break;
                }
                team.addEntry(player.getName());
        }

        Scoreboard scoreboard = player.getScoreboard();
        for (Player p : Bukkit.getOnlinePlayers()) {

            Team team = scoreboard.getTeam(Ranks.getRankPriority(p) + p.getName().substring(0, 0) + p.getName().substring(1));
            if (team == null) {
                team = scoreboard.registerNewTeam(Ranks.getRankPriority(p) + p.getName().substring(0, 0) + p.getName().substring(1));
            }

            switch (getRank(p)) {
                case "OWNER":
                    team.setPrefix(ChatColor.DARK_RED + "[OWNER] ");
                    break;
                case "ADMIN":
                    team.setPrefix(ChatColor.RED + "[ADMIN] ");
                    break;
                case "MODERATOR":
                    team.setPrefix(ChatColor.AQUA + "[MOD] ");
                    break;
                case "HELPER":
                    team.setPrefix(ChatColor.GREEN + "[HELPER] ");
                    break;
                case "YOUTUBE":
                    team.setPrefix(ChatColor.RED + "[" + ChatColor.WHITE + "YOUTUBE" + ChatColor.RED + "] ");
                    break;
                case "TWITCH":
                    team.setPrefix(ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "TWITCH" + ChatColor.DARK_PURPLE + "] ");
                    break;
                default:
                    team.setPrefix(ChatColor.GRAY + "");
                    break;
                case "VIP":
                    team.setPrefix(ChatColor.GREEN + "[VIP] ");
                    break;
                case "VIP+":
                    team.setPrefix(ChatColor.GREEN + "[VIP" + ChatColor.GOLD + "+" + ChatColor.GREEN + "] ");
                    break;
                case "MVP":
                    team.setPrefix(ChatColor.AQUA + "[MVP] ");
                    break;
                case "MVP+":
                    team.setPrefix(ChatColor.AQUA + "[MVP" + getPlusColor(player) + "+" + ChatColor.AQUA + "] ");
                    break;
                case "MVP++":
                    team.setPrefix(ChatColor.GOLD + "[MVP" + getPlusColor(player) + "++" + ChatColor.GOLD + "] ");
                    break;
                case "DUCK":
                    team.setPrefix(ChatColor.YELLOW + "[" + ChatColor.GOLD + "DUCK" + ChatColor.YELLOW + "] ");
                    break;
            }
            team.addEntry(p.getName());
        }


        Team team = scoreboard.getTeam(Ranks.getRankPriority(player) + player.getName().substring(0, 0) + player.getName().substring(1));
        if (team == null) {
            team = scoreboard.registerNewTeam(Ranks.getRankPriority(player) + player.getName().substring(0, 0) + player.getName().substring(1));
        }

//        ProtocolManager pm = ProtocolLibrary.getProtocolManager();
//        PacketContainer packet = pm.createPacket(PacketType.Play.Server.ANIMATION);
//        packet.getModifier().writeDefaults();
//        packet.getIntegers().write(0, event.getPlayer().getEntityId()).write(1, 0);
//        try {
//            pm.sendServerPacket(event.getPlayer(), packet);
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }





        switch (playerRank) {
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
                event.setJoinMessage(ChatColor.AQUA + "[MVP" + getPlusColor(player) + "+" + ChatColor.AQUA + "] " + player.getName() + ChatColor.AQUA + " joined the lobby!");
                team.setPrefix(ChatColor.AQUA + "[MVP" + getPlusColor(player) + "+" + ChatColor.AQUA + "] ");
                break;
            case "MVP++":
                event.setJoinMessage(ChatColor.GOLD + "[MVP" + getPlusColor(player) + "++" + ChatColor.GOLD + "] " + player.getName() + ChatColor.GOLD + " joined the lobby!");
                team.setPrefix(ChatColor.GOLD + "[MVP" + getPlusColor(player) + "++" + ChatColor.GOLD + "] ");
                break;
            case "DUCK":
                event.setJoinMessage(ChatColor.YELLOW + "[" + ChatColor.GOLD + "DUCK" + ChatColor.YELLOW + "] " + player.getName() + ChatColor.GOLD + " joined the lobby!");
                team.setPrefix(ChatColor.YELLOW + "[" + ChatColor.GOLD + "DUCK" + ChatColor.YELLOW + "] ");
        }
        team.addEntry(player.getName());
        player.setScoreboard(scoreboard);

        Tablist.updateTablist();
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

    public static boolean inBedwars(Player player) {
        return Bedwars.isInGame(player);
    }

    public void cancelGame(Player player) {
        if (SumoGame.get(player)) {
            if (SumoPlayer1 == player) {
                SumoEventEnd(SumoPlayer2, SumoPlayer1, "normal");
            } else {
                SumoEventEnd(SumoPlayer1, SumoPlayer2, "normal");
            }
        }


        if (ClassicGame.get(player)) {
            if (ClassicPlayer1 == player) {
                classicEventEnd(ClassicPlayer2, ClassicPlayer1, "normal");
            } else {
                classicEventEnd(ClassicPlayer1, ClassicPlayer2, "normal");
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
                if (BridgeGame.get((Player) p.getShooter())) {
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
        if (inBedwars(player)) return;
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
                    scheduler.scheduleSyncDelayedTask(this, () -> {
                        Crops cropsDate = (Crops) blockBroken.getState().getData();
//                        cropsDate.setState(CropState.RIPE);
                        BlockState state = blockBroken.getState();
                        state.setData(new Crops(CropState.RIPE));
                        state.update();
                    }, 20 * 60 * 3);
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
                if (!Bedwars.isInGame(player)) {
                    if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                        event.setCancelled(true);
                    } else {
                        if (SumoGame.get(player)) {
                            if (SumoActive) {
                                player.setHealth(20);
                                event.setDamage(0);
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
            if (!ClassicGame.get(player) && !BridgeGame.get(player) && !inBedwars(player)) {
                if (event.getClickedInventory() == null) return;
                if (!event.getClickedInventory().getName().startsWith(ChatColor.GOLD + "Game Settings")) {
                    event.setCancelled(true);
                }
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
                rankMenuMeta.setDisplayName(ChatColor.AQUA + "Rank Switcher");
                rankMenuMeta.setLore(Arrays.asList(ChatColor.GRAY + "Change your rank and", ChatColor.GRAY + "become whoever you want!", "", ChatColor.YELLOW + "Click To Switch!"));
                rankMenu.setItemMeta(rankMenuMeta);

                ItemStack PlusColor = new ItemStack(Material.INK_SACK, 1, (short) 14);
                ItemMeta PlusColorMeta = PlusColor.getItemMeta();
                PlusColorMeta.setDisplayName(ChatColor.GOLD + "Plus Colors");
                PlusColorMeta.setLore(Arrays.asList(ChatColor.GRAY + "Change your rank's", ChatColor.GRAY + "Plus Colors!", "", ChatColor.YELLOW + "Click To Switch!"));
                PlusColor.setItemMeta(PlusColorMeta);

                ItemStack GameSettings = new ItemStack(Material.COMMAND);
                ItemMeta GameSettingsMeta = GameSettings.getItemMeta();
                GameSettingsMeta.setDisplayName(ChatColor.GOLD + "Game Settings");
                GameSettingsMeta.setLore(Arrays.asList(ChatColor.GRAY + "Change your game settings!", "", ChatColor.YELLOW + "Click To Change!"));
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

                        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.YELLOW + "Statistics")) {
                            openStatsMenu(player, player);
                        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Game Settings")) {
                            openKitEditorMenu(player);
                        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Rank Switcher")) {
                            openRankSwitcher(player);
                        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Plus Colors")) {
                            openPlusColorSwitcher(player);
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
                        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Bedwars Duels" + ChatColor.GRAY + " - " + ChatColor.BOLD + "" + ChatColor.LIGHT_PURPLE + "1v1")) {
                            silentLeaveGameQueue(player);
                            addPlayerToQueue(player, "bedwars");
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

//    public void setRank(Player player, String rank) {
//        Document filter = new Document("UUID", player.getUniqueId().toString());
//        Bson updates = push("rank", rank);
//        col.findOneAndUpdate(filter, updates);
//    }

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
        if (inBedwars(player)) return;

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
//            player.getScoreboard().getObjective(player.getUniqueId().toString()).getScore(p.getName()).setScore((int) p.getHealth());
            if (!p.equals(player)) {
                p.getScoreboard().getObjective("playerHealth").getScore(player.getName()).setScore((int) player.getHealth());
            }
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
            MagicToyStick.ToyStick.put(player, false);
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

    public void openProfileMenu(Player player) {
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
        rankMenuMeta.setDisplayName(ChatColor.AQUA + "Rank Switcher");
        rankMenuMeta.setLore(Arrays.asList(ChatColor.GRAY + "Change your rank and", ChatColor.GRAY + "become whoever you want!", "", ChatColor.YELLOW + "Click To Switch!"));
        rankMenu.setItemMeta(rankMenuMeta);

        ItemStack PlusColor = new ItemStack(Material.INK_SACK, 1, (short) 14);
        ItemMeta PlusColorMeta = PlusColor.getItemMeta();
        PlusColorMeta.setDisplayName(ChatColor.GOLD + "Plus Colors");
        PlusColorMeta.setLore(Arrays.asList(ChatColor.GRAY + "Change your rank's", ChatColor.GRAY + "Plus Colors!", "", ChatColor.YELLOW + "Click To Switch!"));
        PlusColor.setItemMeta(PlusColorMeta);

        ItemStack Stats = new ItemStack(Material.GOLD_BLOCK);
        ItemMeta StatsMeta = Stats.getItemMeta();
        StatsMeta.setDisplayName(ChatColor.YELLOW + "Statistics");
        StatsMeta.setLore(Arrays.asList(ChatColor.GRAY + "See your game stats!", "", ChatColor.YELLOW + "Click To View!"));
        Stats.setItemMeta(StatsMeta);

        ItemStack GameSettings = new ItemStack(Material.COMMAND);
        ItemMeta GameSettingsMeta = GameSettings.getItemMeta();
        GameSettingsMeta.setDisplayName(ChatColor.GOLD + "Game Settings");
        GameSettingsMeta.setLore(Arrays.asList(ChatColor.GRAY + "Change your game settings!", "", ChatColor.YELLOW + "Click To Change!"));
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

    @EventHandler
    public void onItemClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (inBedwars(player)) return;

        if (player.getItemInHand().getType() == Material.SKULL_ITEM) {
            openProfileMenu(player);
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
            } else if (!getPlayersVisible(player)){
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
        if (SumoQueue.containsValue(player) || ClassicQueue.containsValue(player) || BridgeQueue.containsValue(player)|| BedwarsQueue.containsValue(player)) {
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
        String SumoPlayMessage = ChatColor.YELLOW + "Click To Play!";
        ChatColor SumoColor = ChatColor.GREEN;
        if (ClassicQueued) {
            SumoPlayMessage = ChatColor.RED + "Queue Full!";
            SumoColor = ChatColor.RED;
        }
        ItemStack sumo = new ItemStack(Material.SLIME_BALL);
        ItemMeta sumoMeta = sumo.getItemMeta();
        sumoMeta.setDisplayName(SumoColor + "Sumo Duels" + ChatColor.GRAY + " - " + ChatColor.BOLD + "" + ChatColor.LIGHT_PURPLE + "1v1");
        sumoMeta.setLore(Arrays.asList(ChatColor.GRAY + "Knock your opponent out of the", ChatColor.GRAY + "arena with your fists!","",ChatColor.WHITE + "Wins: " + ChatColor.YELLOW + getSumoWins(player), "", SumoQueueString, SumoPlayMessage));
        sumo.setItemMeta(sumoMeta);

        String ClassicQueueString;
        if (ClassicQueueSize > 0) {
            ClassicQueueString = ChatColor.GREEN.toString() + "Players In Queue: " + ClassicQueueSize;
        } else {
            ClassicQueueString = ChatColor.DARK_GRAY.toString() + "Players In Queue: " + ClassicQueueSize;
        }
        String ClassicPlayMessage = ChatColor.YELLOW + "Click To Play!";
        ChatColor ClassicColor = ChatColor.GREEN;
        if (ClassicQueued) {
            ClassicPlayMessage = ChatColor.RED + "Queue Full!";
            ClassicColor = ChatColor.RED;
        }
        ItemStack classic = new ItemStack(Material.FISHING_ROD);
        ItemMeta classicMeta = classic.getItemMeta();
        classicMeta.setDisplayName(ClassicColor + "Classic Duels" + ChatColor.GRAY + " - " + ChatColor.BOLD + "" + ChatColor.LIGHT_PURPLE + "1v1");
        classicMeta.setLore(Arrays.asList(ChatColor.GRAY + "Iron gear 1v1 Duel!","",ChatColor.WHITE + "Wins: " + ChatColor.YELLOW + getClassicWins(player), "", ClassicQueueString, ClassicPlayMessage));
        classic.setItemMeta(classicMeta);

        String BedwarsQueueString;
        if (BedwarsQueueSize > 0) {
            BedwarsQueueString = ChatColor.GREEN.toString() + "Players In Queue: " + BedwarsQueueSize;
        } else {
            BedwarsQueueString = ChatColor.DARK_GRAY.toString() + "Players In Queue: " + BedwarsQueueSize;
        }

        String BedwarsPlayMessage = ChatColor.YELLOW + "Click To Play!";
        ChatColor BedwarsColor = ChatColor.GREEN;
        if (Bedwars.BedwarsQueued) {
            BedwarsPlayMessage = ChatColor.RED + "Queue Full!";
            BedwarsColor = ChatColor.RED;
        }
        ItemStack bedwars = new ItemStack(Material.BED);
        ItemMeta bedwarsMeta = bedwars.getItemMeta();
        bedwarsMeta.setDisplayName(BedwarsColor + "Bedwars Duels" + ChatColor.GRAY + " - " + ChatColor.BOLD + "" + ChatColor.LIGHT_PURPLE + "1v1");
        bedwarsMeta.setLore(Arrays.asList(ChatColor.GRAY + "Cross the bridge and score", ChatColor.GRAY + "more goals than the opposing team!","",ChatColor.WHITE + "Wins: " + ChatColor.YELLOW + getBedwarsWins(player), "", BedwarsQueueString, BedwarsPlayMessage));
        bedwars.setItemMeta(bedwarsMeta);

        String BridgeQueueString;
        if (BridgeQueueSize > 0) {
            BridgeQueueString = ChatColor.GREEN.toString() + "Players In Queue: " + BridgeQueueSize;
        } else {
            BridgeQueueString = ChatColor.DARK_GRAY.toString() + "Players In Queue: " + BridgeQueueSize;
        }

        String BridgePlayMessage = ChatColor.YELLOW + "Click To Play!";
        ChatColor BridgeColor = ChatColor.GREEN;
        if (ClassicQueued) {
            BridgePlayMessage = ChatColor.RED + "Queue Full!";
            BridgeColor = ChatColor.RED;
        }
        ItemStack bridge = new ItemStack(Material.STAINED_CLAY, 1,  (short) 14);
        ItemMeta bridgeMeta = bridge.getItemMeta();
        bridgeMeta.setDisplayName(BridgeColor + "Bridge Duels" + ChatColor.GRAY + " - " + ChatColor.BOLD + "" + ChatColor.LIGHT_PURPLE + "1v1");
        bridgeMeta.setLore(Arrays.asList(ChatColor.GRAY + "Cross the bridge and score", ChatColor.GRAY + "more goals than the opposing team!","",ChatColor.WHITE + "Wins: " + ChatColor.YELLOW + getBridgeWins(player), "", BridgeQueueString, BridgePlayMessage));
        bridge.setItemMeta(bridgeMeta);

        ItemStack leaveQueue = new ItemStack(Material.BARRIER);
        ItemMeta leaveQueueMenu = leaveQueue.getItemMeta();
        leaveQueueMenu.setDisplayName(ChatColor.RED + "Leave Queue");
        leaveQueue.setItemMeta(leaveQueueMenu);

        ItemStack air = new ItemStack(Material.AIR);


        gui.setItem(10, sumo);
        gui.setItem(12, classic);
        gui.setItem(16, bedwars);
        gui.setItem(14, bridge);
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



                    Vector vector = player.getLocation().getDirection().multiply(1.5).setY(1.3);
                    player.setVelocity(vector);
                    player.playSound(player.getLocation(), Sound.FIREWORK_LAUNCH, 1, 1);
                    DoubleJumps.put(player, true);
//                    Document filter = new Document("UUID", player.getUniqueId().toString());
                    Bson updates = inc("stats.lobby.doubleJumps", 1);
                    col.findOneAndUpdate(filter, updates);
                }
            }

        }
    }

    final int[] SumoTime = {2 * 60};
    public final String[] SumoTimeFormatted = {"00:00"};
    int sumoTimerID;

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

        player1.setHealth(20);
        player1.setHealth(20);

        Scoreboards.removeLobbyScoreboard(player1);
        Scoreboards.removeLobbyScoreboard(player2);

        Scoreboards.updateSumoScoreboard(player1);
        Scoreboards.updateSumoScoreboard(player2);

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

        SumoTime[0] = 2*60;


        BukkitScheduler Sumoscheduler = Bukkit.getServer().getScheduler();
        sumoTimerID = Sumoscheduler.runTaskTimer(this, new Runnable()
        {

            @Override
            public void run()
            {
                if (SumoActive) {
                    if (SumoTime[0] == 0) {
                        SumoEventEnd(SumoPlayer1, SumoPlayer2, "draw");

                        Bukkit.getScheduler().cancelTask(sumoTimerID);
                        return;
                    }

                    int minutes = SumoTime[0] / 60;
                    int seconds = SumoTime[0] % 60;
                    String secondsFormated = "00";
                    if (seconds < 10) {
                        secondsFormated = "0" + seconds;
                    } else {
                        secondsFormated = "" + seconds;
                    }

                    SumoTimeFormatted[0] = minutes + ":" + secondsFormated;

                    Scoreboards.updateSumoScoreboard(player1);
                    Scoreboards.updateSumoScoreboard(player2);

                    SumoTime[0]--;
                } else {
                    if (!SumoQueued) {
                        SumoTime[0] = 0;
                    }
                }
            }
        }, 0L, 20L).getTaskId();

    }

    public void SumoEventEnd(Player winner, Player loser, String type) {
        if (type == "normal") {
            Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "----------------");
            Bukkit.getServer().broadcastMessage(ChatColor.GREEN + winner.getName() + ChatColor.GOLD + " just won a Sumo Duel against " + ChatColor.RED + loser.getName() + ChatColor.GOLD + "!");
            Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "----------------");
        } else {
            Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "----------------");
            Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + winner.getName() + ChatColor.GOLD + " just tied a Sumo Duel against " + ChatColor.YELLOW + loser.getName() + ChatColor.GOLD + "!");
            Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "----------------");
        }

        Document Wfilter = new Document("UUID", winner.getUniqueId().toString());
        Document Lfilter = new Document("UUID", loser.getUniqueId().toString());

        if (type == "normal") {
            Bson Wupdates = inc("stats.sumo.wins", 1);
            col.findOneAndUpdate(Wfilter, Wupdates);
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


            JSONMessage.create(ChatColor.BOLD + "VICTORY!").color(ChatColor.GOLD).title(1, 60, 1, winner);
            JSONMessage.create(ChatColor.BOLD + "GAME OVER!").color(ChatColor.RED).title(1, 60, 1, loser);
        } else {
            addCoins(winner, 10);
            addCoins(loser, 10);
            SumoPlayer1.teleport(new Location(getServer().getWorld("world"), 109.5, 105.5, 404.5, 0, 0));
            SumoPlayer2.teleport(new Location(getServer().getWorld("world"), 109.5, 105.5, 416.5, 90 * 2, 0));

            loser.playSound(loser.getLocation(), Sound.ANVIL_USE, 1, 1);
            winner.playSound(winner.getLocation(), Sound.ANVIL_USE, 1, 1);


            JSONMessage.create(ChatColor.BOLD + "DRAW!").color(ChatColor.YELLOW).title(1, 60, 1, winner);
            JSONMessage.create(ChatColor.BOLD + "DRAW!").color(ChatColor.YELLOW).title(1, 60, 1, loser);
        }
        SumoActive = false;
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {

                backToHub(loser);
                backToHub(winner);
                SumoQueued = false;

                Scoreboards.removeSumoScoreboard(winner);
                Scoreboards.removeSumoScoreboard(loser);

                Scoreboards.updateLobbyScoreboard(winner);
                Scoreboards.updateLobbyScoreboard(loser);

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
        if (inBedwars(player)) return;
        if (SumoQueued) {
            if (SumoGame.get(player)) {
                if (player.getLocation().getY() < 99) {
                    if (SumoActive) {
                        if (player == SumoPlayer1) {
                            SumoEventEnd(SumoPlayer2, SumoPlayer1, "normal");

                        } else {
                            SumoEventEnd(SumoPlayer1, SumoPlayer2, "normal");
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
                            if (BridgeLastPlayerDamage.get(player) == BridgePlayer1) {
                                BridgeLastPlayerDamage.put(player, player);
                                BridgeKills.put(BridgePlayer1, BridgeKills.get(BridgePlayer1) + 1);
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
            Player player = (Player) e.getEntity();
            if (inBedwars(player)) return;
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
                                    classicEventEnd(ClassicPlayer2, ClassicPlayer1, "normal");
                                } else {
                                    classicEventEnd(ClassicPlayer1, ClassicPlayer2, "normal");
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
            if (Bedwars.getGame(damaged)) return;

            if (e.getDamager() instanceof Player) {
                Player damager = (Player) e.getDamager();

                if (BridgeGame.get(damaged)) {
                    BridgeLastPlayerDamage.put(damaged, damager);
                }

                if ((damaged.getHealth() - e.getFinalDamage()) < 0) {

                    e.setCancelled(true);
                    damager.playSound(damager.getLocation(), Sound.HURT_FLESH, 1, 1);
                    damaged.playSound(damaged.getLocation(), Sound.HURT_FLESH, 1, 1);

                    if (ClassicQueued) {
                        if (ClassicGame.get(damaged)) {
                            if (ClassicActive) {
                                if (damaged == ClassicPlayer1) {
                                    classicEventEnd(ClassicPlayer2, ClassicPlayer1, "normal");
                                } else {
                                    classicEventEnd(ClassicPlayer1, ClassicPlayer2, "normal");
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

                    if ((damaged.getHealth() - e.getFinalDamage()) < 0) {

                        e.setCancelled(true);
                        damager.playSound(damager.getLocation(), Sound.HURT_FLESH, 1, 1);
                        damaged.playSound(damaged.getLocation(), Sound.HURT_FLESH, 1, 1);

                        if (ClassicQueued) {
                            if (ClassicGame.get(damaged)) {
                                if (ClassicActive) {
                                    if (damaged == ClassicPlayer1) {
                                        classicEventEnd(ClassicPlayer2, ClassicPlayer1, "normal");
                                    } else {
                                        classicEventEnd(ClassicPlayer1, ClassicPlayer2, "normal");
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

    public boolean containsCaseInsensitive(String s, List<String> l){
        for (String string : l){
            if (string.equalsIgnoreCase(s)){
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = (Player) event.getEntity();
        event.setKeepInventory(true);
        if (ClassicQueued) {
            if (ClassicGame.get(player)) {
                    if (ClassicActive) {
                        if (player == ClassicPlayer1) {

                            classicEventEnd(ClassicPlayer2, ClassicPlayer1, "normal");

                        } else {
                            classicEventEnd(ClassicPlayer1, ClassicPlayer2, "normal");
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

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        if (!BridgeGame.get(player) && !SumoGame.get(player) && !ClassicGame.get(player) && !Bedwars.BedwarsGame.get(player)) {
            if (event.getRightClicked() instanceof Player) {
                openDuelsMenu(player, ((Player)event.getRightClicked()));
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("kit")) {
            Player player = (Player) sender;

            if (args.length < 1) {
                openKitEditorMenu(player);
                return true;
            }
            if (args[0].equalsIgnoreCase("classic")) {
                openClassicEditor(player, player);
            } else if (args[0].equalsIgnoreCase("bridge")){
                openBridgeEditor(player, player);
            } else {
                player.sendMessage(ChatColor.RED + "Uh Oh. Looks like there is no kit editor for " + args[0] + "!");
            }
        }
        if (command.getName().equalsIgnoreCase("test")) {
            Player player = (Player) sender;
            player.getInventory().addItem(getSkull("http://textures.minecraft.net/texture/d2f6c07a326def984e72f772ed645449f5ec96c6ca256499b5d2b84a8dce"));
        }
        if (command.getName().equalsIgnoreCase("duel")) {
            Player player = (Player) sender;
            Player target = Bukkit.getPlayerExact(args[0]);
            if (args.length < 1) {
                player.sendMessage(ChatColor.RED + "Please include who you would like to invite!");
                return true;
            }
            if (target == null) {
                player.sendMessage(ChatColor.RED + "This player is not online!");
                return true;
            }
            openDuelsMenu(player, target);

        }
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
        if (command.getName().equalsIgnoreCase("bed")) {
            Blocks.spawnBeds();
        }
        if (command.getName().equalsIgnoreCase("grapplehook")) {
            Player player = (Player) sender;
            ItemStack grappleHook = new ItemStack(Material.FISHING_ROD);
            ItemMeta grappleMeta = grappleHook.getItemMeta();
            grappleMeta.spigot().setUnbreakable(true);

            grappleMeta.setDisplayName(ChatColor.GREEN + "Grappling Hook" + ChatColor.GRAY + " (Right Click)");
            grappleHook.setItemMeta(grappleMeta);
            player.getInventory().addItem(grappleHook);
        }
        if (command.getName().equalsIgnoreCase("grapplegun")) {
            Player player = (Player) sender;
            ItemStack grappleGun = new ItemStack(Material.FISHING_ROD);
            ItemMeta grappleMeta = grappleGun.getItemMeta();
            grappleMeta.spigot().setUnbreakable(true);

            grappleMeta.setDisplayName(ChatColor.GREEN + "Grappling Gun" + ChatColor.GRAY + " (Right Click)");
            grappleGun.setItemMeta(grappleMeta);
            player.getInventory().addItem(grappleGun);
        }
        if (command.getName().equalsIgnoreCase("toystick")) {
            Player player = (Player) sender;
            ItemStack toyStick = new ItemStack(Material.STICK);
            ItemMeta toyStickMeta = toyStick.getItemMeta();
            toyStickMeta.spigot().setUnbreakable(true);

            toyStickMeta.setDisplayName(ChatColor.GREEN + "Magic Toy Stick" + ChatColor.GRAY + " (Right Click)");
            toyStick.setItemMeta(toyStickMeta);
            player.getInventory().addItem(toyStick);
        }
        if (command.getName().equalsIgnoreCase("kittycannon")) {
            Player player = (Player) sender;
            ItemStack toyStick = new ItemStack(Material.BLAZE_ROD);
            ItemMeta toyStickMeta = toyStick.getItemMeta();
            toyStickMeta.spigot().setUnbreakable(true);

            toyStickMeta.setDisplayName(ChatColor.GREEN + "Kitty Cannon" + ChatColor.GRAY + " (Right Click)");
            toyStick.setItemMeta(toyStickMeta);
            player.getInventory().addItem(toyStick);
        }
        if (command.getName().equalsIgnoreCase("rainbow")) {
            Player player = (Player) sender;
            ItemStack toyStick = new ItemStack(Material.WOOL, 64, (byte) 14);
            ItemMeta toyStickMeta = toyStick.getItemMeta();

            toyStickMeta.setDisplayName(ChatColor.RED + "R" + ChatColor.GOLD + "a" + ChatColor.YELLOW + "i" + ChatColor.GREEN + "n" + ChatColor.AQUA + "b" + ChatColor.DARK_AQUA + "o" + ChatColor.LIGHT_PURPLE + "w" + ChatColor.RED + " W" + ChatColor.GOLD + "o" + ChatColor.YELLOW + "o" + ChatColor.GREEN + "l");
            toyStick.setItemMeta(toyStickMeta);
            player.getInventory().addItem(toyStick);
        }
        if (command.getName().equalsIgnoreCase("wool")) {
            Player player = (Player) sender;
            ItemStack toyStick = Wool.woolItem();
            ItemMeta toyStickMeta = toyStick.getItemMeta();

            toyStickMeta.setDisplayName(ChatColor.GREEN + "Wool");
            toyStick.setItemMeta(toyStickMeta);
            player.getInventory().addItem(toyStick);
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
        if (command.getName().equals("createnpc")) {
            Player player = (Player) sender;
            if (!player.isOp()) {
                player.sendMessage(ChatColor.RED + "You must be OP to use this command!");
                return true;
            }
            if (args.length == 0) {
                NPC.createNPC(player.getLocation(), player.getName(), player.getName());
                player.sendMessage(ChatColor.GOLD + "NPC Created with the skin of " + player.getName() + "!");
                return true;
            }
            NPC.createNPC(player.getLocation(), args[0], args[1]);
            player.sendMessage(ChatColor.GOLD + "NPC Created with the skin of " + args[0] + "!");
        }
        if (command.getName().equals("disguise") || command.getName().equals("d")) {
            Player player = (Player) sender;
            if (args[0].length() == 0) {
                sender.sendMessage(ChatColor.RED + "Please include who you would like to disguise as!");
                return true;
            }
            if (args[0].equalsIgnoreCase("reset")) {
                args[0] = "reset";
            }
            if (args[0].length() < 3) {
                sender.sendMessage(ChatColor.RED + "You're disguise must be greater than 2 characters!");
                return true;
            }
            if (args[0].length() > 16) {
                sender.sendMessage(ChatColor.RED + "Are you trying to crash the server? A Minecraft name can be 16 characters max!");
                return true;
            }
            try {
                SkinGrabber.changeSkin((Player) sender, args[0]);

                Scoreboard sb = player.getScoreboard();
//                if (sb.getObjective(player.getUniqueId().toString()) != null) {
//                    sb.getObjective(player.getUniqueId().toString()).unregister();
//                }
                if (sb.getTeam(Ranks.getRankPriority(player) + player.getName().substring(0, 0) + player.getName().substring(1)) != null) {
                    sb.getTeam(Ranks.getRankPriority(player) + player.getName().substring(0, 0) + player.getName().substring(1)).unregister();
                }

                PlayerInventory inventory = player.getInventory();

                player.teleport(new Location(getServer().getWorld("world"), 86.5, 137.5, 3.5, 90, 0));
                player.getInventory().setHeldItemSlot(0);
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
                        Wool.woolItem(),
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

                Scoreboard healthboard = player.getScoreboard();
                Objective sbHealth;
                if (healthboard.getObjective("playerHealth") == null) {
                    sbHealth = healthboard.registerNewObjective("playerHealth", "dummy");
                    sbHealth.setDisplayName(ChatColor.RED + "❤");
                    sbHealth.setDisplaySlot(DisplaySlot.BELOW_NAME);
                } else {
                    sbHealth = healthboard.getObjective("playerHealth");
                }

                sbHealth.getScore(player.getName()).setScore((int) player.getHealth());

                for (Player p: Bukkit.getOnlinePlayers()) {
//            player.getScoreboard().getObjective(player.getUniqueId().toString()).getScore(p.getName()).setScore((int) p.getHealth());
                    if (!p.equals(player)) {
                        p.getScoreboard().getObjective("playerHealth").getScore(player.getName()).setScore((int) player.getHealth());
                    }
                }

                if (NPC.getNPCs() != null && !NPC.getNPCs().isEmpty()) {
                    NPC.addJoinPacket(player);
                }
                GameProfile ProfileSelector = new GameProfile(UUID.randomUUID(), "ProfileSelector");
                String[] skin = NPC.getSkin(args[0]);
                ProfileSelector.getProperties().put("textures", new Property("textures", skin[0], skin[1]));
                NPC.loadPlayerNPC(new Location(Bukkit.getWorld("world"), 74.5, 136, -5.5, -45, 0), ProfileSelector, player);

                PacketReader reader = new PacketReader();
                reader.inject(player);


                player.setScoreboard(healthboard);

                Scoreboard scoreboard = player.getScoreboard();

                Team team = scoreboard.getTeam(Ranks.getRankPriority(player) + player.getName().substring(0, 0) + player.getName().substring(1));
                if (team == null) {
                    team = scoreboard.registerNewTeam(Ranks.getRankPriority(player) + player.getName().substring(0, 0) + player.getName().substring(1));
                }

//        ProtocolManager pm = ProtocolLibrary.getProtocolManager();
//        PacketContainer packet = pm.createPacket(PacketType.Play.Server.ANIMATION);
//        packet.getModifier().writeDefaults();
//        packet.getIntegers().write(0, event.getPlayer().getEntityId()).write(1, 0);
//        try {
//            pm.sendServerPacket(event.getPlayer(), packet);
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }





                switch (getRank(player)) {
                    case "OWNER":

                        team.setPrefix(ChatColor.DARK_RED + "[OWNER] ");
                        break;
                    case "ADMIN":
                        team.setPrefix(ChatColor.RED + "[ADMIN] ");
                        break;
                    case "MODERATOR":
                        team.setPrefix(ChatColor.AQUA + "[MOD] ");
                        break;
                    case "HELPER":
                        team.setPrefix(ChatColor.GREEN + "[HELPER] ");
                        break;
                    case "YOUTUBE":
                        team.setPrefix(ChatColor.RED + "[" + ChatColor.WHITE + "YOUTUBE" + ChatColor.RED + "] ");
                        break;
                    case "TWITCH":
                        team.setPrefix(ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "TWITCH" + ChatColor.DARK_PURPLE + "] ");
                        break;
                    default:
                        team.setPrefix(ChatColor.GRAY + "");
                        break;
                    case "VIP":
                        team.setPrefix(ChatColor.GREEN + "[VIP] ");
                        break;
                    case "VIP+":
                        team.setPrefix(ChatColor.GREEN + "[VIP" + ChatColor.GOLD + "+" + ChatColor.GREEN + "] ");
                        break;
                    case "MVP":
                        team.setPrefix(ChatColor.AQUA + "[MVP] ");
                        break;
                    case "MVP+":
                        team.setPrefix(ChatColor.AQUA + "[MVP" + getPlusColor(player) + "+" + ChatColor.AQUA + "] ");
                        break;
                    case "MVP++":
                        team.setPrefix(ChatColor.GOLD + "[MVP" + getPlusColor(player) + "++" + ChatColor.GOLD + "] ");
                        break;
                    case "DUCK":
                        team.setPrefix(ChatColor.YELLOW + "[" + ChatColor.GOLD + "DUCK" + ChatColor.YELLOW + "] ");
                        break;
                }
                team.addEntry(player.getName());
                player.setScoreboard(scoreboard);
                if (!args[0].equalsIgnoreCase("reset")) {
                    player.sendMessage(ChatColor.GREEN + "You have been disguised! Type " + ChatColor.GOLD + "/disguise reset" + ChatColor.GREEN + " to reset your disguise!");
                } else {
                    player.sendMessage(ChatColor.GREEN + "Your disguise has been reset!");
                }
                cancelGame(player);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                player.sendMessage(ChatColor.RED + "Uh Oh. Something went wrong!");
            }
        }
        if (command.getName().equals("nick")) {
            if (args[0].length() == 0) {
                sender.sendMessage(ChatColor.RED + "Please include who you would like to nick as!");
                return true;
            }
            if (args[0].equalsIgnoreCase("reset")) {
                args[0] = "reset";
            }
            if (args[0].length() < 3) {
                sender.sendMessage(ChatColor.RED + "You're name must be greater than 2 characters!");
                return true;
            }
            if (args[0].length() > 16) {
                sender.sendMessage(ChatColor.RED + "Are you trying to crash the server? A Minecraft name can be 16 characters max!");
                return true;
            }
            if (getPlayerName(new Mojang().getStringUUID(args[0])) != null) {
                sender.sendMessage(ChatColor.RED + "Hmm. this player previously joined the server! You can't just steal " + args[0] + "'s identity!");
                return true;
            }
            if (containsCaseInsensitive(args[0], SkinGrabber.getNicks())) {
                sender.sendMessage(ChatColor.RED + "Hmm. this player already exists!");
                return true;
            }
            try {
                SkinGrabber.changeSkinAndName((Player) sender, args[0]);

                Player player = (Player) sender;

                Scoreboard sb = player.getScoreboard();
//                if (sb.getObjective(player.getUniqueId().toString()) != null) {
//                    sb.getObjective(player.getUniqueId().toString()).unregister();
//                }
                if (sb.getTeam(Ranks.getRankPriority(player) + player.getName().substring(0, 0) + player.getName().substring(1)) != null) {
                    sb.getTeam(Ranks.getRankPriority(player) + player.getName().substring(0, 0) + player.getName().substring(1)).unregister();
                }

                PlayerInventory inventory = player.getInventory();

                player.teleport(new Location(getServer().getWorld("world"), 86.5, 137.5, 3.5, 90, 0));
                player.getInventory().setHeldItemSlot(0);
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
                        Wool.woolItem(),
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

                Scoreboard healthboard = player.getScoreboard();
                Objective sbHealth;
                if (healthboard.getObjective("playerHealth") == null) {
                    sbHealth = healthboard.registerNewObjective("playerHealth", "dummy");
                    sbHealth.setDisplayName(ChatColor.RED + "❤");
                    sbHealth.setDisplaySlot(DisplaySlot.BELOW_NAME);
                } else {
                    sbHealth = healthboard.getObjective("playerHealth");
                }

                sbHealth.getScore(player.getName()).setScore((int) player.getHealth());

                for (Player p: Bukkit.getOnlinePlayers()) {
//            player.getScoreboard().getObjective(player.getUniqueId().toString()).getScore(p.getName()).setScore((int) p.getHealth());
                    if (!p.equals(player)) {
                        p.getScoreboard().getObjective("playerHealth").getScore(player.getName()).setScore((int) player.getHealth());
                    }
                }

                if (NPC.getNPCs() != null && !NPC.getNPCs().isEmpty()) {
                    NPC.addJoinPacket(player);
                }
                GameProfile ProfileSelector = new GameProfile(UUID.randomUUID(), "ProfileSelector");
                String[] skin = NPC.getSkin(player.getName());
                ProfileSelector.getProperties().put("textures", new Property("textures", skin[0], skin[1]));
                NPC.loadPlayerNPC(new Location(Bukkit.getWorld("world"), 74.5, 136, -5.5, -45, 0), ProfileSelector, player);

                PacketReader reader = new PacketReader();
                reader.inject(player);


                player.setScoreboard(healthboard);




//        ProtocolManager pm = ProtocolLibrary.getProtocolManager();
//        PacketContainer packet = pm.createPacket(PacketType.Play.Server.ANIMATION);
//        packet.getModifier().writeDefaults();
//        packet.getIntegers().write(0, event.getPlayer().getEntityId()).write(1, 0);
//        try {
//            pm.sendServerPacket(event.getPlayer(), packet);
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }

                String playerRank = getRank(player);



//     NICK GO HERE

                for (Player p : Bukkit.getOnlinePlayers()) {
                    Scoreboard scoreboard = p.getScoreboard();

                    Team team = scoreboard.getTeam(Ranks.getRankPriority(player) + player.getName().substring(0, 0) + player.getName().substring(1));
                    if (team == null) {
                        team = scoreboard.registerNewTeam(Ranks.getRankPriority(player) + player.getName().substring(0, 0) + player.getName().substring(1));
                    }

                    switch (playerRank) {
                        case "OWNER":
                            team.setPrefix(ChatColor.DARK_RED + "[OWNER] ");
                            break;
                        case "ADMIN":
                            team.setPrefix(ChatColor.RED + "[ADMIN] ");
                            break;
                        case "MODERATOR":
                            team.setPrefix(ChatColor.AQUA + "[MOD] ");
                            break;
                        case "HELPER":
                            team.setPrefix(ChatColor.GREEN + "[HELPER] ");
                            break;
                        case "YOUTUBE":
                            team.setPrefix(ChatColor.RED + "[" + ChatColor.WHITE + "YOUTUBE" + ChatColor.RED + "] ");
                            break;
                        case "TWITCH":
                            team.setPrefix(ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "TWITCH" + ChatColor.DARK_PURPLE + "] ");
                            break;
                        default:
                            team.setPrefix(ChatColor.GRAY + "");
                            break;
                        case "VIP":
                            team.setPrefix(ChatColor.GREEN + "[VIP] ");
                            break;
                        case "VIP+":
                            team.setPrefix(ChatColor.GREEN + "[VIP" + ChatColor.GOLD + "+" + ChatColor.GREEN + "] ");
                            break;
                        case "MVP":
                            team.setPrefix(ChatColor.AQUA + "[MVP] ");
                            break;
                        case "MVP+":
                            team.setPrefix(ChatColor.AQUA + "[MVP" + getPlusColor(player) + "+" + ChatColor.AQUA + "] ");
                            break;
                        case "MVP++":
                            team.setPrefix(ChatColor.GOLD + "[MVP" + getPlusColor(player) + "++" + ChatColor.GOLD + "] ");
                            break;
                        case "DUCK":
                            team.setPrefix(ChatColor.YELLOW + "[" + ChatColor.GOLD + "DUCK" + ChatColor.YELLOW + "] ");
                            break;
                    }
                    team.addEntry(player.getName());
                }

                Scoreboard scoreboard = player.getScoreboard();
                for (Player p : Bukkit.getOnlinePlayers()) {

                    Team team = scoreboard.getTeam(Ranks.getRankPriority(p) + p.getName().substring(0, 0) + p.getName().substring(1));
                    if (team == null) {
                        team = scoreboard.registerNewTeam(Ranks.getRankPriority(p) + p.getName().substring(0, 0) + p.getName().substring(1));
                    }

                    switch (getRank(p)) {
                        case "OWNER":

                            team.setPrefix(ChatColor.DARK_RED + "[OWNER] ");
                            break;
                        case "ADMIN":
                            team.setPrefix(ChatColor.RED + "[ADMIN] ");
                            break;
                        case "MODERATOR":
                            team.setPrefix(ChatColor.AQUA + "[MOD] ");
                            break;
                        case "HELPER":
                            team.setPrefix(ChatColor.GREEN + "[HELPER] ");
                            break;
                        case "YOUTUBE":
                            team.setPrefix(ChatColor.RED + "[" + ChatColor.WHITE + "YOUTUBE" + ChatColor.RED + "] ");
                            break;
                        case "TWITCH":
                            team.setPrefix(ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "TWITCH" + ChatColor.DARK_PURPLE + "] ");
                            break;
                        default:
                            team.setPrefix(ChatColor.GRAY + "");
                            break;
                        case "VIP":
                            team.setPrefix(ChatColor.GREEN + "[VIP] ");
                            break;
                        case "VIP+":
                            team.setPrefix(ChatColor.GREEN + "[VIP" + ChatColor.GOLD + "+" + ChatColor.GREEN + "] ");
                            break;
                        case "MVP":
                            team.setPrefix(ChatColor.AQUA + "[MVP] ");
                            break;
                        case "MVP+":
                            team.setPrefix(ChatColor.AQUA + "[MVP" + getPlusColor(player) + "+" + ChatColor.AQUA + "] ");
                            break;
                        case "MVP++":
                            team.setPrefix(ChatColor.GOLD + "[MVP" + getPlusColor(player) + "++" + ChatColor.GOLD + "] ");
                            break;
                        case "DUCK":
                            team.setPrefix(ChatColor.YELLOW + "[" + ChatColor.GOLD + "DUCK" + ChatColor.YELLOW + "] ");
                            break;
                    }
                    team.addEntry(p.getName());
                }


                Team team = scoreboard.getTeam(Ranks.getRankPriority(player) + player.getName().substring(0, 0) + player.getName().substring(1));
                if (team == null) {
                    team = scoreboard.registerNewTeam(Ranks.getRankPriority(player) + player.getName().substring(0, 0) + player.getName().substring(1));
                }

//        ProtocolManager pm = ProtocolLibrary.getProtocolManager();
//        PacketContainer packet = pm.createPacket(PacketType.Play.Server.ANIMATION);
//        packet.getModifier().writeDefaults();
//        packet.getIntegers().write(0, event.getPlayer().getEntityId()).write(1, 0);
//        try {
//            pm.sendServerPacket(event.getPlayer(), packet);
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }





                switch (playerRank) {
                    case "OWNER":

                        team.setPrefix(ChatColor.DARK_RED + "[OWNER] ");
                        break;
                    case "ADMIN":
                        team.setPrefix(ChatColor.RED + "[ADMIN] ");
                        break;
                    case "MODERATOR":
                        team.setPrefix(ChatColor.AQUA + "[MOD] ");
                        break;
                    case "HELPER":
                        team.setPrefix(ChatColor.GREEN + "[HELPER] ");
                        break;
                    case "YOUTUBE":
                        team.setPrefix(ChatColor.RED + "[" + ChatColor.WHITE + "YOUTUBE" + ChatColor.RED + "] ");
                        break;
                    case "TWITCH":
                        team.setPrefix(ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "TWITCH" + ChatColor.DARK_PURPLE + "] ");
                        break;
                    default:
                        team.setPrefix(ChatColor.GRAY + "");
                        break;
                    case "VIP":
                        team.setPrefix(ChatColor.GREEN + "[VIP] ");
                        break;
                    case "VIP+":
                        team.setPrefix(ChatColor.GREEN + "[VIP" + ChatColor.GOLD + "+" + ChatColor.GREEN + "] ");
                        break;
                    case "MVP":
                        team.setPrefix(ChatColor.AQUA + "[MVP] ");
                        break;
                    case "MVP+":
                        team.setPrefix(ChatColor.AQUA + "[MVP" + getPlusColor(player) + "+" + ChatColor.AQUA + "] ");
                        break;
                    case "MVP++":
                        team.setPrefix(ChatColor.GOLD + "[MVP" + getPlusColor(player) + "++" + ChatColor.GOLD + "] ");
                        break;
                    case "DUCK":
                        team.setPrefix(ChatColor.YELLOW + "[" + ChatColor.GOLD + "DUCK" + ChatColor.YELLOW + "] ");
                        break;
                }
                team.addEntry(player.getName());
                player.setScoreboard(scoreboard);
                if (!args[0].equalsIgnoreCase("reset")) {
                    player.sendMessage(ChatColor.GREEN + "You have been nicked! Type " + ChatColor.GOLD + "/nick reset" + ChatColor.GREEN + " to reset your name!");
                } else {
                    player.sendMessage(ChatColor.GREEN + "Your nick has been reset!");
                }

                cancelGame(player);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                sender.sendMessage(ChatColor.RED + "Uh Oh. Something went wrong!");
            }
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
                            if (SumoQueued) {
                                target1.sendMessage(ChatColor.RED + "Hmm. Looks like all Sumo lobbies are full! Please try again after the game is done!");
                                target2.sendMessage(ChatColor.RED + "Hmm. Looks like all Sumo lobbies are full! Please try again after the game is done!");
                                return true;
                            }
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

        if (command.getName().equals("bedwars")) {

            Player target = Bukkit.getPlayerExact(args[0]);
            Player player = (Player) sender;

            if (!player.isOp()) return true;

            if (player != target) {
                if (!Bedwars.BedwarsQueued) {

                    //                PlayerConnection connection = ((CraftPlayer) target.getPlayer()).getHandle().playerConnection;
                    //                IChatBaseComponent text = IChatBaseComponent.ChatSerializer.a("{'text': '" + "Hello!" + "'}");
                    //                PacketPlayOutTitle packet = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, text, 1, 20, 1);
                    //                connection.sendPacket(packet);

                    if (target == null) {
                        //Target is not online
                        player.sendMessage(ChatColor.RED + "Your target \"" + args[0] + "\" is not online!");
                    } else {
                        if (!Bedwars.BedwarsRequest.containsKey(player)) {
                            if (!Bedwars.BedwarsRequest.containsValue(target)) {
                                TextComponent text = new net.md_5.bungee.api.chat.TextComponent(ChatColor.GOLD + "             " + ChatColor.BOLD + "CLICK HERE" + ChatColor.YELLOW + " to accept the duel!");
                                text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/playbedwars " + player.getName() + " " + target.getName()));
                                text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to duel " + player.getName()).color(net.md_5.bungee.api.ChatColor.GOLD).italic(true).create()));

                                Bedwars.BedwarsRequest.put(player, target);
                                //                            player.sendMessage("{\"text\":\"Hello!\", \"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Hey There!\"}}");
                                //                            player.getServer().dispatchCommand(this.getServer().getConsoleSender(),"tellraw " + player.getName() + " " + "{\"text\":\"Hello!\", \"clickEvent\":{\"action\":\"run_command\",\"value\":\"/help\"}}");
                                target.sendMessage(ChatColor.GOLD + "--------------------------------------------");
                                target.sendMessage(" ");
                                target.sendMessage("    " + ChatColor.GOLD + player.getName() + ChatColor.AQUA + " has invited you to Bedwars Duels!");
                                target.sendMessage(" ");
                                //                            target.getServer().dispatchCommand(this.getServer().getConsoleSender(),"tellraw " + target.getName() + " " + "{\"text\":\"" + ChatColor.GOLD + "             " + ChatColor.BOLD + "CLICK HERE" + ChatColor.YELLOW + " to accept the duel!" + "\", \"clickEvent\":{\"action\":\"run_command\",\"value\":\"playsumo " + player.getName() + " " + target.getName() + "\"}");
                                //                            target.sendMessage(ChatColor.GOLD + "             " + ChatColor.BOLD + "CLICK HERE" + ChatColor.YELLOW + " to accept the duel!");
                                target.spigot().sendMessage(text);

                                target.sendMessage(" ");
                                target.sendMessage(ChatColor.GOLD + "--------------------------------------------");
                                player.sendMessage(ChatColor.GOLD + "---------------------------------");
                                player.sendMessage(ChatColor.YELLOW + "You invited " + ChatColor.GOLD + target.getName() + ChatColor.YELLOW + " to a Bedwars Duel!");
                                player.sendMessage(ChatColor.GOLD + "---------------------------------");

                                Integer duelNo = new Random().nextInt(10000);
                                Bedwars.BedwarsRequestNo.put(player, duelNo);

                                BukkitScheduler scheduler5 = Bukkit.getServer().getScheduler();
                                scheduler5.scheduleSyncDelayedTask(this, new Runnable() {
                                    @Override
                                    public void run() {
                                        if (Bedwars.BedwarsRequest.get(player) == target) {
                                            if (Bedwars.BedwarsRequestNo.get(player) == duelNo) {
                                                Bedwars.BedwarsRequestNo.remove(player);
                                                Bedwars.BedwarsRequest.remove(player);

                                                player.sendMessage(ChatColor.GOLD + "---------------------------------");
                                                player.sendMessage(ChatColor.YELLOW + "Bedwars Request to " + target.getName() + ChatColor.YELLOW + " timed out!");
                                                player.sendMessage(ChatColor.GOLD + "---------------------------------");

                                                target.sendMessage(ChatColor.GOLD + "---------------------------------");
                                                target.sendMessage(ChatColor.YELLOW + "Bedwars Request from " + player.getName() + ChatColor.YELLOW + " timed out!");
                                                target.sendMessage(ChatColor.GOLD + "---------------------------------");
                                            }
                                        }
                                    }
                                }, 60 * 20);

                            } else {
                                player.sendMessage(ChatColor.RED + "This player has already been invited to a bedwars duel!");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "You have already invited a player to a bedwars duel!");
                        }
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "There is already a bedwars duel queued!");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Stop Hitting Yourself!");
            }
        }
        if (command.getName().equals("playbedwars")) {
            Player player = (Player) sender;
            if (args[0] != null && args[1] != null) {
                Player target1 = Bukkit.getPlayerExact(args[0]);
                Player target2 = Bukkit.getPlayerExact(args[1]);

                if (Bedwars.BedwarsRequest.containsKey(target1)) {
                    if (Bedwars.BedwarsRequest.containsValue(target2)) {
                        if (player == target2) {
                            if (Bedwars.BedwarsQueued) {
                                target1.sendMessage(ChatColor.RED + "Hmm. Looks like all Bedwars lobbies are full! Please try again after the game is done!");
                                target2.sendMessage(ChatColor.RED + "Hmm. Looks like all Bedwars lobbies are full! Please try again after the game is done!");
                                return true;
                            }
                            BedwarsQueue.remove(1);
                            BedwarsQueue.remove(2);
                            BedwarsQueueSize = 0;
                            cancelGame(target1);
                            cancelGame(target2);
                            Bedwars.BedwarsEvent(target1, target2);
                            Bedwars.BedwarsRequest.remove(target1);

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
                            if (ClassicQueued) {
                                target1.sendMessage(ChatColor.RED + "Hmm. Looks like all Classic lobbies are full! Please try again after the game is done!");
                                target2.sendMessage(ChatColor.RED + "Hmm. Looks like all Classic lobbies are full! Please try again after the game is done!");
                                return true;
                            }
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
                            if (BridgeQueued) {
                                target1.sendMessage(ChatColor.RED + "Hmm. Looks like all Bridge lobbies are full! Please try again after the game is done!");
                                target2.sendMessage(ChatColor.RED + "Hmm. Looks like all Bridge lobbies are full! Please try again after the game is done!");
                                return true;
                            }
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
            if (args.length < 1) {
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
                Wool.woolItem(),
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
        removePotionEffects(player);

        player.teleport(new Location(getServer().getWorld("world"), 86.5, 137.5, 3.5, 90, 0));
        player.getInventory().setHeldItemSlot(0);
        player.setFireTicks(0);

        if (NPC.getNPCs() != null && !NPC.getNPCs().isEmpty()) {
            NPC.addJoinPacket(player);
        }
        GameProfile ProfileSelector = new GameProfile(UUID.randomUUID(), "ProfileSelector");
        String[] skin = NPC.getSkin(player.getName());
        ProfileSelector.getProperties().put("textures", new Property("textures", skin[0], skin[1]));
        NPC.loadPlayerNPC(new Location(Bukkit.getWorld("world"), 74.5, 136, -5.5, -45, 0), ProfileSelector, player);
    }

    final int[] ClassicTime = {4 * 60};
    public final String[] ClassicTimeFormatted = {"00:00"};
    int classicTimerID;

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

        Scoreboards.removeLobbyScoreboard(player1);
        Scoreboards.removeLobbyScoreboard(player2);

        Scoreboards.updateClassicScoreboard(player1);
        Scoreboards.updateClassicScoreboard(player2);

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

        ClassicTime[0] = 4*60;


        BukkitScheduler Classicscheduler = Bukkit.getServer().getScheduler();
        classicTimerID = Classicscheduler.runTaskTimer(this, new Runnable()
        {

            @Override
            public void run()
            {
                if (ClassicActive) {
                    if (ClassicTime[0] == 0) {
                        classicEventEnd(ClassicPlayer1, ClassicPlayer2, "draw");

                        Bukkit.getScheduler().cancelTask(classicTimerID);
                        return;
                    }

                    int minutes = ClassicTime[0] / 60;
                    int seconds = ClassicTime[0] % 60;
                    String secondsFormated = "00";
                    if (seconds < 10) {
                        secondsFormated = "0" + seconds;
                    } else {
                        secondsFormated = "" + seconds;
                    }

                    ClassicTimeFormatted[0] = minutes + ":" + secondsFormated;

                    Scoreboards.updateClassicScoreboard(player1);
                    Scoreboards.updateClassicScoreboard(player2);

                    ClassicTime[0]--;
                } else {
                    if (!ClassicQueued) {
                        ClassicTime[0] = 0;
                    }
                }
            }
        }, 0L, 20L).getTaskId();

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (SumoQueued) {
            if (player == SumoPlayer1) {
                SumoEventEnd(SumoPlayer2, SumoPlayer1, "normal");
            } else if (player == SumoPlayer2) {
                SumoEventEnd(SumoPlayer1, SumoPlayer2, "normal");
            }
        }
        if (ClassicQueued) {
            if (player == ClassicPlayer1) {
                classicEventEnd(ClassicPlayer2, ClassicPlayer1, "normal");
            } else if (player == ClassicPlayer2) {
                classicEventEnd(ClassicPlayer1, ClassicPlayer2, "normal");
            }
        }
        if (BridgeQueued) {
            if (player == BridgePlayer1) {
                BridgeEventEnd(BridgePlayer2, BridgePlayer1, "normal");
            } else if (player == BridgePlayer2) {
                BridgeEventEnd(BridgePlayer1, BridgePlayer2, "normal");
            }
        }
        leaveGameQueue(player);

        PacketReader reader = new PacketReader();
        reader.uninject(event.getPlayer());
        Scoreboard sb = player.getScoreboard();
        sb.getTeam(Ranks.getRankPriority(player) + player.getName().substring(0, 0) + player.getName().substring(1)).unregister();
//        sb.getObjective(player.getUniqueId().toString()).unregister();

//        if (SkinGrabber.Nicks.contains(player.getName())) {
//            SkinGrabber.Nicks.remove(player.getName());
//        }
    }




    public void classicEventEnd(Player winner, Player loser, String type) {
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

        if (type == "normal") {
            addCoins(winner, 25);
            addCoins(loser, 5);
        } else {
            addCoins(winner, 15);
            addCoins(loser, 15);
        }


        if (type.equalsIgnoreCase("normal")) {
            Bson WupdatesWins = inc("stats.classic.wins", 1);
            col.findOneAndUpdate(Wfilter, WupdatesWins);

            Bson LupdatesWins = inc("stats.classic.losses", 1);
            col.findOneAndUpdate(Lfilter, LupdatesWins);


            Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "----------------");
            Bukkit.getServer().broadcastMessage(ChatColor.GREEN + winner.getName() + ChatColor.GOLD + " just won a Classic Duel against " + ChatColor.GREEN + loser.getName() + ChatColor.GOLD + "!");
            Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "----------------");
        } else {
            Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "----------------");
            Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + winner.getName() + ChatColor.GOLD + " just tied a Classic Duel against " + ChatColor.YELLOW + loser.getName() + ChatColor.GOLD + "!");
            Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "----------------");
        }


        if (type.equals("normal")) {
            loser.playSound(loser.getLocation(), Sound.ANVIL_BREAK, 1, 1);
            winner.playSound(winner.getLocation(), Sound.LEVEL_UP, 1, 1);
            winner.playSound(winner.getLocation(), Sound.NOTE_PLING, 1, 1);
        } else {
            winner.playSound(winner.getLocation(), Sound.ANVIL_USE, 1, 1);
            loser.playSound(loser.getLocation(), Sound.ANVIL_USE, 1, 1);
        }

        if (type.equals("normal")) {
            JSONMessage.create(ChatColor.BOLD + "VICTORY!").color(ChatColor.GOLD).title(1, 60, 1, winner);
            JSONMessage.create(ChatColor.BOLD + "GAME OVER!").color(ChatColor.RED).title(1, 60, 1, loser);
        } else {
            JSONMessage.create(ChatColor.BOLD + "DRAW!").color(ChatColor.YELLOW).title(1, 60, 1, winner);
            JSONMessage.create(ChatColor.BOLD + "DRAW!").color(ChatColor.YELLOW).title(1, 60, 1, loser);
        }

        if (type == "normal") {
            if (loser.equals(ClassicPlayer1)) {
                ClassicPlayer1.teleport(new Location(getServer().getWorld("world"), 3001.5, 40.5, 3061.5, 180, 0));
            } else {
                ClassicPlayer2.teleport(new Location(getServer().getWorld("world"), 3001.5, 40.5, 2993.5, 0, 0));
            }
        } else {
            ClassicPlayer1.teleport(new Location(getServer().getWorld("world"), 3001.5, 40.5, 3061.5, 180, 0));
            ClassicPlayer2.teleport(new Location(getServer().getWorld("world"), 3001.5, 40.5, 2993.5, 0, 0));
        }

        DecimalFormat df = new DecimalFormat("#.#");

        if (type == "normal") {

            winner.sendMessage(ChatColor.BOLD.toString() + ChatColor.GREEN + "-------------------------");
            winner.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "       Classic Duel");
            winner.sendMessage(" ");
            winner.sendMessage(ChatColor.YELLOW.toString() + ChatColor.GRAY + "       " + rankedName(winner) + ChatColor.GRAY + " - " + ChatColor.WHITE + df.format(winner.getHealth()) + ChatColor.RED + "❤");
            winner.sendMessage(ChatColor.YELLOW.toString() + ChatColor.GRAY + "     " + rankedName(loser) + ChatColor.GRAY + " - " + ChatColor.WHITE + "" + ChatColor.RED + ChatColor.BOLD.toString() + "DEAD");
            winner.sendMessage(" ");
            winner.sendMessage(ChatColor.BOLD.toString() + ChatColor.GREEN + "-------------------------");

            loser.sendMessage(ChatColor.BOLD.toString() + ChatColor.GREEN + "-------------------------");
            loser.sendMessage(ChatColor.YELLOW.toString() + ChatColor.YELLOW + "          Classic Duel");
            loser.sendMessage(" ");
            loser.sendMessage(ChatColor.YELLOW.toString() + ChatColor.GRAY + "       " + rankedName(winner) + ChatColor.GRAY + " - " + ChatColor.WHITE + df.format(winner.getHealth()) + ChatColor.RED + "❤");
            loser.sendMessage(ChatColor.YELLOW.toString() + ChatColor.GRAY + "     " + rankedName(loser) + ChatColor.GRAY + " - " + ChatColor.WHITE + "" + ChatColor.RED + ChatColor.BOLD.toString() + "DEAD");
            loser.sendMessage(" ");
            loser.sendMessage(ChatColor.BOLD.toString() + ChatColor.GREEN + "-------------------------");
        } else {
            winner.sendMessage(ChatColor.BOLD.toString() + ChatColor.GREEN + "-------------------------");
            winner.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "       Classic Duel");
            winner.sendMessage(" ");
            winner.sendMessage(ChatColor.YELLOW.toString() + ChatColor.GRAY + "       " + rankedName(winner) + ChatColor.GRAY + " - " + ChatColor.WHITE + df.format(winner.getHealth()) + ChatColor.RED + "❤");
            winner.sendMessage(ChatColor.YELLOW.toString() + ChatColor.GRAY + "       " + rankedName(loser) + ChatColor.GRAY + " - " + ChatColor.WHITE + "" + df.format(loser.getHealth()) + ChatColor.RED + "❤");
            winner.sendMessage(" ");
            winner.sendMessage(ChatColor.BOLD.toString() + ChatColor.GREEN + "-------------------------");

            loser.sendMessage(ChatColor.BOLD.toString() + ChatColor.GREEN + "-------------------------");
            loser.sendMessage(ChatColor.YELLOW.toString() + ChatColor.YELLOW + "          Classic Duel");
            loser.sendMessage(" ");
            loser.sendMessage(ChatColor.YELLOW.toString() + ChatColor.GRAY + "       " + rankedName(winner) + ChatColor.GRAY + " - " + ChatColor.WHITE + df.format(winner.getHealth()) + ChatColor.RED + "❤");
            loser.sendMessage(ChatColor.YELLOW.toString() + ChatColor.GRAY + "       " + rankedName(loser) + ChatColor.GRAY + " - " + ChatColor.WHITE + "" + df.format(loser.getHealth()) + ChatColor.RED + "❤");
            loser.sendMessage(" ");
            loser.sendMessage(ChatColor.BOLD.toString() + ChatColor.GREEN + "-------------------------");
        }

        winner.setHealth(20);
        loser.setHealth(20);

        ClassicActive = false;
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {

                backToHub(loser);
                backToHub(winner);
                ClassicQueued = false;

                Scoreboards.removeClassicScoreboard(winner);
                Scoreboards.removeClassicScoreboard(loser);

                Scoreboards.updateLobbyScoreboard(winner);
                Scoreboards.updateLobbyScoreboard(loser);

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


        Scoreboards.removeLobbyScoreboard(player1);
        Scoreboards.removeLobbyScoreboard(player2);

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
        if (BridgeGame.get(player)) {
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


                Scoreboards.updateLobbyScoreboard(winner);
                Scoreboards.updateLobbyScoreboard(loser);

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

    public int getBridgeSwordSlot(Player player, Document obj) {
        Document gameType = (Document) obj.get("bridge");
        if (gameType.getInteger("swordSlot") != null) {
            return gameType.getInteger("swordSlot");
        } else {
            return 0;
        }
//        return gameType.getInteger("swordSlot");
    }
    public int getBridgeBowSlot(Player player, Document obj) {
        Document gameType = (Document) obj.get("bridge");
        return gameType.getInteger("bowSlot");
    }
    public int getBridgePickaxeSlot(Player player, Document obj) {
        Document gameType = (Document) obj.get("bridge");
        return gameType.getInteger("pickaxeSlot");
    }
    public int getBridgeBlockSlot1(Player player, Document obj) {
        Document gameType = (Document) obj.get("bridge");
        return gameType.getInteger("blockSlot1");
    }
    public int getBridgeBlockSlot2(Player player, Document obj) {
        Document gameType = (Document) obj.get("bridge");
        return gameType.getInteger("blockSlot2");
    }
    public int getBridgeGappleSlot(Player player, Document obj) {
        Document gameType = (Document) obj.get("bridge");
        return gameType.getInteger("gappleSlot");
    }
    public int getBridgeArrowSlot(Player player, Document obj) {
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
                    if (!BedwarsQueue.containsValue(player) && !ClassicQueue.containsValue(player) && !BridgeQueue.containsValue(player)) {
                        if (SumoQueueSize < 3) {
                            SumoQueueSize++;
                            SumoQueue.put(SumoQueueSize, player);

                            if (SumoQueueSize > 1) {
                                if (SumoQueued) {
                                    player.sendMessage(ChatColor.RED + "Hmm. Looks like all Sumo lobbies are full! Please try again after the game is done!");
                                    return;
                                }
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
                    if (!BedwarsQueue.containsValue(player) && !SumoQueue.containsValue(player) && !BridgeQueue.containsValue(player)) {
                        if (ClassicQueueSize < 3) {
                            ClassicQueueSize++;
                            ClassicQueue.put(ClassicQueueSize, player);

                            if (ClassicQueueSize > 1) {
                                if (ClassicQueued) {
                                    player.sendMessage(ChatColor.RED + "Hmm. Looks like all Classic lobbies are full! Please try again after the game is done!");
                                    return;
                                }
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
                    if (!BedwarsQueue.containsValue(player) && !SumoQueue.containsValue(player) && !ClassicQueue.containsValue(player)) {
                        if (BridgeQueueSize < 3) {
                            BridgeQueueSize++;
                            BridgeQueue.put(BridgeQueueSize, player);

                            if (BridgeQueueSize > 1) {
                                if (BridgeQueued) {
                                    player.sendMessage(ChatColor.RED + "Hmm. Looks like all Bridge lobbies are full! Please try again after the game is done!");
                                    return;
                                }
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
            case "bedwars":
                if (!BedwarsQueue.containsValue(player)) {
                    if (!BridgeQueue.containsValue(player) && !SumoQueue.containsValue(player) && !ClassicQueue.containsValue(player)) {
                        if (BedwarsQueueSize < 13) {
                            BedwarsQueueSize++;
                            BedwarsQueue.put(BedwarsQueueSize, player);

                            if (BedwarsQueueSize > 1) {
                                Bedwars.BedwarsEvent(BedwarsQueue.get(1), BedwarsQueue.get(2));
                                BedwarsQueue.remove(1);
                                BedwarsQueue.remove(2);
                                BedwarsQueueSize = 0;
                                player.sendMessage(ChatColor.GREEN + "Joined Bedwars Queue!");
                            } else {
                                player.sendMessage(ChatColor.GREEN + "Joined Bedwars Queue! Waiting on 1 player...");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "The queue for Bedwars is full!");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Looks like you're already in a queue! Type /leavequeue to leave it!");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Looks like you're already in the queue for Bedwars!");
                }
                break;
        }
    }

    public ItemStack getSkull(String url) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        if(url.isEmpty())return item;


        SkullMeta itemMeta = (SkullMeta) item.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try
        {
            profileField = itemMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(itemMeta, profile);
        }
        catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
        item.setItemMeta(itemMeta);
        return item;
    }

    public void openDuelsMenu(Player sender, Player sendTo) {
        Inventory shopGUI = Bukkit.createInventory(sender, 5 * 9, ChatColor.GOLD + "Duel - " + rankedName(sendTo));
        ItemStack air = new ItemStack(Material.AIR);
        ItemStack playerHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skull = (SkullMeta) playerHead.getItemMeta();
        skull.setDisplayName(rankedName(sendTo));
        skull.setOwner(sendTo.getName());
        playerHead.setItemMeta(skull);

        ItemStack OG = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1);
        ItemMeta OGMeta = OG.getItemMeta();
        OGMeta.setDisplayName(ChatColor.GOLD + " ");
        OG.setItemMeta(OGMeta);

        ItemStack sumo = new ItemStack(Material.SLIME_BALL);
        ItemMeta sumoMeta = sumo.getItemMeta();
        sumoMeta.setDisplayName(ChatColor.GREEN + "Sumo Duels" + ChatColor.GRAY + " - " + ChatColor.BOLD + "" + ChatColor.LIGHT_PURPLE + "1v1");
        sumoMeta.setLore(Arrays.asList(ChatColor.GRAY + "Knock your opponent out of the", ChatColor.GRAY + "arena with your fists!","", ChatColor.YELLOW + "Click To Play!"));
        sumo.setItemMeta(sumoMeta);

        ItemStack classic = new ItemStack(Material.FISHING_ROD);
        ItemMeta classicMeta = classic.getItemMeta();
        classicMeta.setDisplayName(ChatColor.GREEN + "Classic Duels" + ChatColor.GRAY + " - " + ChatColor.BOLD + "" + ChatColor.LIGHT_PURPLE + "1v1");
        classicMeta.setLore(Arrays.asList(ChatColor.GRAY + "Iron gear 1v1 Duel!","", ChatColor.YELLOW + "Click To Invite!"));
        classic.setItemMeta(classicMeta);

        ItemStack bridge = new ItemStack(Material.STAINED_CLAY, 1,  (short) 14);
        ItemMeta bridgeMeta = bridge.getItemMeta();
        bridgeMeta.setDisplayName(ChatColor.GREEN + "Bridge Duels" + ChatColor.GRAY + " - " + ChatColor.BOLD + "" + ChatColor.LIGHT_PURPLE + "1v1");
        bridgeMeta.setLore(Arrays.asList(ChatColor.GRAY + "Cross the bridge and score", ChatColor.GRAY + "more goals than the opposing team!","", ChatColor.YELLOW + "Click To Invite!"));
        bridge.setItemMeta(bridgeMeta);

        ItemStack bedwars = new ItemStack(Material.BED);
        ItemMeta bedwarsMeta = bedwars.getItemMeta();
        bedwarsMeta.setDisplayName(ChatColor.GREEN + "Bedwars Duels" + ChatColor.GRAY + " - " + ChatColor.BOLD + "" + ChatColor.LIGHT_PURPLE + "1v1");
        bedwarsMeta.setLore(Arrays.asList(ChatColor.GRAY + "Destroy enemy beds, and kill", ChatColor.GRAY + "them to win the game!","", ChatColor.YELLOW + "Click To Invite!"));
        bedwars.setItemMeta(bedwarsMeta);

        ItemStack cancel = new ItemStack(Material.BARRIER);
        ItemMeta cancelMeta = cancel.getItemMeta();
        cancelMeta.setDisplayName(ChatColor.RED + "Cancel");
        cancelMeta.setLore(Collections.singletonList(ChatColor.GRAY + "Cancel Invitation"));
        cancel.setItemMeta(cancelMeta);

        DuelMenuRequests.put(sender, sendTo);


        shopGUI.setItem(4, playerHead);
        shopGUI.setItem(9, OG);
        shopGUI.setItem(10, OG);
        shopGUI.setItem(11, OG);
        shopGUI.setItem(12, OG);
        shopGUI.setItem(13, OG);
        shopGUI.setItem(14, OG);
        shopGUI.setItem(15, OG);
        shopGUI.setItem(16, OG);
        shopGUI.setItem(17, OG);
        shopGUI.setItem(19+9, sumo);
        shopGUI.setItem(21+9, classic);
        shopGUI.setItem(23+9, bridge);
        shopGUI.setItem(25+9, bedwars);
        shopGUI.setItem(53-9, cancel);

        sender.openInventory(shopGUI);
    }

    @EventHandler
    public void inventoryClose(InventoryCloseEvent event) {
        Inventory inv = event.getInventory();
        if (inv.getName().startsWith(ChatColor.GOLD + "Duel - "))
            DuelMenuRequests.remove(event.getPlayer());
    }

    @EventHandler
    public void InventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getClickedInventory();
        if (inv == null) return;
        if (event.getCurrentItem() == null) return;
        if (!event.getCurrentItem().hasItemMeta()) return;
        if (inv.getName().startsWith(ChatColor.GOLD + "Duel - ")) {
            if (event.getCurrentItem().getType() == Material.SLIME_BALL) {
//                Invite to sumo duel
                player.chat("/sumo " + DuelMenuRequests.get(player).getName());
                DuelMenuRequests.remove(player);
                player.closeInventory();
            }
            if (event.getCurrentItem().getType() == Material.FISHING_ROD) {
//                Invite to classic duel
                player.chat("/classic " + DuelMenuRequests.get(player).getName());
                DuelMenuRequests.remove(player);
                player.closeInventory();
            }
            if (event.getCurrentItem().getType() == Material.BED) {
//                Invite to bedwars duel
                player.chat("/bedwars " + DuelMenuRequests.get(player).getName());
                DuelMenuRequests.remove(player);
                player.closeInventory();
            }
            if (event.getCurrentItem().getType() == Material.STAINED_CLAY) {
//                Invite to bridge duel
                player.chat("/bridge " + DuelMenuRequests.get(player).getName());
                DuelMenuRequests.remove(player);
                player.closeInventory();
            }
            if (event.getCurrentItem().getType() == Material.BARRIER) {
//                Invite to bridge duel
                DuelMenuRequests.remove(player);
                player.closeInventory();
            }
        }
    }

    @EventHandler
    public void InventoryClick(PlayerInteractEvent event) {
        if (inBedwars(event.getPlayer())) return;
        if (BridgeGame.get(event.getPlayer())) return;
        if (ClassicGame.get(event.getPlayer())) return;
        if (SumoGame.get(event.getPlayer())) return;
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
                if (event.getClickedBlock().getType() == Material.BEACON)
                    event.setCancelled(true);
                if (event.getClickedBlock().getType() == Material.WOODEN_DOOR)
                    event.setCancelled(true);
                if (event.getClickedBlock().getType() == Material.SPRUCE_DOOR)
                    event.setCancelled(true);
                if (event.getClickedBlock().getType() == Material.TRAP_DOOR)
                    event.setCancelled(true);
                if (event.getClickedBlock().getType() == Material.CHEST)
                    event.setCancelled(true);
                if (event.getClickedBlock().getType() == Material.WORKBENCH)
                    event.setCancelled(true);
                if (event.getClickedBlock().getType() == Material.FURNACE)
                    event.setCancelled(true);
                if (event.getClickedBlock().getType() == Material.TRAPPED_CHEST)
                    event.setCancelled(true);
                if (event.getClickedBlock().getType() == Material.ENDER_CHEST)
                    event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void armorstandsInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode().equals(GameMode.CREATIVE)) return;
        if (event.getRightClicked().getType() == EntityType.ARMOR_STAND)
            event.setCancelled(true);
    }
    @EventHandler
    public void armorstandsAtInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode().equals(GameMode.CREATIVE)) return;
        if (event.getRightClicked().getType() == EntityType.ARMOR_STAND)
            event.setCancelled(true);
    }
    @EventHandler
    public void armorStandDamage(EntityDamageByEntityEvent event) {
        if (((Player)event.getDamager()).getGameMode().equals(GameMode.CREATIVE)) return;
        if (event.getEntity().getType().equals(EntityType.ARMOR_STAND))
            event.setCancelled(true);
    }

    public void openStatsMenu(Player player, Player viewing) {
        Document filter = new Document("UUID", viewing.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("stats");

        Inventory shopGUI = Bukkit.createInventory(player, 5 * 9, ChatColor.GOLD + "Stats - " + rankedName(viewing));
        ItemStack air = new ItemStack(Material.AIR);
        ItemStack playerHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skull = (SkullMeta) playerHead.getItemMeta();
        skull.setDisplayName(rankedName(viewing));
        skull.setOwner(viewing.getName());
        playerHead.setItemMeta(skull);

        ItemStack OG = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1);
        ItemMeta OGMeta = OG.getItemMeta();
        OGMeta.setDisplayName(ChatColor.GOLD + " ");
        OG.setItemMeta(OGMeta);

        ItemStack sumo = new ItemStack(Material.SLIME_BALL);
        ItemMeta sumoMeta = sumo.getItemMeta();
        sumoMeta.setDisplayName(ChatColor.GREEN + "Sumo Duels" + ChatColor.GRAY + " - " + ChatColor.BOLD + "" + ChatColor.LIGHT_PURPLE + "1v1");
        sumoMeta.setLore(Arrays.asList(ChatColor.GRAY + "Knock your opponent out of the", ChatColor.GRAY + "arena with your fists!","", ChatColor.WHITE + "Games Played: " + ChatColor.YELLOW + getSumoGames(viewing, obj), "", ChatColor.WHITE + "Wins: " + ChatColor.YELLOW + getSumoWins(viewing, obj), ChatColor.WHITE + "Losses: " + ChatColor.YELLOW + getSumoLosses(viewing, obj)));
        sumo.setItemMeta(sumoMeta);

        ItemStack classic = new ItemStack(Material.FISHING_ROD);
        ItemMeta classicMeta = classic.getItemMeta();
        classicMeta.setDisplayName(ChatColor.GREEN + "Classic Duels" + ChatColor.GRAY + " - " + ChatColor.BOLD + "" + ChatColor.LIGHT_PURPLE + "1v1");
        classicMeta.setLore(Arrays.asList(ChatColor.GRAY + "Iron gear 1v1 Duel!","", ChatColor.WHITE + "Games Played: " + ChatColor.YELLOW + getClassicGames(viewing, obj), "", ChatColor.WHITE + "Wins: " + ChatColor.YELLOW + getClassicWins(viewing, obj), ChatColor.WHITE + "Losses: " + ChatColor.YELLOW + getClassicLosses(viewing, obj)));
        classic.setItemMeta(classicMeta);

        ItemStack bridge = new ItemStack(Material.STAINED_CLAY, 1,  (short) 14);
        ItemMeta bridgeMeta = bridge.getItemMeta();
        bridgeMeta.setDisplayName(ChatColor.GREEN + "Bridge Duels" + ChatColor.GRAY + " - " + ChatColor.BOLD + "" + ChatColor.LIGHT_PURPLE + "1v1");
        bridgeMeta.setLore(Arrays.asList(ChatColor.GRAY + "Cross the bridge and score", ChatColor.GRAY + "more goals than the opposing team!","", ChatColor.WHITE + "Games Played: " + ChatColor.YELLOW + getBridgeGames(viewing, obj), "", ChatColor.WHITE + "Wins: " + ChatColor.YELLOW + getBridgeWins(viewing, obj), ChatColor.WHITE + "Losses: " + ChatColor.YELLOW + getBridgeLosses(viewing, obj), "", ChatColor.WHITE + "Goals: " + ChatColor.YELLOW + getBridgeGoals(viewing, obj), ChatColor.WHITE + "Kills: " + ChatColor.YELLOW + getBridgeKills(viewing, obj)));
        bridge.setItemMeta(bridgeMeta);

        ItemStack bedwars = new ItemStack(Material.BED);
        ItemMeta bedwarsMeta = bedwars.getItemMeta();
        bedwarsMeta.setDisplayName(ChatColor.GREEN + "Bedwars Duels" + ChatColor.GRAY + " - " + ChatColor.BOLD + "" + ChatColor.LIGHT_PURPLE + "1v1");
        bedwarsMeta.setLore(Arrays.asList(ChatColor.GRAY + "Destroy enemy beds, and kill", ChatColor.GRAY + "them to win the game!","", ChatColor.WHITE + "Games Played: " + ChatColor.YELLOW + getBedwarsGames(player, obj), "", ChatColor.WHITE + "Wins: " + ChatColor.YELLOW + getBedwarsWins(player, obj), ChatColor.WHITE + "Losses: " + ChatColor.YELLOW + getBedwarsLosses(player, obj), "", ChatColor.WHITE + "Beds Broken: " + ChatColor.YELLOW + getBedwarsBedsBroken(player, obj), ChatColor.WHITE + "Beds Lost: " + ChatColor.YELLOW + getBedwarsBedsLost(player, obj), "", ChatColor.WHITE + "Kills: " + ChatColor.YELLOW + getBedwarsKills(player, obj), ChatColor.WHITE + "Final Kills: " + ChatColor.YELLOW + getBedwarsFinalKills(player, obj), "", ChatColor.WHITE + "Deaths: " + ChatColor.YELLOW + getBedwarsDeaths(player, obj), ChatColor.WHITE + "Final Deaths: " + ChatColor.YELLOW + getBedwarsFinalDeaths(player, obj)));
        bedwars.setItemMeta(bedwarsMeta);



        shopGUI.setItem(4, playerHead);
        shopGUI.setItem(9, OG);
        shopGUI.setItem(10, OG);
        shopGUI.setItem(11, OG);
        shopGUI.setItem(12, OG);
        shopGUI.setItem(13, OG);
        shopGUI.setItem(14, OG);
        shopGUI.setItem(15, OG);
        shopGUI.setItem(16, OG);
        shopGUI.setItem(17, OG);
        shopGUI.setItem(19+9, sumo);
        shopGUI.setItem(21+9, classic);
        shopGUI.setItem(23+9, bridge);
        shopGUI.setItem(25+9, bedwars);

        player.openInventory(shopGUI);
    }


    public void openBridgeEditor(Player player, Player viewing) {

        Inventory shopGUI = Bukkit.createInventory(player, 6 * 9, ChatColor.GOLD + "Game Settings - " + ChatColor.BLUE + "The " + ChatColor.RED + "Bridge");
        ItemStack air = new ItemStack(Material.AIR);
        ItemStack playerHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skull = (SkullMeta) playerHead.getItemMeta();
        skull.setDisplayName(rankedName(viewing));
        skull.setOwner(viewing.getName());
        playerHead.setItemMeta(skull);

        ItemStack BlueGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11);
        ItemMeta BlueGlassMeta = BlueGlass.getItemMeta();
        BlueGlassMeta.setDisplayName(ChatColor.GOLD + " ");
        BlueGlass.setItemMeta(BlueGlassMeta);

        ItemStack WhiteGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0);
        ItemMeta WhiteGlassMeta = WhiteGlass.getItemMeta();
        WhiteGlassMeta.setDisplayName(ChatColor.GOLD + " ");
        WhiteGlass.setItemMeta(WhiteGlassMeta);

        ItemStack GrayGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemMeta GrayGlassMeta = GrayGlass.getItemMeta();
        GrayGlassMeta.setDisplayName(ChatColor.GOLD + " ");
        GrayGlass.setItemMeta(GrayGlassMeta);

        ItemStack RedGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
        ItemMeta RedGlassMeta = RedGlass.getItemMeta();
        RedGlassMeta.setDisplayName(ChatColor.GOLD + " ");
        RedGlass.setItemMeta(RedGlassMeta);
        ItemStack cancel = new ItemStack(Material.BARRIER);
        ItemMeta cancelMeta = cancel.getItemMeta();
        cancelMeta.setDisplayName(ChatColor.RED + "Cancel");
        cancelMeta.setLore(Collections.singletonList(ChatColor.GRAY + "Cancel Edit"));
        cancel.setItemMeta(cancelMeta);

        ItemStack confirm = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta confirmMeta = confirm.getItemMeta();
        confirmMeta.setDisplayName(ChatColor.GREEN + "Save Changes");
        confirmMeta.setLore(Collections.singletonList(ChatColor.GRAY + "Save & Use Configuration"));
        confirm.setItemMeta(confirmMeta);

        ItemStack blocks1 = new ItemStack(Material.STAINED_CLAY, 1, (byte) 14);
        ItemMeta blocks1Meta = blocks1.getItemMeta();
        blocks1Meta.setDisplayName(ChatColor.WHITE + "Blocks Slot 1");
        blocks1.setItemMeta(blocks1Meta);

        ItemStack blocks2 = new ItemStack(Material.STAINED_CLAY, 1, (byte) 14);
        ItemMeta blocks2Meta = blocks2.getItemMeta();
        blocks2Meta.setDisplayName(ChatColor.WHITE + "Blocks Slot 2");
        blocks2.setItemMeta(blocks2Meta);



//        shopGUI.setItem(0, BlueGlass);
//        shopGUI.setItem(1, BlueGlass);
//        shopGUI.setItem(2, BlueGlass);
//        shopGUI.setItem(3, BlueGlass);
//        shopGUI.setItem(4, WhiteGlass);
//        shopGUI.setItem(5, RedGlass);
//        shopGUI.setItem(6, RedGlass);
//        shopGUI.setItem(7, RedGlass);
//        shopGUI.setItem(8, RedGlass);

        shopGUI.setItem(36-9, BlueGlass);
        shopGUI.setItem(37-9, BlueGlass);
        shopGUI.setItem(38-9, BlueGlass);
        shopGUI.setItem(39-9, BlueGlass);
        shopGUI.setItem(40-9, WhiteGlass);
        shopGUI.setItem(41-9, RedGlass);
        shopGUI.setItem(42-9, RedGlass);
        shopGUI.setItem(43-9, RedGlass);
        shopGUI.setItem(44-9, RedGlass);

        shopGUI.setItem(36+9, cancel);
        shopGUI.setItem(37+9, GrayGlass);
        shopGUI.setItem(38+9, GrayGlass);
        shopGUI.setItem(39+9, GrayGlass);
        shopGUI.setItem(40+9, GrayGlass);
        shopGUI.setItem(41+9, GrayGlass);
        shopGUI.setItem(42+9, GrayGlass);
        shopGUI.setItem(43+9, GrayGlass);
        shopGUI.setItem(44+9, confirm);
//        shopGUI.setItem(17, OG);


        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("gameSettings");
        shopGUI.setItem(InvToGameSettingsSlot(getBridgeSwordSlot(player, obj)), new ItemStack(Material.IRON_SWORD));
        shopGUI.setItem(InvToGameSettingsSlot(getBridgePickaxeSlot(player, obj)), new ItemStack(Material.DIAMOND_PICKAXE));
        shopGUI.setItem(InvToGameSettingsSlot(getBridgeBlockSlot1(player, obj)), blocks1);
        shopGUI.setItem(InvToGameSettingsSlot(getBridgeBlockSlot2(player, obj)), blocks2);
        shopGUI.setItem(InvToGameSettingsSlot(getBridgeBowSlot(player, obj)), new ItemStack(Material.BOW));
        shopGUI.setItem(InvToGameSettingsSlot(getBridgeArrowSlot(player, obj)), new ItemStack(Material.ARROW));
        shopGUI.setItem(InvToGameSettingsSlot(getBridgeGappleSlot(player, obj)), new ItemStack(Material.GOLDEN_APPLE));
//        shopGUI.setItem(23+9, bridge);
//        shopGUI.setItem(25+9, bedwars);

        player.openInventory(shopGUI);
    }

    public int InvToGameSettingsSlot(int slot) {
        if (slot == 0) return 45-9;
        if (slot == 1) return 46-9;
        if (slot == 2) return 47-9;
        if (slot == 3) return 48-9;
        if (slot == 4) return 49-9;
        if (slot == 5) return 50-9;
        if (slot == 6) return 51-9;
        if (slot == 7) return 52-9;
        if (slot == 8) return 53-9;
        return slot-9;

    }

    public int GameSettingsToInvSlot(int slot) {
        if (slot == 45-9) return 0;
        if (slot == 46-9) return 1;
        if (slot == 47-9) return 2;
        if (slot == 48-9) return 3;
        if (slot == 49-9) return 4;
        if (slot == 50-9) return 5;
        if (slot == 51-9) return 6;
        if (slot == 52-9) return 7;
        if (slot == 53-9) return 8;
        return slot+9;
    }

    @EventHandler
    public void bridgeEditor(InventoryClickEvent event) {
        Inventory inv = event.getClickedInventory();
        if (inv == null) return;
        Player player = (Player) event.getWhoClicked();
        if (inv.getName().equalsIgnoreCase(ChatColor.GOLD + "Game Settings - " + ChatColor.BLUE + "The " + ChatColor.RED + "Bridge")) {
            if (event.getClick().equals(ClickType.SHIFT_LEFT) || event.getClick().equals(ClickType.SHIFT_RIGHT)) event.setCancelled(true);
            if (!((event.getSlot() < 27) || (event.getSlot() > 35 && event.getSlot() < 45))) {
                event.setCancelled(true);
                if (event.getCurrentItem().getType().equals(Material.BARRIER)) {
                    player.closeInventory();
                    player.sendMessage(ChatColor.BOLD.toString() + ChatColor.RED + "Cancelled Kit Edit!");
                }
                if (event.getCurrentItem().getType().equals(Material.EMERALD_BLOCK)) {
                    int swordSlot = -1;
                    int bowSlot = -1;
                    int pickaxeSlot = -1;
                    int blocks1Slot = -1;
                    int blocks2Slot = -1;
                    int gappleSlot = -1;
                    int arrowSlot = -1;

                    for (int i = 0; i < 54; i++) {
                        ItemStack item = inv.getItem(i);
                        if (item == null) continue;
                        if (item.getType().equals(Material.IRON_SWORD)) swordSlot = GameSettingsToInvSlot(i);
                        if (item.getType().equals(Material.BOW)) bowSlot = GameSettingsToInvSlot(i);
                        if (item.getType().equals(Material.DIAMOND_PICKAXE)) pickaxeSlot = GameSettingsToInvSlot(i);
                        if (blocks1Slot == -1) {
                            if (item.getType().equals(Material.STAINED_CLAY)) blocks1Slot = GameSettingsToInvSlot(i);
                        } else {
                            if (item.getType().equals(Material.STAINED_CLAY)) blocks2Slot = GameSettingsToInvSlot(i);
                        }
                        if (item.getType().equals(Material.GOLDEN_APPLE)) gappleSlot = GameSettingsToInvSlot(i);
                        if (item.getType().equals(Material.ARROW)) arrowSlot = GameSettingsToInvSlot(i);
                    }
                    if (swordSlot == -1 || bowSlot == -1 || pickaxeSlot == -1 || blocks1Slot == -1 || blocks2Slot == -1 || gappleSlot == -1 || arrowSlot == -1) {
                        player.sendMessage(ChatColor.RED + "Error Saving Kit! All Items must be present!");
                        return;
                    }


                    Document filter = new Document("UUID", player.getUniqueId().toString());
                    Bson update1 = set("gameSettings.bridge.swordSlot", swordSlot);
                    Bson update2 = set("gameSettings.bridge.bowSlot", bowSlot);
                    Bson update3 = set("gameSettings.bridge.pickaxeSlot", pickaxeSlot);
                    Bson update4 = set("gameSettings.bridge.blockSlot1", blocks1Slot);
                    Bson update5 = set("gameSettings.bridge.blockSlot2", blocks2Slot);
                    Bson update6 = set("gameSettings.bridge.gappleSlot", gappleSlot);
                    Bson update7 = set("gameSettings.bridge.arrowSlot", arrowSlot);
                    Bson updates = combine(update1, update2, update3, update4, update5, update6, update7);
                    col.findOneAndUpdate(filter, updates);


                    player.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "Saved Bridge Kit!");
                    player.closeInventory();
                }
            }
        }
    }


    @EventHandler
    public void inventoryCloseBridge(InventoryCloseEvent e) {
        if(e.getInventory().getName().equals(ChatColor.GOLD + "Game Settings - " + ChatColor.BLUE + "The " + ChatColor.RED + "Bridge") || e.getInventory().getName().equalsIgnoreCase(ChatColor.GOLD + "Game Settings - Classic Duels")) {
            e.getPlayer().setItemOnCursor(null);
        }
    }

    public void openKitEditorMenu(Player player) {
        Inventory gui = Bukkit.createInventory(player, 5*9, ChatColor.GOLD + "Game Settings");

        ItemStack playerHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skull = (SkullMeta) playerHead.getItemMeta();
        skull.setDisplayName(rankedName(player));
        skull.setOwner(player.getName());
        playerHead.setItemMeta(skull);

        ItemStack OrangeGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1);
        ItemMeta OrangeGlassMeta = OrangeGlass.getItemMeta();
        OrangeGlassMeta.setDisplayName(ChatColor.GOLD + " ");
        OrangeGlass.setItemMeta(OrangeGlassMeta);

        ItemStack classic = new ItemStack(Material.FISHING_ROD);
        ItemMeta classicMeta = classic.getItemMeta();
        classicMeta.setDisplayName(ChatColor.GREEN + "Classic Duels" + ChatColor.GRAY + " - " + ChatColor.BOLD + "" + ChatColor.LIGHT_PURPLE + "1v1");
        classicMeta.setLore(Arrays.asList(ChatColor.GRAY + "Iron gear 1v1 Duel!","", ChatColor.YELLOW + "Click To Edit!"));
        classic.setItemMeta(classicMeta);

        ItemStack bridge = new ItemStack(Material.STAINED_CLAY, 1,  (short) 14);
        ItemMeta bridgeMeta = bridge.getItemMeta();
        bridgeMeta.setDisplayName(ChatColor.GREEN + "Bridge Duels" + ChatColor.GRAY + " - " + ChatColor.BOLD + "" + ChatColor.LIGHT_PURPLE + "1v1");
        bridgeMeta.setLore(Arrays.asList(ChatColor.GRAY + "Cross the bridge and score", ChatColor.GRAY + "more goals than the opposing team!","", ChatColor.YELLOW + "Click To Edit!"));
        bridge.setItemMeta(bridgeMeta);

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(ChatColor.YELLOW + "Back");
        backMeta.setLore(Collections.singletonList(ChatColor.GRAY + "Go Back to Profile!"));
        back.setItemMeta(backMeta);

        ItemStack cancel = new ItemStack(Material.BARRIER);
        ItemMeta cancelMeta = cancel.getItemMeta();
        cancelMeta.setDisplayName(ChatColor.RED + "Close");
        cancelMeta.setLore(Collections.singletonList(ChatColor.GRAY + "Close the menu!"));
        cancel.setItemMeta(cancelMeta);

        gui.setItem(4, playerHead);
        gui.setItem(9, OrangeGlass);
        gui.setItem(10, OrangeGlass);
        gui.setItem(11, OrangeGlass);
        gui.setItem(12, OrangeGlass);
        gui.setItem(13, OrangeGlass);
        gui.setItem(14, OrangeGlass);
        gui.setItem(15, OrangeGlass);
        gui.setItem(16, OrangeGlass);
        gui.setItem(17, OrangeGlass);


        gui.setItem(29, classic);
        gui.setItem(33, bridge);

        gui.setItem(36, back);
        gui.setItem(44, cancel);

        player.openInventory(gui);
    }

    @EventHandler
    public void kitEditor(InventoryClickEvent event) {
        Inventory inv = event.getClickedInventory();
        Player player = (Player) event.getWhoClicked();
        if (inv == null) return;
        if (inv.getName().equalsIgnoreCase(ChatColor.GOLD + "Game Settings")) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) return;
            if (event.getCurrentItem().getType().equals(Material.BARRIER)) {
                player.closeInventory();
                player.sendMessage(ChatColor.BOLD.toString() + ChatColor.RED + "Cancelled Kit Edit!");
                return;
            }

            if (event.getCurrentItem().getType().equals(Material.ARROW)) {
                openProfileMenu(player);
                return;
            }

            if (event.getCurrentItem().getType().equals(Material.FISHING_ROD)) {
                openClassicEditor(player, player);
                return;
            }

            if (event.getCurrentItem().getType().equals(Material.STAINED_CLAY)) {
                openBridgeEditor(player, player);
                return;
            }
        }
    }

    public void openClassicEditor(Player player, Player viewing) {

        Inventory shopGUI = Bukkit.createInventory(player, 6 * 9, ChatColor.GOLD + "Game Settings - Classic Duels");
        ItemStack air = new ItemStack(Material.AIR);
        ItemStack playerHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skull = (SkullMeta) playerHead.getItemMeta();
        skull.setDisplayName(rankedName(viewing));
        skull.setOwner(viewing.getName());
        playerHead.setItemMeta(skull);

        ItemStack BlackGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        ItemMeta BlackGlassMeta = BlackGlass.getItemMeta();
        BlackGlassMeta.setDisplayName(ChatColor.GOLD + " ");
        BlackGlass.setItemMeta(BlackGlassMeta);

        ItemStack WhiteGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0);
        ItemMeta WhiteGlassMeta = WhiteGlass.getItemMeta();
        WhiteGlassMeta.setDisplayName(ChatColor.GOLD + " ");
        WhiteGlass.setItemMeta(WhiteGlassMeta);

        ItemStack GrayGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemMeta GrayGlassMeta = GrayGlass.getItemMeta();
        GrayGlassMeta.setDisplayName(ChatColor.GOLD + " ");
        GrayGlass.setItemMeta(GrayGlassMeta);

        ItemStack RedGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
        ItemMeta RedGlassMeta = RedGlass.getItemMeta();
        RedGlassMeta.setDisplayName(ChatColor.GOLD + " ");
        RedGlass.setItemMeta(RedGlassMeta);

        ItemStack cancel = new ItemStack(Material.BARRIER);
        ItemMeta cancelMeta = cancel.getItemMeta();
        cancelMeta.setDisplayName(ChatColor.RED + "Cancel");
        cancelMeta.setLore(Collections.singletonList(ChatColor.GRAY + "Cancel Edit"));
        cancel.setItemMeta(cancelMeta);

        ItemStack confirm = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta confirmMeta = confirm.getItemMeta();
        confirmMeta.setDisplayName(ChatColor.GREEN + "Save Changes");
        confirmMeta.setLore(Collections.singletonList(ChatColor.GRAY + "Save & Use Configuration"));
        confirm.setItemMeta(confirmMeta);

        ItemStack blocks1 = new ItemStack(Material.STAINED_CLAY, 1, (byte) 14);
        ItemMeta blocks1Meta = blocks1.getItemMeta();
        blocks1Meta.setDisplayName(ChatColor.WHITE + "Blocks Slot 1");
        blocks1.setItemMeta(blocks1Meta);

        ItemStack blocks2 = new ItemStack(Material.STAINED_CLAY, 1, (byte) 14);
        ItemMeta blocks2Meta = blocks2.getItemMeta();
        blocks2Meta.setDisplayName(ChatColor.WHITE + "Blocks Slot 2");
        blocks2.setItemMeta(blocks2Meta);



//        shopGUI.setItem(0, BlueGlass);
//        shopGUI.setItem(1, BlueGlass);
//        shopGUI.setItem(2, BlueGlass);
//        shopGUI.setItem(3, BlueGlass);
//        shopGUI.setItem(4, WhiteGlass);
//        shopGUI.setItem(5, RedGlass);
//        shopGUI.setItem(6, RedGlass);
//        shopGUI.setItem(7, RedGlass);
//        shopGUI.setItem(8, RedGlass);

        shopGUI.setItem(36-9, WhiteGlass);
        shopGUI.setItem(37-9, BlackGlass);
        shopGUI.setItem(38-9, WhiteGlass);
        shopGUI.setItem(39-9, BlackGlass);
        shopGUI.setItem(40-9, WhiteGlass);
        shopGUI.setItem(41-9, BlackGlass);
        shopGUI.setItem(42-9, WhiteGlass);
        shopGUI.setItem(43-9, BlackGlass);
        shopGUI.setItem(44-9, WhiteGlass);

        shopGUI.setItem(36+9, cancel);
        shopGUI.setItem(37+9, GrayGlass);
        shopGUI.setItem(38+9, GrayGlass);
        shopGUI.setItem(39+9, GrayGlass);
        shopGUI.setItem(40+9, GrayGlass);
        shopGUI.setItem(41+9, GrayGlass);
        shopGUI.setItem(42+9, GrayGlass);
        shopGUI.setItem(43+9, GrayGlass);
        shopGUI.setItem(44+9, confirm);
//        shopGUI.setItem(17, OG);


        Document filter = new Document("UUID", player.getUniqueId().toString());

        Document obj = (Document) col.find(filter).first().get("gameSettings");
        shopGUI.setItem(InvToGameSettingsSlot(getClassicSwordSlot(player, obj)), new ItemStack(Material.IRON_SWORD));
        shopGUI.setItem(InvToGameSettingsSlot(getClassicFishingRodSlot(player, obj)), new ItemStack(Material.FISHING_ROD));
        shopGUI.setItem(InvToGameSettingsSlot(getClassicFlintAndSteelSlot(player, obj)), new ItemStack(Material.FLINT_AND_STEEL));
        shopGUI.setItem(InvToGameSettingsSlot(getClassicArrowSlot(player, obj)), new ItemStack(Material.ARROW));
        shopGUI.setItem(InvToGameSettingsSlot(getClassicBowSlot(player, obj)), new ItemStack(Material.BOW));

        player.openInventory(shopGUI);
    }

    @EventHandler
    public void classicEditor(InventoryClickEvent event) {
        Inventory inv = event.getClickedInventory();
        if (inv == null) return;
        Player player = (Player) event.getWhoClicked();
        if (inv.getName().equalsIgnoreCase(ChatColor.GOLD + "Game Settings - Classic Duels")) {
            if (event.getClick().equals(ClickType.SHIFT_LEFT) || event.getClick().equals(ClickType.SHIFT_RIGHT)) event.setCancelled(true);
            if (!((event.getSlot() < 27) || (event.getSlot() > 35 && event.getSlot() < 45))) {
                event.setCancelled(true);
                if (event.getCurrentItem().getType().equals(Material.BARRIER)) {
                    player.closeInventory();
                    player.sendMessage(ChatColor.BOLD.toString() + ChatColor.RED + "Cancelled Kit Edit!");
                }
                if (event.getCurrentItem().getType().equals(Material.EMERALD_BLOCK)) {
                    int swordSlot = -1;
                    int bowSlot = -1;
                    int fishingRodSlot = -1;
                    int flintAndSteelSlot = -1;
                    int arrowSlot = -1;

                    for (int i = 0; i < 54; i++) {
                        ItemStack item = inv.getItem(i);
                        if (item == null) continue;
                        if (item.getType().equals(Material.IRON_SWORD)) swordSlot = GameSettingsToInvSlot(i);
                        if (item.getType().equals(Material.BOW)) bowSlot = GameSettingsToInvSlot(i);
                        if (item.getType().equals(Material.FISHING_ROD)) fishingRodSlot = GameSettingsToInvSlot(i);
                        if (item.getType().equals(Material.FLINT_AND_STEEL)) flintAndSteelSlot = GameSettingsToInvSlot(i);
                        if (item.getType().equals(Material.ARROW)) arrowSlot = GameSettingsToInvSlot(i);
                    }
                    if (swordSlot == -1 || bowSlot == -1 || fishingRodSlot == -1 || flintAndSteelSlot == -1 || arrowSlot == -1) {
                        player.sendMessage(ChatColor.RED + "Error Saving Kit! All Items must be present!");
                        return;
                    }


                    Document filter = new Document("UUID", player.getUniqueId().toString());
                    Bson update1 = set("gameSettings.classic.swordSlot", swordSlot);
                    Bson update2 = set("gameSettings.classic.bowSlot", bowSlot);
                    Bson update3 = set("gameSettings.classic.fishingRodSlot", fishingRodSlot);
                    Bson update4 = set("gameSettings.classic.flintAndSteelSlot", flintAndSteelSlot);
                    Bson update5 = set("gameSettings.classic.arrowSlot", arrowSlot);
                    Bson updates = combine(update1, update2, update3, update4, update5);
                    col.findOneAndUpdate(filter, updates);


                    player.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "Saved Classic Kit!");
                    player.closeInventory();
                }
            }
        }
    }

    public void openRankSwitcher(Player player) {
        ArrayList<String> purchased = getPurchased(player);

        Inventory gui = Bukkit.createInventory(player, 6*9, ChatColor.AQUA + "Rank Switcher");

        ItemStack Ranks = new ItemStack(Material.DIAMOND);
        ItemMeta RanksMeta = Ranks.getItemMeta();
        RanksMeta.setDisplayName(ChatColor.AQUA + "Rank Switcher");
        Ranks.setItemMeta(RanksMeta);

        ItemStack OG = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 3);
        ItemMeta OGMeta = OG.getItemMeta();
        OGMeta.setDisplayName(ChatColor.GOLD + " ");
        OG.setItemMeta(OGMeta);

        ItemStack non = new ItemStack(Material.BARRIER);
        ItemMeta nonMeta = non.getItemMeta();
        nonMeta.setDisplayName(ChatColor.GRAY + "Default Rank");
        nonMeta.setLore(Arrays.asList("", ChatColor.YELLOW + "Click To Select!"));
        non.setItemMeta(nonMeta);

        ItemStack vip = new ItemStack(Material.EMERALD);
        ItemMeta vipMeta = vip.getItemMeta();
        vipMeta.setDisplayName((arrayListIncludes(purchased, "VIP") ? ChatColor.GREEN : ChatColor.RED) + "VIP Rank");
        vipMeta.setLore(Arrays.asList("", arrayListIncludes(purchased, "VIP") ? ChatColor.YELLOW + "Click To Select!" : ChatColor.RED + "Not Purchased!"));
        vip.setItemMeta(vipMeta);

        ItemStack vipPlus = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta vipPlusMeta = vipPlus.getItemMeta();
        vipPlusMeta.setDisplayName(arrayListIncludes(purchased, "VIP+") ? ChatColor.GREEN + "VIP" + ChatColor.GOLD + "+" + ChatColor.GREEN + " Rank" : ChatColor.RED + "VIP+ Rank");
        vipPlusMeta.setLore(Arrays.asList("", arrayListIncludes(purchased, "VIP+") ? ChatColor.YELLOW + "Click To Select!" : ChatColor.RED + "Not Purchased!"));
        vipPlus.setItemMeta(vipPlusMeta);

        ItemStack mvp = new ItemStack(Material.DIAMOND);
        ItemMeta mvpMeta = mvp.getItemMeta();
        mvpMeta.setDisplayName(arrayListIncludes(purchased, "MVP") ? ChatColor.AQUA + "MVP Rank" : ChatColor.RED + "MVP Rank");
        mvpMeta.setLore(Arrays.asList("", arrayListIncludes(purchased, "MVP") ? ChatColor.YELLOW + "Click To Select!" : ChatColor.RED + "Not Purchased!"));
        mvp.setItemMeta(mvpMeta);

        ItemStack mvpPlus = new ItemStack(Material.DIAMOND_BLOCK);
        ItemMeta mvpPlusMeta = mvpPlus.getItemMeta();
        mvpPlusMeta.setDisplayName(arrayListIncludes(purchased, "MVP+") ? ChatColor.AQUA + "MVP" + ChatColor.RED + "+" + ChatColor.AQUA + " Rank" : ChatColor.RED + "MVP+ Rank");
        mvpPlusMeta.setLore(Arrays.asList("", arrayListIncludes(purchased, "MVP+") ? ChatColor.YELLOW + "Click To Select!" : ChatColor.RED + "Not Purchased!"));
        mvpPlus.setItemMeta(mvpPlusMeta);

        ItemStack mvpPlusPlus = new ItemStack(Material.GOLD_BLOCK);
        ItemMeta mvpPlusPlusMeta = mvpPlusPlus.getItemMeta();
        mvpPlusPlusMeta.setDisplayName(arrayListIncludes(purchased, "MVP++") ? ChatColor.GOLD + "MVP" + ChatColor.RED + "++" + ChatColor.GOLD + " Rank" : ChatColor.RED + "MVP++ Rank");
        mvpPlusPlusMeta.setLore(Arrays.asList("", arrayListIncludes(purchased, "MVP++") ? ChatColor.YELLOW + "Click To Select!" : ChatColor.RED + "Not Purchased!"));
        mvpPlusPlus.setItemMeta(mvpPlusPlusMeta);

        ItemStack specialRanks = new ItemStack(Material.ENDER_CHEST);
        ItemMeta specialRanksMeta = specialRanks.getItemMeta();
        specialRanksMeta.setDisplayName(ChatColor.YELLOW + "Special Ranks");
        specialRanksMeta.setLore(Arrays.asList("", ChatColor.YELLOW + "Click To Choose!"));
        specialRanks.setItemMeta(specialRanksMeta);

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

        gui.setItem(27, vip);
        gui.setItem(29, vipPlus);
        gui.setItem(31, mvp);
        gui.setItem(33, mvpPlus);
        gui.setItem(35, mvpPlusPlus);

        gui.setItem(45, non);
        if (arrayListIncludes(purchased, "YOUTUBE") || arrayListIncludes(purchased, "TWITCH") || arrayListIncludes(purchased, "DUCK") || arrayListIncludes(purchased, "HELPER") || arrayListIncludes(purchased, "MODERATOR") || arrayListIncludes(purchased, "ADMIN") || arrayListIncludes(purchased, "OWNER")) {
            gui.setItem(53, specialRanks);
        }

        player.openInventory(gui);
    }

    public void openSpecialRankSwitcher(Player player) {
        ArrayList<String> purchased = getPurchased(player);

        Inventory gui = Bukkit.createInventory(player, 6*9, ChatColor.AQUA + "Rank Switcher - " + ChatColor.YELLOW + "Special");

        ItemStack Ranks = new ItemStack(Material.DIAMOND);
        ItemMeta RanksMeta = Ranks.getItemMeta();
        RanksMeta.setDisplayName(ChatColor.AQUA + "Rank Switcher");
        Ranks.setItemMeta(RanksMeta);

        ItemStack OG = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4);
        ItemMeta OGMeta = OG.getItemMeta();
        OGMeta.setDisplayName(ChatColor.GOLD + " ");
        OG.setItemMeta(OGMeta);

        ItemStack youtube = getSkull("http://textures.minecraft.net/texture/d2f6c07a326def984e72f772ed645449f5ec96c6ca256499b5d2b84a8dce");
        ItemMeta youtubeMeta = youtube.getItemMeta();
        youtubeMeta.setDisplayName((arrayListIncludes(purchased, "YOUTUBE") ? ChatColor.RED + "YouTube " + ChatColor.WHITE + "Rank" : ChatColor.RED + "YouTube Rank") );
        youtubeMeta.setLore(Arrays.asList("", arrayListIncludes(purchased, "YOUTUBE") ? ChatColor.YELLOW + "Click To Select!" : ChatColor.RED + "Not Owned!"));
        youtube.setItemMeta(youtubeMeta);

        ItemStack twitch = getSkull("http://textures.minecraft.net/texture/46be65f44cd21014c8cddd0158bf75227adcb1fd179f4c1acd158c88871a13f");
        ItemMeta twitchMeta = twitch.getItemMeta();
        twitchMeta.setDisplayName((arrayListIncludes(purchased, "TWITCH") ? ChatColor.DARK_PURPLE + "Twitch " + ChatColor.WHITE + "Rank" : ChatColor.RED + "Twitch Rank") );
        twitchMeta.setLore(Arrays.asList("", arrayListIncludes(purchased, "TWITCH") ? ChatColor.YELLOW + "Click To Select!" : ChatColor.RED + "Not Owned!"));
        twitch.setItemMeta(twitchMeta);

        ItemStack duck = getSkull("http://textures.minecraft.net/texture/c882eb49d89f1eb8c68814e71c6263bbec7ba633f27bff6efdb92a6799ba");
        ItemMeta duckMeta = duck.getItemMeta();
        duckMeta.setDisplayName((arrayListIncludes(purchased, "DUCK") ? ChatColor.GOLD : ChatColor.RED) + "Duck Rank" );
        duckMeta.setLore(Arrays.asList("", arrayListIncludes(purchased, "DUCK") ? ChatColor.YELLOW + "Click To Select!" : ChatColor.RED + "Not Owned!"));
        duck.setItemMeta(duckMeta);

        ItemStack helper = getSkull("http://textures.minecraft.net/texture/1c4fd7a754aeadeeeeaf12d78308ed4ed52c2bec9f8d0b4c8e35f7df94865aa");
        ItemMeta helperMeta = helper.getItemMeta();
        helperMeta.setDisplayName((arrayListIncludes(purchased, "HELPER") ? ChatColor.GREEN : ChatColor.RED) + "Helper Rank" );
        helperMeta.setLore(Arrays.asList("", arrayListIncludes(purchased, "HELPER") ? ChatColor.YELLOW + "Click To Select!" : ChatColor.RED + "Not Owned!"));
        helper.setItemMeta(helperMeta);

        ItemStack moderator = getSkull("http://textures.minecraft.net/texture/4a898d266fc6cc74edf095d9999922d156c8e78cb07a99bd52a2d7867b858");
        ItemMeta moderatorMeta = moderator.getItemMeta();
        moderatorMeta.setDisplayName((arrayListIncludes(purchased, "MODERATOR") ? ChatColor.AQUA : ChatColor.RED) + "Mod Rank" );
        moderatorMeta.setLore(Arrays.asList("", arrayListIncludes(purchased, "MODERATOR") ? ChatColor.YELLOW + "Click To Select!" : ChatColor.RED + "Not Owned!"));
        moderator.setItemMeta(moderatorMeta);

        ItemStack admin = getSkull("http://textures.minecraft.net/texture/249c5d42c4f4afba964657fdbc4ff67c2be7aa573f733d4a92e1a3a49a8feb");
        ItemMeta adminMeta = admin.getItemMeta();
        adminMeta.setDisplayName((arrayListIncludes(purchased, "ADMIN") ? ChatColor.RED : ChatColor.RED) + "Admin Rank" );
        adminMeta.setLore(Arrays.asList("", arrayListIncludes(purchased, "ADMIN") ? ChatColor.YELLOW + "Click To Select!" : ChatColor.RED + "Not Owned!"));
        admin.setItemMeta(adminMeta);

        ItemStack owner = getSkull("http://textures.minecraft.net/texture/e6a0cfa27ae00b74d7ba27a7f72a47626f933dece6d337a64c8943223dd82188");
        ItemMeta ownerMeta = owner.getItemMeta();
        ownerMeta.setDisplayName((arrayListIncludes(purchased, "OWNER") ? ChatColor.DARK_RED : ChatColor.RED) + "Owner Rank" );
        ownerMeta.setLore(Arrays.asList("", arrayListIncludes(purchased, "OWNER") ? ChatColor.YELLOW + "Click To Select!" : ChatColor.RED + "Not Owned!"));
        owner.setItemMeta(ownerMeta);

        ItemStack specialRanks = new ItemStack(Material.ENDER_CHEST);
        ItemMeta specialRanksMeta = specialRanks.getItemMeta();
        specialRanksMeta.setDisplayName(ChatColor.YELLOW + "Special Ranks");
        specialRanks.setItemMeta(specialRanksMeta);

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(ChatColor.YELLOW + "Back");
        backMeta.setLore(Arrays.asList(ChatColor.GRAY + "Go back to", ChatColor.GRAY + "previous menu!"));
        back.setItemMeta(backMeta);

        gui.setItem(4, specialRanks);
        gui.setItem(9, OG);
        gui.setItem(10, OG);
        gui.setItem(11, OG);
        gui.setItem(12, OG);
        gui.setItem(13, OG);
        gui.setItem(14, OG);
        gui.setItem(15, OG);
        gui.setItem(16, OG);
        gui.setItem(17, OG);

        gui.setItem(29, youtube);
        gui.setItem(31, twitch);
        gui.setItem(33, duck);
        gui.setItem(38, helper);
        gui.setItem(40, moderator);
        gui.setItem(42, admin);
        gui.setItem(49, owner);

        gui.setItem(45, back);

        player.openInventory(gui);
    }

    public void setRank(Player player, String rank) {
        Document filter = new Document("UUID", player.getUniqueId().toString());
        Bson update = set("rank", rank);
        col.findOneAndUpdate(filter, update);

        String playerRank = rank;
        for (Player p : Bukkit.getOnlinePlayers()) {
            Scoreboard scoreboard = p.getScoreboard();

            Team team = scoreboard.getTeam(Ranks.getRankPriority(player) + player.getName().substring(0, 0) + player.getName().substring(1));
            if (team == null) {
                team = scoreboard.registerNewTeam(Ranks.getRankPriority(player) + player.getName().substring(0, 0) + player.getName().substring(1));
            }


            switch (playerRank) {
                case "OWNER":
                    team.setPrefix(ChatColor.DARK_RED + "[OWNER] ");
                    break;
                case "ADMIN":
                    team.setPrefix(ChatColor.RED + "[ADMIN] ");
                    break;
                case "MODERATOR":
                    team.setPrefix(ChatColor.AQUA + "[MOD] ");
                    break;
                case "HELPER":
                    team.setPrefix(ChatColor.GREEN + "[HELPER] ");
                    break;
                case "YOUTUBE":
                    team.setPrefix(ChatColor.RED + "[" + ChatColor.WHITE + "YOUTUBE" + ChatColor.RED + "] ");
                    break;
                case "TWITCH":
                    team.setPrefix(ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "TWITCH" + ChatColor.DARK_PURPLE + "] ");
                    break;
                default:
                    team.setPrefix(ChatColor.GRAY + "");
                    break;
                case "VIP":
                    team.setPrefix(ChatColor.GREEN + "[VIP] ");
                    break;
                case "VIP+":
                    team.setPrefix(ChatColor.GREEN + "[VIP" + ChatColor.GOLD + "+" + ChatColor.GREEN + "] ");
                    break;
                case "MVP":
                    team.setPrefix(ChatColor.AQUA + "[MVP] ");
                    break;
                case "MVP+":
                    team.setPrefix(ChatColor.AQUA + "[MVP" + getPlusColor(player) + "+" + ChatColor.AQUA + "] ");
                    break;
                case "MVP++":
                    team.setPrefix(ChatColor.GOLD + "[MVP" + getPlusColor(player) + "++" + ChatColor.GOLD + "] ");
                    break;
                case "DUCK":
                    team.setPrefix(ChatColor.YELLOW + "[" + ChatColor.GOLD + "DUCK" + ChatColor.YELLOW + "] ");
                    break;
            }
            team.addEntry(player.getName());
        }

        Scoreboard scoreboard = player.getScoreboard();
        for (Player p : Bukkit.getOnlinePlayers()) {

            Team team = scoreboard.getTeam(Ranks.getRankPriority(p) + p.getName().substring(0, 0) + p.getName().substring(1));
            if (team == null) {
                team = scoreboard.registerNewTeam(Ranks.getRankPriority(p) + p.getName().substring(0, 0) + p.getName().substring(1));
            }

            switch (getRank(p)) {
                case "OWNER":
                    team.setPrefix(ChatColor.DARK_RED + "[OWNER] ");
                    break;
                case "ADMIN":
                    team.setPrefix(ChatColor.RED + "[ADMIN] ");
                    break;
                case "MODERATOR":
                    team.setPrefix(ChatColor.AQUA + "[MOD] ");
                    break;
                case "HELPER":
                    team.setPrefix(ChatColor.GREEN + "[HELPER] ");
                    break;
                case "YOUTUBE":
                    team.setPrefix(ChatColor.RED + "[" + ChatColor.WHITE + "YOUTUBE" + ChatColor.RED + "] ");
                    break;
                case "TWITCH":
                    team.setPrefix(ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "TWITCH" + ChatColor.DARK_PURPLE + "] ");
                    break;
                default:
                    team.setPrefix(ChatColor.GRAY + "");
                    break;
                case "VIP":
                    team.setPrefix(ChatColor.GREEN + "[VIP] ");
                    break;
                case "VIP+":
                    team.setPrefix(ChatColor.GREEN + "[VIP" + ChatColor.GOLD + "+" + ChatColor.GREEN + "] ");
                    break;
                case "MVP":
                    team.setPrefix(ChatColor.AQUA + "[MVP] ");
                    break;
                case "MVP+":
                    team.setPrefix(ChatColor.AQUA + "[MVP" + getPlusColor(player) + "+" + ChatColor.AQUA + "] ");
                    break;
                case "MVP++":
                    team.setPrefix(ChatColor.GOLD + "[MVP" + getPlusColor(player) + "++" + ChatColor.GOLD + "] ");
                    break;
                case "DUCK":
                    team.setPrefix(ChatColor.YELLOW + "[" + ChatColor.GOLD + "DUCK" + ChatColor.YELLOW + "] ");
                    break;
            }
            team.addEntry(p.getName());
        }


        Team team = scoreboard.getTeam(Ranks.getRankPriority(player) + player.getName().substring(0, 0) + player.getName().substring(1));
        if (team == null) {
            team = scoreboard.registerNewTeam(Ranks.getRankPriority(player) + player.getName().substring(0, 0) + player.getName().substring(1));
        }

//        ProtocolManager pm = ProtocolLibrary.getProtocolManager();
//        PacketContainer packet = pm.createPacket(PacketType.Play.Server.ANIMATION);
//        packet.getModifier().writeDefaults();
//        packet.getIntegers().write(0, event.getPlayer().getEntityId()).write(1, 0);
//        try {
//            pm.sendServerPacket(event.getPlayer(), packet);
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }




        switch (playerRank) {
            case "OWNER":
                team.setPrefix(ChatColor.DARK_RED + "[OWNER] ");
                break;
            case "ADMIN":
                team.setPrefix(ChatColor.RED + "[ADMIN] ");
                break;
            case "MODERATOR":
                team.setPrefix(ChatColor.AQUA + "[MOD] ");
                break;
            case "HELPER":
                team.setPrefix(ChatColor.GREEN + "[HELPER] ");
                break;
            case "YOUTUBE":
                team.setPrefix(ChatColor.RED + "[" + ChatColor.WHITE + "YOUTUBE" + ChatColor.RED + "] ");
                break;
            case "TWITCH":
                team.setPrefix(ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "TWITCH" + ChatColor.DARK_PURPLE + "] ");
                break;
            default:
                team.setPrefix(ChatColor.GRAY + "");
                break;
            case "VIP":
                team.setPrefix(ChatColor.GREEN + "[VIP] ");
                break;
            case "VIP+":
                team.setPrefix(ChatColor.GREEN + "[VIP" + ChatColor.GOLD + "+" + ChatColor.GREEN + "] ");
                break;
            case "MVP":
                team.setPrefix(ChatColor.AQUA + "[MVP] ");
                break;
            case "MVP+":
                team.setPrefix(ChatColor.AQUA + "[MVP" + getPlusColor(player) + "+" + ChatColor.AQUA + "] ");
                break;
            case "MVP++":
                team.setPrefix(ChatColor.GOLD + "[MVP" + getPlusColor(player) + "++" + ChatColor.GOLD + "] ");
                break;
            case "DUCK":
                team.setPrefix(ChatColor.YELLOW + "[" + ChatColor.GOLD + "DUCK" + ChatColor.YELLOW + "] ");
        }
        team.addEntry(player.getName());
        player.setScoreboard(scoreboard);
    }

    public void setPlusColor(Player player, String plusColor) {
        Document filter = new Document("UUID", player.getUniqueId().toString());
        Bson update = set("plusColor", plusColor);
        col.findOneAndUpdate(filter, update);

        String playerRank = getRank(player);
        for (Player p : Bukkit.getOnlinePlayers()) {
            Scoreboard scoreboard = p.getScoreboard();

            Team team = scoreboard.getTeam(Ranks.getRankPriority(player) + player.getName().substring(0, 0) + player.getName().substring(1));
            if (team == null) {
                team = scoreboard.registerNewTeam(Ranks.getRankPriority(player) + player.getName().substring(0, 0) + player.getName().substring(1));
            }


            switch (playerRank) {
                case "OWNER":
                    team.setPrefix(ChatColor.DARK_RED + "[OWNER] ");
                    break;
                case "ADMIN":
                    team.setPrefix(ChatColor.RED + "[ADMIN] ");
                    break;
                case "MODERATOR":
                    team.setPrefix(ChatColor.AQUA + "[MOD] ");
                    break;
                case "HELPER":
                    team.setPrefix(ChatColor.GREEN + "[HELPER] ");
                    break;
                case "YOUTUBE":
                    team.setPrefix(ChatColor.RED + "[" + ChatColor.WHITE + "YOUTUBE" + ChatColor.RED + "] ");
                    break;
                case "TWITCH":
                    team.setPrefix(ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "TWITCH" + ChatColor.DARK_PURPLE + "] ");
                    break;
                default:
                    team.setPrefix(ChatColor.GRAY + "");
                    break;
                case "VIP":
                    team.setPrefix(ChatColor.GREEN + "[VIP] ");
                    break;
                case "VIP+":
                    team.setPrefix(ChatColor.GREEN + "[VIP" + ChatColor.GOLD + "+" + ChatColor.GREEN + "] ");
                    break;
                case "MVP":
                    team.setPrefix(ChatColor.AQUA + "[MVP] ");
                    break;
                case "MVP+":
                    team.setPrefix(ChatColor.AQUA + "[MVP" + getPlusColor(player) + "+" + ChatColor.AQUA + "] ");
                    break;
                case "MVP++":
                    team.setPrefix(ChatColor.GOLD + "[MVP" + getPlusColor(player) + "++" + ChatColor.GOLD + "] ");
                    break;
                case "DUCK":
                    team.setPrefix(ChatColor.YELLOW + "[" + ChatColor.GOLD + "DUCK" + ChatColor.YELLOW + "] ");
                    break;
            }
            team.addEntry(player.getName());
        }

        Scoreboard scoreboard = player.getScoreboard();
        for (Player p : Bukkit.getOnlinePlayers()) {

            Team team = scoreboard.getTeam(Ranks.getRankPriority(p) + p.getName().substring(0, 0) + p.getName().substring(1));
            if (team == null) {
                team = scoreboard.registerNewTeam(Ranks.getRankPriority(p) + p.getName().substring(0, 0) + p.getName().substring(1));
            }

            switch (getRank(p)) {
                case "OWNER":
                    team.setPrefix(ChatColor.DARK_RED + "[OWNER] ");
                    break;
                case "ADMIN":
                    team.setPrefix(ChatColor.RED + "[ADMIN] ");
                    break;
                case "MODERATOR":
                    team.setPrefix(ChatColor.AQUA + "[MOD] ");
                    break;
                case "HELPER":
                    team.setPrefix(ChatColor.GREEN + "[HELPER] ");
                    break;
                case "YOUTUBE":
                    team.setPrefix(ChatColor.RED + "[" + ChatColor.WHITE + "YOUTUBE" + ChatColor.RED + "] ");
                    break;
                case "TWITCH":
                    team.setPrefix(ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "TWITCH" + ChatColor.DARK_PURPLE + "] ");
                    break;
                default:
                    team.setPrefix(ChatColor.GRAY + "");
                    break;
                case "VIP":
                    team.setPrefix(ChatColor.GREEN + "[VIP] ");
                    break;
                case "VIP+":
                    team.setPrefix(ChatColor.GREEN + "[VIP" + ChatColor.GOLD + "+" + ChatColor.GREEN + "] ");
                    break;
                case "MVP":
                    team.setPrefix(ChatColor.AQUA + "[MVP] ");
                    break;
                case "MVP+":
                    team.setPrefix(ChatColor.AQUA + "[MVP" + getPlusColor(player) + "+" + ChatColor.AQUA + "] ");
                    break;
                case "MVP++":
                    team.setPrefix(ChatColor.GOLD + "[MVP" + getPlusColor(player) + "++" + ChatColor.GOLD + "] ");
                    break;
                case "DUCK":
                    team.setPrefix(ChatColor.YELLOW + "[" + ChatColor.GOLD + "DUCK" + ChatColor.YELLOW + "] ");
                    break;
            }
            team.addEntry(p.getName());
        }


        Team team = scoreboard.getTeam(Ranks.getRankPriority(player) + player.getName().substring(0, 0) + player.getName().substring(1));
        if (team == null) {
            team = scoreboard.registerNewTeam(Ranks.getRankPriority(player) + player.getName().substring(0, 0) + player.getName().substring(1));
        }

//        ProtocolManager pm = ProtocolLibrary.getProtocolManager();
//        PacketContainer packet = pm.createPacket(PacketType.Play.Server.ANIMATION);
//        packet.getModifier().writeDefaults();
//        packet.getIntegers().write(0, event.getPlayer().getEntityId()).write(1, 0);
//        try {
//            pm.sendServerPacket(event.getPlayer(), packet);
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }




        switch (playerRank) {
            case "OWNER":
                team.setPrefix(ChatColor.DARK_RED + "[OWNER] ");
                break;
            case "ADMIN":
                team.setPrefix(ChatColor.RED + "[ADMIN] ");
                break;
            case "MODERATOR":
                team.setPrefix(ChatColor.AQUA + "[MOD] ");
                break;
            case "HELPER":
                team.setPrefix(ChatColor.GREEN + "[HELPER] ");
                break;
            case "YOUTUBE":
                team.setPrefix(ChatColor.RED + "[" + ChatColor.WHITE + "YOUTUBE" + ChatColor.RED + "] ");
                break;
            case "TWITCH":
                team.setPrefix(ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "TWITCH" + ChatColor.DARK_PURPLE + "] ");
                break;
            default:
                team.setPrefix(ChatColor.GRAY + "");
                break;
            case "VIP":
                team.setPrefix(ChatColor.GREEN + "[VIP] ");
                break;
            case "VIP+":
                team.setPrefix(ChatColor.GREEN + "[VIP" + ChatColor.GOLD + "+" + ChatColor.GREEN + "] ");
                break;
            case "MVP":
                team.setPrefix(ChatColor.AQUA + "[MVP] ");
                break;
            case "MVP+":
                team.setPrefix(ChatColor.AQUA + "[MVP" + getPlusColor(player) + "+" + ChatColor.AQUA + "] ");
                break;
            case "MVP++":
                team.setPrefix(ChatColor.GOLD + "[MVP" + getPlusColor(player) + "++" + ChatColor.GOLD + "] ");
                break;
            case "DUCK":
                team.setPrefix(ChatColor.YELLOW + "[" + ChatColor.GOLD + "DUCK" + ChatColor.YELLOW + "] ");
        }
        team.addEntry(player.getName());
        player.setScoreboard(scoreboard);
    }

    @EventHandler
    public void rankSwitcher(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getClickedInventory() == null) return;
        if (event.getCurrentItem() == null) return;
        if (!event.getCurrentItem().hasItemMeta()) return;
        Inventory inv = event.getClickedInventory();
        ItemStack item = event.getCurrentItem();
        String itemName = item.getItemMeta().getDisplayName();

        if (inv.getName().equalsIgnoreCase(ChatColor.AQUA + "Rank Switcher")) {
            if (itemName.equalsIgnoreCase(ChatColor.GRAY + "Default Rank")) {
                setRank(player, "DEFAULT");
                player.sendMessage(ChatColor.GREEN + "Your Rank is now set to " + ChatColor.GRAY + "DEFAULT" + ChatColor.GREEN + "! \nYou may have to re-log to see some effects of your rank!");
                Scoreboards.updateLobbyScoreboard(player);
                player.closeInventory();
            }
            if (itemName.equalsIgnoreCase(ChatColor.GREEN + "VIP Rank")) {
                setRank(player, "VIP");
                player.sendMessage(ChatColor.GREEN + "Your Rank is now set to " + ChatColor.GREEN + "VIP" + ChatColor.GREEN + "! \nYou may have to re-log to see some effects of your rank!");
                Scoreboards.updateLobbyScoreboard(player);
                player.closeInventory();
            }
            if (itemName.equalsIgnoreCase(ChatColor.GREEN + "VIP" + ChatColor.GOLD + "+" + ChatColor.GREEN + " Rank")) {
                setRank(player, "VIP+");
                player.sendMessage(ChatColor.GREEN + "Your Rank is now set to " + ChatColor.GREEN + "VIP" + ChatColor.GOLD + "+" + ChatColor.GREEN + "! \nYou may have to re-log to see some effects of your rank!");
                Scoreboards.updateLobbyScoreboard(player);
                player.closeInventory();
            }
            if (itemName.equalsIgnoreCase(ChatColor.AQUA + "MVP Rank")) {
                setRank(player, "MVP");
                player.sendMessage(ChatColor.GREEN + "Your Rank is now set to " + ChatColor.AQUA + "MVP" + ChatColor.GREEN + "! \nYou may have to re-log to see some effects of your rank!");
                Scoreboards.updateLobbyScoreboard(player);
                player.closeInventory();
            }
            if (itemName.equalsIgnoreCase(ChatColor.AQUA + "MVP" + ChatColor.RED + "+" + ChatColor.AQUA + " Rank")) {
                setRank(player, "MVP+");
                player.sendMessage(ChatColor.GREEN + "Your Rank is now set to " + ChatColor.AQUA + "MVP" + ChatColor.RED + "+" + ChatColor.GREEN + "! \nYou may have to re-log to see some effects of your rank!");
                Scoreboards.updateLobbyScoreboard(player);
                player.closeInventory();
            }
            if (itemName.equalsIgnoreCase(ChatColor.GOLD + "MVP" + ChatColor.RED + "++" + ChatColor.GOLD + " Rank")) {
                setRank(player, "MVP++");
                player.sendMessage(ChatColor.GREEN + "Your Rank is now set to " + ChatColor.GOLD + "MVP" + ChatColor.RED + "++" + ChatColor.GREEN + "! \nYou may have to re-log to see some effects of your rank!");
                Scoreboards.updateLobbyScoreboard(player);
                player.closeInventory();
            }
            if (itemName.equalsIgnoreCase(ChatColor.YELLOW + "Special Ranks")) {
                openSpecialRankSwitcher(player);
            }

        }
        if (inv.getName().equalsIgnoreCase(ChatColor.AQUA + "Rank Switcher - " + ChatColor.YELLOW + "Special")) {
            if (itemName.equalsIgnoreCase(ChatColor.YELLOW + "Back")) {
                openRankSwitcher(player);
            }
            if (itemName.equalsIgnoreCase(ChatColor.RED + "YouTube " + ChatColor.WHITE + "Rank")) {
                setRank(player, "YOUTUBE");
                player.sendMessage(ChatColor.GREEN + "Your Rank is now set to " + ChatColor.RED + "YOU" + ChatColor.WHITE + "TUBE" + ChatColor.GREEN + "! \nYou may have to re-log to see some effects of your rank!");
                Scoreboards.updateLobbyScoreboard(player);
                player.closeInventory();
            }
            if (itemName.equalsIgnoreCase(ChatColor.DARK_PURPLE + "Twitch " + ChatColor.WHITE + "Rank")) {
                setRank(player, "TWITCH");
                player.sendMessage(ChatColor.GREEN + "Your Rank is now set to " + ChatColor.DARK_PURPLE + "TWITCH" + ChatColor.GREEN + "! \nYou may have to re-log to see some effects of your rank!");
                Scoreboards.updateLobbyScoreboard(player);
                player.closeInventory();
            }
            if (itemName.equalsIgnoreCase(ChatColor.GOLD + "Duck Rank")) {
                setRank(player, "DUCK");
                player.sendMessage(ChatColor.GREEN + "Your Rank is now set to " + ChatColor.GOLD + "DUCK" + ChatColor.GREEN + "! \nYou may have to re-log to see some effects of your rank!");
                Scoreboards.updateLobbyScoreboard(player);
                player.closeInventory();
            }
            if (itemName.equalsIgnoreCase(ChatColor.GREEN + "Helper Rank")) {
                setRank(player, "HELPER");
                player.sendMessage(ChatColor.GREEN + "Your Rank is now set to " + ChatColor.GREEN + "HELPER" + ChatColor.GREEN + "! \nYou may have to re-log to see some effects of your rank!");
                Scoreboards.updateLobbyScoreboard(player);
                player.closeInventory();
            }
            if (itemName.equalsIgnoreCase(ChatColor.AQUA + "Mod Rank")) {
                setRank(player, "MODERATOR");
                player.sendMessage(ChatColor.GREEN + "Your Rank is now set to " + ChatColor.AQUA + "MODERATOR" + ChatColor.GREEN + "! \nYou may have to re-log to see some effects of your rank!");
                Scoreboards.updateLobbyScoreboard(player);
                player.closeInventory();
            }
            if (itemName.equalsIgnoreCase(ChatColor.RED + "Admin Rank")) {
                setRank(player, "ADMIN");
                player.sendMessage(ChatColor.GREEN + "Your Rank is now set to " + ChatColor.RED + "ADMIN" + ChatColor.GREEN + "! \nYou may have to re-log to see some effects of your rank!");
                Scoreboards.updateLobbyScoreboard(player);
                player.closeInventory();
            }
            if (itemName.equalsIgnoreCase(ChatColor.DARK_RED + "Owner Rank")) {
                setRank(player, "OWNER");
                player.sendMessage(ChatColor.GREEN + "Your Rank is now set to " + ChatColor.DARK_RED + "OWNER" + ChatColor.GREEN + "! \nYou may have to re-log to see some effects of your rank!");
                Scoreboards.updateLobbyScoreboard(player);
                player.closeInventory();
            }
        } else if (inv.getName().equalsIgnoreCase(ChatColor.GOLD + "Plus Colors")) {
            if (itemName.equalsIgnoreCase(ChatColor.RED + "Red")) setPlusColor(player, "RED");
            if (itemName.equalsIgnoreCase(ChatColor.GREEN + "Lime")) setPlusColor(player, "LIME");
        }
    }

    public void openPlusColorSwitcher(Player player) {
        ArrayList<String> purchased = getPurchased(player);

        Inventory gui = Bukkit.createInventory(player, 6 * 9, ChatColor.GOLD + "Plus Colors");

        int coins = getPlayerCoins(player);

        ItemStack playerHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skull = (SkullMeta) playerHead.getItemMeta();
        skull.setDisplayName(ChatColor.GOLD + "Your Profile");
        skull.setOwner(player.getName());
        playerHead.setItemMeta(skull);

        ItemStack PlusColors = new ItemStack(Material.INK_SACK, 1, (short) 14);
        ItemMeta PlusColorsMeta = PlusColors.getItemMeta();
        PlusColorsMeta.setDisplayName(ChatColor.GOLD + "Plus Colors");
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
        RedColorMeta.setLore(Arrays.asList("", arrayListIncludes(purchased, "RED_PLUS") ? ChatColor.YELLOW + "Click To Select!" : ChatColor.RED + "Not Owned!"));
        RedColorMeta.setDisplayName(ChatColor.RED + "Red");
        RedColor.setItemMeta(RedColorMeta);

        ItemStack LimeColor = new ItemStack(Material.INK_SACK, 1, (short) 10);
        ItemMeta LimeColorMeta = LimeColor.getItemMeta();
        LimeColorMeta.setLore(Arrays.asList("", arrayListIncludes(purchased, "LIME_PLUS") ? ChatColor.YELLOW + "Click To Select!" : ChatColor.RED + "Not Owned!"));
        LimeColorMeta.setDisplayName((arrayListIncludes(purchased, "LIME_PLUS") ? ChatColor.GREEN : ChatColor.RED) + "Lime");
        LimeColor.setItemMeta(LimeColorMeta);

        ItemStack AquaColor = new ItemStack(Material.INK_SACK, 1, (short) 12);
        ItemMeta AquaColorMeta = AquaColor.getItemMeta();
        AquaColorMeta.setLore(Arrays.asList("", arrayListIncludes(purchased, "AQUA_PLUS") ? ChatColor.YELLOW + "Click To Select!" : ChatColor.RED + "Not Owned!"));
        AquaColorMeta.setDisplayName((arrayListIncludes(purchased, "AQUA_PLUS") ? ChatColor.AQUA : ChatColor.RED) + "Aqua");
        AquaColor.setItemMeta(AquaColorMeta);

        ItemStack YellowColor = new ItemStack(Material.INK_SACK, 1, (short) 11);
        ItemMeta YellowColorMeta = YellowColor.getItemMeta();
        YellowColorMeta.setLore(Arrays.asList("", arrayListIncludes(purchased, "YELLOW_PLUS") ? ChatColor.YELLOW + "Click To Select!" : ChatColor.RED + "Not Owned!"));
        YellowColorMeta.setDisplayName((arrayListIncludes(purchased, "YELLOW_PLUS") ? ChatColor.YELLOW : ChatColor.RED) + "Yellow");
        YellowColor.setItemMeta(YellowColorMeta);

        ItemStack GreenColor = new ItemStack(Material.INK_SACK, 1, (short) 2);
        ItemMeta GreenColorMeta = GreenColor.getItemMeta();
        GreenColorMeta.setLore(Arrays.asList("", arrayListIncludes(purchased, "DARK_GREEN_PLUS") ? ChatColor.YELLOW + "Click To Select!" : ChatColor.RED + "Not Owned!"));
        GreenColorMeta.setDisplayName((arrayListIncludes(purchased, "DARK_GREEN_PLUS") ? ChatColor.DARK_GREEN : ChatColor.RED) + "Dark Green");
        GreenColor.setItemMeta(GreenColorMeta);

        ItemStack BlueColor = new ItemStack(Material.INK_SACK, 1, (short) 4);
        ItemMeta BlueColorMeta = BlueColor.getItemMeta();
        BlueColorMeta.setLore(Arrays.asList("", arrayListIncludes(purchased, "BLUE_PLUS") ? ChatColor.YELLOW + "Click To Select!" : ChatColor.RED + "Not Owned!"));
        BlueColorMeta.setDisplayName((arrayListIncludes(purchased, "BLUE_PLUS") ? ChatColor.BLUE : ChatColor.RED) + "Blue");
        BlueColor.setItemMeta(BlueColorMeta);

        ItemStack CyanColor = new ItemStack(Material.INK_SACK, 1, (short) 6);
        ItemMeta CyanColorMeta = CyanColor.getItemMeta();
        CyanColorMeta.setLore(Arrays.asList("", arrayListIncludes(purchased, "CYAN_PLUS") ? ChatColor.YELLOW + "Click To Select!" : ChatColor.RED + "Not Owned!"));
        CyanColorMeta.setDisplayName((arrayListIncludes(purchased, "CYAN_PLUS") ? ChatColor.DARK_AQUA : ChatColor.RED) + "Cyan");
        CyanColor.setItemMeta(CyanColorMeta);

        ItemStack PurpleColor = new ItemStack(Material.INK_SACK, 1, (short) 5);
        ItemMeta PurpleColorMeta = PurpleColor.getItemMeta();
        PurpleColorMeta.setLore(Arrays.asList("", arrayListIncludes(purchased, "PURPLE_PLUS") ? ChatColor.YELLOW + "Click To Select!" : ChatColor.RED + "Not Owned!"));
        PurpleColorMeta.setDisplayName((arrayListIncludes(purchased, "PURPLE_PLUS") ? ChatColor.DARK_PURPLE : ChatColor.RED) + "Purple");
        PurpleColor.setItemMeta(PurpleColorMeta);

        ItemStack DarkRedColor = new ItemStack(Material.INK_SACK, 1, (short) 1);
        ItemMeta DarkRedColorMeta = DarkRedColor.getItemMeta();
        DarkRedColorMeta.setLore(Arrays.asList("", arrayListIncludes(purchased, "DARK_RED_PLUS") ? ChatColor.YELLOW + "Click To Select!" : ChatColor.RED + "Not Owned!"));
        DarkRedColorMeta.setDisplayName((arrayListIncludes(purchased, "DARK_RED_PLUS") ? ChatColor.DARK_RED : ChatColor.RED) + "Dark Red");
        DarkRedColor.setItemMeta(DarkRedColorMeta);

        ItemStack GoldColor = new ItemStack(Material.INK_SACK, 1, (short) 14);
        ItemMeta GoldColorMeta = GoldColor.getItemMeta();
        GoldColorMeta.setLore(Arrays.asList("", arrayListIncludes(purchased, "GOLD_PLUS") ? ChatColor.YELLOW + "Click To Select!" : ChatColor.RED + "Not Owned!"));
        GoldColorMeta.setDisplayName((arrayListIncludes(purchased, "GOLD_PLUS") ? ChatColor.GOLD : ChatColor.RED) + "Gold");
        GoldColor.setItemMeta(GoldColorMeta);

        ItemStack WhiteColor = new ItemStack(Material.INK_SACK, 1, (short) 15);
        ItemMeta WhiteColorMeta = WhiteColor.getItemMeta();
        WhiteColorMeta.setLore(Arrays.asList("", arrayListIncludes(purchased, "WHITE_PLUS") ? ChatColor.YELLOW + "Click To Select!" : ChatColor.RED + "Not Owned!"));
        WhiteColorMeta.setDisplayName((arrayListIncludes(purchased, "WHITE_PLUS") ? ChatColor.WHITE : ChatColor.RED) + "White");
        WhiteColor.setItemMeta(WhiteColorMeta);

        ItemStack DarkGrayColor = new ItemStack(Material.INK_SACK, 1, (short) 8);
        ItemMeta DarkGrayColorMeta = DarkGrayColor.getItemMeta();
        DarkGrayColorMeta.setLore(Arrays.asList("", arrayListIncludes(purchased, "GRAY_PLUS") ? ChatColor.YELLOW + "Click To Select!" : ChatColor.RED + "Not Owned!"));
        DarkGrayColorMeta.setDisplayName((arrayListIncludes(purchased, "GRAY_PLUS") ? ChatColor.GRAY : ChatColor.RED) + "Gray");
        DarkGrayColor.setItemMeta(DarkGrayColorMeta);

        ItemStack DarkBlueColor = new ItemStack(Material.INK_SACK, 1, (short) 4);
        ItemMeta DarkBlueColorMeta = DarkBlueColor.getItemMeta();
        DarkBlueColorMeta.setLore(Arrays.asList("", arrayListIncludes(purchased, "DARK_BLUE_PLUS") ? ChatColor.YELLOW + "Click To Select!" : ChatColor.RED + "Not Owned!"));
        DarkBlueColorMeta.setDisplayName((arrayListIncludes(purchased, "DARK_BLUE_PLUS") ? ChatColor.DARK_BLUE : ChatColor.RED) + "Dark Blue");
        DarkBlueColor.setItemMeta(DarkBlueColorMeta);

        ItemStack BlackColor = new ItemStack(Material.INK_SACK, 1);
        ItemMeta BlackColorMeta = BlackColor.getItemMeta();
        BlackColorMeta.setLore(Arrays.asList("", arrayListIncludes(purchased, "BLACK_PLUS") ? ChatColor.YELLOW + "Click To Select!" : ChatColor.RED + "Not Owned!"));
        BlackColorMeta.setDisplayName((arrayListIncludes(purchased, "BLACK_PLUS") ? ChatColor.BLACK : ChatColor.RED) + "Black");
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
    }

}