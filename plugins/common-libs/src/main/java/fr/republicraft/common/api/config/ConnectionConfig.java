package fr.republicraft.common.api.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ConnectionConfig {
    String host = "localhost";
    int port = 3306;
    String database;
    String username;
    String password;
    String prefix = "";

    String driverClassName = "com.mysql.cj.jdbc.Driver";
    int initialSize = 3;
    int maxTotal = 8;
    int minIdle = 3;
    int maxIdle = 8;
    long maxWaitMillis = 2000;
    int maxOpenPreparedStatements = 100;
}
