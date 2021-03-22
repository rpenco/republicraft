package fr.republicraft.common.api.channel.events;

import java.util.HashMap;
import java.util.Map;

public enum HomeChannelCommand implements CommandEvent {
    SHOW("show"),
    DEL_HOME("delhome"),
    SET_HOME("sethome"),
    COMMAND("command"),
    TELEPORT("teleport");

    //Lookup table
    private static final Map<String, HomeChannelCommand> lookup = new HashMap<>();

    //Populate the lookup table on loading time
    static {
        for (HomeChannelCommand env : HomeChannelCommand.values()) {
            lookup.put(env.toString(), env);
        }
    }

    private final String command;

    //****** Reverse Lookup Implementation************//

    HomeChannelCommand(String command) {
        this.command = command;
    }

    //This method can be used for reverse lookup purpose
    public static HomeChannelCommand get(String url) {
        return lookup.get(url);
    }

    @Override
    public String toString() {
        return command;
    }
}
