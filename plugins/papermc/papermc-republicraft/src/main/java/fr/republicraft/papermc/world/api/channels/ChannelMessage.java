package fr.republicraft.papermc.world.api.channels;


import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.republicraft.common.api.channel.events.ChannelEventName;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.slf4j.Logger;

public class ChannelMessage {

    String channelIdentifier;
    String[] messages;
    Player player;
    Plugin plugin;
    Logger logger;
    ChannelEventName eventName;

    public ChannelMessage setChannelIdentifier(String channelIdentifier) {
        this.channelIdentifier = channelIdentifier;
        return this;
    }

    public ChannelMessage setMessages(String[] messages) {
        this.messages = messages;
        return this;
    }

    public ChannelMessage setPlayer(Player player) {
        this.player = player;
        return this;
    }

    public ChannelMessage setLogger(Logger logger) {
        this.logger = logger;
        return this;
    }

    public ChannelMessage setPlugin(Plugin plugin) {
        this.plugin = plugin;
        return this;
    }

    public ChannelMessage setEventName(ChannelEventName eventName) {
        this.eventName = eventName;
        return this;
    }

    public void send() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(eventName.name());
        for (String message : messages) {
            out.writeUTF(message);
        }
        //Send data on network channel
        logger.info("send channel message to player=" + player.getName() +
                " channelId=" + channelIdentifier +
                " eventName=" + eventName.name());
        player.sendPluginMessage(plugin, channelIdentifier, out.toByteArray());
    }

}
