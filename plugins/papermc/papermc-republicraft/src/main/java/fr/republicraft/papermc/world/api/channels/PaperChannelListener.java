package fr.republicraft.papermc.world.api.channels;

import fr.republicraft.common.api.channel.ChannelListener;
import org.bukkit.entity.Player;

import java.util.List;

public interface PaperChannelListener extends ChannelListener {
    void onChannelMessage(Player player, String channel, String eventName, List<String> messages);
}
