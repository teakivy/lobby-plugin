package me.teakivy.lobby.Bedwars;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mongodb.client.MongoCollection;
import me.rayzr522.jsonmessage.JSONMessage;
import me.teakivy.lobby.Bedwars.Events.Blocks;
import me.teakivy.lobby.Bedwars.util.BedwarsPlayer;
import me.teakivy.lobby.Bedwars.util.BedwarsInventory;
import me.teakivy.lobby.Main;
import me.teakivy.lobby.NPC;
import me.teakivy.lobby.utils.Scoreboards;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.mongodb.client.model.Updates.inc;

public class Bedwars {
    static MongoCollection< Document > col = me.teakivy.lobby.Main.getPlugin(Main.class).col;
    public static Main Main = me.teakivy.lobby.Main.getPlugin(Main.class);


    public static Map< Player, Boolean > BedwarsGame = new HashMap< >();

    public static Map < Player, Player > BedwarsRequest = new HashMap < > ();
    public static Map < Player, Integer > BedwarsRequestNo = new HashMap < > ();
    public static Map < Player, Integer > BedwarsKills = new HashMap < > ();
    public static Map < Player, Boolean> HasBed = new HashMap < > ();
    public static Map < Player, Player > BedwarsLastPlayerDamage = new HashMap < > ();

    public static Boolean BedwarsActive = false;
    public static Boolean BedwarsQueued = false;
    public static Boolean BedwarsWaiting = false;

    public static Player BedwarsPlayer1;
    public static Player BedwarsPlayer2;

    public static ArrayList<Entity> EntitySpawns = new ArrayList<>();



    public static Location RedGenerator = new Location(Bukkit.getWorld("world"), 20082.5, 96.5, 20020.5);
    public static Location BlueGenerator = new Location(Bukkit.getWorld("world"), 20134.5, 96.5, 20020.5);

    public static Location RedDiamondGenerator = new Location(Bukkit.getWorld("world"), 20056.5, 99.5, 20059.5);
    public static Location BlueDiamondGenerator = new Location(Bukkit.getWorld("world"), 20160.5, 99.5, 20059.5);

    static int BedwarsTimerID;
    static int[] BedwarsTimer = {20 * 60};
    static String[] BedwarsTimeFormatted = {"20:00"};

    static boolean GeneratorsActive = false;
    static int IronTimerID;
    public static JSONObject redGen;
    public static JSONObject blueGen;

    static int GoldTimerID;
    static int DiamondTimerID;
    public static JSONObject redDiamondGen;
    public static JSONObject blueDiamondGen;

    static int EmeraldTimerID;
    public static JSONObject emeraldGen1;
    public static JSONObject emeraldGen2;
    public static JSONObject emeraldGen3;
    public static JSONObject emeraldGen4;

    public static Location EmeraldGenSpawn1 = new Location(Bukkit.getWorld("world"), 20118.5, 97.5, 20097.5);
    public static Location EmeraldGenSpawn2 = new Location(Bukkit.getWorld("world"), 20098.5, 97.5, 20125.5);
    public static Location EmeraldGenSpawn3 = new Location(Bukkit.getWorld("world"), 20108.5, 119.5, 20118.5);
    public static Location EmeraldGenSpawn4 = new Location(Bukkit.getWorld("world"), 20108.5, 119.5, 20104.5);


