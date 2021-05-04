package me.teakivy.lobby;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class NPC {
    private static List<EntityPlayer> NPC = new ArrayList<EntityPlayer>();
    private static List<EntityPlayer> PlayerNPC = new ArrayList<EntityPlayer>();
    private static Map<UUID, EntityPlayer> ProfileNPCs = new HashMap<>();

    public static String teamName = "team0";
    public static int teamCount = 0;

    public static void createNPC(Location location, String skin, String daName) {
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = ((CraftWorld) Bukkit.getWorld(location.getWorld().getName())).getHandle();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), daName);

        EntityPlayer npc = new EntityPlayer(server, world, gameProfile, new PlayerInteractManager(world));
        npc.setLocation(location.getX(), location.getY(), location.getZ(),
                location.getYaw(), location.getPitch());

        String[] name = getSkin(skin);
        gameProfile.getProperties().put("textures", new Property("textures", name[0], name[1]));
        DataWatcher watcher = npc.getDataWatcher();
        watcher.watch(10, (byte) 127);


        NPC.add(npc);
        addNPCPacket(npc);
    }

//    public static void loadNPC(Location location, GameProfile gameProfile) {
//        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
//        WorldServer world = ((CraftWorld) Bukkit.getWorld(location.getWorld().getName())).getHandle();
//
//        EntityPlayer npc = new EntityPlayer(server, world, gameProfile, new PlayerInteractManager(world));
//        npc.setLocation(location.getX(), location.getY(), location.getZ(),
//                location.getYaw(), location.getPitch());
//
//        DataWatcher watcher = npc.getDataWatcher();
//        watcher.watch(10, (byte) 127);
//
//
//        NPC.add(npc);
//        addNPCPacket(npc);
//    }

    public static String[] getSkin(String name) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            String uuid = new JsonParser().parse(reader).getAsJsonObject().get("id").getAsString();

            URL url2 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            InputStreamReader reader2 = new InputStreamReader(url2.openStream());
            JsonObject property = new JsonParser().parse(reader2).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            String texture = property.get("value").getAsString();
            String signature = property.get("signature").getAsString();

            return new String[] {texture, signature};
        } catch (Exception e) {
//            System.out.println("exception");
//            EntityPlayer p = ((CraftPlayer) player).getHandle();
//            GameProfile profile = p.getProfile();
//            Property property = profile.getProperties().get("textures").iterator().next();
//            String texture = property.getValue();
//            String signature = property.getSignature();
            System.out.println(">>> Failure to load NPC: " + name);

            String texture = "ewogICJ0aW1lc3RhbXAiIDogMTYxODAwMTQ4NDIwOCwKICAicHJvZmlsZUlkIiA6ICJmZGI1NTk5YzFiMTQ0NDBlODJkZmQ2OTcxOTcwM2QyMSIsCiAgInByb2ZpbGVOYW1lIiA6ICJUZWFrSXZ5IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2QxOWNhMjg3ZmI5ZDYxYjQyMTFhZWZkMTI4YTcwOGMyMzVkZjgwYzJhYzY5ZjY5MGMyYTk3YmUwOGZkMzYwNmIiCiAgICB9CiAgfQp9";
            String signature = "jiq0T8nvkb/wZg8vzIljf+z2NvkPybSinnX4HQTM6/j608CGZs9fWjAMrT8MBp/u3SFZB8FSMPlrTxBI1jWng66bZWWiuv/CpsZJ0S6BVXRYIHa6VxrJ0t/+Nut9HRMl+Q9yZKGupMbott7tgKGTPOJNuREotzIw8tVV6zdxqmT3QEXeebBAHft6s/JzPxvsHqoH8x2o5MHfKlCIiGzii2F2hv2YelxhcriBoRTz7Km2lUdpemE5Pb+CdDcSfFzlOY1rUIcut8mYn8BImHamy9Jv6uE4uV17hV9kTvCa4Eg3JDlqxGyf6bH4E33tekvTuT42u1sz2BCbHHaLPg7TK/csP9RjYnsytEMgAqKgf8/hUIqv8HM4QIcfdFGUeURxbKLlgQaQx1fEagtIYd3CXW2oqzg9vSZ9Juec8Rblf3Nr+L0yG7zLrQlDGFNn+XjYmDOtmP22fRlPbOc8yD6zaOSK40oNEpWzrPEYQHKWMk0npM+JLtipDzje8cfBkS8Z56gP7YT48W5CqOBrlx+BZKscf818XMiDVSfSNa9f18ctbwgY6Wfu4XhjKDjDan4o9Ou7GRdEev/4Rw84sm3vtVoa3MZpVOHtFuwWNiBDgdNe3cdxXad+Ygcog7BRVjYN2VxXyPQTmTINBqEmJLzjYkn5fJYGQXoXosY6llsZazY=";
            return new String[] {texture, signature};


        }
    }

    public static void addNPCPacket(EntityPlayer npc) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
            connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
            connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.yaw * 256 / 360)));

            teamName = "team" + teamCount + 1;
            teamCount++;

            ScoreboardTeam team = new ScoreboardTeam(((CraftScoreboard) Bukkit.getScoreboardManager().getMainScoreboard()).getHandle(), teamName);

