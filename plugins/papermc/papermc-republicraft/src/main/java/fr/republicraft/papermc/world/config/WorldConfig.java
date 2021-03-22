package fr.republicraft.papermc.world.config;

import fr.republicraft.common.api.config.CommonConfig;
import fr.republicraft.papermc.world.config.hologram.HologramConfig;
import fr.republicraft.papermc.world.config.portals.PortalConfig;
import fr.republicraft.papermc.world.config.spawn.SpawnConfig;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Configuration du plugin de vote
 */
@Data
@NoArgsConstructor
public class WorldConfig extends CommonConfig {

    String world = "world";

    /**
     * Spawn point location when join
     */
    SpawnConfig spawn;

    List<PortalConfig> portals;
    List<HologramConfig> holograms;

    List<String> entityDisabled;

    /**
     * bedThreshold for skip night...
     */
    double bedThreshold = 0;

}
