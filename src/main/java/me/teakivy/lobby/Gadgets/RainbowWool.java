package me.teakivy.lobby.Gadgets;

import com.mongodb.client.MongoCollection;
import me.teakivy.lobby.Main;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
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
import org.bukkit.material.Wool;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Random;

public class RainbowWool implements Listener {

    static MongoCollection< Document > col = Main.getPlugin(Main.class).col;
    private static final Random random = new Random();

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
                if (player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.RED + "R" + ChatColor.GOLD + "a" + ChatColor.YELLOW + "i" + ChatColor.GREEN + "n" + ChatColor.AQUA + "b" + ChatColor.DARK_AQUA + "o" + ChatColor.LIGHT_PURPLE + "w" + ChatColor.RED + " W" + ChatColor.GOLD + "o" + ChatColor.YELLOW + "o" + ChatColor.GREEN + "l")) {
                    BlockState blockState = b.getState();
                    blockState.setData(new Wool(randomColor()));
                    blockState.update();
                    BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
                    scheduler.scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
                        @Override
                        public void run() {
                            b.getLocation().getBlock().setType(Material.AIR);
                            PlayerInventory inventory = player.getInventory();
                            if (!Main.getPlugin(Main.class).BridgeGame.get(player) && !Main.getPlugin(Main.class).SumoGame.get(player) && !Main.getPlugin(Main.class).ClassicGame.get(player)) {

                                ItemStack rainbowWool = new ItemStack(Material.WOOL, 1, (byte) 14);
                                ItemMeta rainbowMeta = rainbowWool.getItemMeta();

                                rainbowMeta.setDisplayName(ChatColor.RED + "R" + ChatColor.GOLD + "a" + ChatColor.YELLOW + "i" + ChatColor.GREEN + "n" + ChatColor.AQUA + "b" + ChatColor.DARK_AQUA + "o" + ChatColor.LIGHT_PURPLE + "w" + ChatColor.RED + " W" + ChatColor.GOLD + "o" + ChatColor.YELLOW + "o" + ChatColor.GREEN + "l");
                                rainbowWool.setItemMeta(rainbowMeta);
                                player.getInventory().addItem(rainbowWool);
                            }

                        }
                    }, 45);
                }
            }
        }
    }

    public static DyeColor randomColor() {
        int number = random.nextInt(16);

        switch (number) {
            case 0:
                return DyeColor.WHITE;
            case 1:
                return DyeColor.ORANGE;
            case 2:
                return DyeColor.MAGENTA;
            case 3:
                return DyeColor.LIGHT_BLUE;
            case 4:
                return DyeColor.YELLOW;
            case 5:
                return DyeColor.LIME;
            case 6:
                return DyeColor.PINK;
            case 7:
                return DyeColor.GRAY;
            case 8:
                return DyeColor.SILVER;
            case 9:
                return DyeColor.CYAN;
            case 10:
                return DyeColor.PURPLE;
            case 11:
                return DyeColor.BLUE;
            case 12:
                return DyeColor.BROWN;
            case 13:
                return DyeColor.GREEN;
            case 14:
                return DyeColor.RED;
            case 15:
                return DyeColor.BLACK;
            default:
                return DyeColor.RED;
        }
    }
}
