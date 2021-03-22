package fr.republicraft.common.api.dao.vote;

import fr.republicraft.common.api.dao.Dao;
import fr.republicraft.common.api.jdbc.DBClient;
import org.javatuples.Pair;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SpVoteHistoryDao extends Dao {

    private static final String TABLE = "sp_vote_history";

    public SpVoteHistoryDao(DBClient client) {
        super(client);
    }

    public void migrate() {
        try {
            client.execute(Stream.of("CREATE TABLE IF NOT EXISTS `" + TABLE + "` (" +
                    "`id` INT(11) NOT NULL AUTO_INCREMENT," +
                    "`ip` varchar(45) COLLATE latin1_general_ci DEFAULT NULL," +
                    "`time` bigint(15) DEFAULT NULL," +
                    "`username` varchar(50) COLLATE latin1_general_ci DEFAULT NULL," +
                    "`datetime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    "PRIMARY KEY (id)" +
                    ") ENGINE=Aria DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;"
            ));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Vote> all() throws SQLException {
        List<Vote> votes = new ArrayList<>();
        client.all(TABLE, resultSet -> {
            while (resultSet.next()) {
                Vote vote = new Vote();
                vote.setId(resultSet.getInt("id"));
                vote.setUsername(resultSet.getString("username"));
                vote.setIp(resultSet.getString("ip"));
                vote.setDateTime(resultSet.getObject("datetime", LocalDateTime.class));
                votes.add(vote);
            }
        });
        return votes;
    }

    public List<Vote> fetchByIpAndTime(String ip, LocalDateTime time) throws SQLException {
        List<Vote> votes = new ArrayList<>();
        client.get(TABLE, Stream.of("id", "ip", "datetime", "username"), Stream.of(
                Pair.with("ip", ip),
                Pair.with("datetime", time)
        ), resultSet -> {
            while (resultSet.next()) {
                Vote vote = new Vote();
                vote.setId(resultSet.getInt("id"));
                vote.setUsername(resultSet.getString("username"));
                vote.setIp(resultSet.getString("ip"));
                vote.setDateTime(resultSet.getObject("datetime", LocalDateTime.class));
                votes.add(vote);
            }
        });
        return votes;
    }

    public boolean delete(long id) {
        try {
            return client.delete(TABLE, Stream.of(Pair.with("id", id)));
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    public long insert(String ip, LocalDateTime dateTime, String username) throws SQLException {
        return client.insert(TABLE, Stream.of(
                Pair.with("ip", ip),
                Pair.with("datetime", dateTime),
                Pair.with("username", username)));
    }
}
