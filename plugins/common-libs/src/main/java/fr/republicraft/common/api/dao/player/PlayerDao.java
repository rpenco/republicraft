package fr.republicraft.common.api.dao.player;

import fr.republicraft.common.api.dao.Dao;
import fr.republicraft.common.api.jdbc.DBClient;
import org.javatuples.Pair;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class PlayerDao extends Dao {

    private static final String TABLE = "players";

    public PlayerDao(DBClient client) {
        super(client);
        migrate();
    }

    public void migrate() {
        try {
            client.execute(Stream.of(
                    "CREATE TABLE IF NOT EXISTS `" + TABLE + "` (\n" +
                            "`uuid` VARCHAR(50) NOT NULL,\n" +
                            "`username` VARCHAR(50) NOT NULL,\n" +
                            "`created_at` DATETIME NOT NULL,\n" +
                            "`balance` DOUBLE NOT NULL DEFAULT 0,\n" +
                            "`ban` BOOLEAN NOT NULL DEFAULT 0\n" +
                            ") ENGINE=Aria DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci"
            ));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<Profile> get(UUID uuid) {
        AtomicReference<Optional<Profile>> p = new AtomicReference<>(Optional.empty());
        try {
            client.get(TABLE, Stream.of("uuid", "username", "created_at", "balance", "ban"), Stream.of(Pair.with("uuid", uuid)), resultSet -> {
                if (resultSet.next()) {
                    Profile profile = new Profile();
                    profile.setUuid(UUID.fromString(resultSet.getString("uuid")));
                    profile.setUsername(resultSet.getString("username"));
                    profile.setBalance(resultSet.getDouble("balance"));
                    profile.setBanned(resultSet.getBoolean("ban"));
                    profile.setCreatedAt(resultSet.getObject("created_at", LocalDateTime.class));
                    p.set(Optional.of(profile));
                }
            });
            return p.get();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<Profile> get(String name) {
        AtomicReference<Optional<Profile>> p = new AtomicReference<>(Optional.empty());
        try {
            client.get(TABLE, Stream.of("uuid", "username", "created_at", "balance", "ban"), Stream.of(Pair.with("username", name)), resultSet -> {
                if (resultSet.next()) {
                    Profile profile = new Profile();
                    profile.setUuid(UUID.fromString(resultSet.getString("uuid")));
                    profile.setUsername(resultSet.getString("username"));
                    profile.setBalance(resultSet.getDouble("balance"));
                    profile.setBalance(resultSet.getDouble("ban"));
                    profile.setCreatedAt(resultSet.getObject("created_at", LocalDateTime.class));
                    p.set(Optional.of(profile));
                }
            });
            return p.get();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return p.get();
    }


    public long insert(Profile profile) {
        try {
            return client.insert(TABLE, Stream.of(
                    Pair.with("uuid", profile.getUuid()),
                    Pair.with("username", profile.getUsername()),
                    Pair.with("balance", profile.getBalance()),
                    Pair.with("created_at", LocalDateTime.now())));
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }


    public List<Profile> getPlayers(boolean withBanned) {
        List<Profile> items = new ArrayList<>();
        try {
            client.all(TABLE,
                    Stream.of("uuid", "username", "created_at", "balance", "ban"),
                    Stream.of(Pair.with("ban", withBanned)), resultSet -> {
                        while (resultSet.next()) {
                            Profile item = new Profile();
                            item.setUsername(resultSet.getString("username"));
                            item.setUuid(UUID.fromString(resultSet.getString("uuid")));
                            item.setBalance(resultSet.getDouble("balance"));
                            item.setBanned(resultSet.getBoolean("ban"));
                            item.setCreatedAt(resultSet.getObject("created_at", LocalDateTime.class));
                            items.add(item);
                        }
                    });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<Profile> getPlayers() {
        return getPlayers(false);
    }

}
