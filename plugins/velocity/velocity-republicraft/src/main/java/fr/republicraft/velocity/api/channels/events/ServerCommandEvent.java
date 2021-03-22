package fr.republicraft.velocity.api.channels.events;

import fr.republicraft.common.api.channel.events.ChannelEventName;
import fr.republicraft.common.api.channel.events.CommandEvent;

public class ServerCommandEvent extends VelocityChannelEvent {

    public void setCommand(CommandEvent command, String... args) {
        this.messages = new String[args.length + 1];
        messages[0] = command.toString();
        System.arraycopy(args, 0, messages, 1, args.length);
    }

    @Override
    public ChannelEventName getEventName() {
        return ChannelEventName.SERVER_COMMAND;
    }

}
