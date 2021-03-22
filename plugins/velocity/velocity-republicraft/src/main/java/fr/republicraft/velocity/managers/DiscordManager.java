package fr.republicraft.velocity.managers;

import com.velocitypowered.api.proxy.Player;
import fr.republicraft.common.api.discord.DiscordBot;
import fr.republicraft.common.api.helper.DiscordWebhookHelper;
import fr.republicraft.common.api.managers.Manager;
import fr.republicraft.velocity.RepublicraftPlugin;
import fr.republicraft.velocity.commands.DiscordCommand;
import fr.republicraft.velocity.listeners.DiscordListener;
import lombok.Data;

import javax.security.auth.login.LoginException;

import static fr.republicraft.common.api.helper.StringHelper.__;

@Data
public class DiscordManager extends Manager {
    static DiscordWebhookHelper webhook;
    final RepublicraftPlugin plugin;
    String webhookURL;
    String link;
    boolean enabled;

    DiscordBot bot;
    private DiscordListener listener;

    /**
     * Constructeur privé
     */
    public DiscordManager(RepublicraftPlugin plugin) {
        this.plugin = plugin;
    }


    private void sendBotMessage(String message) {
        if (plugin.getConfig().getDiscord().isEnabled()) {
            bot.sendMessage(message);
        } else {
            plugin.getLogger().info("message=\"discord is disabled\"");
        }
    }

    public void sendMessage(String message) {
        try {
            plugin.getProxy().getScheduler().buildTask(plugin, () -> {
                sendBotMessage(message);
            }).schedule();
        }catch (Exception e){
            sendBotMessage(message);
        }
    }

    public void setPlayerChatMessage(Player player, String message) {
        sendMessage(__("**{0}** {1}", player.getUsername(), message));
    }

    public void sendPlayerJoinMessage(Player player) {
        sendMessage(":green_circle: " + player.getUsername());
    }

    public void sendPlayerWelcomeMessage(Player player) {
        sendMessage("Bienvenue sur Républicraft " + player.getUsername() + "!");
    }

    public void sendPlayerQuitMessage(Player player) {
        sendMessage(":red_circle: " + player.getUsername());
    }

    public void sendProxyShutdown() {
        sendMessage(":red_square: le serveur est actuellement down.");
    }

    public void sendProxyInitialize() {
        sendMessage(":green_square: le serveur est actuellement up.");
    }

    @Override
    public void start() {
        listener = new DiscordListener(plugin);
        plugin.getProxy().getEventManager().register(plugin, listener);
        plugin.getProxy().getCommandManager().register(new DiscordCommand(plugin), "discord");
        if (plugin.getConfig().getDiscord().isEnabled()) {
            bot = new DiscordBot(plugin.getConfig().getDiscord(), listener);
            try {
                bot.start();
                sendProxyInitialize();
            } catch (InterruptedException | LoginException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stop() {
        plugin.getProxy().getCommandManager().unregister("discord");
        plugin.getProxy().getEventManager().unregisterListener(plugin, listener);
        if (bot != null) {
            bot.stop();
        }
    }

}