    public static void BedwarsEvent(Player player1, Player player2) {

        BedwarsWaiting = true;

        Document filter = new Document("UUID", player1.getUniqueId().toString());
        Bson updates = inc("stats.bedwars.gamesPlayed", 1);
        col.findOneAndUpdate(filter, updates);

        Document Killerfilter = new Document("UUID", player2.getUniqueId().toString());
        Bson Killerupdates = inc("stats.bedwars.gamesPlayed", 1);
        col.findOneAndUpdate(Killerfilter, Killerupdates);


        Blocks.spawnBeds();
        respawnChests();

        Main.silentLeaveGameQueue(player1);
        Main.silentLeaveGameQueue(player2);


        Scoreboards.removeLobbyScoreboard(player1);
        Scoreboards.removeLobbyScoreboard(player2);

//        updateBridgeScoreboard(player1);
//        updateBridgeScoreboard(player2);

        BedwarsRequestNo.remove(player1);
        player1.showPlayer(player2);
        player2.showPlayer(player1);

        BedwarsQueued = true;
        BedwarsGame.put(player1, true);
        BedwarsGame.put(player2, true);

        HasBed.put(player1, true);
        HasBed.put(player2, true);

        Document filter1 = new Document("UUID", player1.getUniqueId().toString());
        Bson updates1 = inc("stats.bridge.gamesPlayed", 1);
        col.findOneAndUpdate(filter1, updates1);
        Document filter2 = new Document("UUID", player2.getUniqueId().toString());
        Bson updates2 = inc("stats.bridge.gamesPlayed", 1);
        col.findOneAndUpdate(filter2, updates2);

        BedwarsPlayer.setColor(player1, Color.BLUE);
        BedwarsPlayer.setColor(player2, Color.RED);

        player1.teleport(BedwarsPlayer.SpectatorSpawnLocation);
        player2.teleport(BedwarsPlayer.SpectatorSpawnLocation);

        player1.setHealth(20);
        player2.setHealth(20);

        BedwarsPlayer1 = player2;
        BedwarsPlayer2 = player1;


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
        scheduler5.scheduleSyncDelayedTask(Main, new Runnable() {
            @Override
            public void run() {
                JSONMessage.create("5").color(ChatColor.GREEN).title(1, 18, 1, player1);
                JSONMessage.create("5").color(ChatColor.GREEN).title(1, 18, 1, player2);
                player1.playSound(player1.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);
                player2.playSound(player2.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);
            }
        }, 20);
        BukkitScheduler scheduler4 = Bukkit.getServer().getScheduler();
        scheduler4.scheduleSyncDelayedTask(Main, new Runnable() {
            @Override
            public void run() {
                JSONMessage.create("4").color(ChatColor.YELLOW).title(1, 18, 1, player1);
                JSONMessage.create("4").color(ChatColor.YELLOW).title(1, 18, 1, player2);
                player1.playSound(player1.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);
                player2.playSound(player2.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);
            }
        }, 40);

        BukkitScheduler scheduler3 = Bukkit.getServer().getScheduler();
        scheduler3.scheduleSyncDelayedTask(Main, new Runnable() {
            @Override
            public void run() {
                JSONMessage.create("3").color(ChatColor.GOLD).title(1, 18, 1, player1);
                JSONMessage.create("3").color(ChatColor.GOLD).title(1, 18, 1, player2);
                player1.playSound(player1.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);
                player2.playSound(player2.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);
            }
        }, 60);
        BukkitScheduler scheduler2 = Bukkit.getServer().getScheduler();
        scheduler2.scheduleSyncDelayedTask(Main, new Runnable() {
            @Override
            public void run() {
                JSONMessage.create("2").color(ChatColor.RED).title(1, 18, 1, player1);
                JSONMessage.create("2").color(ChatColor.RED).title(1, 18, 1, player2);
                player1.playSound(player1.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);
                player2.playSound(player2.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);

            }
        }, 80);
        BukkitScheduler scheduler1 = Bukkit.getServer().getScheduler();
        scheduler1.scheduleSyncDelayedTask(Main, new Runnable() {
            @Override
            public void run() {
                JSONMessage.create("1").color(ChatColor.DARK_RED).title(1, 18, 1, player1);
                JSONMessage.create("1").color(ChatColor.DARK_RED).title(1, 18, 1, player2);
                player1.playSound(player1.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);
                player2.playSound(player2.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);
            }
        }, 100);
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(Main, new Runnable() {
            @Override
            public void run() {

                BedwarsInventory.PlayerArmor.put(player1, Material.LEATHER_BOOTS);
                BedwarsInventory.PlayerArmor.put(player2, Material.LEATHER_BOOTS);

                BedwarsInventory.PlayerShears.put(player1, false);
                BedwarsInventory.PlayerShears.put(player2, false);

                BedwarsInventory.PlayerPickaxe.put(player1, Material.AIR);
                BedwarsInventory.PlayerPickaxe.put(player2, Material.AIR);

                BedwarsInventory.PlayerAxe.put(player1, Material.AIR);
                BedwarsInventory.PlayerAxe.put(player2, Material.AIR);


                BedwarsInventory.setBedwarsInv(player2, Color.RED);
                BedwarsInventory.setBedwarsInv(player1, Color.BLUE);


//                createRedCage(player1);
//                createBlueCage(player2);

                BedwarsActive = true;
                BedwarsPlayer.respawnPlayer(player1);
                BedwarsPlayer.respawnPlayer(player2);

                player1.playSound(player1.getLocation(), Sound.NOTE_PLING, 1, 1);
                player2.playSound(player2.getLocation(), Sound.NOTE_PLING, 1, 1);

                BedwarsInventory.setBedwarsInv(BedwarsPlayer1, Color.RED);
                BedwarsInventory.setBedwarsInv(BedwarsPlayer2, Color.BLUE);
                startGenerators();
                createShopKeepers(BedwarsPlayer1, BedwarsPlayer2, player1);
                createShopKeepers(BedwarsPlayer1, BedwarsPlayer2, player2);


            }
        }, 120);

        BedwarsTimer[0] = 20*60;

        BukkitScheduler Bridgescheduler = Bukkit.getServer().getScheduler();
        BedwarsTimerID = Bridgescheduler.runTaskTimer(Main, new Runnable()
        {

            @Override
            public void run()
            {
                if (BedwarsActive) {
                    if (BedwarsTimer[0] == 0) {
                        BedwarsEventEnd(BedwarsPlayer1, BedwarsPlayer2, "draw");

                        Bukkit.getScheduler().cancelTask(BedwarsTimerID);
                        return;
                    }

                    int minutes = BedwarsTimer[0] / 60;
                    int seconds = BedwarsTimer[0] % 60;
                    String secondsFormated = "00";
                    if (seconds < 10) {
                        secondsFormated = "0" + seconds;
                    } else {
                        secondsFormated = "" + seconds;
                    }

                    BedwarsTimeFormatted[0] = minutes + ":" + secondsFormated;

//                    updateBridgeScoreboard(player1);
//                    updateBridgeScoreboard(player2);

                    BedwarsTimer[0]--;
                } else {
                    if (!BedwarsQueued && !BedwarsWaiting) {
                        BedwarsTimer[0] = 0;
                    }
                }
            }
        }, 0L, 20L).getTaskId();
    }

