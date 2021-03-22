package fr.republicraft.common.api.dao.trade;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CurrentTradeItem {
    int id;
    String playerUniqueId;
    String itemId;
    int quantity;
    String category;
    double price;
    LocalDateTime saleDate;
    LocalDateTime buyDate;
    String buyerUniqueId;
}
