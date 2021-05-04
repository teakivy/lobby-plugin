package me.teakivy.lobby.Bedwars.Events;

import me.teakivy.lobby.Bedwars.Bedwars;
import me.teakivy.lobby.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.Set;

public class Fireball implements Listener {

    @EventHandler
    public void fireballThrow (PlayerInteractEvent event) {
        Action a = event.getAction();
        Player player = event.getPlayer();
        if (a == Action.RIGHT_CLICK_BLOCK || a == Action.RIGHT_CLICK_AIR) {
            if (player.getItemInHand().getType() == Material.FIREBALL) {
                event.setCancelled(true);
                player.getInventory().removeItem(new ItemStack(Material.FIREBALL, 1));
                org.bukkit.entity.Fireball fireball = player.launchProjectile(org.bukkit.entity.Fireball.class);
//                fireball.teleport(new Location(fireball.getLocation().getWorld(), fireball.getLocation().getX(), fireball.getLocation().getY() - .4, fireball.getLocation().getZ()));
                fireball.setYield(2);
                fireball.setVelocity(fireball.getDirection().multiply(4));
//                fireball.


                Bedwars.EntitySpawns.add(fireball);

                org.bukkit.entity.Fireball finalFireball = fireball;
                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
                    if (!fireball.isDead()) {
                        fireball.remove();
                    }
                }, 3 * 20);
            }
        }
    }
}
