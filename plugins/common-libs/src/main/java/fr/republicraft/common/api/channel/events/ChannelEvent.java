package fr.republicraft.common.api.channel.events;

public interface ChannelEvent {
    ChannelEventName getEventName();

    String[] getMessages();
}
