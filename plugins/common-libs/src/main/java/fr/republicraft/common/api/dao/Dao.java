package fr.republicraft.common.api.dao;

import fr.republicraft.common.api.jdbc.DBClient;
import lombok.Getter;

public abstract class Dao {

    @Getter
    protected DBClient client;

    public Dao(DBClient client) {
        this.client = client;
        migrate();
    }

    public void migrate() {

    }

    public void close() {
        if(client != null){
            client.close();
        }
    }
}
