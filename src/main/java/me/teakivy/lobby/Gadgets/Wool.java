package me.teakivy.lobby.Gadgets;

import me.teakivy.lobby.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;

public class Wool implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block b = event.getBlock();
        BlockState bReplaced = event.getBlockReplacedState();
        if (b.getType() == Material.WOOL) {
//            Document filter = new Document("UUID", player.getUniqueId().toString());
//            Bson updates = inc("stats.lobby.blocksPlaced", 1);
//            col.findOneAndUpdate(filter, updates);

            if (Main.getPlugin(Main.class).getLobbyBlocksPlaced(player) % 15 == 0) {
                Main.getPlugin(Main.class).addCoins(player, 1);
            }

            if (player.getItemInHand().getItemMeta().getDisplayName() != null) {
                if (player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Wool")) {
                    BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
                    scheduler.scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
                        @Override
                        public void run() {
                            b.getLocation().getBlock().setType(Material.AIR);
                            PlayerInventory inventory = player.getInventory();
                            if (!Main.getPlugin(Main.class).BridgeGame.get(player) && !Main.getPlugin(Main.class).SumoGame.get(player) && !Main.getPlugin(Main.class).ClassicGame.get(player)) {

                                ItemStack rainbowWool = new ItemStack(Material.WOOL, 1);
                                ItemMeta rainbowMeta = rainbowWool.getItemMeta();

                                rainbowMeta.setDisplayName(ChatColor.GREEN + "Wool");
                                rainbowWool.setItemMeta(rainbowMeta);
                                player.getInventory().addItem(rainbowWool);
                            }

                        }
                    }, 45);
                }
            }
        }
    }

    public static ItemStack woolItem() {
        ItemStack wool = new ItemStack(Material.WOOL, 64);
        ItemMeta woolMeta = wool.getItemMeta();

        woolMeta.setDisplayName(ChatColor.GREEN + "Wool");
        wool.setItemMeta(woolMeta);
        return wool;
    }
}
