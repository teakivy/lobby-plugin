package me.teakivy.lobby.Events;

import me.teakivy.lobby.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ClickNPC implements Listener {

    @EventHandler
    public void onClick(RickClickNPC event) {
        Player player = event.getPlayer();
        if (event.getNPC().getName().equalsIgnoreCase("DuelsSelector")) {
            Main.getPlugin(Main.class).openGameMenu(player);
        }
        if (event.getNPC().getName().equalsIgnoreCase("ProfileSelector")) {
            Main.getPlugin(Main.class).openProfileMenu(player);
        }
    }

}
