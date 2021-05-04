package me.teakivy.lobby.Bedwars.Events;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class Step implements Runnable{

    public static boolean foot = true;

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) //Loop through all the online players
        {
            if (!p.hasPotionEffect(PotionEffectType.INVISIBILITY)) continue;
            Location l = p.getLocation(); //Get the player's location
            l.setY(Math.floor(l.getY())); //Make sure the location's y is an integer

            if (!l.clone().subtract(0, 1, 0).getBlock().isEmpty()) //Get the block under the player's feet and make sure it exists (This prevents footprints from spawning in the air)
            {
                double x = Math.cos(Math.toRadians(p.getLocation().getYaw())) * 0.25d; //If you don't understand trigonometry, just think of it as rotating the footprints to the direction the player is looking.
                double y = Math.sin(Math.toRadians(p.getLocation().getYaw())) * 0.25d;

                if (foot) //This code just modifies the location with the rotation and the current foot
                    l.add(x, 0.025D, y);
                else
                    l.subtract(x, -0.025D, y);

                Bukkit.getWorld("world").spigot().playEffect(l, Effect.FOOTSTEP);
//                ParticleEffect.FOOTSTEP.display(0, 0, 0, 0, 2, l, 100); //And finally spawn the footprints in, increase the "2" argument for how dark/visible the footprints should be
            }
        }

        foot = !foot; //Switch to the other foot
    }
}
