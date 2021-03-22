package fr.republicraft.velocity.config.discord;

import fr.republicraft.common.api.config.DiscordBotConfig;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DiscordConfig  extends DiscordBotConfig {
    public String link;
    boolean enabled;
}
