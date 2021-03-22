package fr.republicraft.velocity.api.channels.events;

import com.velocitypowered.api.proxy.Player;
import fr.republicraft.common.api.channel.events.ChannelEventName;
import fr.republicraft.common.api.helper.FullLocation;

public class ServerTeleportEvent extends VelocityChannelEvent {

    /**
     * Teleport to a specific location
     * @param location
     */
    public void setFullLocation(FullLocation location) {
        this.messages = new String[]{
                "location",
                location.getServer(),
                location.getWorld(),
                Float.toString(location.getX()),
                Float.toString(location.getY()),
                Float.toString(location.getZ()),
                Float.toString(location.getYaw())
        };
    }

    /**
     * Teleport to a remote player
     * @param player
     */
    public void setRemotePlayer(Player player) {
        this.messages = new String[]{
                "player",
                player.getUsername(),
                player.getUniqueId().toString()
        };
    }

    @Override
    public ChannelEventName getEventName() {
        return ChannelEventName.SERVER_TELEPORT;
    }

}
