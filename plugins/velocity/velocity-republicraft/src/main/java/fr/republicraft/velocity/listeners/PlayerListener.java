package fr.republicraft.velocity.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import fr.republicraft.velocity.RepublicraftPlugin;
import fr.republicraft.velocity.managers.PlayerManager;
import lombok.Getter;

public class PlayerListener {

    @Getter
    final RepublicraftPlugin plugin;

    public PlayerListener(RepublicraftPlugin plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void onPostLoginEvent(PostLoginEvent event) {
        ((PlayerManager) getPlugin().getManagers().get(PlayerManager.class)).playerJoin(event.getPlayer());
    }


}
