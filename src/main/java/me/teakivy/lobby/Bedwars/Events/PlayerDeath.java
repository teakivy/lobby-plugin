package me.teakivy.lobby.Bedwars.Events;

import me.teakivy.lobby.Bedwars.Bedwars;
import me.teakivy.lobby.Bedwars.util.BedwarsPlayer;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArrow;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftGolem;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftIronGolem;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftSilverfish;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.material.Bed;
import org.bukkit.util.Vector;

import java.util.Iterator;

public class PlayerDeath implements Listener {

//    @EventHandler
//    public void PlayerDamageReceive(EntityDamageByEntityEvent e) {
//        if (!(e.getEntity() instanceof Player)) return;
//            Player damaged = (Player) e.getEntity();
//
//            if (!Bedwars.getGame(damaged)) {
//                return;
//            }
//            if (!Bedwars.BedwarsActive) {
//                e.setCancelled(true);
//                return;
//            }
//
//
//            if (e.getDamager() instanceof Player) {
//                Player damager = (Player) e.getDamager();
//
//                if (Bedwars.BedwarsGame.get(damaged)) {
//                    Bedwars.BedwarsLastPlayerDamage.put(damaged, damager);
//                }
//
//                if ((damaged.getHealth() - e.getFinalDamage()) < 0) {
//
//                    e.setCancelled(true);
//                    damager.playSound(damager.getLocation(), Sound.HURT_FLESH, 1, 1);
//                    damaged.playSound(damaged.getLocation(), Sound.HURT_FLESH, 1, 1);
//
//
//
//                        if (damaged == Bedwars.BedwarsPlayer1) {
//
//                            Bedwars.BedwarsKills.put(damager, Bedwars.BedwarsKills.get(damager) + 1);
////                            updateBridgeScoreboard(BridgePlayer1);
////                            updateBridgeScoreboard(BridgePlayer2);
//
//                            Bedwars.BedwarsPlayer1.sendMessage(ChatColor.RED + Bedwars.BedwarsPlayer1.getName() + ChatColor.GRAY + " was stabbed by " + ChatColor.BLUE + Bedwars.BedwarsPlayer2.getName());
//                            Bedwars.BedwarsPlayer2.sendMessage(ChatColor.RED + Bedwars.BedwarsPlayer1.getName() + ChatColor.GRAY + " was stabbed by " + ChatColor.BLUE + Bedwars.BedwarsPlayer2.getName());
//
//                            Bedwars.BedwarsPlayer1.playSound(Bedwars.BedwarsPlayer1.getLocation(), Sound.ANVIL_LAND, 1, 1);
//                            Bedwars.BedwarsPlayer2.playSound(Bedwars.BedwarsPlayer2.getLocation(), Sound.NOTE_PLING, 1, 1);
//
//                            if (BedwarsPlayer.hasBed(damaged)) {
//                                BedwarsPlayer.playerDeath(damaged, damager);
//                            } else {
//                                BedwarsPlayer.endGame(damaged);
//                            }
//                        } else {
//                            Bedwars.BedwarsKills.put(damager, Bedwars.BedwarsKills.get(damager) + 1);
////                            updateBridgeScoreboard(BridgePlayer1);
////                            updateBridgeScoreboard(BridgePlayer2);
//
//                            Bedwars.BedwarsPlayer1.sendMessage(ChatColor.BLUE + Bedwars.BedwarsPlayer2.getName() + ChatColor.GRAY + " was stabbed by " + ChatColor.RED + Bedwars.BedwarsPlayer1.getName());
//                            Bedwars.BedwarsPlayer2.sendMessage(ChatColor.BLUE + Bedwars.BedwarsPlayer2.getName() + ChatColor.GRAY + " was stabbed by " + ChatColor.RED + Bedwars.BedwarsPlayer1.getName());
//                            Bedwars.BedwarsPlayer2.playSound(Bedwars.BedwarsPlayer1.getLocation(), Sound.ANVIL_LAND, 1, 1);
//                            Bedwars.BedwarsPlayer1.playSound(Bedwars.BedwarsPlayer2.getLocation(), Sound.NOTE_PLING, 1, 1);
//                            if (BedwarsPlayer.hasBed(damaged)) {
//                                BedwarsPlayer.playerDeath(damaged, damager);
//                            } else {
//                                BedwarsPlayer.endGame(damaged);
//                            }
//                        }
//                    }
//                }
//            } else if (e.getDamager() instanceof CraftArrow) {
//                CraftArrow arrow = (CraftArrow) e.getDamager();
//                if (arrow.getShooter() instanceof Player) {
//                    Player damager = (Player) arrow.getShooter();
//                    Player damaged = (Player) e.getEntity();
//
//                    if (Bedwars.BedwarsGame.get(damaged)) {
//                        Bedwars.BedwarsLastPlayerDamage.put(damaged, damager);
//                    }
//
//                    if ((damaged.getHealth() - e.getFinalDamage()) < 0) {
//
//                        e.setCancelled(true);
//                        damager.playSound(damager.getLocation(), Sound.HURT_FLESH, 1, 1);
//                        damaged.playSound(damaged.getLocation(), Sound.HURT_FLESH, 1, 1);
//
//
//
//                        if (damaged == Bedwars.BedwarsPlayer1) {
//                            BedwarsPlayer.playerDeath(damaged, damager);
//                            Bedwars.BedwarsKills.put(damager, Bedwars.BedwarsKills.get(damager) + 1);
////                            updateBridgeScoreboard(BridgePlayer1);
////                            updateBridgeScoreboard(BridgePlayer2);
//
//                            Bedwars.BedwarsPlayer1.sendMessage(ChatColor.RED + Bedwars.BedwarsPlayer1.getName() + ChatColor.GRAY + " was shot by " + ChatColor.BLUE + Bedwars.BedwarsPlayer2.getName());
//                            Bedwars.BedwarsPlayer2.sendMessage(ChatColor.RED + Bedwars.BedwarsPlayer1.getName() + ChatColor.GRAY + " was shot by " + ChatColor.BLUE + Bedwars.BedwarsPlayer2.getName());
//
//                            Bedwars.BedwarsPlayer1.playSound(Bedwars.BedwarsPlayer1.getLocation(), Sound.ANVIL_LAND, 1, 1);
//                            Bedwars.BedwarsPlayer2.playSound(Bedwars.BedwarsPlayer2.getLocation(), Sound.NOTE_PLING, 1, 1);
//
//                        } else {
//                            BedwarsPlayer.playerDeath(damaged, damager);
//                            Bedwars.BedwarsKills.put(damager, Bedwars.BedwarsKills.get(damager) + 1);
////                            updateBridgeScoreboard(BridgePlayer1);
////                            updateBridgeScoreboard(BridgePlayer2);
//
//                            Bedwars.BedwarsPlayer1.sendMessage(ChatColor.BLUE + Bedwars.BedwarsPlayer2.getName() + ChatColor.GRAY + " was shot by " + ChatColor.RED + Bedwars.BedwarsPlayer1.getName());
//                            Bedwars.BedwarsPlayer2.sendMessage(ChatColor.BLUE + Bedwars.BedwarsPlayer2.getName() + ChatColor.GRAY + " was shot by " + ChatColor.RED + Bedwars.BedwarsPlayer1.getName());
//                            Bedwars.BedwarsPlayer2.playSound(Bedwars.BedwarsPlayer1.getLocation(), Sound.ANVIL_LAND, 1, 1);
//                            Bedwars.BedwarsPlayer1.playSound(Bedwars.BedwarsPlayer2.getLocation(), Sound.NOTE_PLING, 1, 1);
//                        }
//                    }
//                }
//
//
//
//        }
//        if (e.getDamager().getType() == EntityType.IRON_GOLEM) {
//            System.out.println("Hello");
//            if (!Bedwars.BedwarsGame.get(e.getEntity())) return;
//            if (!e.isCancelled() && e.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
//                e.setDamage(e.getDamage() * .2);
//            }
//            Player damaged = (Player) e.getEntity();
//
//            if (Bedwars.BedwarsGame.get(damaged)) {
//                Bedwars.BedwarsLastPlayerDamage.put(damaged, damaged);
//            }
//
//            if ((damaged.getHealth() - e.getFinalDamage()) < 0) {
//
//                e.setCancelled(true);
//                damaged.playSound(damaged.getLocation(), Sound.HURT_FLESH, 1, 1);
//
//
//
//                BedwarsPlayer.playerDeath(damaged, damaged);
//                BedwarsPlayer.sendMessage(BedwarsPlayer.getChatColor(damaged) + damaged.getName() + ChatColor.GRAY + " died!");
//            }
//
//
//    }

