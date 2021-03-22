package fr.republicraft.common.api.dao.vote;

import fr.republicraft.common.api.dao.Dao;
import fr.republicraft.common.api.jdbc.DBClient;
import org.javatuples.Pair;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.stream.Stream;

public class VoteDao extends Dao {

    private static final String TABLE = "votes";

    public VoteDao(DBClient client) {
        super(client);
    }

    public void migrate() {
        try {
            client.execute(Stream.of("CREATE TABLE IF NOT EXISTS `" + TABLE + "` (\n" +
                    "\t`id` INT NOT NULL AUTO_INCREMENT,\n" +
                    "\t`player_username` VARCHAR(50) NOT NULL,\n" +
                    "\t`provider` VARCHAR(100) NOT NULL,\n" +
                    "\t`vote_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                    "\t`status` VARCHAR(20) NOT NULL,\n" +
                    "\tPRIMARY KEY (id)\n" +
                    ") ENGINE=Aria DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;"
            ));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public long insert(String username, String provider, LocalDateTime dateTime, String status) throws SQLException {
        return client.insert(TABLE, Stream.of(
                Pair.with("player_username", username),
                Pair.with("provider", provider),
                Pair.with("vote_date", dateTime),
                Pair.with("status", status)
        ));
    }
}
