package fr.republicraft.velocity.managers;

import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.messages.ChannelMessageSource;
import fr.republicraft.common.api.channel.events.ChannelEventName;
import fr.republicraft.common.api.channel.events.HomeChannelCommand;
import fr.republicraft.common.api.dao.home.Home;
import fr.republicraft.common.api.dao.home.HomeDao;
import fr.republicraft.common.api.managers.Manager;
import fr.republicraft.velocity.api.channels.VelocityChannelListener;
import fr.republicraft.velocity.api.channels.events.ServerCommandEvent;
import fr.republicraft.velocity.api.helpers.chat.ChatFormat;
import fr.republicraft.velocity.RepublicraftPlugin;
import fr.republicraft.velocity.commands.HomeCommand;
import net.kyori.text.TextComponent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static fr.republicraft.common.api.channel.ChannelFilterName.COMMAND;
import static fr.republicraft.velocity.api.helpers.tp.TeleportHelper.tp;

public class HomeManager extends Manager implements VelocityChannelListener {

    private static HomeDao homeDao;
    private final RepublicraftPlugin plugin;

    public HomeManager(RepublicraftPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void start() {
        homeDao = new HomeDao(plugin.getClient());
        homeDao.migrate();
        plugin.getProxy().getCommandManager().register(new HomeCommand(plugin), "home", "h");
        plugin.getProxy().getEventManager().register(plugin, this);
        plugin.getChannel().register(this, ChannelEventName.SERVER_COMMAND);
    }

    @Override
    public void stop() {
        plugin.getProxy().getCommandManager().unregister("home");
        plugin.getProxy().getEventManager().unregisterListener(plugin, this);
        plugin.getChannel().unregister(this, ChannelEventName.SERVER_COMMAND);
        homeDao.close();
    }

    @Override
    public void onChannelMessage(ChannelMessageSource source, PluginMessageEvent event, String eventName, List<String> messages) {
        String command = messages.get(0);
        switch (command) {
            case COMMAND:
                String playerUuid = messages.get(1);
                plugin.getProxy().getPlayer(playerUuid).ifPresent(player -> {
                    String pCommand = messages.get(2);
                    plugin.getLogger().info("player=" + player.getUsername() + " executing home channel command=" + pCommand);
                    plugin.getProxy().getCommandManager().execute(player, pCommand);
                });
                break;
            default:
                plugin.getLogger().error("receive an invalid channel message for home. command=" + command);

        }
    }

    public void showHome(Player player) {
        ServerCommandEvent event = new ServerCommandEvent();
        event.setPlayer(player);
        event.setCommand(HomeChannelCommand.SHOW);
        plugin.getChannel().sendChannelEvent(event);
    }

    public void delHome(Player player, String homeName) {
        ServerCommandEvent event = new ServerCommandEvent();
        event.setPlayer(player);
        event.setCommand(HomeChannelCommand.DEL_HOME, homeName);
        plugin.getChannel().sendChannelEvent(event);
    }

    public void addHome(Player player, String homeName) {
        player.getCurrentServer().ifPresent(serverConnection -> {
            ServerCommandEvent event = new ServerCommandEvent();
            event.setPlayer(player);
            event.setCommand(HomeChannelCommand.SET_HOME, homeName, serverConnection.getServer().getServerInfo().getName());
            plugin.getChannel().sendChannelEvent(event);
        });
    }

    public void teleport(Player player, String homeName) {
        Optional<Home> home = homeDao.get(player.getUniqueId(), homeName);
        ChatManager chat = ((ChatManager) plugin.getManagers().get(ChatManager.class));
        if (home.isPresent()) {
            tp(plugin, player, home.get().getFullLocation(), home.get().getName());
        } else {
            chat.sendPrivateServerToPlayerMessage(player, TextComponent.builder()
                    .append(ChatFormat.e("Le home "))
                    .append(ChatFormat.h(homeName))
                    .append(ChatFormat.e(" n'existe pas."))
                    .build());
        }
    }

    public List<Home> getAll(UUID uuid) {
        return homeDao.all(uuid);
    }
}
