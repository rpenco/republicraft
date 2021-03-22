package fr.republicraft.papermc.world.listeners;

import fr.republicraft.papermc.world.RepublicraftPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class WordListener implements Listener {

    private final RepublicraftPlugin plugin;

    public WordListener(RepublicraftPlugin plugin) {
        this.plugin = plugin;

    }
}
