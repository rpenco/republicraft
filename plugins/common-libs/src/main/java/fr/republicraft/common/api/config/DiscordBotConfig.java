package fr.republicraft.common.api.config;

import lombok.Data;

@Data
public class DiscordBotConfig {
    String token;
    long channelId;
}
