package fr.republicraft.velocity.votes;


import fr.republicraft.velocity.RepublicraftPlugin;
import fr.republicraft.velocity.config.votes.providers.ServerPriveProviderConfig;
import fr.republicraft.velocity.votes.serveurprive.ServeurPriveUpdater;
import lombok.Getter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class VotesTask {
    static final int START_TIME = 0;

    @Getter
    final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Getter
    final RepublicraftPlugin plugin;

    public VotesTask(RepublicraftPlugin plugin) {
        this.plugin = plugin;
    }

    public void run() {
        ServerPriveProviderConfig spProvider = getPlugin().getConfig().getVotes().getProviders().getServerprive();
        if (spProvider.isEnabled()) {
            if (spProvider.getRefreshInterval() > 0) {
                ServeurPriveUpdater spUpdater = new ServeurPriveUpdater(plugin, spProvider);
                scheduler.scheduleAtFixedRate(spUpdater::update, START_TIME, spProvider.getRefreshInterval(), TimeUnit.SECONDS);
                plugin.getLogger().info("message=\"votes updater enabled for provider\" provider=\"{}\" interval=\"{}\"",
                        spProvider.getName(),
                        spProvider.getRefreshInterval());
            } else {
                plugin.getLogger().warn("votes updater disabled because interval < 1 for provider={}", spProvider.getName());
            }
        } else {
            plugin.getLogger().warn("votes updater disabled because provider={} disabled", spProvider.getName());
        }

    }

    public void stop() {
        scheduler.shutdown();
    }
}
