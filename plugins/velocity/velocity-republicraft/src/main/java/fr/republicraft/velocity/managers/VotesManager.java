package fr.republicraft.velocity.managers;


import fr.republicraft.common.api.dao.vote.Vote;
import fr.republicraft.common.api.dao.vote.SpVoteHistoryDao;
import fr.republicraft.common.api.dao.vote.VoteDao;
import fr.republicraft.common.api.managers.Manager;
import fr.republicraft.velocity.RepublicraftPlugin;
import fr.republicraft.velocity.commands.VoteCommand;
import fr.republicraft.velocity.votes.VotesTask;
import lombok.Getter;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class VotesManager extends Manager {

    @Getter
    final RepublicraftPlugin plugin;
    @Getter
    SpVoteHistoryDao spVoteHistoryDao;

    @Getter
    VoteDao voteDao;

    @Getter
    VotesTask updater;

    public VotesManager(RepublicraftPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void start() {

        getPlugin().getProxy().getCommandManager().register(new VoteCommand(plugin), "votes", "vote");
        this.spVoteHistoryDao = new SpVoteHistoryDao(plugin.getClient());
        this.voteDao = new VoteDao(plugin.getClient());
        if (plugin.getConfig().getVotes().isEnabled()) {
            this.updater = new VotesTask(plugin);
            updater.run();
        }
    }

    @Override
    public void stop() {
        getPlugin().getProxy().getCommandManager().unregister("votes");
        if (updater != null) {
            updater.stop();
            updater = null;
        }
    }

    public void clearOldVotes() {
        try {
            spVoteHistoryDao.all().parallelStream().forEach(vote -> {
                long diffTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) - vote.getDateTime().toEpochSecond(ZoneOffset.UTC);
                if (diffTime > 300) {
                    plugin.getLogger().info("delete expired vote voteId={} username={}", vote.getId(), vote.getUsername());
                    spVoteHistoryDao.delete(vote.getId());
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Vote> fetchByIpAndTime(String ip, LocalDateTime time) {
        try {
            return spVoteHistoryDao.fetchByIpAndTime(ip, time);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insert(String ip, LocalDateTime dateTime, String pseudo, String provider) {
        try {
            spVoteHistoryDao.insert(ip, dateTime, pseudo);
            voteDao.insert(pseudo, provider, dateTime, "voted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
