package me.teakivy.lobby.utils;

import me.teakivy.lobby.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TeamNames {

    public static void setNamePrefix(Player player) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Scoreboard scoreboard = p.getScoreboard();

            Team team = scoreboard.getTeam(Ranks.getRankPriority(player) + player.getName().substring(0, 0) + player.getName().substring(1));
            if (team == null) {
                team = scoreboard.registerNewTeam(Ranks.getRankPriority(player) + player.getName().substring(0, 0) + player.getName().substring(1));
            }

            String playerRank = getRank(player);

            switch (playerRank) {
                case "OWNER":
                    team.setPrefix(ChatColor.DARK_RED + "[OWNER] ");
                    break;
                case "ADMIN":
                    team.setPrefix(ChatColor.RED + "[ADMIN] ");
                    break;
                case "MODERATOR":
                    team.setPrefix(ChatColor.AQUA + "[MOD] ");
                    break;
                case "HELPER":
                    team.setPrefix(ChatColor.GREEN + "[HELPER] ");
                    break;
                case "YOUTUBE":
                    team.setPrefix(ChatColor.RED + "[" + ChatColor.WHITE + "YOUTUBE" + ChatColor.RED + "] ");
                    break;
                case "TWITCH":
                    team.setPrefix(ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "TWITCH" + ChatColor.DARK_PURPLE + "] ");
                    break;
                default:
                    team.setPrefix(ChatColor.GRAY + "");
                    break;
                case "VIP":
                    team.setPrefix(ChatColor.GREEN + "[VIP] ");
                    break;
                case "VIP+":
                    team.setPrefix(ChatColor.GREEN + "[VIP" + ChatColor.GOLD + "+" + ChatColor.GREEN + "] ");
                    break;
                case "MVP":
                    team.setPrefix(ChatColor.AQUA + "[MVP] ");
                    break;
                case "MVP+":
                    team.setPrefix(ChatColor.GREEN + "[MVP" + getPlusColor(player) + "+" + ChatColor.AQUA + "] ");
                    break;
                case "MVP++":
                    team.setPrefix(ChatColor.GOLD + "[MVP" + getPlusColor(player) + "++" + ChatColor.GOLD + "] ");
                    break;
            }
            team.addEntry(player.getName());
        }

        Scoreboard scoreboard = player.getScoreboard();
        for (Player p : Bukkit.getOnlinePlayers()) {

            Team team = scoreboard.getTeam(Ranks.getRankPriority(p) + p.getName().substring(0, 0) + p.getName().substring(1));
            if (team == null) {
                team = scoreboard.registerNewTeam(Ranks.getRankPriority(p) + p.getName().substring(0, 0) + p.getName().substring(1));
            }

            switch (getRank(p)) {
                case "OWNER":

                    team.setPrefix(ChatColor.DARK_RED + "[OWNER] ");
                    break;
                case "ADMIN":
                    team.setPrefix(ChatColor.RED + "[ADMIN] ");
                    break;
                case "MODERATOR":
                    team.setPrefix(ChatColor.AQUA + "[MOD] ");
                    break;
                case "HELPER":
                    team.setPrefix(ChatColor.GREEN + "[HELPER] ");
                    break;
                case "YOUTUBE":
                    team.setPrefix(ChatColor.RED + "[" + ChatColor.WHITE + "YOUTUBE" + ChatColor.RED + "] ");
                    break;
                case "TWITCH":
                    team.setPrefix(ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "TWITCH" + ChatColor.DARK_PURPLE + "] ");
                    break;
                default:
                    team.setPrefix(ChatColor.GRAY + "");
                    break;
                case "VIP":
                    team.setPrefix(ChatColor.GREEN + "[VIP] ");
                    break;
                case "VIP+":
                    team.setPrefix(ChatColor.GREEN + "[VIP" + ChatColor.GOLD + "+" + ChatColor.GREEN + "] ");
                    break;
                case "MVP":
                    team.setPrefix(ChatColor.AQUA + "[MVP] ");
                    break;
                case "MVP+":
                    team.setPrefix(ChatColor.GREEN + "[MVP" + getPlusColor(player) + "+" + ChatColor.AQUA + "] ");
                    break;
                case "MVP++":
                    team.setPrefix(ChatColor.GOLD + "[MVP" + getPlusColor(player) + "++" + ChatColor.GOLD + "] ");
                    break;
            }
            team.addEntry(p.getName());
        }


        Team team = scoreboard.getTeam(Ranks.getRankPriority(player) + player.getName().substring(0, 0) + player.getName().substring(1));
        if (team == null) {
            team = scoreboard.registerNewTeam(Ranks.getRankPriority(player) + player.getName().substring(0, 0) + player.getName().substring(1));
        }

