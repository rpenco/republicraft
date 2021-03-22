package fr.republicraft.common.api.channel.events;

import java.util.HashMap;
import java.util.Map;

public enum ChannelEventName implements CommandEvent {
    SERVER_COMMAND("LocalCommandEvent"),
    SERVER_TELEPORT("LocalTeleportEvent"),
    PROXY_COMMAND("ProxyTeleportEvent"),
    PROXY_TELEPORT("ProxyCommandEvent");

    //Lookup table
    private static final Map<String, ChannelEventName> lookup = new HashMap<>();

    //Populate the lookup table on loading time
    static {
        for (ChannelEventName env : ChannelEventName.values()) {
            lookup.put(env.toString(), env);
        }
    }

    private final String command;

    //****** Reverse Lookup Implementation************//

    ChannelEventName(String command) {
        this.command = command;
    }

    //This method can be used for reverse lookup purpose
    public static ChannelEventName get(String url) {
        return lookup.get(url);
    }

    @Override
    public String toString() {
        return name();
    }
}
