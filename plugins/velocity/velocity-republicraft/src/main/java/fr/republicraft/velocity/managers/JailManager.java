package fr.republicraft.velocity.managers;

import com.velocitypowered.api.proxy.Player;
import fr.republicraft.common.api.dao.jail.JailDao;
import fr.republicraft.common.api.dao.jail.JailedPlayer;
import fr.republicraft.common.api.helper.FullLocation;
import fr.republicraft.common.api.helper.SimpleLocation;
import fr.republicraft.common.api.managers.Manager;
import fr.republicraft.velocity.api.helpers.chat.ChatFormat;
import fr.republicraft.velocity.RepublicraftPlugin;
import fr.republicraft.velocity.commands.JailCommand;
import fr.republicraft.velocity.listeners.JailListener;
import net.kyori.text.TextComponent;

import java.util.Optional;
import java.util.UUID;

import static fr.republicraft.velocity.api.helpers.tp.TeleportHelper.tp;

public class JailManager extends Manager {
    final RepublicraftPlugin plugin;
    private JailDao jailDao;
    private ChatManager chat;
    private JailListener listener;

    public JailManager(RepublicraftPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void start() {
        chat = ((ChatManager) plugin.getManagers().get(ChatManager.class));
        jailDao = new JailDao(plugin.getClient());
        jailDao.migrate();
        plugin.getProxy().getCommandManager().register(new JailCommand(plugin), "jail");
        listener = new JailListener(plugin);
        plugin.getProxy().getEventManager().register(plugin, listener);
    }

    @Override
    public void stop() {
        plugin.getProxy().getCommandManager().unregister("jail");
        plugin.getProxy().getEventManager().unregisterListener(plugin, listener);
        jailDao.close();
    }


    public Optional<JailedPlayer> isInJail(UUID uuid) {
        return jailDao.getPlayer(uuid);
    }

    public boolean putInJail(Player jailer, Player jailed) {
        if (!isInJail(jailed.getUniqueId()).isPresent()) {
            jailDao.insert(jailed.getUniqueId(), jailed.getUsername(), "");
            chat.sendPublicServerToPlayersMessage(TextComponent.builder()
                    .append(ChatFormat.h(jailer.getUsername()))
                    .append(ChatFormat.g(" a placé "))
                    .append(ChatFormat.e(jailed.getUsername()))
                    .append(ChatFormat.g(" en prison."))
                    .build());

            // try to tp in jail
            FullLocation jailLocation = new FullLocation();
            jailLocation.setServer(plugin.getConfig().getJail().getServer());
            jailLocation.setWorld(plugin.getConfig().getJail().getWorld());
            jailLocation.fromSimpleLocation(SimpleLocation.fromString(plugin.getConfig().getJail().getJailLocation()));
            tp(plugin, jailed, jailLocation);
            return true;
        }
        return false;
    }

    public boolean releaseFromJail(Player player) {
        if (isInJail(player.getUniqueId()).isPresent()) {
            jailDao.delete(player.getUniqueId());
            chat.sendPublicServerToPlayersMessage(TextComponent.builder()
                    .append(ChatFormat.g("le joueur "))
                    .append(ChatFormat.h(player.getUsername()))
                    .append(ChatFormat.g(" est maintenant libre."))
                    .build());

            // try to tp out of jail
            FullLocation jailLocation = new FullLocation();
            jailLocation.setServer(plugin.getConfig().getJail().getServer());
            jailLocation.setWorld(plugin.getConfig().getJail().getWorld());
            jailLocation.fromSimpleLocation(SimpleLocation.fromString(plugin.getConfig().getJail().getFreeLocation()));
            tp(plugin, player, jailLocation);
            return true;
        }
        return false;
    }
}