    public static boolean isInGame(Player player) {
        return BedwarsGame.get(player);
    }
    public static boolean getGame(Player player) {
        return BedwarsGame.get(player);
    }

    public static void putGame(Player player, Boolean bool) {
        BedwarsGame.put(player, bool);
    }

    public static void createShopKeepers(Player red, Player blue, Player player) {
        GameProfile RedShopKeeper = new GameProfile(UUID.randomUUID(), "ShopKeeper");
        String[] RedShopKeeperSkin = NPC.getSkin(red.getName());
        RedShopKeeper.getProperties().put("textures", new Property("textures", RedShopKeeperSkin[0], RedShopKeeperSkin[1]));
        NPC.loadPlayerNPC(new Location(Bukkit.getWorld("world"), 20088.5, 96, 20023.5, 90, 0), RedShopKeeper, player);

        GameProfile BlueShopKeeper = new GameProfile(UUID.randomUUID(), "ShopKeeper");
        String[] BlueShopKeeperSkin = NPC.getSkin(blue.getName());
        BlueShopKeeper.getProperties().put("textures", new Property("textures", BlueShopKeeperSkin[0], BlueShopKeeperSkin[1]));
        NPC.loadPlayerNPC(new Location(Bukkit.getWorld("world"), 20140.5, 96, 20023.5, 90, 0), BlueShopKeeper, player);

        GameProfile RedUpgradeShop = new GameProfile(UUID.randomUUID(), "UpgradeShop");
        String[] RedUpgradeShopSkin = NPC.getSkin(red.getName());
        RedUpgradeShop.getProperties().put("textures", new Property("textures", RedUpgradeShopSkin[0], RedUpgradeShopSkin[1]));
        NPC.loadPlayerNPC(new Location(Bukkit.getWorld("world"), 20077.5, 96, 20023.5, -90, 0), RedUpgradeShop, player);

        GameProfile BlueUpgradeShop = new GameProfile(UUID.randomUUID(), "UpgradeShop");
        String[] BlueUpgradeShopSkin = NPC.getSkin(blue.getName());
        BlueUpgradeShop.getProperties().put("textures", new Property("textures", BlueUpgradeShopSkin[0], BlueUpgradeShopSkin[1]));
        NPC.loadPlayerNPC(new Location(Bukkit.getWorld("world"), 20129.5, 96, 20023.5, -90, 0), BlueUpgradeShop, player);
    }

