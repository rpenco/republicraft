package fr.republicraft.velocity.api.channels;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import fr.republicraft.common.api.channel.events.ChannelEventName;
import fr.republicraft.common.api.helper.Retry;
import org.slf4j.Logger;

public class ChannelMessage {

    MinecraftChannelIdentifier channelIdentifier;
    ChannelEventName eventName;
    String[] messages;
    Player player;
    Logger logger;

    public ChannelMessage setChannelIdentifier(MinecraftChannelIdentifier channelIdentifier) {
        this.channelIdentifier = channelIdentifier;
        return this;
    }

    public ChannelMessage logger(Logger logger) {
        this.logger = logger;
        return this;
    }

    public ChannelMessage setEventName(ChannelEventName eventName) {
        this.eventName = eventName;
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

    public void send() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(eventName.name());
        for (String message : messages) {
            out.writeUTF(message);
        }

        retrySend(out.toByteArray());
    }

    public void retrySend(byte[] bytes) {
        try {
            Retry.execute(5, 1000, (maxTries, wait, current) -> {
                if (player.getCurrentServer().isPresent()) {
                    ServerConnection serverConnection = player.getCurrentServer().get();
                    logger.info("send channel message to player=" + player.getUsername() +
                            " channelId=" + channelIdentifier.getId() +
                            " eventName=" + eventName.name() +
                            " try=" + current + "/" + maxTries);
                    serverConnection.sendPluginMessage(channelIdentifier, bytes);
                    return true;
                } else {
                    logger.warn("failed to send channel message to player=" + player.getUsername() +
                            " channelId=" + channelIdentifier.getId() +
                            " eventName=" + eventName.name() +
                            " try=" + current + "/" + maxTries);
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
