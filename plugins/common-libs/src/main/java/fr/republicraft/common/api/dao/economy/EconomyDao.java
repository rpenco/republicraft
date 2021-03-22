package fr.republicraft.common.api.dao.economy;

import fr.republicraft.common.api.dao.Dao;
import fr.republicraft.common.api.jdbc.DBClient;
import org.javatuples.Pair;

import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class EconomyDao extends Dao {

    private static final String TABLE = "players";

    public EconomyDao(DBClient client) {
        super(client);
    }

    public void migrate() {

    }


    public void setBalance(UUID uuid, double balance) {
        try {
            client.update(TABLE, Stream.of(Pair.with("balance", balance)), Stream.of(Pair.with("uuid", uuid)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Short update balance
     *
     * @param uuid
     * @param balance exemple : "+{value}"
     */
    public void updateBalance(UUID uuid, String balance) {
        try {
            client.execute(Stream.of("UPDATE " + TABLE + " SET balance = balance" + balance + " WHERE uuid='" + uuid + "'"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Short update balance
     *
     * @param playerName
     * @param balance exemple : "+{value}"
     */
    public void updateBalance(String playerName, String balance) {
        try {
            client.execute(Stream.of("UPDATE " + TABLE + " SET balance = balance" + balance + " WHERE username='" + playerName + "'"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //
    public double getBalance(UUID uuid) {
        AtomicReference<Double> balance = new AtomicReference<>(0.0);
        try {
            client.get(TABLE, Stream.of("balance"), Stream.of(Pair.with("uuid", uuid)), resultSet -> {
                if (resultSet.next()) {
                    balance.set(resultSet.getDouble("balance"));
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance.get();
    }
//
//    public void updateBalance(String playerName, double moneyEarned, OnUpdateListener listener) {
//        client.write(__("UPDATE players SET balance = balance + {0} WHERE username='{1}';",
//                moneyEarned, playerName), listener);
//    }
//
//    public void updateBalanceSync(String playerName, double moneyEarned) {
//        client.writeSync(__("UPDATE players SET balance = balance + {0} WHERE username='{1}';",
//                moneyEarned, playerName));
//    }
}