    public static void startGenerators() {
        if (!GeneratorsActive) {
            GeneratorsActive = true;
            redGen = new JSONObject();
            redGen.put("iron", 0);
            redGen.put("gold", 0);

            blueGen = new JSONObject();
            blueGen.put("iron", 0);
            blueGen.put("gold", 0);

            redDiamondGen = new JSONObject();
            redDiamondGen.put("diamond", 0);

            blueDiamondGen = new JSONObject();
            blueDiamondGen.put("diamond", 0);

            emeraldGen1 = new JSONObject();
            emeraldGen1.put("emerald", 0);
            emeraldGen2 = new JSONObject();
            emeraldGen2.put("emerald", 0);
            emeraldGen3 = new JSONObject();
            emeraldGen3.put("emerald", 0);
            emeraldGen4 = new JSONObject();
            emeraldGen4.put("emerald", 0);

            BukkitScheduler scheduler = Bukkit.getScheduler();
            IronTimerID = scheduler.runTaskTimer(Main, () -> {
                if (BedwarsActive) {
                    if (!GeneratorsActive) {
                        Bukkit.getScheduler().cancelTask(IronTimerID);
                        return;
                    }

                    if ((int)redGen.get("iron") < 48) {
                        Item i = Bukkit.getWorld("world").dropItem(RedGenerator, new ItemStack(Material.IRON_INGOT));
                        i.setVelocity(new Vector(0, 0, 0));

                        EntitySpawns.add(i);
                        redGen.put("iron", ((int) redGen.get("iron")) + 1);
                    }
                    if ((int)blueGen.get("iron") < 48) {
                        Item i = Bukkit.getWorld("world").dropItem(BlueGenerator, new ItemStack(Material.IRON_INGOT));
                        i.setVelocity(new Vector(0, 0, 0));
                        EntitySpawns.add(i);
                        blueGen.put("iron", ((int) blueGen.get("iron")) + 1);
                    }
                }
            }, 5L, 18).getTaskId();

            GoldTimerID = scheduler.runTaskTimer(Main, () -> {
                if (BedwarsActive) {
                    if (!GeneratorsActive) {
                        Bukkit.getScheduler().cancelTask(GoldTimerID);
                        return;
                    }

                    if ((int)redGen.get("gold") < 8) {
                        Item i = Bukkit.getWorld("world").dropItem(RedGenerator, new ItemStack(Material.GOLD_INGOT));
                        i.setVelocity(new Vector(0, 0, 0));
                        EntitySpawns.add(i);
                        redGen.put("gold", ((int) redGen.get("gold")) + 1);
                    }
                    if ((int)blueGen.get("gold") < 8) {
                        Item i = Bukkit.getWorld("world").dropItem(BlueGenerator, new ItemStack(Material.GOLD_INGOT));
                        i.setVelocity(new Vector(0, 0, 0));
                        EntitySpawns.add(i);
                        blueGen.put("gold", ((int) blueGen.get("gold")) + 1);
                    }
                }
            }, 5L, 3 * 20).getTaskId();

            DiamondTimerID = scheduler.runTaskTimer(Main, () -> {
                if (BedwarsActive) {
                    if (!GeneratorsActive) {
                        Bukkit.getScheduler().cancelTask(DiamondTimerID);
                        return;
                    }

                    if ((int)redDiamondGen.get("diamond") < 4) {
                        Item i = Bukkit.getWorld("world").dropItem(RedDiamondGenerator, new ItemStack(Material.DIAMOND));
                        i.setVelocity(new Vector(0, 0, 0));
                        EntitySpawns.add(i);
                        redDiamondGen.put("diamond", ((int) redDiamondGen.get("diamond")) + 1);
                    }
                    if ((int)blueDiamondGen.get("diamond") < 4) {
                        Item i = Bukkit.getWorld("world").dropItem(BlueDiamondGenerator, new ItemStack(Material.DIAMOND));
                        i.setVelocity(new Vector(0, 0, 0));
                        EntitySpawns.add(i);
                        blueDiamondGen.put("diamond", ((int) blueDiamondGen.get("diamond")) + 1);
                    }
                }
            }, 5L, 20 * 20).getTaskId();

            EmeraldTimerID = scheduler.runTaskTimer(Main, () -> {
                if (BedwarsActive) {
                    if (!GeneratorsActive) {
                        Bukkit.getScheduler().cancelTask(EmeraldTimerID);
                        return;
                    }

                    if ((int)emeraldGen1.get("emerald") < 2) {
                        Item i = Bukkit.getWorld("world").dropItem(EmeraldGenSpawn1, new ItemStack(Material.EMERALD));
                        i.setVelocity(new Vector(0, 0, 0));
                        EntitySpawns.add(i);
                        emeraldGen1.put("emerald", ((int) emeraldGen1.get("emerald")) + 1);
                    }
                    if ((int)emeraldGen2.get("emerald") < 2) {
                        Item i = Bukkit.getWorld("world").dropItem(EmeraldGenSpawn2, new ItemStack(Material.EMERALD));
                        i.setVelocity(new Vector(0, 0, 0));
                        EntitySpawns.add(i);
                        emeraldGen2.put("emerald", ((int) emeraldGen2.get("emerald")) + 1);
                    }
                    if ((int)emeraldGen3.get("emerald") < 2) {
                        Item i = Bukkit.getWorld("world").dropItem(EmeraldGenSpawn3, new ItemStack(Material.EMERALD));
                        i.setVelocity(new Vector(0, 0, 0));
                        EntitySpawns.add(i);
                        emeraldGen3.put("emerald", ((int) emeraldGen3.get("emerald")) + 1);
                    }
                    if ((int)emeraldGen4.get("emerald") < 2) {
                        Item i = Bukkit.getWorld("world").dropItem(EmeraldGenSpawn4, new ItemStack(Material.EMERALD));
                        i.setVelocity(new Vector(0, 0, 0));
                        EntitySpawns.add(i);
                        emeraldGen4.put("emerald", ((int) emeraldGen4.get("emerald")) + 1);
                    }
                }
            }, 5L, 60 * 20).getTaskId();
        }
    }

