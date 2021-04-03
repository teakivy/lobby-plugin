//package me.teakivy.lobby.NPC;
//
//import me.teakivy.lobby.Lobby;
//import net.jitse.npclib.NPCLib;
//import net.jitse.npclib.api.NPC;
//import net.jitse.npclib.api.events.NPCInteractEvent;
//import net.jitse.npclib.api.skin.MineSkinFetcher;
//import org.bukkit.Bukkit;
//import org.bukkit.ChatColor;
//import org.bukkit.Location;
//import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//import org.bukkit.event.player.PlayerJoinEvent;
//
//import javax.persistence.Lob;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
//public class NPCManager implements Listener {
//    private Lobby lobby;
//    private NPCLib npcLib;
//    private Map<Player, Boolean> ClickedNPC = new HashMap<>();
//
//    private NPC gameLobbyNPC;
//
//
//    public NPCManager(Lobby lobby) {
//        this.lobby = lobby;
//        this.npcLib = new NPCLib(lobby);
//        load();
//        Bukkit.getPluginManager().registerEvents(this, lobby);
//    }
//
//
//    private void load() {
//        int skinId = 277513;
//        MineSkinFetcher.fetchSkinFromIdAsync(skinId, skin -> {
//            gameLobbyNPC = npcLib.createNPC(Arrays.asList(ChatColor.GOLD + "Game Lobbies", ChatColor.GRAY + "Click To Connect!"));
//            gameLobbyNPC.setLocation(new Location(Bukkit.getWorld("world"), 74.5, 136, 12.5, -135, 0));
//            gameLobbyNPC.setSkin(skin);
//            gameLobbyNPC.create();
//        });
//    }
//
//    @EventHandler
//    public void onNPCInteract(NPCInteractEvent event) {
//        Player player = event.getWhoClicked();
//
//        if (!ClickedNPC.containsKey(player)) {
//            ClickedNPC.put(player, true);
//            if (event.getNPC() == gameLobbyNPC) {
//                player.sendMessage(ChatColor.GOLD + "Yay! You clicked me!");
//            }
//        } else {
//            ClickedNPC.remove(player);
//        }
//    }
//
//    @EventHandler
//    public void onJoin(PlayerJoinEvent event) {
////        Spawn NPC's
//
//        gameLobbyNPC.show(event.getPlayer());
//
//    }
//
//}
