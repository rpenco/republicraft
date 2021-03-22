package fr.republicraft.common.api.dao.vote;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Vote {
    int id;
    String ip;
    String username;
    LocalDateTime dateTime;
}
