package fr.republicraft.papermc.shops.persistance;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TradeItem {

    double minPrice;
    double maxPrice;
    double price;
    String name;
    boolean enabled;
}
