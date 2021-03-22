package fr.republicraft.common.api.jdbc;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.mockito.Mockito.mock;

class DBClientTest {

    @Test
    public void testInsert() throws SQLException {
        DBClient dbClient = mock(DBClient.class);
//        when(dbClient, "getConnection").then()
//        dbClient.insert("test", Stream.of(Pair.with("key", "value")));
    }

}
