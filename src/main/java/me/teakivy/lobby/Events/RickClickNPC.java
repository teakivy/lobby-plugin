package me.teakivy.lobby.Events;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RickClickNPC extends Event implements Cancellable {

    private final Player player;
    private final EntityPlayer npc;
    private boolean isCancelled;
    private static final HandlerList HANDERS = new HandlerList();


    public RickClickNPC(Player player, EntityPlayer npc) {
        this.player = player;
        this.npc = npc;
    }

    public Player getPlayer() {
        return player;
    }

    public EntityPlayer getNPC() {
        return npc;
    }


    @Override
    public HandlerList getHandlers() {

        return HANDERS;
    }

    public static HandlerList getHandlerList() {

        return HANDERS;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        isCancelled = b;
    }
}
