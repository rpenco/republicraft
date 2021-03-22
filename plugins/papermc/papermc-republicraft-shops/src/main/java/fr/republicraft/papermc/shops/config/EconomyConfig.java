package fr.republicraft.papermc.shops.config;

import fr.republicraft.papermc.shops.persistance.TradeItem;
import lombok.Getter;

import java.util.List;

/**
 * Configuration du plugin de vote
 */
@Getter
public class EconomyConfig {
    List<ShopConfig> shops;
    List<TradeItem> items;
}
