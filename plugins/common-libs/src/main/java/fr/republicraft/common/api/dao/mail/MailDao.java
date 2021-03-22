package fr.republicraft.common.api.dao.mail;


import fr.republicraft.common.api.jdbc.DBClient;

import java.util.UUID;

public class MailDao {

    private DBClient client;

    public MailDao(DBClient client) {
        this.client = client;
        setSchema();
    }

    public void setSchema() {

    }

    /**
     * Send mail to player
     *
     * @param sender
     * @param receiver
     * @param message
     * @param listener
     */
    public void send(UUID sender, UUID receiver, String message) {
//        client.write(__("INSERT INTO mails (uuid, uuid, message, date, status) VALUES ('{0}','{1}','{2}','{3}',{4});",
//                sender.toString(), receiver.toString(), message, "unread", new Date(System.currentTimeMillis())), result -> {
//            System.out.println("Insert new mail in database. result=" + result);
//            if (result == 1) {
//                listener.send(null);
//            }
//            throw new RuntimeException("Failed to save mail sender=" + sender + " returns code=" + result);
//        });
    }

    public void delete(UUID uniqueId, String position) {
    }

    public void list(UUID uniqueId) {
    }

    public void migrate() {

    }
}
