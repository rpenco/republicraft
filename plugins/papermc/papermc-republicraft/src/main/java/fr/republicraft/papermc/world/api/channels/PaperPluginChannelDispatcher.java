package fr.republicraft.papermc.world.api.channels;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import fr.republicraft.common.api.channel.PluginChannelDispatcher;
import fr.republicraft.common.api.channel.identifiers.ChannelGroupIdentifier;
import fr.republicraft.common.api.channel.identifiers.ChannelNameIdentifier;
import fr.republicraft.papermc.world.api.channels.events.PaperEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.ArrayList;
import java.util.List;

public class PaperPluginChannelDispatcher extends PluginChannelDispatcher implements PluginMessageListener {
    protected static final String CHANNEL_NAME = ChannelGroupIdentifier.CHANNEL_NAMESPACE + ":" + ChannelNameIdentifier.HUB;

    public PaperPluginChannelDispatcher() {
        super();
    }

    @Override
    protected void register() {
        Messenger messenger = Bukkit.getServer().getMessenger();
        messenger.registerOutgoingPluginChannel((Plugin) plugin, CHANNEL_NAME);
        messenger.registerIncomingPluginChannel((Plugin) plugin, CHANNEL_NAME, this);
    }

    @Override
    protected void unregister() {
        Messenger messenger = Bukkit.getServer().getMessenger();
        messenger.unregisterIncomingPluginChannel((Plugin) plugin, CHANNEL_NAME, this);
        messenger.unregisterOutgoingPluginChannel((Plugin) plugin);
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        Bukkit.getScheduler().runTaskAsynchronously((JavaPlugin) plugin, () -> {
            if (!channel.equalsIgnoreCase(CHANNEL_NAME)) return;
            ByteArrayDataInput input = ByteStreams.newDataInput(message);
            String eventName = input.readUTF();
            List<String> messages = new ArrayList<>();
            while (true) {
                try {
                    messages.add(input.readUTF());
                } catch (IllegalStateException e) {
                    break;
                }
            }
            logger.info("receive channel event channelId=" + channel +
                    " eventName=" + eventName +
                    " source=" + player.getName() +
                    " message=" + String.join(",", messages));

            synchronized (forwardListeners) {
                forwardListeners.getOrDefault(eventName, new ArrayList<>())
                        .forEach(channelListener -> ((PaperChannelListener) channelListener)
                                .onChannelMessage(player, channel, eventName, messages));
            }
        });
    }


    public void sendChannelEvent(PaperEvent event) {
        new ChannelMessage()
                .setLogger(logger)
                .setPlugin((JavaPlugin) plugin)
                .setChannelIdentifier(CHANNEL_NAME)
                .setPlayer(event.getPlayer())
                .setEventName(event.getEventName())
                .setMessages(event.getMessages()).send();
    }

}
