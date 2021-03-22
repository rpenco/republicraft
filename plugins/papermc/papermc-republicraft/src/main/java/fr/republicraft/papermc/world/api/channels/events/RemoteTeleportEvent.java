package fr.republicraft.papermc.world.api.channels.events;

import fr.republicraft.common.api.channel.events.ChannelEventName;
import fr.republicraft.common.api.helper.FullLocation;

public class RemoteTeleportEvent extends PaperChannelEvent {

    public void setFullLocation(FullLocation location) {
        this.messages = new String[]{
                location.getServer(),
                location.getWorld(),
                Float.toString(location.getX()),
                Float.toString(location.getY()),
                Float.toString(location.getZ()),
                Float.toString(location.getYaw())
        };
    }

    @Override
    public ChannelEventName getEventName() {
        return ChannelEventName.PROXY_TELEPORT;
    }
}
