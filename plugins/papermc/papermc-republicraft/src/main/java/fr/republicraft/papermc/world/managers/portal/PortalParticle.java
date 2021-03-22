package fr.republicraft.papermc.world.managers.portal;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;

import java.util.ArrayList;
import java.util.List;

public class PortalParticle {
    long chunkId;
    Color color = Color.RED;
    /**
     * Block location
     */
    List<Location> locations = new ArrayList<>();

    /**
     * Update particle effect (called by an async task)
     */
    public void update() {
        for (Location location : locations) {
            if (location.getChunk().isLoaded()) {


//                location.getWorld().playEffect(new Location(location.getWorld(), location.getX(), location.getY(), location.getZ()), Effect.BLAZE_SHOOT,4096);
                location.getWorld().spawnParticle(Particle.SNOWBALL,new Location(location.getWorld(), location.getX(), location.getY(), location.getZ()),1);
//                location.getWorld().playEffect(new Location(location.getWorld(), location.getX(), location.getY(), location.getZ()), Effect.MOBSPAWNER_FLAMES, 2004);

//                location.getWorld()
//                        .spawnParticle(Particle.REDSTONE, location.getX(),
//                                location.getY(), location.getZ(), 0, 0, 0, 0,  1,
//                                new Particle.DustOptions(color, 1));

//                displaySpellMobParticles(location.getWorld(), location.getX(),
//                        location.getY(), location.getZ(), color);
            }
        }
    }

    public void addLocation(Location location) {
        locations.add(location);
        chunkId = location.getChunk().getChunkKey();
    }
}
