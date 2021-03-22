package fr.republicraft.common.api.dao.trade;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TradeCategoryStats {
    int total;
    double minPrice;
    double maxPrice;
    String category;
}
