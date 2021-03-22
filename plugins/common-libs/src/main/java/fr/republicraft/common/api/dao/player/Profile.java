package fr.republicraft.common.api.dao.player;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
public class Profile {
    private UUID uuid;
    private double balance;
    private String username;
    private LocalDateTime createdAt;
    private Boolean banned;
}
