package fr.republicraft.velocity.managers;

import fr.republicraft.common.api.managers.Manager;
import fr.republicraft.velocity.RepublicraftPlugin;
import fr.republicraft.velocity.listeners.ProxyListener;
import lombok.Getter;

public class ProxyManager extends Manager {
    @Getter
    private final RepublicraftPlugin plugin;
    private ProxyListener listener;

    public ProxyManager(RepublicraftPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void start() {
        listener = new ProxyListener(getPlugin());
        getPlugin().getProxy().getEventManager().register(getPlugin(), listener);
    }

    @Override
    public void stop() {
        getPlugin().getProxy().getEventManager().unregisterListener(getPlugin(), listener);
    }
}
