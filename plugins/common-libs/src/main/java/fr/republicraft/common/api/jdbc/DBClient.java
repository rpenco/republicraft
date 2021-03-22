package fr.republicraft.common.api.jdbc;


import fr.republicraft.common.api.config.ConnectionConfig;
import org.apache.commons.dbcp2.BasicDataSource;
import org.javatuples.Pair;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static fr.republicraft.common.api.helper.StringHelper.__;


/**
 * JDBC Client for plugins
 *
 * @author romain
 */
public class DBClient {
    private static final DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static BasicDataSource ds;
    private static Logger log;
    private final ConnectionConfig config;

    public DBClient(ConnectionConfig config, Logger logger) {
        this.config = config;
        log = logger;
    }

    public static String toDateTime(LocalDateTime date) {
        return date.format(sdf);
    }

    public Connection getConnection() throws SQLException {
        if (ds == null || ds.isClosed()) {
            open();
        }
        log.debug("message=\"get connection in pool\" event=\"sql\" active=\"" + ds.getNumActive() + "\" idle=\"" + ds.getNumIdle() + "\"");
        return ds.getConnection();
    }

    void open() {
        ds = new BasicDataSource();

        String url = __("jdbc:mysql://{0}:{1}/{2}",
                config.getHost(),
                Long.toString(config.getPort()),
                config.getDatabase());

        ds.setUrl(url);
        ds.setUsername(config.getUsername());
        ds.setPassword(config.getPassword());

        ds.setMinIdle(config.getMinIdle());
        ds.setMaxIdle(config.getMaxIdle());
        ds.setMaxTotal(config.getMaxTotal());
        ds.setInitialSize(config.getInitialSize());
        ds.setMaxWaitMillis(config.getMaxWaitMillis());
        ds.setMaxOpenPreparedStatements(config.getMaxOpenPreparedStatements());
        ds.setDriverClassName(config.getDriverClassName());
        log.info("message=\"connection to database\" jdbc=\"" + url + "\"");
    }

    public void execute(Stream<String> queries) throws SQLException {
        Connection connection = getConnection();
        try {
            queries.forEach(s -> {
                try {
                    Statement stmt = connection.createStatement();
                    stmt.execute(s);
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } finally {
            connection.close();
        }
    }

    public void execute(String query, ResultSetConsumer consumer) throws SQLException {
        Connection connection = getConnection();
        Statement stmt = connection.createStatement();
        try {
            ResultSet resultSet = stmt.executeQuery(query);
            if (consumer != null) {
                consumer.consume(resultSet);
                resultSet.close();
                connection.close();
            }
        } catch (SQLException e) {
            log.error("execute query error=" + query);
            throw e;
        } finally {
            stmt.close();
            connection.close();
        }
    }


    public long insert(String table, Stream<Pair<String, Object>> fieldValues) throws SQLException {
        List<Pair<String, Object>> fields = fieldValues.collect(Collectors.toList());

        String query = "INSERT INTO " + table + " (" +
                fields.stream().map(Pair::getValue0).collect(Collectors.joining(",")) + ") VALUES(" +
                fields.stream().map(objects -> "'" + objects.getValue1().toString() + "'").collect(Collectors.joining(",")) + ");";
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        try {
            if (statement.executeUpdate(query) == 0) {
                throw new SQLException("Insert failed. query=" + query);
            }
            return 0;
        } catch (SQLException e) {
            log.error("insert query error=" + query);
            throw e;
        } finally {
            statement.close();
            connection.close();
        }
    }

    public int update(String table, Stream<Pair<String, Object>> fieldsValues, Stream<Pair<String, Object>> wheres) throws SQLException {
        String query = "UPDATE " + table + " SET " +
                fieldsValues.map(t -> t.getValue0() + "='" + t.getValue1() + "'").collect(Collectors.joining(",")) + "";
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        try {

            String where = wheres.map(t -> t.getValue0() + "='" + t.getValue1() + "'").collect(Collectors.joining(","));
            if (where.length() > 0) {
                query += " WHERE " + where;
            }

            return statement.executeUpdate(query);
        } catch (SQLException e) {
            log.error("update query error=" + query);
            throw e;
        } finally {
            statement.close();
            connection.close();
        }
    }

    public boolean delete(String table, Stream<Pair<String, Object>> wheres) throws SQLException {
        String query = "DELETE FROM " + table + " WHERE " +
                wheres.map(t -> t.getValue0() + "='" + t.getValue1() + "'").collect(Collectors.joining(" AND "));

        Connection connection = getConnection();
        Statement stmt = connection.createStatement();
        try {
            return stmt.execute(query);
        } catch (SQLException e) {
            //TODO fixme not logged in error
            log.error("delete query error=" + query);
            throw e;
        } finally {
            stmt.close();
            connection.close();
        }
    }

    public void all(String table, Stream<String> fields, Stream<Pair<String, Object>> wheres, ResultSetConsumer consumer) throws SQLException {
        get(table, fields, wheres, consumer);
    }

    public void all(String table, Stream<String> fields, ResultSetConsumer consumer) throws SQLException {
        all(table, fields, Stream.of(), consumer);
    }

    public void all(String table, ResultSetConsumer consumer) throws SQLException {
        all(table, Stream.of("*"), Stream.of(), consumer);
    }

    public void get(String table, Stream<String> fields, Stream<Pair<String, Object>> wheres, ResultSetConsumer consumer) throws SQLException {

        String query = "SELECT " +
                fields.collect(Collectors.joining(",")) + " FROM " + table;

        String where = wheres.map(t -> {
            if (t.getValue1().toString().contains("IS")) {
                return t.getValue0() + " " + t.getValue1() + "";
            } else {
                return t.getValue0() + "='" + t.getValue1() + "'";
            }
        }).collect(Collectors.joining(" AND "));
        if (where.length() > 0) {
            query += " WHERE " + where;
        }

        Connection connection = getConnection();
        Statement stmt = connection.createStatement();

        try {
            ResultSet resultSet = stmt.executeQuery(query);
            if (consumer != null) {
                consumer.consume(resultSet);
                resultSet.close();
                connection.close();
            }
        } catch (SQLException e) {
            log.error("get query error=" + query);
            throw e;
        } finally {
            stmt.close();
            connection.close();
        }
    }

    public void close() {
        try {
            ds.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FunctionalInterface
    public interface ResultSetConsumer {
        void consume(ResultSet resultSet) throws SQLException;
    }

}
