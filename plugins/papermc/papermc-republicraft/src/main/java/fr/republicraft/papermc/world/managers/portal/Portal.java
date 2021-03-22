package fr.republicraft.papermc.world.managers.portal;

import fr.republicraft.papermc.world.config.portals.PortalConfig;
import org.bukkit.entity.Entity;

public class Portal {
    PortalConfig config;

    Entity entity;

    public Portal(Entity entity, PortalConfig config) {
        this.entity = entity;
        this.config = config;
    }

    public Entity getEntity() {
        return entity;
    }

    public PortalConfig getConfig() {
        return config;
    }
}
