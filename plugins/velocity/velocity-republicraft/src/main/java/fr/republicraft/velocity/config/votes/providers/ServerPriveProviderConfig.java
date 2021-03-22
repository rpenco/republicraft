package fr.republicraft.velocity.config.votes.providers;

public class ServerPriveProviderConfig extends VotesProviderConfig {
    /**
     * URL de l'API pour valider le vote.
     */
    String apiUrl;

    /**
     * Token de l'API
     */
    String token;

    /**
     * Pool interval for checks votes. Disable scheduler when interval is prior or equal to 0.
     */
    int refreshInterval;

    public String getApiUrl() {
        return apiUrl;
    }

    public String getToken() {
        return token;
    }

    public long getRefreshInterval() {
        return refreshInterval;
    }
}
