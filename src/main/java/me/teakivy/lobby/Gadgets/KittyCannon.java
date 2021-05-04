package me.teakivy.lobby.Gadgets;

import me.teakivy.lobby.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.Random;

public class KittyCannon implements Listener {

    private static final Random random = new Random();
    public static ArrayList<Player> KittyCooldown = new ArrayList<>();

    @EventHandler
    public void kittyGun(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getType().equals(Material.BLAZE_ROD)) {
            String name = player.getItemInHand().getItemMeta().getDisplayName();
            if (name.equalsIgnoreCase(ChatColor.GREEN + "Kitty Cannon" + ChatColor.GRAY + " (Right Click)")) {
                if (!KittyCooldown.contains(player)) {
                    kittyCannon(player);
                    KittyCooldown.add(player);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
                        @Override
                        public void run() {
                            KittyCooldown.remove(player);
                        }
                    }, 20);
                } else {
                    player.sendMessage(ChatColor.RED + "Kitty Cannon is on Cooldown! Please wait " + ChatColor.WHITE + "1 Second" + ChatColor.RED + " between uses!");
                }
            }
        }
    }

    public static void kittyCannon(Player player) {
        final EntityType cat = EntityType.OCELOT;
//        final Ocelot ocelot = (Ocelot)cat.spawn(user.getWorld(), server, user.getBase().getEyeLocation());
        Ocelot ocelot = (Ocelot) player.getWorld().spawnEntity(player.getEyeLocation(), cat);
        if (ocelot == null)
        {
            return;
        }
        final int i = random.nextInt(Ocelot.Type.values().length);
        ocelot.setCatType(Ocelot.Type.values()[i]);
        ocelot.setTamed(true);
        ocelot.setBaby();
        ocelot.setVelocity(player.getEyeLocation().getDirection().multiply(2));

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
            final Location loc = ocelot.getLocation();
            ocelot.remove();
            loc.getWorld().createExplosion(loc, 0F);
        }, 20);
    }
}
