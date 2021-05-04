package me.teakivy.lobby.utils;

import me.teakivy.lobby.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class Scoreboards {

    public static void updateSumoScoreboard(Player player) {
        if (Main.getPlugin(Main.class).SumoGame.get(player)) {
            String time = Main.getPlugin(Main.class).SumoTimeFormatted[0];
            Player opponent;

            if (player == Main.getPlugin(Main.class).SumoPlayer1) {
                opponent = Main.getPlugin(Main.class).SumoPlayer2;
            } else {
                opponent = Main.getPlugin(Main.class).SumoPlayer1;
            }
            String OpponentName = Main.getPlugin(Main.class).rankedName(opponent);

            Scoreboard sumoBoard = player.getScoreboard();
            if (sumoBoard.getObjective("sumo") != null) {
                sumoBoard.getObjective("sumo").unregister();
            }

            Objective bObjective = sumoBoard.registerNewObjective("sumo", "dummy");
            bObjective.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "DUELS");
            bObjective.setDisplaySlot(DisplaySlot.SIDEBAR);


            Score s1 = bObjective.getScore("");
            s1.setScore(9);

            Score s2 = bObjective.getScore(ChatColor.WHITE + "Mode: " + ChatColor.GREEN + "Sumo              ");
            s2.setScore(8);
            Score s3 = bObjective.getScore(" ");
            s3.setScore(7);
            Score s4 = bObjective.getScore("Time Left: " + ChatColor.GREEN + time);
            s4.setScore(6);
            Score s5 = bObjective.getScore("  ");
            s5.setScore(5);
            Score s6 = bObjective.getScore(ChatColor.GOLD + ChatColor.BOLD.toString() + "OPPONENT");
            s6.setScore(4);
            Score s7 = bObjective.getScore(ChatColor.GRAY + OpponentName);
            s7.setScore(3);
            Score s8 = bObjective.getScore("   ");
            s8.setScore(2);
            Score s9 = bObjective.getScore(ChatColor.YELLOW + "youtube.com/teakivy");
            s9.setScore(1);
            player.setScoreboard(sumoBoard);
        }
    }


    public static void removeSumoScoreboard(Player player) {
        Scoreboard sumoBoard = player.getScoreboard();
        if (sumoBoard.getObjective("sumo") != null) {
            sumoBoard.getObjective("sumo").unregister();
        }
        player.setScoreboard(sumoBoard);
    }

    public static String getLocationFrom(Player player, Player target) {
//        Vector vector = target.getLocation().toVector().subtract(player.getLocation().toVector());
//        Vector playerDirection = player.getEyeLocation().getDirection();
//        double angle = vector.angle(playerDirection);
//        if ((angle * 180 / Math.PI) > 45 && (angle * 180 / Math.PI) < 136) return ">";
//        if ((angle * 180 / Math.PI) > 135 && (angle * 180 / Math.PI) < 226) return "v";
//        if ((angle * 180 / Math.PI) > 225 && (angle * 180 / Math.PI) < 316) return "<";
        return "^";
    }

    public static void updateClassicScoreboard(Player player) {
        if (Main.getPlugin(Main.class).ClassicGame.get(player)) {
            String time = Main.getPlugin(Main.class).ClassicTimeFormatted[0];
            Player opponent;

            if (player == Main.getPlugin(Main.class).ClassicPlayer1) {
                opponent = Main.getPlugin(Main.class).ClassicPlayer2;
            } else {
                opponent = Main.getPlugin(Main.class).ClassicPlayer1;
            }
            String OpponentName = Main.getPlugin(Main.class).rankedName(opponent);

            Scoreboard classicBoard = player.getScoreboard();
            if (classicBoard.getObjective("classic") != null) {
                classicBoard.getObjective("classic").unregister();
            }

            Objective bObjective = classicBoard.registerNewObjective("classic", "dummy");
            bObjective.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "DUELS");
            bObjective.setDisplaySlot(DisplaySlot.SIDEBAR);


            Score s1 = bObjective.getScore("");
            s1.setScore(9);

            Score s2 = bObjective.getScore(ChatColor.WHITE + "Mode: " + ChatColor.GREEN + "Classic             ");
            s2.setScore(8);
            Score s3 = bObjective.getScore(" ");
            s3.setScore(7);
            Score s4 = bObjective.getScore("Time Left: " + ChatColor.GREEN + time);
            s4.setScore(6);
            Score s5 = bObjective.getScore("  ");
            s5.setScore(5);
            Score s6 = bObjective.getScore(ChatColor.GOLD + ChatColor.BOLD.toString() + "OPPONENT");
            s6.setScore(4);
            Score s7 = bObjective.getScore(ChatColor.GRAY + OpponentName + ChatColor.WHITE + " | " + Math.round(opponent.getHealth()) + ChatColor.RED + " ❤");
            s7.setScore(3);
            Score s8 = bObjective.getScore("   ");
            s8.setScore(2);
            Score s9 = bObjective.getScore(ChatColor.YELLOW + "youtube.com/teakivy");
            s9.setScore(1);
            player.setScoreboard(classicBoard);
        }
    }


    public static void removeClassicScoreboard(Player player) {
        Scoreboard classicBoard = player.getScoreboard();
        if (classicBoard.getObjective("classic") != null) {
            classicBoard.getObjective("classic").unregister();
        }
        player.setScoreboard(classicBoard);
    }

    public static void updateLobbyScoreboard(Player player) {
        int players = Bukkit.getOnlinePlayers().size();
        String rank = new Ranks().getRankFormatted(player);

        Scoreboard lobbyBoard = player.getScoreboard();
        if (lobbyBoard.getObjective("lobby") != null) {
            lobbyBoard.getObjective("lobby").unregister();
        }

        Objective bObjective = lobbyBoard.registerNewObjective("lobby", "dummy");
        bObjective.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "TeakMC");
        bObjective.setDisplaySlot(DisplaySlot.SIDEBAR);



        Score s1 = bObjective.getScore("");
        s1.setScore(9);
        Score s2 = bObjective.getScore("Rank: " + rank);
        s2.setScore(8);
        Score s3 = bObjective.getScore("Level: " + ChatColor.GREEN + "0                 ");
        s3.setScore(7);
        Score s4 = bObjective.getScore("Party: " + ChatColor.GREEN + "None");
        s4.setScore(6);
        Score s5 = bObjective.getScore(" ");
        s5.setScore(5);
        Score s6 = bObjective.getScore("IP: " + ChatColor.GREEN + "play.teakmc.tk");
        s6.setScore(4);
        Score s7 = bObjective.getScore("Players: " + ChatColor.GREEN + players + "/" + Bukkit.getServer().getMaxPlayers());
        s7.setScore(3);
        Score s8 = bObjective.getScore("  ");
        s8.setScore(2);
        Score s9 = bObjective.getScore(ChatColor.YELLOW + "youtube.com/teakivy");
        s9.setScore(1);
        player.setScoreboard(lobbyBoard);
    }


    public static void removeLobbyScoreboard(Player player) {
        Scoreboard classicBoard = player.getScoreboard();
        if (classicBoard.getObjective("classic") != null) {
            classicBoard.getObjective("classic").unregister();
        }
        player.setScoreboard(classicBoard);
    }
}
