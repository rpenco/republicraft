package fr.republicraft.velocity.api.channels;

import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.messages.ChannelMessageSource;
import fr.republicraft.common.api.channel.ChannelListener;

import java.util.List;

public interface VelocityChannelListener extends ChannelListener {
    void onChannelMessage(ChannelMessageSource source, PluginMessageEvent event, String eventName, List<String> messages);
}
