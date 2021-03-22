package fr.republicraft.velocity.config;


import fr.republicraft.common.api.config.CommonConfig;
import fr.republicraft.velocity.config.discord.DiscordConfig;
import fr.republicraft.velocity.config.jail.JailConfig;
import fr.republicraft.velocity.config.votes.VotesConfig;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Configuration du plugin de vote
 */
@Data
public class PluginConfig extends CommonConfig {

    /**
     * Discord configuration
     */
    DiscordConfig discord;

    /**
     * Votes configuration
     */
    VotesConfig votes;

    /**
     * Jail configuration
     */
    JailConfig jail;
}
