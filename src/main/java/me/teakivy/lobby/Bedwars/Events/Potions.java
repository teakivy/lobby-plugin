package me.teakivy.lobby.Bedwars.Events;

import me.teakivy.lobby.Bedwars.Bedwars;
import me.teakivy.lobby.Bedwars.util.BedwarsInventory;
import me.teakivy.lobby.Main;
import me.teakivy.lobby.utils.TeamNames;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.material.Bed;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class Potions implements Listener {

    @EventHandler
    public void onPotion(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (!Bedwars.BedwarsGame.get(player)) return;
        if (!item.getType().equals(Material.POTION)) {
            System.out.println("Type");
            return;
        }

        if (!item.hasItemMeta()) {
            System.out.println("No Meta");
            return;
        }
        ;
        if (!(item.getItemMeta() instanceof PotionMeta)) {
            System.out.println("Meta");
            return;
        }
        if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Invisibility Potion (30 Seconds)")) {
            PacketPlayOutEntityEquipment helmetPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 1, null);
            PacketPlayOutEntityEquipment chestPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 2, null);
            PacketPlayOutEntityEquipment legPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 3, null);
            PacketPlayOutEntityEquipment bootsPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 4, null);
            for (Entity ent : player.getNearbyEntities(300, 300, 300)) {
                if (!(ent instanceof Player) || ent == player) continue;
                Player reciever = (Player) ent;
                if (!Bedwars.BedwarsGame.get(reciever)) continue;
                ((CraftPlayer) reciever).getHandle().playerConnection.sendPacket(helmetPacket);
                ((CraftPlayer) reciever).getHandle().playerConnection.sendPacket(chestPacket);
                ((CraftPlayer) reciever).getHandle().playerConnection.sendPacket(legPacket);
                ((CraftPlayer) reciever).getHandle().playerConnection.sendPacket(bootsPacket);

                PlayerConnection connection = ((CraftPlayer) reciever).getHandle().playerConnection;

                invisTeamName = "team" + invisTeamCount + 1;
                invisTeamCount++;

                ScoreboardTeam team = new ScoreboardTeam(((CraftScoreboard) Bukkit.getScoreboardManager().getMainScoreboard()).getHandle(), invisTeamName);
//            ScoreboardTeam team = (ScoreboardTeam) player.getScoreboard().getTeam(player.getName());

                team.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.NEVER);

                ArrayList<String> playerToAdd = new ArrayList<>();

                playerToAdd.add(player.getName()); //Add the fake player so this player will not have a nametag

                connection.sendPacket(new PacketPlayOutScoreboardTeam(team, 1));
                connection.sendPacket(new PacketPlayOutScoreboardTeam(team, 0));
                connection.sendPacket(new PacketPlayOutScoreboardTeam(team, playerToAdd, 3));
            }
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
            if (player.getInventory().containsAtLeast(new ItemStack(Material.GLASS_BOTTLE), 1)) {
                player.getInventory().removeItem(new ItemStack(Material.GLASS_BOTTLE, 1));
            }
        }, 1);
    }

    String invisTeamName;
    int invisTeamCount = 0;

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.hasPotionEffect(PotionEffectType.INVISIBILITY) && Bedwars.BedwarsGame.get(player)) {
            PacketPlayOutEntityEquipment helmetPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 1, null);
            PacketPlayOutEntityEquipment chestPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 2, null);
            PacketPlayOutEntityEquipment legPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 3, null);
            PacketPlayOutEntityEquipment bootsPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 4, null);
            for (Entity ent : player.getNearbyEntities(300, 300, 300)) {
                if (!(ent instanceof Player) || ent == player) continue;
                Player reciever = (Player) ent;
                if (!Bedwars.BedwarsGame.get(reciever)) continue;
                ((CraftPlayer) reciever).getHandle().playerConnection.sendPacket(helmetPacket);
                ((CraftPlayer) reciever).getHandle().playerConnection.sendPacket(chestPacket);
                ((CraftPlayer) reciever).getHandle().playerConnection.sendPacket(legPacket);
                ((CraftPlayer) reciever).getHandle().playerConnection.sendPacket(bootsPacket);

                PlayerConnection connection = ((CraftPlayer) reciever).getHandle().playerConnection;

                invisTeamName = "team" + invisTeamCount + 1;
                invisTeamCount++;

                ScoreboardTeam team = new ScoreboardTeam(((CraftScoreboard) Bukkit.getScoreboardManager().getMainScoreboard()).getHandle(), invisTeamName);
//            ScoreboardTeam team = (ScoreboardTeam) player.getScoreboard().getTeam(player.getName());

                team.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.NEVER);

                ArrayList<String> playerToAdd = new ArrayList<>();

                playerToAdd.add(player.getName()); //Add the fake player so this player will not have a nametag

                connection.sendPacket(new PacketPlayOutScoreboardTeam(team, 1));
                connection.sendPacket(new PacketPlayOutScoreboardTeam(team, 0));
                connection.sendPacket(new PacketPlayOutScoreboardTeam(team, playerToAdd, 3));
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) && !(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;
        if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            PlayerInventory inv = player.getInventory();
            PacketPlayOutEntityEquipment helmetPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 4, CraftItemStack.asNMSCopy(player.getInventory().getHelmet()));
            PacketPlayOutEntityEquipment chestPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 3, CraftItemStack.asNMSCopy(player.getInventory().getChestplate()));
            PacketPlayOutEntityEquipment legPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 2, CraftItemStack.asNMSCopy(player.getInventory().getLeggings()));
            PacketPlayOutEntityEquipment bootsPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 1, CraftItemStack.asNMSCopy(player.getInventory().getBoots()));
            for (Entity ent : player.getNearbyEntities(300, 300, 300)) {
                if (!(ent instanceof Player) || ent == player) continue;
                Player reciever = (Player) ent;
                if (!Bedwars.BedwarsGame.get(reciever)) continue;
                ((CraftPlayer) reciever).getHandle().playerConnection.sendPacket(helmetPacket);
                ((CraftPlayer) reciever).getHandle().playerConnection.sendPacket(chestPacket);
                ((CraftPlayer) reciever).getHandle().playerConnection.sendPacket(legPacket);
                ((CraftPlayer) reciever).getHandle().playerConnection.sendPacket(bootsPacket);

                PlayerConnection connection = ((CraftPlayer) reciever).getHandle().playerConnection;

                invisTeamName = "team" + invisTeamCount + 1;
                invisTeamCount++;

                ScoreboardTeam team = new ScoreboardTeam(((CraftScoreboard) Bukkit.getScoreboardManager().getMainScoreboard()).getHandle(), invisTeamName);
                team.setPrefix(TeamNames.getRankPrefix(player));
//            ScoreboardTeam team = (ScoreboardTeam) player.getScoreboard().getTeam(player.getName());

                team.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS);

                ArrayList<String> playerToAdd = new ArrayList<>();

                playerToAdd.add(player.getName()); //Add the fake player so this player will not have a nametag

                connection.sendPacket(new PacketPlayOutScoreboardTeam(team, 1));
                connection.sendPacket(new PacketPlayOutScoreboardTeam(team, 0));
                connection.sendPacket(new PacketPlayOutScoreboardTeam(team, playerToAdd, 3));
            }
//            inv.setHelmet(BedwarsInventory.PlayerHelmet.get(player));
//            inv.setChestplate(BedwarsInventory.PlayerChestplate.get(player));
//            inv.setLeggings(BedwarsInventory.PlayerLeggings.get(player));
//            inv.setBoots(BedwarsInventory.PlayerBoots.get(player));
        }

    }
}
