package me.teakivy.lobby.Gadgets;

import me.teakivy.lobby.Main;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MagicToyStick implements Listener {

    public static Map< Player, Boolean > ToyStick = new HashMap< >();
    public static ArrayList<Player> ToyStickCoolDown = new ArrayList<>();

    @EventHandler
    public void toyStick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!ToyStick.containsKey(player)) {
            ToyStick.put(player, false);
        }
            if (!ToyStick.get(player)) {
                if (player.getItemInHand().getType() == Material.STICK) {
                    ItemStack item = player.getItemInHand();
                    ItemMeta meta = item.getItemMeta();
                    String name = meta.getDisplayName();
                    if (name.equalsIgnoreCase(ChatColor.GREEN + "Magic Toy Stick" + ChatColor.GRAY + " (Right Click)")) {
                        if (!ToyStickCoolDown.contains(player)) {
//            Location playerLocation = player.getLocation();
//            Location hookLocation = player.getEyeLocation();
//            Location change = hookLocation.subtract(playerLocation);
//            player.setVelocity(change.toVector().multiply(4));
                            Vector vector = player.getLocation().getDirection().multiply(-7).setY(20);
                            player.setVelocity(vector);
                            player.playSound(player.getLocation(), Sound.FIREWORK_LAUNCH, 1, 1.4F);
                            ToyStickCoolDown.add(player);
                            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
                                @Override
                                public void run() {
                                    player.playSound(player.getLocation(), Sound.EXPLODE, 1, .7F);
                                }
                            }, 4);
                            ToyStick.put(player, true);
                            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
                                @Override
                                public void run() {
                                    ToyStickCoolDown.remove(player);
                                }
                            }, 10 * 20);
                        } else {
                            player.sendMessage(ChatColor.RED + "Toy Stick is on Cooldown! Please wait " + ChatColor.WHITE + "10 Seconds" + ChatColor.RED + " between uses!");
                        }
                    }
                }
            }

    }
}
