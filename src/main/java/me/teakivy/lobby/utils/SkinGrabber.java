package me.teakivy.lobby.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.teakivy.lobby.NPC;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;

public class SkinGrabber {

    public static ArrayList<String> Nicks = new ArrayList<>();
    public static Map<UUID, String> OriginalNames = new HashMap<>();
    public static Map<UUID, String> OriginalSkin = new HashMap<>();

    public static void changeSkinAndName(Player player, String skinName) throws InvocationTargetException {
        Nicks.remove(player.getName());
        if (!OriginalNames.containsKey(player.getUniqueId())) {
            OriginalNames.put(player.getUniqueId(), player.getName());
        }
        if (skinName.equals("reset")) {
            skinName = OriginalNames.get(player.getUniqueId());
            OriginalNames.remove(player.getUniqueId());
        }
        GameProfile gameProfile = ((CraftPlayer)player).getHandle().getProfile();
        PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;

        EntityPlayer entity = NPC.getProfileNPCs().get(player.getUniqueId());
        PacketPlayOutEntityDestroy destroyPacket = new PacketPlayOutEntityDestroy(entity.getId());
        connection.sendPacket(destroyPacket);

        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer)player).getHandle()));

        String[] skin = getSkin(player, skinName);
        gameProfile.getProperties().clear();
        gameProfile.getProperties().put("textures", new Property("textures", skin[0], skin[1]));

        try {
            Field field = gameProfile.getClass().getDeclaredField("name");
            field.setAccessible(true);
            field.set(gameProfile, skinName);
            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        if (skinName != null) player.setDisplayName(skinName);

        Bukkit.getOnlinePlayers().forEach(all -> {
            all.hidePlayer(player);
            all.showPlayer(player);
        });


        Nicks.add(player.getName());



        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer)player).getHandle()));
//        connection.sendPacket(new PacketPlayOutEntityDestroy(PacketPlayOutEntityDestroy., ((CraftPlayer)player).getHandle())));
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.hidePlayer(player);
            p.showPlayer(player);
        }

        reloadSkinForSelf(player);
        CraftPlayer craftPlayer = (CraftPlayer) player;
        craftPlayer.setDisplayName(skinName);
//        craftPlayer.
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME));

    }

    public static void changeSkin(Player player, String skinName) throws InvocationTargetException {
        if (!OriginalSkin.containsKey(player.getUniqueId())) {
            OriginalSkin.put(player.getUniqueId(), player.getName());
        }
        if (skinName.equalsIgnoreCase("reset")) {
            skinName = OriginalSkin.get(player.getUniqueId());
            OriginalSkin.remove(player.getUniqueId());
        }

        GameProfile gameProfile = ((CraftPlayer)player).getHandle().getProfile();
        PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;

        EntityPlayer entity = NPC.getProfileNPCs().get(player.getUniqueId());
        PacketPlayOutEntityDestroy destroyPacket = new PacketPlayOutEntityDestroy(entity.getId());
        connection.sendPacket(destroyPacket);

        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer)player).getHandle()));

        String[] skin = getSkin(player, skinName);
        gameProfile.getProperties().clear();
        gameProfile.getProperties().put("textures", new Property("textures", skin[0], skin[1]));


        Bukkit.getOnlinePlayers().forEach(all -> {
            all.hidePlayer(player);
            all.showPlayer(player);
        });


        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer)player).getHandle()));
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.hidePlayer(player);
            p.showPlayer(player);
        }

        reloadSkinForSelf(player);
    }

    public static ArrayList<String> getNicks() {
        return Nicks;
    }



    public static void reloadSkinForSelf(Player player) {
        final EntityPlayer ep = ((CraftPlayer) player).getHandle();
        final PacketPlayOutPlayerInfo removeInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ep);
        final PacketPlayOutEntityDestroy removeEntity = new PacketPlayOutEntityDestroy(player.getEntityId());
        final PacketPlayOutNamedEntitySpawn addNamed = new PacketPlayOutNamedEntitySpawn(ep);
        final PacketPlayOutPlayerInfo addInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ep);
        final PacketPlayOutRespawn respawn = new PacketPlayOutRespawn(ep.dimension, ep.getWorld().getDifficulty(), ep.getWorld().getWorldData().getType(), ep.playerInteractManager.getGameMode());
        ep.playerConnection.sendPacket(removeInfo);
        ep.playerConnection.sendPacket(removeEntity);
        ep.playerConnection.sendPacket(addNamed);
        ep.playerConnection.sendPacket(addInfo);
        ep.playerConnection.sendPacket(respawn);
        player.updateInventory();
    }

    private static String[] getSkin(Player player, String name) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            String uuid = new JsonParser().parse(reader).getAsJsonObject().get("id").getAsString();

            URL url2 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            InputStreamReader reader2 = new InputStreamReader(url2.openStream());
            JsonObject property = new JsonParser().parse(reader2).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            String texture = property.get("value").getAsString();
            String signature = property.get("signature").getAsString();

            return new String[]{texture, signature};
        } catch (Exception e) {
            EntityPlayer p = ((CraftPlayer) player).getHandle();
            GameProfile profile = p.getProfile();
            Property property = profile.getProperties().get("textures").iterator().next();
            String texture = property.getValue();
            String signature = property.getSignature();

            player.sendMessage(ChatColor.RED + ">>> Failure to load Skin: " + name);

            return new String[]{texture, signature};
        }
    }
}
