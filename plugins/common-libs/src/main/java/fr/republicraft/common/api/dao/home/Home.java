package fr.republicraft.common.api.dao.home;

import fr.republicraft.common.api.helper.FullLocation;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class Home {
    int id;
    UUID uuid;
    String name;
    LocalDateTime creationDate;
    String username;
    String material;
    String server;
    String world;
    float locationX;
    float locationY;
    float locationZ;
    float yaw;

    @Deprecated
    String location;


    @Deprecated
    public FullLocation getLegacyFulLocation() {
        return FullLocation.toFullLocation(location);
    }

    public FullLocation getFullLocation() {
        return new FullLocation(server, world, locationX, locationY, locationZ, yaw, material);
    }

    public void setFullLocation(FullLocation fullLocation) {
        server = fullLocation.getServer();
        world = fullLocation.getWorld();
        locationX = fullLocation.getX();
        locationY = fullLocation.getY();
        locationZ = fullLocation.getZ();
        material = fullLocation.getMaterial();
        yaw = fullLocation.getYaw();
    }

    public LocalDateTime getCreationDate() {
        return creationDate != null ? creationDate : LocalDateTime.now();
    }
}
