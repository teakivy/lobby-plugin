package me.teakivy.lobby;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import me.teakivy.lobby.Events.RickClickNPC;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PacketReader {
//    PacketPlayInUseEntity
    Channel channel;
    public static Map<UUID, Channel> channels = new HashMap<UUID, Channel>();

    public void inject(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        channel = craftPlayer.getHandle().playerConnection.networkManager.channel;
        channels.put(player.getUniqueId(), channel);
        if (channel.pipeline().get("PacketInjector") != null) return;

        channel.pipeline().addAfter("decoder", "PacketInjector", new MessageToMessageDecoder<PacketPlayInUseEntity>() {
            @Override
            protected void decode(ChannelHandlerContext channel, PacketPlayInUseEntity packet, List<Object> arg) {
                arg.add(packet);
                readPacket(player, packet);

            }
        });
    }

    public void uninject(Player player) {
        channel = channels.get(player.getUniqueId());
        if (channel.pipeline().get("PacketInjector") != null) {
            channel.pipeline().remove("PacketInjector");
        }
    }

    public void readPacket(Player player, Packet<?> packet) {

        if (packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInUseEntity")) {
//            if (getValue(packet, "action").toString().equalsIgnoreCase("ATTACK")) return;
//            if (getValue(packet, "d").toString().equalsIgnoreCase("OFF_HAND")) return;

            int id = (int) getValue(packet, "a");

            if (getValue(packet, "action").toString().equalsIgnoreCase("INTERACT") || getValue(packet, "action").toString().equalsIgnoreCase("ATTACK")) {
                for (EntityPlayer npc : NPC.getNPCs()) {
                    if (npc.getId() == id) {
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
                            @Override
                            public void run() {
                                Bukkit.getPluginManager().callEvent(new RickClickNPC(player, npc));
                            }
                        }, 0);
                    }
                }
                for (EntityPlayer npc : NPC.getPlayerNPCs()) {
                    if (npc.getId() == id) {
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
                            @Override
                            public void run() {
                                Bukkit.getPluginManager().callEvent(new RickClickNPC(player, npc));
                            }
                        }, 0);
                    }
                }
            }
        }
    }

    private Object getValue(Object instance, String name) {
        Object result = null;

        try {

            Field field = instance.getClass().getDeclaredField(name);
            field.setAccessible(true);

            result = field.get(instance);

            field.setAccessible(false);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


}