//            ScoreboardTeam team = (ScoreboardTeam) player.getScoreboard().getTeam(player.getName());

            team.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.NEVER);

            ArrayList<String> playerToAdd = new ArrayList<>();

            playerToAdd.add(npc.getName()); //Add the fake player so this player will not have a nametag

            connection.sendPacket(new PacketPlayOutScoreboardTeam(team, 1));
            connection.sendPacket(new PacketPlayOutScoreboardTeam(team, 0));
            connection.sendPacket(new PacketPlayOutScoreboardTeam(team, playerToAdd, 3));
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
                @Override
                public void run() {
                    connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc));
                }
            }, 20);
        }
    }

    public static void addPlayerNPCPacket(EntityPlayer npc, Player player) {
            PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
            connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
            connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.yaw * 256 / 360)));

            teamName = "team" + teamCount + 1;
            teamCount++;

            ScoreboardTeam team = new ScoreboardTeam(((CraftScoreboard) Bukkit.getScoreboardManager().getMainScoreboard()).getHandle(), teamName);

//        ScoreboardTeam team = (ScoreboardTeam) player.getScoreboard().getTeam(player.getName());

            team.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.NEVER);

            ArrayList<String> playerToAdd = new ArrayList<>();

            playerToAdd.add(npc.getName()); //Add the fake player so this player will not have a nametag

            connection.sendPacket(new PacketPlayOutScoreboardTeam(team, 1));
            connection.sendPacket(new PacketPlayOutScoreboardTeam(team, 0));
            connection.sendPacket(new PacketPlayOutScoreboardTeam(team, playerToAdd, 3));

            ProfileNPCs.put(player.getUniqueId(), npc);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
            @Override
            public void run() {
                connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc));
            }
        }, 60);

    }



    public static void addJoinPacket(Player player) {
        for (EntityPlayer npc : NPC) {
            PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
            connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
            connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.yaw * 256 / 360)));

            teamName = "team" + teamCount + 1;
            teamCount++;

            ScoreboardTeam team = new ScoreboardTeam(((CraftScoreboard) Bukkit.getScoreboardManager().getMainScoreboard()).getHandle(), teamName);
//            ScoreboardTeam team = (ScoreboardTeam) player.getScoreboard().getTeam(player.getName());

            team.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.NEVER);

            ArrayList<String> playerToAdd = new ArrayList<>();

            playerToAdd.add(npc.getName()); //Add the fake player so this player will not have a nametag

            connection.sendPacket(new PacketPlayOutScoreboardTeam(team, 1));
            connection.sendPacket(new PacketPlayOutScoreboardTeam(team, 0));
            connection.sendPacket(new PacketPlayOutScoreboardTeam(team, playerToAdd, 3));
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
                @Override
                public void run() {
                    connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc));
                }
            }, 20);

        }
    }



    public static List<EntityPlayer> getNPCs() {
        return NPC;
    }

    public static List<EntityPlayer> getPlayerNPCs() {
        return PlayerNPC;
    }

    public static Map<UUID, EntityPlayer> getProfileNPCs() {
        return ProfileNPCs;
    }

    public static void loadNPC(Location location, GameProfile gameProfile) {
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = ((CraftWorld) Bukkit.getWorld(location.getWorld().getName())).getHandle();

        EntityPlayer npc = new EntityPlayer(server, world, gameProfile, new PlayerInteractManager(world));
        npc.setLocation(location.getX(), location.getY(), location.getZ(),
                location.getYaw(), location.getPitch());

        DataWatcher watcher = npc.getDataWatcher();
        watcher.watch(10, (byte) 127);


        NPC.add(npc);
        addNPCPacket(npc);
    }

    public static void loadPlayerNPC(Location location, GameProfile gameProfile, Player player) {
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = ((CraftWorld) Bukkit.getWorld(location.getWorld().getName())).getHandle();

        EntityPlayer npc = new EntityPlayer(server, world, gameProfile, new PlayerInteractManager(world));
        npc.setLocation(location.getX(), location.getY(), location.getZ(),
                location.getYaw(), location.getPitch());

        DataWatcher watcher = npc.getDataWatcher();
        watcher.watch(10, (byte) 127);

        PlayerNPC.add(npc);
        addPlayerNPCPacket(npc, player);
    }
}
