package me.teakivy.lobby.Bedwars.Events;

import jdk.nashorn.internal.ir.LiteralNode;
import me.teakivy.lobby.Bedwars.Bedwars;
import me.teakivy.lobby.Bedwars.util.BedwarsPlayer;
import me.teakivy.lobby.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Bed;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class Blocks implements Listener {

    public static ArrayList<Location> BlocksPlaced = new ArrayList<>();

    public static Location redBedFooter = new Location(Bukkit.getWorld("world"), 20082, 97, 20036);
    public static Location redBedHeader = new Location(Bukkit.getWorld("world"), 20082, 97, 20035);

    public static Location blueBedFooter = new Location(Bukkit.getWorld("world"), 20134, 97, 20036);
    public static Location blueBedHeader = new Location(Bukkit.getWorld("world"), 20134, 97, 20035);

    Material[] blocksPossible = {Material.WOOL, Material.ENDER_STONE, Material.WOOD, Material.SPONGE, Material.OBSIDIAN, Material.LADDER, Material.GLASS, Material.HARD_CLAY, Material.TNT};

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!Bedwars.BedwarsActive) return;
        if (!Bedwars.isInGame(event.getPlayer())) return;
        if (event.getBlockReplacedState().getType() != Material.AIR) {
            event.setCancelled(true);
            return;
        }
        Block block = event.getBlockPlaced();
        BlocksPlaced.add(block.getLocation());
        if (block.getType().equals(Material.TNT)) {
            block.setType(Material.AIR);
            TNTPrimed tnt = block.getWorld().spawn(block.getLocation().add(.5, .5, .5), TNTPrimed.class);
            tnt.setFuseTicks(60);
            tnt.setVelocity(new Vector(0, 0, 0));
        }
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent event) {
        event.blockList().removeIf(block -> !BlocksPlaced.contains(block.getLocation()));

        Location loc = event.getLocation();

        for (Entity entity : loc.getWorld().getEntities()) {
            if (entity.getLocation().getWorld() != loc.getWorld()) {
                return;
            }

            double distance = (event.getYield() * 16.0F);
            distance *= 1;
            if (loc.distance(entity.getLocation()) <= distance) {
                if (entity instanceof Player) {
                    EntityDamageEvent DamageEvent = new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, distance - loc.distance(entity.getLocation()));
                    Bukkit.getPluginManager().callEvent(DamageEvent);
                    if (!DamageEvent.isCancelled()) {
                        ((Player) entity).damage(DamageEvent.getFinalDamage());
                    }
                }

                double difZ = entity.getLocation().getZ() - loc.getZ();
                double difX = entity.getLocation().getX() - loc.getX();
                double Z = difZ;
                double X = difX;
                if (difX > 0.0D) {
                    if (difX > distance / 2.0D) {
                        X = distance / 2.0D - (difX - distance / 2.0D);
                    }
                } else if (difX < -(distance / 2.0D)) {
                    X = -(distance / 2.0D) - (distance / 2.0D + difX);
                }

                if (difZ > 0.0D) {
                    if (difZ > distance / 2.0D) {
                        Z = distance / 2.0D - (difZ - distance / 2.0D);
                    }
                } else if (difZ < -(distance / 2.0D)) {
                    Z = -(distance / 2.0D) - (distance / 2.0D + difZ);
                }

                double Y = distance - (loc.distance(entity.getLocation()) - Math.sqrt(difX * difX + difZ * difZ));
                if (X < 0.5D && X > -0.5D) {
                    X *= 0.5D;
                }

                if (Z < 0.5D && Z > -0.5D) {
                    Z *= 0.5D;
                }

                Y *= (double) event.getYield() * 0.2D;
                X *= .78;
                Y *= .7;
                Z *= .78;
                System.out.println(X);
                System.out.println(Z);
                Vector v = entity.getVelocity();
                double yMove = Y + v.getY() + ( 6 - entity.getLocation().distance(event.getLocation()) * 2) - 2;
                if ((Y + v.getY() + ( 6 - entity.getLocation().distance(event.getLocation()) * 2) - 2) < .5) {
                    yMove = .3;
                }
                if ((Y + v.getY() + ( 6 - entity.getLocation().distance(event.getLocation()) * 2) - 2) > 1.6) {
                    yMove = 1.4;
                }
                entity.setVelocity(new Vector(X + v.getX(), Y + v.getY(), Z + v.getZ()).setY(yMove));
            }
        }
    }



    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!Bedwars.isInGame(event.getPlayer())) return;
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (!BlocksPlaced.contains(block.getLocation())) {
            if (block.getType() == Material.BED_BLOCK) {
                if (BedwarsPlayer.getSpawnLocation(event.getPlayer()).distanceSquared(block.getLocation()) <= 256) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You cant break your own bed!");
                }
            } else {
                if (player.getGameMode().equals(GameMode.CREATIVE)) return;
                event.setCancelled(true);
            }
        } else {
            BlocksPlaced.remove(block.getLocation());
        }
    }

    @EventHandler
    public void onDrop(EntitySpawnEvent event) {
        if (event.getEntityType() == EntityType.DROPPED_ITEM) {
            Item item = (Item) event.getEntity();
            if (item.getItemStack().getType() == Material.BED) {
                if (item.getLocation().getX() > 20000 && item.getLocation().getX() < 20100) {
                    Bedwars.HasBed.put(Bedwars.BedwarsPlayer1, false);
                    BedwarsPlayer.bedBreak(Bedwars.BedwarsPlayer2);
                }else if (item.getLocation().getX() < 20200 && item.getLocation().getX() > 20100) {
                    Bedwars.HasBed.put(Bedwars.BedwarsPlayer2, false);
                    BedwarsPlayer.bedBreak(Bedwars.BedwarsPlayer1);
                } else {
                    System.out.println("no bed");
                }
                item.remove();
            }
        }
    }

    @EventHandler
    public void noSleep(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        if (!Bedwars.isInGame(player)) return;
        event.setCancelled(true);

    }

    @EventHandler
    public void interactEvent(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (event.getClickedBlock().getType() == Material.BED_BLOCK) {
                    if (!event.getPlayer().isSneaking()) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void waterBucket(PlayerBucketEmptyEvent event) {
        if (event.getBucket() == Material.WATER_BUCKET) {
            System.out.println("Water Bucket");
            Player player = event.getPlayer();

            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
                player.getInventory().removeItem(new ItemStack(Material.BUCKET));
            }, 1);
        }
    }

    public static void addBlock(Location location) {
        if (BlocksPlaced.contains(location)) return;
        BlocksPlaced.add(location);
    }

    public static void spawnBeds() {
        Block block;
        block = redBedFooter.getBlock();
        block.setType(Material.BED_BLOCK);
        block.setData((byte) 2);

        block = blueBedFooter.getBlock();
        block.setType(Material.BED_BLOCK);
        block.setData((byte) 2);

        block = redBedHeader.getBlock();
        block.setType(Material.BED_BLOCK);
        block.setData((byte) 10);

        block = blueBedHeader.getBlock();
        block.setType(Material.BED_BLOCK);
        block.setData((byte) 10);

    }
}
