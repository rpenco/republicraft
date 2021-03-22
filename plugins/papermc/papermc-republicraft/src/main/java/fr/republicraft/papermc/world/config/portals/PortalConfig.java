package fr.republicraft.papermc.world.config.portals;


import fr.republicraft.common.api.config.EntityConfig;
import fr.republicraft.common.api.helper.FullLocation;
import lombok.Getter;
import org.bukkit.Location;

import static fr.republicraft.papermc.world.api.utils.StringUtils.parseLocation;

@Getter
public class PortalConfig {
    String id;
    String server;
    String world;
    String location;
    float yaw = 0;
    EntityConfig entity;


    public FullLocation getRemoteFullLocation() {
        FullLocation fullLoc = new FullLocation();
        fullLoc.setWorld(getWorld());
        fullLoc.setServer(getServer());
        fullLoc.setYaw(getYaw());

        Location loc = getLocation();
        if(loc != null) {
            fullLoc.setX(loc.getBlockX());
            fullLoc.setY(loc.getBlockY());
            fullLoc.setZ(loc.getBlockZ());
        }

        return fullLoc;
    }

    public Location getLocation() {
        return location != null? parseLocation(world, location) : null;
    }
}
