package fr.republicraft.common.api.dao.home;

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


public class HomeDao extends Dao {
    private static final String TABLE = "homes";

    public HomeDao(DBClient client) {
        super(client);
    }

    public void migrate() {
        try {
            client.execute(Stream.of(
                    "CREATE TABLE IF NOT EXISTS `" + TABLE + "` (" +
                            "`id` int(11) NOT NULL AUTO_INCREMENT," +
                            "`uuid` VARCHAR(100) NOT NULL," +
                            "`name` VARCHAR(100) NOT NULL," +
                            "`username` VARCHAR(100) NOT NULL," +
                            "`location_x` DOUBLE NOT NULL," +
                            "`location_y` DOUBLE NOT NULL," +
                            "`location_z` DOUBLE NOT NULL," +
                            "`yaw` DOUBLE NOT NULL," +
                            "`server` VARCHAR(50) NOT NULL," +
                            "`world` VARCHAR(50) NOT NULL," +
                            "`material` VARCHAR(50)," +
                            "`creation_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                            "PRIMARY KEY (id)" +
                            ") ENGINE=Aria DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci"
            ));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Home> all(UUID uuid) {
        List<Home> homes = new ArrayList<>();
        try {
            client.get(TABLE, Stream.of("id", "uuid", "name", "username", "location_x",
                    "location_y", "location_z", "yaw", "server", "world", "material", "creation_date"), Stream.of(Pair.with("uuid", uuid)), resultSet -> {
                if (resultSet != null) {
                    while (resultSet.next()) {
                        Home home = new Home();
                        home.setId(resultSet.getInt("id"));
                        home.setUuid(UUID.fromString(resultSet.getString("uuid")));
                        home.setName(resultSet.getString("name"));
                        home.setUsername(resultSet.getString("username"));
                        home.setMaterial(resultSet.getString("material"));
                        home.setServer(resultSet.getString("server"));
                        home.setWorld(resultSet.getString("world"));
                        home.setLocationX(resultSet.getFloat("location_x"));
                        home.setLocationY(resultSet.getFloat("location_y"));
                        home.setLocationZ(resultSet.getFloat("location_z"));
                        home.setYaw(resultSet.getFloat("yaw"));
                        home.setCreationDate(resultSet.getObject("creation_date", LocalDateTime.class));
                        homes.add(home);
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return homes;
    }

    public List<Home> all() {
        List<Home> homes = new ArrayList<>();
        try {
            client.get(TABLE, Stream.of("id", "uuid", "name", "username", "location_x",
                    "location_y", "location_z", "yaw", "server", "world", "material", "creation_date"), Stream.of(), resultSet -> {
                if (resultSet != null) {
                    while (resultSet.next()) {
                        Home home = new Home();
                        home.setId(resultSet.getInt("id"));
                        home.setUuid(UUID.fromString(resultSet.getString("uuid")));
                        home.setName(resultSet.getString("name"));
                        home.setUsername(resultSet.getString("username"));
                        home.setMaterial(resultSet.getString("material"));
                        home.setServer(resultSet.getString("server"));
                        home.setWorld(resultSet.getString("world"));
                        home.setLocationX(resultSet.getFloat("location_x"));
                        home.setLocationY(resultSet.getFloat("location_y"));
                        home.setLocationZ(resultSet.getFloat("location_z"));
                        home.setYaw(resultSet.getFloat("yaw"));
                        home.setCreationDate(resultSet.getObject("creation_date", LocalDateTime.class));
                        homes.add(home);
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return homes;
    }


    public Optional<Home> get(UUID uuid, String name) {
        AtomicReference<Optional<Home>> opHome = new AtomicReference<>(Optional.empty());
        try {
            client.get(TABLE, Stream.of("id", "uuid", "name", "username", "location_x",
                    "location_y", "location_z", "yaw", "server", "world", "material", "creation_date"),
                    Stream.of(Pair.with("uuid", uuid), Pair.with("name", name)), resultSet -> {
                        if (resultSet.next()) {
                            Home home = new Home();
                            home.setId(resultSet.getInt("id"));
                            home.setUuid(UUID.fromString(resultSet.getString("uuid")));
                            home.setName(resultSet.getString("name"));
                            home.setUsername(resultSet.getString("username"));
                            home.setMaterial(resultSet.getString("material"));
                            home.setServer(resultSet.getString("server"));
                            home.setWorld(resultSet.getString("world"));
                            home.setLocationX(resultSet.getFloat("location_x"));
                            home.setLocationY(resultSet.getFloat("location_y"));
                            home.setLocationZ(resultSet.getFloat("location_z"));
                            home.setYaw(resultSet.getFloat("yaw"));
                            home.setCreationDate(resultSet.getObject("creation_date", LocalDateTime.class));
                            opHome.set(Optional.of(home));
                        }
                    });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return opHome.get();
    }

    public long insert(Home home) {
        try {
            return client.insert(TABLE, Stream.of(
                    Pair.with("uuid", home.getUuid()),
                    Pair.with("name", home.getName()),
                    Pair.with("username", home.getUsername()),
                    Pair.with("location_x", home.getLocationX()),
                    Pair.with("location_y", home.getLocationY()),
                    Pair.with("location_z", home.getLocationZ()),
                    Pair.with("yaw", home.getYaw()),
                    Pair.with("server", home.getServer()),
                    Pair.with("world", home.getWorld()),
                    Pair.with("material", home.getMaterial()),
                    Pair.with("creation_date", home.getCreationDate()))
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean delete(UUID uniqueId, String name) {
        try {
            return client.delete(TABLE, Stream.of(Pair.with("uuid", uniqueId), Pair.with("name", name)));
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    public void update(Home home) {
        try {
            client.update(TABLE, Stream.of(
                    Pair.with("uuid", home.getUuid()),
                    Pair.with("name", home.getName()),
                    Pair.with("username", home.getUsername()),
                    Pair.with("location_x", home.getLocationX()),
                    Pair.with("location_y", home.getLocationY()),
                    Pair.with("location_z", home.getLocationZ()),
                    Pair.with("yaw", home.getYaw()),
                    Pair.with("server", home.getServer()),
                    Pair.with("world", home.getWorld()),
                    Pair.with("material", home.getMaterial()),
                    Pair.with("creation_date", home.getCreationDate())), Stream.of(Pair.with("id", home.getId())));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
