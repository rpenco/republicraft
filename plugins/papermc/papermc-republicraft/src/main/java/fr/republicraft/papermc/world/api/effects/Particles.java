package fr.republicraft.papermc.world.api.effects;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.World;

public class Particles {

    public static void displayRedstoneParticles(World world, double x, double y, double z){
        displayRedstoneParticles(world, x, y, z, Color.BLUE);
    }

    public static void displayRedstoneParticles(World world, double x, double y, double z, Color color){
        world.spawnParticle(Particle.REDSTONE, x, y, z, 0, 0, 0, 0, 1, new Particle.DustOptions(color, 1));
    }


    public static void displaySpellMobParticles(World world, double x, double y, double z, Color color){
        world.spawnParticle(Particle.FIREWORKS_SPARK, x, y, z, 0, 0, 0, 0,  new Particle.DustOptions(color, 1));
    }

//    public static void displaySpellMobParticles(World world, double x, double y, double z, Color color){
//        world.spawnParticle(Particle.BLOCK_CRACK, b.getLocation().add(0,1,0), 100, new MaterialData(Material.DIRT));
// }
}
