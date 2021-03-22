package fr.republicraft.papermc.shops.config;


import fr.republicraft.common.api.config.EntityConfig;
import lombok.Getter;

@Getter
public class ShopConfig {
    String id;
    String world;
    String location;
    EntityConfig entity;
}
