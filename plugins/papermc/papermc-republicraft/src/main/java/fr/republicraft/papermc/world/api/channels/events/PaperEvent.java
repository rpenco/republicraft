package fr.republicraft.papermc.world.api.channels.events;

import fr.republicraft.common.api.channel.events.ChannelEvent;
import org.bukkit.entity.Player;

public interface PaperEvent extends ChannelEvent {
    Player getPlayer();

    void setPlayer(Player player);
}
