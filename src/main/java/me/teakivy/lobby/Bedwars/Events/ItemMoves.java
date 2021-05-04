package me.teakivy.lobby.Bedwars.Events;

import me.teakivy.lobby.Bedwars.Bedwars;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Bed;

public class ItemMoves implements Listener {

    @EventHandler
    public void dropSwordEvent(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemDrop().getItemStack();
        if (Bedwars.BedwarsGame.get(player) && item.getType() == Material.WOOD_SWORD) {
            event.setCancelled(true);
        }
        if (Bedwars.BedwarsGame.get(player) && (item.getType() == Material.STONE_SWORD || item.getType() == Material.IRON_SWORD || item.getType() == Material.DIAMOND_SWORD)) {
            if (!contains(player, Material.WOOD_SWORD) && !contains(player, Material.STONE_SWORD) && !contains(player, Material.IRON_SWORD) && !contains(player, Material.DIAMOND_SWORD))
                player.getInventory().addItem(new ItemStack(Material.WOOD_SWORD));
        }
        if (Bedwars.BedwarsGame.get(player) && (item.getType().equals(Material.SHEARS) || item.getType().equals(Material.WOOD_PICKAXE) || item.getType().equals(Material.IRON_PICKAXE) || item.getType().equals(Material.GOLD_PICKAXE) || item.getType().equals(Material.DIAMOND_PICKAXE))) {
            event.setCancelled(true);
        }
        if (Bedwars.BedwarsGame.get(player) && (item.getType().equals(Material.WOOD_AXE) || item.getType().equals(Material.STONE_AXE) || item.getType().equals(Material.IRON_AXE) || item.getType().equals(Material.DIAMOND_AXE))) {
            event.setCancelled(true);
        }
    }


    public boolean contains(Player player, Material material) {
        return player.getInventory().containsAtLeast(new ItemStack(material), 1);
    }

    @EventHandler
    public void pickupSwordEvent(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem().getItemStack();

        if (Bedwars.BedwarsGame.get(player) && (item.getType() == Material.STONE_SWORD || item.getType() == Material.IRON_SWORD || item.getType() == Material.DIAMOND_SWORD)) {
            if (contains(player, Material.WOOD_SWORD))
                player.getInventory().removeItem(new ItemStack(Material.WOOD_SWORD));
        }
    }

    @EventHandler
    public void craftEvent(CraftItemEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if (Bedwars.BedwarsGame.get(player)) event.setCancelled(true);
    }
}
