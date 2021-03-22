package fr.republicraft.velocity.config.votes;


import fr.republicraft.common.api.config.ConnectionConfig;
import fr.republicraft.velocity.config.votes.providers.VotesProvidersConfig;

/**
 * Configuration du plugin de vote
 */
public class VotesConfig {
    /**
     * Plugin de vote est activé
     */
    boolean enabled;

    /**
     * Database connection settings
     */
    ConnectionConfig connection;

    /**
     * Liste des serveurs tiers
     */
    VotesProvidersConfig providers;


    public boolean isEnabled() {
        return enabled;
    }

    public VotesProvidersConfig getProviders() {
        return providers;
    }

    public ConnectionConfig getConnection() {
        return connection;
    }
}