    @EventHandler
    public void onDamgeByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (!Bedwars.BedwarsGame.get(player)) return;
        if (!Bedwars.BedwarsActive) {
            event.setCancelled(true);
            return;
        }

        Player damaged = player;

        Entity damagerEntity = event.getDamager();

        if (damagerEntity.getType() == EntityType.PLAYER) {
            Player damager = (Player) event.getDamager();
            if (Bedwars.BedwarsGame.get(damaged)) {
                Bedwars.BedwarsLastPlayerDamage.put(damaged, damager);
            }
            if ((damaged.getHealth() - event.getFinalDamage()) < 0) {
                event.setCancelled(true);
                damager.playSound(damager.getLocation(), Sound.HURT_FLESH, 1, 1);
                damaged.playSound(damaged.getLocation(), Sound.HURT_FLESH, 1, 1);
                if (damaged == Bedwars.BedwarsPlayer1) {
                    Bedwars.BedwarsKills.put(damager, Bedwars.BedwarsKills.get(damager) + 1);
//                           updateBridgeScoreboard(BridgePlayer1);
//                           updateBridgeScoreboard(BridgePlayer2);
                    Bedwars.BedwarsPlayer1.sendMessage(ChatColor.RED + Bedwars.BedwarsPlayer1.getName() + ChatColor.GRAY + " was stabbed by " + ChatColor.BLUE + Bedwars.BedwarsPlayer2.getName());
                    Bedwars.BedwarsPlayer2.sendMessage(ChatColor.RED + Bedwars.BedwarsPlayer1.getName() + ChatColor.GRAY + " was stabbed by " + ChatColor.BLUE + Bedwars.BedwarsPlayer2.getName());
                    Bedwars.BedwarsPlayer1.playSound(Bedwars.BedwarsPlayer1.getLocation(), Sound.ANVIL_LAND, 1, 1);
                    Bedwars.BedwarsPlayer2.playSound(Bedwars.BedwarsPlayer2.getLocation(), Sound.NOTE_PLING, 1, 1);
                    if (BedwarsPlayer.hasBed(damaged)) {
                        BedwarsPlayer.playerDeath(damaged, damager);
                    } else {
                        BedwarsPlayer.endGame(damaged);
                    }
                } else {
                    Bedwars.BedwarsKills.put(damager, Bedwars.BedwarsKills.get(damager) + 1);
//                           updateBridgeScoreboard(BridgePlayer1);
//                           updateBridgeScoreboard(BridgePlayer2);
                    Bedwars.BedwarsPlayer1.sendMessage(ChatColor.BLUE + Bedwars.BedwarsPlayer2.getName() + ChatColor.GRAY + " was stabbed by " + ChatColor.RED + Bedwars.BedwarsPlayer1.getName());
                    Bedwars.BedwarsPlayer2.sendMessage(ChatColor.BLUE + Bedwars.BedwarsPlayer2.getName() + ChatColor.GRAY + " was stabbed by " + ChatColor.RED + Bedwars.BedwarsPlayer1.getName());
                    Bedwars.BedwarsPlayer2.playSound(Bedwars.BedwarsPlayer1.getLocation(), Sound.ANVIL_LAND, 1, 1);
                    Bedwars.BedwarsPlayer1.playSound(Bedwars.BedwarsPlayer2.getLocation(), Sound.NOTE_PLING, 1, 1);
                    if (BedwarsPlayer.hasBed(damaged)) {
                        BedwarsPlayer.playerDeath(damaged, damager);
                    } else {
                        BedwarsPlayer.endGame(damaged);
                    }
                }
            }
        } else if (damagerEntity.getType() == EntityType.ARROW) {
            CraftArrow arrow = (CraftArrow) event.getDamager();
                if (arrow.getShooter() instanceof Player) {
                    Player damager = (Player) arrow.getShooter();

                    if (Bedwars.BedwarsGame.get(damaged)) {
                        Bedwars.BedwarsLastPlayerDamage.put(damaged, damager);
                    }

                    if ((damaged.getHealth() - event.getFinalDamage()) < 0) {

                        event.setCancelled(true);
                        damager.playSound(damager.getLocation(), Sound.HURT_FLESH, 1, 1);
                        damaged.playSound(damaged.getLocation(), Sound.HURT_FLESH, 1, 1);



                        if (damaged == Bedwars.BedwarsPlayer1) {
                            if (BedwarsPlayer.hasBed(damaged)) {
                                BedwarsPlayer.playerDeath(damaged, damager);
                            } else {
                                BedwarsPlayer.endGame(damaged);
                            }
                            Bedwars.BedwarsKills.put(damager, Bedwars.BedwarsKills.get(damager) + 1);
//                            updateBridgeScoreboard(BridgePlayer1);
//                            updateBridgeScoreboard(BridgePlayer2);

                            Bedwars.BedwarsPlayer1.sendMessage(ChatColor.RED + Bedwars.BedwarsPlayer1.getName() + ChatColor.GRAY + " was shot by " + ChatColor.BLUE + Bedwars.BedwarsPlayer2.getName());
                            Bedwars.BedwarsPlayer2.sendMessage(ChatColor.RED + Bedwars.BedwarsPlayer1.getName() + ChatColor.GRAY + " was shot by " + ChatColor.BLUE + Bedwars.BedwarsPlayer2.getName());

                            Bedwars.BedwarsPlayer1.playSound(Bedwars.BedwarsPlayer1.getLocation(), Sound.ANVIL_LAND, 1, 1);
                            Bedwars.BedwarsPlayer2.playSound(Bedwars.BedwarsPlayer2.getLocation(), Sound.NOTE_PLING, 1, 1);

                        } else {
                            if (BedwarsPlayer.hasBed(damaged)) {
                                BedwarsPlayer.playerDeath(damaged, damager);
                            } else {
                                BedwarsPlayer.endGame(damaged);
                            }
                            Bedwars.BedwarsKills.put(damager, Bedwars.BedwarsKills.get(damager) + 1);
//                            updateBridgeScoreboard(BridgePlayer1);
//                            updateBridgeScoreboard(BridgePlayer2);

                            Bedwars.BedwarsPlayer1.sendMessage(ChatColor.BLUE + Bedwars.BedwarsPlayer2.getName() + ChatColor.GRAY + " was shot by " + ChatColor.RED + Bedwars.BedwarsPlayer1.getName());
                            Bedwars.BedwarsPlayer2.sendMessage(ChatColor.BLUE + Bedwars.BedwarsPlayer2.getName() + ChatColor.GRAY + " was shot by " + ChatColor.RED + Bedwars.BedwarsPlayer1.getName());
                            Bedwars.BedwarsPlayer2.playSound(Bedwars.BedwarsPlayer1.getLocation(), Sound.ANVIL_LAND, 1, 1);
                            Bedwars.BedwarsPlayer1.playSound(Bedwars.BedwarsPlayer2.getLocation(), Sound.NOTE_PLING, 1, 1);
                        }
                    }
                }
        } else if (damagerEntity.getType() == EntityType.IRON_GOLEM || damagerEntity.getType() == EntityType.SILVERFISH) {

            if (Bedwars.BedwarsGame.get(damaged)) {
                Bedwars.BedwarsLastPlayerDamage.put(damaged, damaged);
            }

            if ((damaged.getHealth() - event.getFinalDamage()) < 0) {

                event.setCancelled(true);
                damaged.playSound(damaged.getLocation(), Sound.HURT_FLESH, 1, 1);



                if (BedwarsPlayer.hasBed(damaged)) {
                    BedwarsPlayer.playerDeath(damaged, BedwarsPlayer.getOpponent(damaged));
                } else {
                    BedwarsPlayer.endGame(damaged);
                }
                BedwarsPlayer.sendMessage(BedwarsPlayer.getChatColor(damaged) + damaged.getName() + ChatColor.GRAY + " was slain by " + ChatColor.BOLD + BedwarsPlayer.getChatColor(BedwarsPlayer.getOpponent(player)) + BedwarsPlayer.getOpponent(player).getName() + "'s " + event.getDamager().getCustomName());
            }
        } else if (damagerEntity.getType() == EntityType.SNOWBALL) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (event.getEntity().getType() == EntityType.DROPPED_ITEM && event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
            event.setCancelled(true);

            return;
        };
        if (event.getEntity() instanceof Player) {
            if (!Bedwars.BedwarsGame.get((Player) event.getEntity())) return;
            if (!event.isCancelled() && event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
                event.setDamage(event.getDamage() * .2);
            }
            if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;
            Player damaged = (Player) event.getEntity();
            event.setDamage(2);

            if (Bedwars.BedwarsGame.get(damaged)) {
                Bedwars.BedwarsLastPlayerDamage.put(damaged, damaged);
            }

            if ((damaged.getHealth() - event.getFinalDamage()) < 0) {

                event.setCancelled(true);
                damaged.playSound(damaged.getLocation(), Sound.HURT_FLESH, 1, 1);



                BedwarsPlayer.playerDeath(damaged, damaged);
                BedwarsPlayer.sendMessage(BedwarsPlayer.getChatColor(damaged) + damaged.getName() + ChatColor.GRAY + " died!");
            }
        }

    }

