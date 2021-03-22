package fr.republicraft.common.api.dao.trade;

import fr.republicraft.common.api.dao.Dao;
import fr.republicraft.common.api.jdbc.DBClient;
import org.javatuples.Pair;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class TradeDao extends Dao {
    private static final String TABLE = "trade_items";

    public TradeDao(DBClient client) {
        super(client);
    }


    public void migrate() {
        try {
            client.execute(Stream.of(
                    "CREATE TABLE IF NOT EXISTS `" + TABLE + "` (" +
                            "`id` int(10) NOT NULL AUTO_INCREMENT," +
                            "`player_uuid` VARCHAR(100) NOT NULL," +
                            "`item_id` VARCHAR(100) NOT NULL," +
                            "`item_quantity` INT(10) NOT NULL," +
                            "`item_price` DOUBLE NOT NULL DEFAULT 0," +
                            "`sale_date` DATETIME  NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                            "`category` VARCHAR(50) NOT NULL," +
                            "`buyer_uuid` VARCHAR(100) DEFAULT NULL," +
                            "`buy_date` DATETIME DEFAULT NULL," +
                            "PRIMARY KEY (id)" +
                            ") ENGINE=Aria DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci"
            ));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public long sellItems(UUID uuid, String itemName, int size, double price, String category) {
        try {
            return client.insert(TABLE,
                    Stream.of(Pair.with("item_id", itemName), Pair.with("player_uuid", uuid),
                            Pair.with("item_quantity", size), Pair.with("item_price", price), Pair.with("category", category)));
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public long buyItem(UUID uuid, int itemId) {
        try {
            return client.update(TABLE,
                    Stream.of(Pair.with("buyer_uuid", uuid), Pair.with("buy_date", LocalDateTime.now())),
                    Stream.of(Pair.with("id", itemId)));
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Map<String, TradeCategoryStats> getCategoriesStats() {
        Map<String, TradeCategoryStats> stats = new HashMap<>();
        try {
            client.execute("SELECT category, COUNT(*) AS total, " +
                    "MIN(item_price) AS min_price, " +
                    "MAX(item_price) AS max_price FROM " + TABLE + " WHERE buyer_uuid IS NULL GROUP BY category", resultSet -> {
                while (resultSet.next()) {
                    TradeCategoryStats categoryStats = new TradeCategoryStats();
                    categoryStats.setCategory(resultSet.getString("category"));
                    categoryStats.setTotal(resultSet.getInt("total"));
                    categoryStats.setMinPrice(resultSet.getDouble("min_price"));
                    categoryStats.setMaxPrice(resultSet.getDouble("max_price"));
                    stats.put(categoryStats.getCategory(), categoryStats);
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }

    public List<CurrentTradeItem> getCurrentSales() {
        List<CurrentTradeItem> items = new ArrayList<>();
        try {
            client.all(TABLE,
                    Stream.of("id", "player_uuid", "item_id", "item_quantity", "item_price", "sale_date", "category"),
                    Stream.of(Pair.with("buyer_uuid", "IS NULL")),
                    resultSet -> {
                        while (resultSet.next()) {
                            CurrentTradeItem item = new CurrentTradeItem();
                            item.setId(resultSet.getInt("id"));
                            item.setPlayerUniqueId(resultSet.getString("player_uuid"));
                            item.setItemId(resultSet.getString("item_id"));
                            item.setQuantity(resultSet.getInt("item_quantity"));
                            item.setPrice(resultSet.getDouble("item_price"));
                            item.setCategory(resultSet.getString("category"));
                            item.setSaleDate(resultSet.getObject("sale_date", LocalDateTime.class));
                            items.add(item);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    public Optional<CurrentTradeItem> getItem(int tradeItemId) {
        AtomicReference<Optional<CurrentTradeItem>> optional = new AtomicReference<>(Optional.empty());
        try {
            client.get(TABLE, Stream.of("id", "player_uuid", "item_id", "item_quantity", "item_price", "sale_date", "category"),
                    Stream.of(Pair.with("id", tradeItemId)), resultSet -> {
                        if (resultSet.next()) {
                            CurrentTradeItem item = new CurrentTradeItem();
                            item.setId(resultSet.getInt("id"));
                            item.setPlayerUniqueId(resultSet.getString("player_uuid"));
                            item.setQuantity(resultSet.getInt("item_quantity"));
                            item.setItemId(resultSet.getString("item_id"));
                            item.setPrice(resultSet.getDouble("item_price"));
                            item.setCategory(resultSet.getString("category"));
                            item.setSaleDate(resultSet.getObject("sale_date", LocalDateTime.class));
                            optional.set(Optional.of(item));
                        }
                    });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return optional.get();
    }

    public void delete(int id) {
        try {
            client.delete(TABLE, Stream.of(Pair.with("id", id)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
