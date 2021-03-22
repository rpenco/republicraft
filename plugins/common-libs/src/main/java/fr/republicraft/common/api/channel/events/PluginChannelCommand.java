package fr.republicraft.common.api.channel.events;

import java.util.HashMap;
import java.util.Map;

public enum PluginChannelCommand implements CommandEvent {
    TITLE("title");

    //Lookup table
    private static final Map<String, PluginChannelCommand> lookup = new HashMap<>();

    //Populate the lookup table on loading time
    static {
        for (PluginChannelCommand env : PluginChannelCommand.values()) {
            lookup.put(env.toString(), env);
        }
    }

    private final String command;

    //****** Reverse Lookup Implementation************//

    PluginChannelCommand(String command) {
        this.command = command;
    }

    //This method can be used for reverse lookup purpose
    public static PluginChannelCommand get(String url) {
        return lookup.get(url);
    }

    @Override
    public String toString() {
        return command;
    }
}