//    @EventHandler
//    public void onTNT(EntityExplodeEvent e) {
//        Location loc = e.getLocation();
//
//        for (Entity entity : loc.getWorld().getEntities()) {
//            if (entity.getLocation().getWorld() != loc.getWorld()) {
//                return;
//            }
//
//            double distance = (double) (e.getYield() * 16.0F);
//            distance *= 1;
//            if (loc.distance(entity.getLocation()) <= distance) {
//                if (entity instanceof Player) {
//                    EntityDamageEvent DamageEvent = new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, distance - loc.distance(entity.getLocation()));
//                    Bukkit.getPluginManager().callEvent(DamageEvent);
//                    if (!DamageEvent.isCancelled()) {
//                        ((Player) entity).damage(DamageEvent.getFinalDamage());
//                    }
//                }
//
//                double difZ = entity.getLocation().getZ() - loc.getZ();
//                double difX = entity.getLocation().getX() - loc.getX();
//                double Z = difZ;
//                double X = difX;
//                if (difX > 0.0D) {
//                    if (difX > distance / 2.0D) {
//                        X = distance / 2.0D - (difX - distance / 2.0D);
//                    }
//                } else if (difX < -(distance / 2.0D)) {
//                    X = -(distance / 2.0D) - (distance / 2.0D + difX);
//                }
//
//                if (difZ > 0.0D) {
//                    if (difZ > distance / 2.0D) {
//                        Z = distance / 2.0D - (difZ - distance / 2.0D);
//                    }
//                } else if (difZ < -(distance / 2.0D)) {
//                    Z = -(distance / 2.0D) - (distance / 2.0D + difZ);
//                }
//
//                double Y = distance - (loc.distance(entity.getLocation()) - Math.sqrt(difX * difX + difZ * difZ));
//                if (X < 0.5D && X > -0.5D) {
//                    X *= 0.5D;
//                }
//
//                if (Z < 0.5D && Z > -0.5D) {
//                    Z *= 0.5D;
//                }
//
//                Y *= (double) e.getYield() * 0.2D;
//                X *= 1;
//                Y *= 1;
//                Z *= 1;
//                Vector v = entity.getVelocity();
//                entity.setVelocity(new Vector(X + v.getX(), Y + v.getY(), Z + v.getZ()));
//            }
//        }
//
//    }
}
