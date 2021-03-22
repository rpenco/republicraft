package fr.republicraft.common.api.dao.syncinv;

import fr.republicraft.common.api.dao.Dao;
import fr.republicraft.common.api.jdbc.DBClient;
import org.javatuples.Pair;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class SyncInvDao extends Dao {


    private static final String TABLE = "sync_inventory";

    public SyncInvDao(DBClient client) {
        super(client);
        migrate();
    }

    public void migrate() {
//        FIXME no id field need ! uuid instead!
        try {
            client.execute(Stream.of(
                    "CREATE TABLE IF NOT EXISTS `" + TABLE + "` (" +
                            "id int(10) AUTO_INCREMENT, " +
                            "player_uuid char(36) NOT NULL UNIQUE, " +
                            "inventory LONGTEXT NOT NULL, " +
                            "armor LONGTEXT NOT NULL, " +
                            "game_mode varchar(10) NOT NULL, " +
                            "food_level int(10) NOT NULL, " +
                            "health DOUBLE NOT NULL ," +
                            "total_exp int(10) DEFAULT 0," +
                            "levels int(10) DEFAULT 0," +
                            "exp float DEFAULT 0," +
                            "PRIMARY KEY(id));",
                    "ALTER TABLE `" + TABLE + "` " +
                            // set nullable column for new sync version
                            "MODIFY `inventory` LONGTEXT NULL," +
                            "MODIFY `armor` LONGTEXT NULL," +
                            "MODIFY `game_mode` varchar(10) NULL," +
                            "MODIFY `food_level` int(10) NULL," +
                            "MODIFY `health` DOUBLE NULL," +
                            "MODIFY `total_exp` int(10) NULL," +
                            "MODIFY `levels` int(10) NULL," +
                            "MODIFY `exp` float NULL," +
                            // new sync version
                            "ADD IF NOT EXISTS`data_profile` LONGTEXT NOT NULL"
            ));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public long insert(UUID uuid, InventoryData data) {
        try {
            return client.insert(TABLE, Stream.of(
                    Pair.with("player_uuid", uuid),
                    Pair.with("data_profile", data.getData()))
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int update(UUID uuid, InventoryData data) {
        try {
            return client.update(TABLE, Stream.of(
                    Pair.with("data_profile", data.getData()),
                    // migrate account for null value
                    Pair.with("inventory", ""),
                    Pair.with("armor", ""),
                    Pair.with("game_mode", ""),
                    Pair.with("food_level", 0),
                    Pair.with("health", 0),
                    Pair.with("total_exp", 0),
                    Pair.with("levels", 0),
                    Pair.with("exp", 0)
                    ),
                    Stream.of(Pair.with("player_uuid", uuid)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Optional<InventoryData> get(UUID uuid) {
        AtomicReference<Optional<InventoryData>> data = new AtomicReference<>(Optional.empty());
        try {
            client.get(
                    TABLE,
                    Stream.of("data_profile",
                            // legacy fields
                            "inventory", "armor", "game_mode", "food_level", "health", "total_exp", "levels", "exp"),
                    Stream.of(Pair.with("player_uuid", uuid)), resultSet -> {
                        while (resultSet.next()) {
                            InventoryData inv = new InventoryData();
                            // new inventory
                            inv.setData(resultSet.getString("data_profile"));

                            if (resultSet.getString("inventory") != null) {
                                // keep legacy inventory will be migrated when player quit
                                inv.setRawInventory(resultSet.getString("inventory"));
                                inv.setRawArmor(resultSet.getString("armor"));
                                inv.setFoodLevel(resultSet.getInt("food_level"));
                                inv.setHealth(resultSet.getDouble("health"));
                                inv.setGameMode(resultSet.getString("game_mode"));
                                inv.setTotalExperience(resultSet.getInt("total_exp"));
                                inv.setLevels(resultSet.getInt("levels"));
                                inv.setExp(resultSet.getFloat("exp"));
                            }
                            data.set(Optional.of(inv));
                        }
                    });

            return data.get();
        } catch (Exception e) {
            System.err.println("failed to read inventory sync. " + e.getMessage());
            e.printStackTrace();
            return data.get();
        }
    }
}
