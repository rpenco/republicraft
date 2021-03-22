package fr.republicraft.velocity.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import fr.republicraft.velocity.api.helpers.chat.ChatFormat;
import fr.republicraft.velocity.RepublicraftPlugin;
import fr.republicraft.velocity.managers.ChatManager;
import lombok.Getter;
import net.kyori.text.TextComponent;

public class ChatListener {
    @Getter
    final RepublicraftPlugin plugin;
    private final ChatManager chat;


    public ChatListener(RepublicraftPlugin plugin) {
        this.plugin = plugin;
        chat = ((ChatManager) plugin.getManagers().get(ChatManager.class));
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        chat.sendPublicServerToPlayersMessage(TextComponent.builder()
                .append(ChatFormat.e("["))
                .append(ChatFormat.e("-"))
                .append(ChatFormat.e("] "))
                .append(ChatFormat.e("Le serveur est en cours d'arrêt."))
                .build());
    }

    @Subscribe
    public void onPostLoginEvent(PostLoginEvent event) {
        chat.sendPublicServerToPlayersMessage(TextComponent.builder()
                .append(ChatFormat.t("["))
                .append(ChatFormat.s("+"))
                .append(ChatFormat.t("] "))
                .append(ChatFormat.t(event.getPlayer().getUsername()))
                .build());
    }

    @Subscribe
    public void onPlayerChat(PlayerChatEvent event) {
        event.setResult(PlayerChatEvent.ChatResult.denied());
        chat.chatPlayerToAll(event.getPlayer(), TextComponent.of(event.getMessage()));
    }

    @Subscribe
    public void onKickedFromServer(KickedFromServerEvent event) {
        if (event.getOriginalReason().isPresent()) {
            chat.sendPrivateServerToPlayerMessage(event.getPlayer(), TextComponent.builder().append(event.getOriginalReason().get()).build());
        } else {
            chat.sendPrivateServerToPlayerMessage(event.getPlayer(), ChatFormat.e("Le serveur t'a déconnecté!"));
        }
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        getPlugin().getReporter().create()
                .setEventName("DisconnectEvent")
                .setPlayer(event.getPlayer())
                .send();
        chat.sendPublicServerToPlayersMessage(TextComponent.builder()
                .append(ChatFormat.t("["))
                .append(ChatFormat.e("-"))
                .append(ChatFormat.t("] "))
                .append(ChatFormat.t(event.getPlayer().getUsername()))
                .build());
    }
}
