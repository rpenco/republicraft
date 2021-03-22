package fr.republicraft.velocity.api.channels;

import com.google.common.io.ByteArrayDataInput;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import fr.republicraft.common.api.channel.PluginChannelDispatcher;
import fr.republicraft.common.api.channel.events.PluginChannelCommand;
import fr.republicraft.common.api.channel.identifiers.ChannelGroupIdentifier;
import fr.republicraft.common.api.channel.identifiers.ChannelNameIdentifier;
import fr.republicraft.velocity.api.channels.events.ServerCommandEvent;
import fr.republicraft.velocity.api.channels.events.VelocityEvent;
import fr.republicraft.velocity.RepublicraftPlugin;

import java.util.ArrayList;
import java.util.List;

import static fr.republicraft.common.api.channel.identifiers.ChannelGroupIdentifier.CHANNEL_NAMESPACE;

public class VelocityPluginChannelDispatcher extends PluginChannelDispatcher {
    static final MinecraftChannelIdentifier CHANNEL_IDENTIFIER = MinecraftChannelIdentifier.create(CHANNEL_NAMESPACE, ChannelNameIdentifier.HUB);
    static final String CHANNEL_NAME = ChannelGroupIdentifier.CHANNEL_NAMESPACE + ":" + ChannelNameIdentifier.HUB;
    static final LegacyChannelIdentifier LEGACY_CHANNEL_IDENTIFIER = new LegacyChannelIdentifier(CHANNEL_NAME);


    public VelocityPluginChannelDispatcher() {
        super();
    }


    @Override
    protected void register() {
        ((RepublicraftPlugin) plugin).getProxy().getChannelRegistrar().register();
        ((RepublicraftPlugin) plugin).getProxy().getChannelRegistrar().register(CHANNEL_IDENTIFIER, LEGACY_CHANNEL_IDENTIFIER);
        ((RepublicraftPlugin) plugin).getProxy().getEventManager().register(plugin, this);
    }

    @Override
    protected void unregister() {
        ((RepublicraftPlugin) plugin).getProxy().getChannelRegistrar().unregister(CHANNEL_IDENTIFIER, LEGACY_CHANNEL_IDENTIFIER);
    }


    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getIdentifier().equals(CHANNEL_IDENTIFIER) && !event.getIdentifier().equals(LEGACY_CHANNEL_IDENTIFIER)) {
            return;
        }

        ByteArrayDataInput input = event.dataAsDataStream();
        String eventName = input.readUTF();

        List<String> messages = new ArrayList<>();
        while (true) {
            try {
                messages.add(input.readUTF());
            } catch (IllegalStateException e) {
                break;
            }
        }

        logger.info("receive channel event channelId=" + event.getIdentifier().getId() +
                " eventName=" + eventName +
                " source=" + event.getSource() +
                " message=" + String.join(",", messages));

        synchronized (forwardListeners) {
            forwardListeners.getOrDefault(eventName, new ArrayList<>())
                    .forEach(channelListener -> {
                        logger.info("forward event to listener={} event={}", channelListener.getClass().getName(), eventName);
                        ((VelocityChannelListener) channelListener)
                                .onChannelMessage(event.getSource(), event, eventName, messages);
                    });
        }
    }

    public void sendChannelEvent(VelocityEvent event) {
        ((RepublicraftPlugin) plugin).getProxy().getScheduler().buildTask(plugin, () -> new ChannelMessage()
                .logger(logger)
                .setChannelIdentifier(CHANNEL_IDENTIFIER)
                .setPlayer(event.getPlayer())
                .setEventName(event.getEventName())
                .setMessages(event.getMessages()).send()).schedule();
    }

    public void sendTitles(Player player, String title, String subtitle) {
        ServerCommandEvent event = new ServerCommandEvent();
        event.setPlayer(player);
        event.setCommand(PluginChannelCommand.TITLE, title, subtitle);
        sendChannelEvent(event);
    }
}
