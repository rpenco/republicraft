package fr.republicraft.velocity.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.scheduler.Scheduler;
import fr.republicraft.common.api.dao.jail.JailDao;
import fr.republicraft.common.api.dao.jail.JailedPlayer;
import fr.republicraft.common.api.helper.FullLocation;
import fr.republicraft.common.api.helper.SimpleLocation;
import fr.republicraft.velocity.api.helpers.chat.ChatFormat;
import fr.republicraft.velocity.RepublicraftPlugin;
import fr.republicraft.velocity.managers.ChatManager;
import lombok.Getter;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static fr.republicraft.velocity.api.helpers.tp.TeleportHelper.tp;

public class JailListener {
    @Getter
    final RepublicraftPlugin plugin;

    @Getter
    final JailDao jailDao;


    public JailListener(RepublicraftPlugin plugin) {
        this.plugin = plugin;
        jailDao = new JailDao(plugin.getClient());
    }
    @Subscribe
    public void onPlayerChat(PlayerChatEvent event) {
        //TODO si message contients insulte => jail auto
    }
    @Subscribe
    public void onServerConnected(ServerConnectedEvent event) {
        getPlugin().getReporter().create()
                .setEventName("ServerConnectedEvent")
                .setPlayer(event.getPlayer())
                .setServer(event.getServer())
                .send();

        if (getPlugin().getConfig().getJail().isEnabled()) {
            Scheduler.TaskBuilder task = getPlugin().getProxy().getScheduler().buildTask(getPlugin(), () -> {
                Optional<JailedPlayer> jailDaoPlayer = jailDao.getPlayer(event.getPlayer().getUniqueId());
                if (jailDaoPlayer.isPresent()) {
                    ((ChatManager) plugin.getManagers().get(ChatManager.class))
                            .sendPrivateServerToPlayerMessage(event.getPlayer(),
                                    ChatFormat.e("Tu es actuellement en prison."));

                    FullLocation fl = new FullLocation();
                    fl.setServer(getPlugin().getConfig().getJail().getServer());
                    fl.setWorld(getPlugin().getConfig().getJail().getWorld());
                    SimpleLocation sl = SimpleLocation.fromString(getPlugin().getConfig().getJail().getJailLocation());
                    fl.fromSimpleLocation(sl);
                    tp(getPlugin(), event.getPlayer(), fl);

                }
            });
            task.delay(1, TimeUnit.SECONDS).schedule();
        }
    }
}
