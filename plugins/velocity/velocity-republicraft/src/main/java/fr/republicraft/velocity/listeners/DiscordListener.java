package fr.republicraft.velocity.listeners;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import fr.republicraft.common.api.discord.DiscordBotListener;
import fr.republicraft.velocity.RepublicraftPlugin;
import fr.republicraft.velocity.managers.ChatManager;
import fr.republicraft.velocity.managers.DiscordManager;
import lombok.Getter;

public class DiscordListener implements DiscordBotListener {
    @Getter
    final RepublicraftPlugin plugin;
    private final DiscordManager discord;


    public DiscordListener(RepublicraftPlugin plugin) {
        this.plugin = plugin;
        this.discord = ((DiscordManager) getPlugin().getManagers().get(DiscordManager.class));
    }

    @Subscribe(order = PostOrder.FIRST)
    public void onProxyShutdown(ProxyShutdownEvent event) {
        discord.sendProxyShutdown();
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        discord.sendProxyInitialize();
    }

    @Subscribe
    public void onPostLoginEvent(PostLoginEvent event) {
        discord.sendPlayerJoinMessage(event.getPlayer());
    }

    @Subscribe
    public void onPlayerChat(PlayerChatEvent event) {
        discord.setPlayerChatMessage(event.getPlayer(), event.getMessage());
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        discord.sendPlayerQuitMessage(event.getPlayer());
    }

    @Override
    public void onMessage(String name, String message) {
        ((ChatManager) getPlugin().getManagers().get(ChatManager.class)).chatExternalToAll(name, message);
    }
}
