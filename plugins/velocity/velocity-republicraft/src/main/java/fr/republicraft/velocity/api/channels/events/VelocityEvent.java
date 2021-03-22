package fr.republicraft.velocity.api.channels.events;

import com.velocitypowered.api.proxy.Player;
import fr.republicraft.common.api.channel.events.ChannelEvent;

public interface VelocityEvent extends ChannelEvent {
    Player getPlayer();

    void setPlayer(Player player);
}
