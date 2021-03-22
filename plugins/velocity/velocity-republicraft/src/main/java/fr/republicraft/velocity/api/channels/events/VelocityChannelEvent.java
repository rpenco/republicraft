package fr.republicraft.velocity.api.channels.events;

import com.velocitypowered.api.proxy.Player;

public abstract class VelocityChannelEvent implements VelocityEvent {
    public static final String NAME = "VelocityChannelEvent";
    protected Player player;
    protected String[] messages;

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public String[] getMessages() {
        return messages;
    }

}
