package fr.republicraft.velocity.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import fr.republicraft.velocity.RepublicraftPlugin;
import fr.republicraft.velocity.managers.ChatManager;
import fr.republicraft.velocity.managers.EconomyManager;
import lombok.Getter;

import static fr.republicraft.velocity.api.helpers.chat.ChatFormat.m;
import static fr.republicraft.velocity.api.helpers.chat.ChatFormat.t;

public class EconomyListener {

    @Getter
    final RepublicraftPlugin plugin;
    private final ChatManager chat;
    private final EconomyManager economyManager;

    public EconomyListener(RepublicraftPlugin plugin) {
        this.plugin = plugin;
        chat = ((ChatManager) plugin.getManagers().get(ChatManager.class));
        economyManager = ((EconomyManager) plugin.getManagers().get(EconomyManager.class));
    }

    @Subscribe
    public void onPostLoginEvent(PostLoginEvent event) {
        Player player = event.getPlayer();
        double balance = economyManager.getBalance(player.getUniqueId());
        chat.sendPrivateServerToPlayerMessage(player, t("Solde: " + m(balance) + " pièces."));
    }
}
