package me.teakivy.lobby.Bedwars.Events;

import jdk.nashorn.internal.ir.Block;
import me.teakivy.lobby.Bedwars.Bedwars;
import me.teakivy.lobby.Bedwars.util.BedwarsPlayer;
import me.teakivy.lobby.Main;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;

public class BedBug implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void NoTargetBedBug(EntityTargetLivingEntityEvent event) {
        Entity entity = event.getEntity();
        if (!entity.getType().equals(EntityType.SILVERFISH)) return;
        if(event.getTarget() instanceof Player) {
            Player player = (Player) event.getTarget();
            if (entity.hasMetadata(player.getName())) event.setCancelled(true);
        }
    }

//    @EventHandler
//    public void eggBridge(PlayerEggThrowEvent e) {
//        Egg egg = e.getEgg();
//        System.out.println("Egg");
//
//        // Start timer
//        BukkitRunnable eggTimer = new BukkitRunnable() {
//
//            int ticks = 0;
//
//            @Override
//            public void run() {
//                System.out.println("block");
//                ticks += 2;
//                if(egg.isDead()) { // If it hits something
//                    this.cancel();
//                    return;
//                }
//
//                //Check time
//                if(ticks >= 30){
//                    egg.remove();
//                    this.cancel();
//                }
//
//                // WARNING: You might need to test if the block ist AIR
//                if (egg.getLocation().getBlock().getType() != Material.AIR) return;
//                Blocks.BlocksPlaced.add(egg.getLocation());
//                egg.getLocation().getBlock().setType(Material.WOOL); // Use any material you want
//            }
//        };
//        eggTimer.runTaskTimer(Main.getPlugin(Main.class), 2L, 2L);
//        System.out.println("dw");
//    }

    @EventHandler
    public void spawnGolem(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (event.getPlayer().getItemInHand().getType().equals(Material.MONSTER_EGG)) {
                    if (!event.getPlayer().getItemInHand().hasItemMeta()) return;
                    if (!event.getPlayer().getItemInHand().getItemMeta().hasDisplayName()) return;
                    if (event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Dream Defender")) {
                        Player player = event.getPlayer();


                        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
                            ItemStack dreamDefender = new ItemStack(Material.MONSTER_EGG);
                            ItemMeta dreamDefenderMeta = dreamDefender.getItemMeta();
                            dreamDefenderMeta.setDisplayName(ChatColor.GOLD + "Dream Defender");
                            dreamDefender.setItemMeta(dreamDefenderMeta);

                            player.getInventory().removeItem(dreamDefender);
                        }, 1);


                        IronGolem ig = event.getClickedBlock().getLocation().getWorld().spawn(event.getClickedBlock().getLocation().add(.5, 2.5, .5), IronGolem.class);
                        ig.setMetadata(player.getName(), new FixedMetadataValue(Main.getPlugin(Main.class), "owner"));

                        ig.setCustomName(BedwarsPlayer.getChatColor(player) + ChatColor.BOLD.toString() + "Dream Defender");
                        ig.setCustomNameVisible(true);
                        ig.setTarget(BedwarsPlayer.getOpponent(player));

                        Bedwars.EntitySpawns.add(ig);


                        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
                            if (!ig.isDead()) {
                                ig.remove();
                                player.sendMessage(ChatColor.RED + "Your " + ig.getCustomName() + ChatColor.RESET + ChatColor.RED.toString() + " has been removed!");;
                            }
                        }, 120 * 20);
                    }
                }
            }
        }
    }

    @EventHandler
    public void NoTargetIG(EntityTargetLivingEntityEvent event) {
        Entity entity = event.getEntity();
        if (!entity.getType().equals(EntityType.IRON_GOLEM)) return;
        if (event.getTarget() == null) return;
        if (event.getTarget().getType() != EntityType.PLAYER) {
            event.setCancelled(true);
            return;
        }
        if(event.getTarget() instanceof Player) {
            Player player = (Player) event.getTarget();
            if (entity.hasMetadata(player.getName())) event.setCancelled(true);
        }
    }

    @EventHandler
    public void chickEgg(CreatureSpawnEvent event) {
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.EGG) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void throwSnowball(ProjectileHitEvent event) {
        Entity entity = event.getEntity();

        if (event.getEntity() instanceof Snowball) {
            if (event.getEntity().getShooter() instanceof Player) {
                Player player = (Player) event.getEntity().getShooter();
                if (!Bedwars.BedwarsGame.get(player)) return;
                Silverfish silverfish = entity.getWorld().spawn(entity.getLocation(), Silverfish.class);
                silverfish.setMetadata(player.getName(), new FixedMetadataValue(Main.getPlugin(Main.class), "owner"));
                silverfish.setCustomName(BedwarsPlayer.getChatColor(player) + ChatColor.BOLD.toString() + "BedBug");
                silverfish.setCustomNameVisible(true);
                silverfish.setTarget(BedwarsPlayer.getOpponent(player));
                Bedwars.EntitySpawns.add(silverfish);

                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
                    if (!silverfish.isDead()) {
                        silverfish.remove();
                        player.sendMessage(ChatColor.RED + "Your " + silverfish.getCustomName() + ChatColor.RESET + ChatColor.RED.toString() + " has been removed!");;
                    }
                }, 30 * 20);
            }
        }
    }

    @EventHandler
    public void throwEgg(ProjectileLaunchEvent event) {
        Entity entity = event.getEntity();

        if (event.getEntity() instanceof Egg) {
            if (event.getEntity().getShooter() instanceof Player) {
                Player player = (Player) event.getEntity().getShooter();
                if (!Bedwars.BedwarsGame.get(player)) return;
                Egg egg = (Egg) entity;


                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
                    BukkitRunnable run = new BukkitRunnable() {
                        int ticks = 22;
                        @Override
                        public void run() {
                            ticks--;
                            if (ticks < 0) {

                                try {
                                    cancel();
                                    return;
                                } catch (IllegalStateException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (egg.isDead()) {
                                try {
                                    cancel();
                                    return;
                                } catch (IllegalStateException e) {
                                    e.printStackTrace();
                                }
                            }

                            Location loc = new Location(egg.getWorld(), (int) egg.getLocation().getX(), (int) egg.getLocation().getY(), (int) egg.getLocation().getZ(), 0, 0);
                            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
                                if (loc.getBlock().getType() == Material.AIR) {
                                    loc.getBlock().setType(Material.WOOL);
                                    loc.getBlock().setData(BedwarsPlayer.getByteColor(player));
                                    Blocks.BlocksPlaced.add(loc);
                                }
                            }, 2);

                            Location loc2 = new Location(egg.getWorld(), (int) egg.getLocation().getX() + 1, (int) egg.getLocation().getY(), (int) egg.getLocation().getZ(), 0, 0);
                            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {

                                if (loc2.getBlock().getType() == Material.AIR) {
                                    loc2.getBlock().setType(Material.WOOL);
                                    loc2.getBlock().setData(BedwarsPlayer.getByteColor(player));
                                    Blocks.BlocksPlaced.add(loc2);
                                }
                            }, 2);

                            Location loc3 = new Location(egg.getWorld(), (int) egg.getLocation().getX() - 1, (int) egg.getLocation().getY(), (int) egg.getLocation().getZ(), 0, 0);
                            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {

                                if (loc3.getBlock().getType() == Material.AIR) {
                                    loc3.getBlock().setType(Material.WOOL);
                                    loc3.getBlock().setData(BedwarsPlayer.getByteColor(player));
                                    Blocks.BlocksPlaced.add(loc3);
                                }
                            }, 2);

                            Location loc4 = new Location(egg.getWorld(), (int) egg.getLocation().getX(), (int) egg.getLocation().getY(), (int) egg.getLocation().getZ() + 1, 0, 0);
                            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {

                                if (loc4.getBlock().getType() == Material.AIR) {
                                    loc4.getBlock().setType(Material.WOOL);
                                    loc4.getBlock().setData(BedwarsPlayer.getByteColor(player));
                                    Blocks.BlocksPlaced.add(loc4);
                                }
                            }, 2);

                        }
                    };
                    run.runTaskTimerAsynchronously(Main.getPlugin(Main.class), 1, 1);
                }, 2);
            }
        }
    }

}
