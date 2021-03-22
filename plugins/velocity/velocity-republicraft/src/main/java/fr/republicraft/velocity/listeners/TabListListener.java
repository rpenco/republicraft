package fr.republicraft.velocity.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import fr.republicraft.velocity.RepublicraftPlugin;
import fr.republicraft.velocity.managers.TablistManager;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

public class TabListListener {
    @Getter
    final RepublicraftPlugin plugin;
    private final TablistManager tab;

    public TabListListener(RepublicraftPlugin plugin) {
        this.plugin = plugin;
        this.tab = ((TablistManager) getPlugin().getManagers().get(TablistManager.class));
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        getPlugin().getProxy().getScheduler().buildTask(plugin,
                () -> tab.playerQuit(event.getPlayer())).delay(2, TimeUnit.SECONDS).schedule();
    }

    @Subscribe
    public void onKickedFromServer(KickedFromServerEvent event) {
        getPlugin().getProxy().getScheduler().buildTask(plugin,
                () -> tab.playerQuit(event.getPlayer())).delay(2, TimeUnit.SECONDS).schedule();
    }

    @Subscribe
    public void ServerConnected(ServerConnectedEvent event) {
        getPlugin().getProxy().getScheduler().buildTask(plugin,
                () -> tab.playerJoin(event.getPlayer())).delay(2, TimeUnit.SECONDS).schedule();
    }

}
