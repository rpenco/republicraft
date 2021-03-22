package fr.republicraft.velocity.config.votes.providers;

public class VotesProviderConfig {
    /**
     * Nom du serveur tier
     */
    String name;

    /**
     * Activer/Desactiver le serveur tier
     */
    boolean enabled;

    /**
     * URL de vote à afficher à l'utilisateur
     */
    String publicUrl;

    /**
     * Récompense du vote pour ce serveur
     */
    float moneyEarned;

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getPublicUrl() {
        return publicUrl;
    }

    public float getMoneyEarned() {
        return moneyEarned;
    }

}
