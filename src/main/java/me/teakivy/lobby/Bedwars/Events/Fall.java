package me.teakivy.lobby.Bedwars.Events;

import com.mongodb.client.MongoCollection;
import me.teakivy.lobby.Bedwars.Bedwars;
import me.teakivy.lobby.Bedwars.util.BedwarsPlayer;
import me.teakivy.lobby.Main;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static com.mongodb.client.model.Updates.inc;

public class Fall implements Listener {

    static MongoCollection< Document > col = me.teakivy.lobby.Main.getPlugin(Main.class).col;

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!Bedwars.isInGame(player)) return;
        if (player.getLocation().getY() < 40) {
            if (BedwarsPlayer.hasBed(player)) {
                if (Bedwars.BedwarsLastPlayerDamage.get(player) != player) {
                    Bedwars.BedwarsKills.put(BedwarsPlayer.getOpponent(player), Bedwars.BedwarsKills.get(BedwarsPlayer.getOpponent(player)) + 1);
                    BedwarsPlayer.sendMessage(BedwarsPlayer.getChatColor(player) + player.getName() + ChatColor.GRAY + " was knocked into the void by " + BedwarsPlayer.getChatColor(BedwarsPlayer.getOpponent(player)) + BedwarsPlayer.getOpponent(player).getName() + ChatColor.GRAY + "!");
                    BedwarsPlayer.getOpponent(player).playSound(BedwarsPlayer.getOpponent(player).getLocation(), Sound.NOTE_PLING, 1, 1);
                    Document filter = new Document("UUID", player.getUniqueId().toString());
                    Bson updates = inc("stats.bedwars.deaths", 1);
                    col.findOneAndUpdate(filter, updates);

                    Document Killerfilter = new Document("UUID", BedwarsPlayer.getOpponent(player).getUniqueId().toString());
                    Bson Killerupdates = inc("stats.bedwars.kills", 1);
                    col.findOneAndUpdate(Killerfilter, Killerupdates);
                } else {
                    BedwarsPlayer.sendMessage(BedwarsPlayer.getChatColor(player) + player.getName() + ChatColor.GRAY + " fell into the void!");
                    Document filter = new Document("UUID", player.getUniqueId().toString());
                    Bson updates = inc("stats.bedwars.deaths", 1);
                    col.findOneAndUpdate(filter, updates);
                }
                player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1, 1);
                BedwarsPlayer.playerDeath(player, player);
            } else {
                if (Bedwars.BedwarsLastPlayerDamage.get(player) != player) {
                    Bedwars.BedwarsKills.put(BedwarsPlayer.getOpponent(player), Bedwars.BedwarsKills.get(BedwarsPlayer.getOpponent(player)) + 1);
                    BedwarsPlayer.sendMessage(BedwarsPlayer.getChatColor(player) + player.getName() + ChatColor.GRAY + " was final killed into the void by " + BedwarsPlayer.getChatColor(BedwarsPlayer.getOpponent(player)) + BedwarsPlayer.getOpponent(player).getName() + ChatColor.GRAY + "!");
                    BedwarsPlayer.getOpponent(player).playSound(BedwarsPlayer.getOpponent(player).getLocation(), Sound.NOTE_PLING, 1, 1);
                    Document filter = new Document("UUID", player.getUniqueId().toString());
                    Bson updates = inc("stats.bedwars.finalDeaths", 1);
                    col.findOneAndUpdate(filter, updates);

                    Document Killerfilter = new Document("UUID", BedwarsPlayer.getOpponent(player).getUniqueId().toString());
                    Bson Killerupdates = inc("stats.bedwars.finalKills", 1);
                    col.findOneAndUpdate(Killerfilter, Killerupdates);
                } else {
                    BedwarsPlayer.sendMessage(BedwarsPlayer.getChatColor(player) + player.getName() + ChatColor.GRAY + " fell as a final kill into the void!");
                }
                player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1, 1);
                BedwarsPlayer.endGame(player);
            }
        }
    }
}
