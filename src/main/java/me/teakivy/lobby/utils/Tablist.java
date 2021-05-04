package me.teakivy.lobby.utils;

import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class Tablist {

    public static void updateTablist() {
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        Object header = new ChatComponentText(ChatColor.YELLOW + "          You Are Playing On " + ChatColor.GOLD + ChatColor.BOLD.toString() + "PLAY.TEAKMC.TK          \n");
        Object footer = new ChatComponentText(ChatColor.YELLOW + "\nNew Mode" + ChatColor.GRAY + ": " + ChatColor.BLUE + ChatColor.BOLD.toString() + "The " + ChatColor.RED + ChatColor.BOLD.toString() + "Bridge");
        try {
            Field a = packet.getClass().getDeclaredField("a");
            a.setAccessible(true);
            Field b = packet.getClass().getDeclaredField("b");
            b.setAccessible(true);

            a.set(packet, header);
            b.set(packet, footer);

            if (Bukkit.getOnlinePlayers().size() == 0) return;
            for (Player p : Bukkit.getOnlinePlayers()) {
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