//        ProtocolManager pm = ProtocolLibrary.getProtocolManager();
//        PacketContainer packet = pm.createPacket(PacketType.Play.Server.ANIMATION);
//        packet.getModifier().writeDefaults();
//        packet.getIntegers().write(0, event.getPlayer().getEntityId()).write(1, 0);
//        try {
//            pm.sendServerPacket(event.getPlayer(), packet);
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }



        String playerRank = getRank(player);

        switch (playerRank) {
            case "OWNER":

                team.setPrefix(ChatColor.DARK_RED + "[OWNER] ");
                break;
            case "ADMIN":
                team.setPrefix(ChatColor.RED + "[ADMIN] ");
                break;
            case "MODERATOR":
                team.setPrefix(ChatColor.AQUA + "[MOD] ");
                break;
            case "HELPER":
                team.setPrefix(ChatColor.GREEN + "[HELPER] ");
                break;
            case "YOUTUBE":
                team.setPrefix(ChatColor.RED + "[" + ChatColor.WHITE + "YOUTUBE" + ChatColor.RED + "] ");
                break;
            case "TWITCH":
                team.setPrefix(ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "TWITCH" + ChatColor.DARK_PURPLE + "] ");
                break;
            default:
                team.setPrefix(ChatColor.GRAY + "");
                break;
            case "VIP":
                team.setPrefix(ChatColor.GREEN + "[VIP] ");
                break;
            case "VIP+":
                team.setPrefix(ChatColor.GREEN + "[VIP" + ChatColor.GOLD + "+" + ChatColor.GREEN + "] ");
                break;
            case "MVP":
                team.setPrefix(ChatColor.AQUA + "[MVP] ");
                break;
            case "MVP+":
                team.setPrefix(ChatColor.GREEN + "[MVP" + getPlusColor(player) + "+" + ChatColor.AQUA + "] ");
                break;
            case "MVP++":
                team.setPrefix(ChatColor.GOLD + "[MVP" + getPlusColor(player) + "++" + ChatColor.GOLD + "] ");
                break;
        }
        team.addEntry(player.getName());
    }

    private static String getRank(Player player) {
        return Main.getPlugin(Main.class).getRank(player);
    }

    private static ChatColor getPlusColor(Player player) {
        return Main.getPlugin(Main.class).getPlusColor(player);
    }

    public static String getRankPrefix(Player player) {
        switch (getRank(player)) {
            case "OWNER":
                return ChatColor.DARK_RED + "[OWNER] ";
            case "ADMIN":
                return ChatColor.RED + "[ADMIN] ";
            case "MODERATOR":
                return ChatColor.AQUA + "[MOD] ";
            case "HELPER":
                return ChatColor.GREEN + "[HELPER] ";
            case "YOUTUBE":
                return ChatColor.RED + "[" + ChatColor.WHITE + "YOUTUBE" + ChatColor.RED + "] ";
            case "TWITCH":
                return ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "TWITCH" + ChatColor.DARK_PURPLE + "] ";
            default:
                return ChatColor.GRAY + "";
            case "VIP":
                return ChatColor.GREEN + "[VIP] ";
            case "VIP+":
                return ChatColor.GREEN + "[VIP" + ChatColor.GOLD + "+" + ChatColor.GREEN + "] ";
            case "MVP":
                return ChatColor.AQUA + "[MVP] ";
            case "MVP+":
                return ChatColor.GREEN + "[MVP" + getPlusColor(player) + "+" + ChatColor.AQUA + "] ";
            case "MVP++":
                return ChatColor.GOLD + "[MVP" + getPlusColor(player) + "++" + ChatColor.GOLD + "] ";
        }
    }
}
