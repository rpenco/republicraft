package fr.republicraft.papermc.world.config.hologram;

import lombok.Getter;

import java.util.List;

@Getter
public class HologramConfig {
    String location;
    List<String> lines;
    private float yaw = 0;

}
