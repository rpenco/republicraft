package fr.republicraft.velocity.votes.serveurprive;

import fr.republicraft.common.api.dao.vote.Vote;
import fr.republicraft.common.api.helper.JsonHelper;
import fr.republicraft.common.api.net.LightHTTPClient;
import fr.republicraft.velocity.RepublicraftPlugin;
import fr.republicraft.velocity.config.votes.providers.ServerPriveProviderConfig;
import fr.republicraft.velocity.managers.VotesManager;
import net.kyori.text.TextComponent;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;

import static fr.republicraft.common.api.helper.StringHelper.__;

public class ServeurPriveUpdater {
    private final VotesManager manager;
    private final RepublicraftPlugin plugin;
    private final ServerPriveProviderConfig config;

    public ServeurPriveUpdater(RepublicraftPlugin plugin, ServerPriveProviderConfig config) {
        this.plugin = plugin;
        this.config = config;
        manager = (VotesManager) plugin.getManagers().get(VotesManager.class);
    }

    public void update() {
        plugin.getLogger().info("message=\"updating votes\" provider={} url={}", config.getName(), config.getApiUrl());
        manager.clearOldVotes();
        fetchVotes();
    }

    public void fetchVotes() {
        String url = __("{0}/vote/list/json/{1}/vote", config.getApiUrl(), config.getToken());
        String output = new LightHTTPClient().get(url);
        plugin.getLogger().info("message=\"fetched votes\" provider={} url={}", config.getName(), url);
        if ("".equals(output)) {
            plugin.getReporter().create()
                    .setEventName("VoteUpdaterEvent")
                    .setProperty("vote.update", 0)
                    .setProperty("vote.server.name", config.getName())
                    .setProperty("vote.server.url", url)
                    .send();
            return;
        }

        ServerPriveVotes votes = JsonHelper.fromJson(output, ServerPriveVotes.class);

        plugin.getReporter().create()
                .setEventName("VoteUpdaterEvent")
                .setProperty("vote.update", votes.getLastvotes().size())
                .send();

        for (ServerPriveVote lastvote : votes.getLastvotes()) {
            LocalDateTime datetime = LocalDateTime.ofInstant(Instant.ofEpochSecond(lastvote.getTime()), TimeZone
                    .getDefault().toZoneId());
            List<Vote> fetchedVotes = manager.fetchByIpAndTime(lastvote.getIp(), datetime);
            if (fetchedVotes.isEmpty()) {
                manager.insert(lastvote.getIp(), datetime, lastvote.getPseudo(), config.getName());
                plugin.getProxy().getPlayer(lastvote.getPseudo()).ifPresent(player -> {
                    plugin.getReporter().create()
                            .setEventName("PlayerVoteEvent")
                            .setProperty("vote.update", 1)
                            .setPlayer(player)
                            .setProperty("vote.ip", lastvote.getIp())
                            .setProperty("vote.datetime", lastvote.getTime())
                            .setProperty("vote.pseudo", lastvote.getPseudo())
                            .setProperty("vote.server.name", config.getName())
                            .setProperty("vote.server.url", url)
                            .send();
                    player.sendMessage(TextComponent.builder()
                            .append("Merci d'avoir voté pour " + config.getName())
                            .build());
                });
                if (!plugin.getProxy().getPlayer(lastvote.getPseudo()).isPresent()) {
                    plugin.getReporter().create()
                            .setEventName("PlayerVoteEvent")
                            .setProperty("vote.update", 1)
                            .setProperty("vote.ip", lastvote.getIp())
                            .setProperty("vote.datetime", lastvote.getTime())
                            .setProperty("vote.pseudo", lastvote.getPseudo())
                            .setProperty("vote.server.name", config.getName())
                            .setProperty("vote.server.url", url)
                            .send();
                }
            }
        }
    }
}