    public static void respawnChests() {
        Location redChest = new Location(Bukkit.getWorld("world"), 20086, 96, 20025);
        redChest.getBlock().setType(Material.AIR);
        redChest.getBlock().setType(Material.CHEST);
        redChest.getBlock().setData((byte) 2);

        Location blueChest = new Location(Bukkit.getWorld("world"), 20138, 96, 20025);
        blueChest.getBlock().setType(Material.AIR);
        blueChest.getBlock().setType(Material.CHEST);
        blueChest.getBlock().setData((byte) 2);
    }

    public static void stopGenerators() {
        GeneratorsActive = false;
    }

    public boolean getGeneratorsActive() {
        return GeneratorsActive;
    }

    public static void BedwarsEventEnd(Player winner, Player loser, String type) {
        Bukkit.getWorld("world").getEntities().stream().filter(Item.class::isInstance).forEach(Entity::remove);
        BedwarsInventory.PlayerArmor.put(winner, Material.LEATHER_BOOTS);
        BedwarsInventory.PlayerArmor.put(loser, Material.LEATHER_BOOTS);
        if (type.equals("normal")) {
            Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "----------------");
            Bukkit.getServer().broadcastMessage(ChatColor.GREEN + winner.getName() + ChatColor.GOLD + " just won a Bedwars Duel against " + ChatColor.RED + loser.getName() + ChatColor.GOLD + "!");
            Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "----------------");
        } else {
            Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "----------------");
            Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + winner.getName() + ChatColor.GOLD + " just tied a Bedwars Duel against " + ChatColor.YELLOW + loser.getName() + ChatColor.GOLD + "!");
            Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "----------------");
        }

        stopGenerators();
        BedwarsLastPlayerDamage.put(winner, winner);
        BedwarsLastPlayerDamage.put(loser, loser);

        Document Wfilter = new Document("UUID", winner.getUniqueId().toString());
        Document Lfilter = new Document("UUID", loser.getUniqueId().toString());

        if (type == "normal") {
            Document filter = new Document("UUID", winner.getUniqueId().toString());
            Bson updates = inc("stats.bedwars.wins", 1);
            col.findOneAndUpdate(filter, updates);

            Document Killerfilter = new Document("UUID", loser.getUniqueId().toString());
            Bson Killerupdates = inc("stats.bedwars.losses", 1);
            col.findOneAndUpdate(Killerfilter, Killerupdates);
//            Bson Wupdates = inc("stats.sumo.wins", 1);
//            col.findOneAndUpdate(Wfilter, Wupdates);
//            Bson Lupdates = inc("stats.sumo.losses", 1);
//            col.findOneAndUpdate(Lfilter, Lupdates);
            Main.addCoins(winner, 100);
            Main.addCoins(loser, 20);

            BedwarsPlayer.setSpectator(loser);

            loser.playSound(loser.getLocation(), Sound.ANVIL_BREAK, 1, 1);
            winner.playSound(winner.getLocation(), Sound.LEVEL_UP, 1, 1);
            winner.playSound(winner.getLocation(), Sound.NOTE_PLING, 1, 1);


            JSONMessage.create(ChatColor.BOLD + "VICTORY!").color(ChatColor.GOLD).title(1, 60, 1, winner);
            JSONMessage.create(ChatColor.BOLD + "GAME OVER!").color(ChatColor.RED).title(1, 60, 1, loser);
        } else {
            Main.addCoins(winner, 10);
            Main.addCoins(loser, 10);
            BedwarsPlayer.setSpectator(winner);
            BedwarsPlayer.setSpectator(loser);

            loser.playSound(loser.getLocation(), Sound.ANVIL_USE, 1, 1);
            winner.playSound(winner.getLocation(), Sound.ANVIL_USE, 1, 1);


            JSONMessage.create(ChatColor.BOLD + "DRAW!").color(ChatColor.YELLOW).title(1, 60, 1, winner);
            JSONMessage.create(ChatColor.BOLD + "DRAW!").color(ChatColor.YELLOW).title(1, 60, 1, loser);
        }
        BedwarsActive = false;
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(Main, () -> {

            BedwarsInventory.PlayerAxe.remove(winner);
            BedwarsInventory.PlayerAxe.remove(loser);
            BedwarsInventory.PlayerShears.remove(winner);
            BedwarsInventory.PlayerShears.remove(loser);
            BedwarsInventory.PlayerPickaxe.remove(winner);
            BedwarsInventory.PlayerPickaxe.remove(loser);
            BedwarsInventory.PlayerArmor.remove(winner);
            BedwarsInventory.PlayerArmor.remove(loser);

            Main.backToHub(loser);
            Main.backToHub(winner);
            BedwarsQueued = false;

            winner.getEnderChest().clear();
            loser.getEnderChest().clear();

//                Scoreboards.removeSumoScoreboard(winner);
//                Scoreboards.removeSumoScoreboard(loser);

            Scoreboards.updateLobbyScoreboard(winner);
            Scoreboards.updateLobbyScoreboard(loser);

            HasBed.remove(winner);
            HasBed.remove(loser);
            BedwarsPlayer.PlayerColor.remove(winner);
            BedwarsPlayer.PlayerColor.remove(loser);

            for (Location loc : Blocks.BlocksPlaced) {
                loc.getBlock().setType(Material.AIR);
            }

            for (Entity entity : EntitySpawns) {
                if (!entity.isDead()) {
                    entity.remove();
                }
            }

            BedwarsKills.put(winner, 0);
            BedwarsKills.put(loser, 0);

            winner.setAllowFlight(Main.getDoubleJumpEnabled(winner) || Main.getFlightEnabled(winner));
            if (Main.getSpeedEnabled(winner)) {
                winner.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 60 * 24, 0, true, true));
            }
            loser.setAllowFlight(Main.getDoubleJumpEnabled(loser) || Main.getFlightEnabled(loser));
            if (Main.getSpeedEnabled(loser)) {
                loser.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 60 * 24, 0, true, true));
            }

            if (!Main.getPlayersVisible(winner)) {
                winner.hidePlayer(loser);
            }
            if (!Main.getPlayersVisible(loser)) {
                loser.hidePlayer(winner);
            }

        }, 70);
        BedwarsPlayer1 = null;
        BedwarsPlayer2 = null;
        BedwarsGame.put(winner, false);
        BedwarsGame.put(loser, false);


    }
}
