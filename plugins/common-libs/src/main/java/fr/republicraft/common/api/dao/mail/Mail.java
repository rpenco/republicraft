package fr.republicraft.common.api.dao.mail;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Mail {
    String id;
    String from;
    String to;
    String message;
    String status;
    LocalDateTime created;

    public String getFromUsername() {
        return null;
    }

    public String getId() {
        return null;
    }
}
