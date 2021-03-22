package fr.republicraft.common.api.dao.jail;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class JailedPlayer {
    UUID uuid;
    String name;
    LocalDateTime jailDate;
    String reason;
}
