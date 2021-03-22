package fr.republicraft.papermc.world.managers;

import fr.republicraft.common.api.managers.Manager;
import fr.republicraft.papermc.world.RepublicraftPlugin;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

import static org.bukkit.Bukkit.getServer;

public class NightManager extends Manager {

    private final RepublicraftPlugin plugin;
    private final Set<Player> voteFor = new HashSet<>();
    private BossBar nightBossBar;
    private int task;

    public NightManager(RepublicraftPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void start() {
        if (plugin.getWorldConfig().getBedThreshold() > 0) {
            nightBossBar = Bukkit.createBossBar("La nuit va être longue...", BarColor.WHITE, BarStyle.SOLID);
            nightBossBar.setVisible(false);
            plugin.getServer().getOnlinePlayers().forEach(player -> nightBossBar.addPlayer(player));
            voteFor.clear();

            task = getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
//                plugin.getSLF4JLogger().info("current time is={} forInBed={} playerInWorld={}",
//                        plugin.getWorld().getTime(), voteFor.size(), plugin.getWorld().getPlayerCount());

                if (!voteFor.isEmpty()) {

                    // no time to vote, clear votes
                    if (plugin.getWorld().getTime() < 12541) {
                        voteFor.clear();
                        if (nightBossBar.isVisible()) {
                            nightBossBar.setVisible(false);
                        }
                        return;
                    }

                    // votes is possible. compute.
                    double threshold = plugin.getWorldConfig().getBedThreshold();
                    double percent = (double) voteFor.size() / (double) nightBossBar.getPlayers().size();
                    double subpercent = percent / threshold;
                    nightBossBar.setVisible(true);

                    plugin.getSLF4JLogger().info("percent={} seuil={} subpercent={} inbed={} total={}", percent, threshold, subpercent, voteFor.size(), nightBossBar.getPlayers().size());

                    // adjust <= 1.0
                    subpercent = subpercent > 1 ? 1 : subpercent;
                    nightBossBar.setProgress(subpercent);
                    nightBossBar.setTitle(voteFor.size() + "/" + nightBossBar.getPlayers().size() + " joueurs ont voté pour passer la nuit (min requis. " + (threshold * 100) + "%)");
                    if (subpercent < 0.25) {
                        nightBossBar.setColor(BarColor.BLUE);
                    } else if (subpercent < 0.49) {
                        nightBossBar.setColor(BarColor.PURPLE);
                    } else if (subpercent < 0.75) {
                        nightBossBar.setColor(BarColor.YELLOW);
                    } else {
                        nightBossBar.setColor(BarColor.GREEN);
                    }

                    if (percent >= threshold) {
                        nightBossBar.setTitle(("Le seuil a été atteint, la nuit va passer..."));
                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            voteFor.clear();
                            nightBossBar.setVisible(false);
                            plugin.getWorld().setTime(0);
                        }, 40);
                    }
                }

            }, 0, 60);
        }
    }

    @Override
    public void stop() {
        if (plugin.getWorldConfig().getBedThreshold() > 0) {
            getServer().getScheduler().cancelTask(task);
            nightBossBar.removeAll();
            nightBossBar = null;
            voteFor.clear();
        }
    }

    public void playerQuit(Player player) {
        if (nightBossBar != null) {
            nightBossBar.removePlayer(player);
        }
    }

    public void playerJoin(Player player) {
        if (nightBossBar != null) {
            nightBossBar.addPlayer(player);
        }
    }

    public void onPlayerEnterBed(Player player) {
        if (nightBossBar != null) {
            voteFor.add(player);
        }
    }

}
