package fr.republicraft.common.api.dao.jail;

import fr.republicraft.common.api.dao.Dao;
import fr.republicraft.common.api.jdbc.DBClient;
import org.javatuples.Pair;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class JailDao extends Dao {
    private static final String TABLE = "jail";

    public JailDao(DBClient client) {
        super(client);
    }


    public void migrate() {
        try {
            client.execute(Stream.of(
                    "CREATE TABLE IF NOT EXISTS `" + TABLE + "` (\n" +
                            "`player_uuid` VARCHAR(50) NOT NULL,\n" +
                            "`player_name` VARCHAR(50) NOT NULL,\n" +
                            "`jail_date` DATETIME NOT NULL,\n" +
                            "`reason` LONGTEXT NOT NULL\n" +
                            ") ENGINE=Aria DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci"
            ));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<JailedPlayer> getPlayer(UUID uuid) {
        AtomicReference<Optional<JailedPlayer>> jailPlayer = new AtomicReference<>(Optional.empty());
        try {
            client.get(TABLE, Stream.of("player_uuid", "player_name", "jail_date", "reason"),
                    Stream.of(Pair.with("player_uuid", uuid)), resultSet -> {
                        if (resultSet != null && resultSet.next()) {
                            JailedPlayer jp = new JailedPlayer();
                            jp.setUuid(UUID.fromString(resultSet.getString("player_uuid")));
                            jp.setJailDate(resultSet.getObject("jail_date", LocalDateTime.class));
                            jp.setReason(resultSet.getString("reason"));
                            jailPlayer.set(Optional.of(jp));
                        }
                    });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jailPlayer.get();
    }

    public long insert(UUID uuid, String name, String reason) {
        try {
            return client.insert(TABLE, Stream.of(
                    Pair.with("player_uuid", uuid),
                    Pair.with("player_name", name),
                    Pair.with("jail_date", LocalDateTime.now()),
                    Pair.with("reason", reason + "")));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean delete(UUID uniqueId) {
        try {
            client.delete(TABLE, Stream.of(Pair.with("player_uuid", uniqueId)));
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String name) {
        try {
            client.delete(TABLE, Stream.of(Pair.with("player_name", name)));
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
