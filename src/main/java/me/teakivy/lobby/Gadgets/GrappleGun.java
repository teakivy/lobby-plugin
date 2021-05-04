package me.teakivy.lobby.Gadgets;

import me.teakivy.lobby.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class GrappleGun implements Listener {

    public static ArrayList<Player> GrappleCooldown = new ArrayList<>();

    @EventHandler
    public void onGrapple(PlayerFishEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();
        ItemMeta meta = item.getItemMeta();
        String name = meta.getDisplayName();
        if (name.equalsIgnoreCase(ChatColor.GREEN + "Grappling Gun" + ChatColor.GRAY + " (Right Click)")) {
            if (event.getState().equals(PlayerFishEvent.State.FAILED_ATTEMPT) || event.getState().equals(PlayerFishEvent.State.IN_GROUND)) {
                if (!GrappleCooldown.contains(player)) {
                    Location playerLocation = player.getLocation();
                    Location hookLocation = event.getHook().getLocation();
                    Location change = hookLocation.subtract(playerLocation);
                    player.setVelocity(change.toVector().multiply(0.8));
                    player.playSound(player.getLocation(), Sound.PISTON_RETRACT, .5F, (float) 0.4);

                    GrappleCooldown.add(player);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
                        @Override
                        public void run() {
                            GrappleCooldown.remove(player);
                        }
                    }, 2 * 20);
                } else {
                    player.sendMessage(ChatColor.RED + "Grappling Gun is on Cooldown! Please wait " + ChatColor.WHITE + "2 Seconds" + ChatColor.RED + " between uses!");
                }
            }
        }
    }
}