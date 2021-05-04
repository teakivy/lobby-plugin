package me.teakivy.lobby.utils;

import com.mongodb.client.MongoCollection;
import me.teakivy.lobby.Main;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Ranks {

    static MongoCollection< Document > col = Main.getPlugin(Main.class).col;

    public static String getRank(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());
        return col.find(filter).first().getString("rank");
    }

    public String getRankFormatted(Player player) {
        String rank = getRank(player);

        switch (rank) {
            case "OWNER":
                return ChatColor.DARK_RED + "OWNER";
            case "ADMIN":
                return ChatColor.RED + "ADMIN";
            case "MODERATOR":
                return ChatColor.AQUA + "MOD";
            case "HELPER":
                return ChatColor.GREEN + "HELPER";
            case "YOUTUBE":
                return ChatColor.RED + "YOUTUBE";
            case "TWITCH":
                return ChatColor.DARK_PURPLE + "TWITCH";
            case "VIP":
                return ChatColor.GREEN + "VIP";
            case "VIP+":
                return ChatColor.GREEN + "VIP" + ChatColor.GOLD + "+";
            case "MVP":
                return ChatColor.AQUA + "MVP";
            case "MVP+":
                return ChatColor.AQUA + "MVP" + getPlusColor(player) + "+";
            case "MVP++":
                return ChatColor.GOLD + "MVP" + getPlusColor(player) + "++";
            default:
                return ChatColor.GRAY + "DEFAULT";
            case "DUCK":
                return ChatColor.GOLD + "DUCK";
        }
    }

    public static String getRankPriority(Player player) {
        String rank = getRank(player);

        switch (rank) {
            case "OWNER":
                return "a";
            case "ADMIN":
                return "b";
            case "MODERATOR":
                return "c";
            case "HELPER":
                return "d";
            case "YOUTUBE":
                return "e";
            case "TWITCH":
                return "f";
            case "VIP":
                return "l";
            case "VIP+":
                return "k";
            case "MVP":
                return "j";
            case "MVP+":
                return "i";
            case "MVP++":
                return "h";
            default:
                return "m";
            case "DUCK":
                return "g";
        }
//        return "";
    }


    public ChatColor getPlusColor(Player player) {
        Document filter = new Document("UUID", player.getUniqueId().toString());
        ChatColor plusColor = ChatColor.RED;

        switch(col.find(filter).first().getString("plusColor")) {
            case "RED":
                plusColor = ChatColor.RED;
                break;
            case "BLUE":
                plusColor = ChatColor.BLUE;
                break;
            case "AQUA":
                plusColor = ChatColor.AQUA;
                break;
            case "GOLD":
                plusColor = ChatColor.GOLD;
                break;
            case "YELLOW":
                plusColor = ChatColor.YELLOW;
                break;
            case "DARK_RED":
                plusColor = ChatColor.DARK_RED;
                break;
            case "DARK_BLUE":
                plusColor = ChatColor.DARK_BLUE;
                break;
            case "GRAY":
                plusColor = ChatColor.DARK_GRAY;
                break;
            case "WHITE":
                plusColor = ChatColor.WHITE;
                break;
            case "BLACK":
                plusColor = ChatColor.BLACK;
                break;
            case "LIME":
                plusColor = ChatColor.GREEN;
                break;
            case "GREEN":
                plusColor = ChatColor.DARK_GREEN;
                break;
            case "CYAN":
                plusColor = ChatColor.DARK_AQUA;
                break;
            case "PURPLE":
                plusColor = ChatColor.DARK_PURPLE;
                break;
        }

        return plusColor;
    }
}
