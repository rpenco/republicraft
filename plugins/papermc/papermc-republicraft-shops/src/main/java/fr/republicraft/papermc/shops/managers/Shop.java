package fr.republicraft.papermc.shops.managers;


import fr.republicraft.papermc.shops.config.ShopConfig;
import org.bukkit.entity.Entity;

public class Shop {
    Entity entity;
    ShopConfig config;

    public Shop(Entity entity, ShopConfig config) {
        this.entity = entity;
        this.config = config;
    }

    public Entity getEntity() {
        return entity;
    }

    public ShopConfig getConfig() {
        return config;
    }
}
