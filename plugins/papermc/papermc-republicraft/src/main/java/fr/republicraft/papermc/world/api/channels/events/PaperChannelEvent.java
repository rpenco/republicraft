package fr.republicraft.papermc.world.api.channels.events;


import org.bukkit.entity.Player;

public abstract class PaperChannelEvent implements PaperEvent {
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
