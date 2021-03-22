package fr.republicraft.velocity.api.metrics;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import fr.republicraft.common.api.helper.JsonHelper;
import fr.republicraft.common.api.metrics.api.BasicReport;
import org.slf4j.event.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VelocityMetricReport extends BasicReport {

    @SerializedName("log.level.name")
    String logLevelName = Level.INFO.name();

    @SerializedName("log.level.code")
    int logLevelCode = Level.INFO.toInt();

    @SerializedName("event.name")
    String eventType;

    @SerializedName("server.version")
    String serverVersion;

    @SerializedName("server.name")
    String serverName = "velocity";

    @SerializedName("plugin.name")
    String pluginName;

    @SerializedName("plugin.version")
    String pluginVersion;

    transient Map<String, String> properties = new HashMap<>();

    @SerializedName("player.username")
    String playerUsername;

    @SerializedName("player.uuid")
    UUID playerUuid;

    @SerializedName("player.server.name")
    String playerServerName;

    @SerializedName("command.name")
    String commandName;

    @SerializedName("command.arguments")
    String[] commandArgs;

    @SerializedName("command.allowed")
    Boolean commandAllowed;

    public VelocityMetricReport setEventName(String eventType) {
        this.eventType = eventType;
        return this;
    }

    public VelocityMetricReport setProxy(ProxyServer proxy) {
        serverVersion = proxy.getVersion().getVersion();
        return this;
    }

    public VelocityMetricReport setPlugin(Plugin plugin) {
        pluginName = plugin.id();
        pluginVersion = plugin.version();
        return this;
    }

    public VelocityMetricReport setPluginName(String name) {
        pluginName = name;
        return this;
    }

    public VelocityMetricReport setLevel(Level level) {
        logLevelName = level.name();
        logLevelCode = level.toInt();
        return this;
    }

    public VelocityMetricReport setPluginVersion(String name) {
        pluginVersion = name;
        return this;
    }

    public VelocityMetricReport setProperty(String key, Double value) {
        properties.put(key, Double.toString(value));
        return this;
    }

    public VelocityMetricReport setProperty(String key, String value) {
        properties.put(key, value);
        return this;
    }

    public VelocityMetricReport setProperty(String key, Integer value) {
        properties.put(key, Integer.toString(value));
        return this;
    }

    public VelocityMetricReport setProperty(String key, Long value) {
        properties.put(key, Long.toString(value));
        return this;
    }

    public VelocityMetricReport setProperty(String key, UUID value) {
        properties.put(key, value.toString());
        return this;
    }

    public VelocityMetricReport setProperty(String key, Boolean value) {
        properties.put(key, Boolean.toString(value));
        return this;
    }

    /**
     * Obtains JSON String for this reporter
     */
    @Override
    public String toString() {
        Gson gson = new GsonBuilder().create();
        JsonElement jsonElement = gson.toJsonTree(this);
        properties.entrySet().parallelStream().forEach(p -> jsonElement.getAsJsonObject().addProperty(p.getKey(), p.getValue()));
        jsonElement.getAsJsonObject().addProperty("log.original", gson.toJson(jsonElement));
        return gson.toJson(jsonElement);
    }

    public VelocityMetricReport setPlayer(Player player) {
        playerUsername = player.getUsername();
        playerUuid = player.getUniqueId();
        player.getCurrentServer().ifPresent(serverConnection -> {
            playerServerName = serverConnection.getServer().getServerInfo().getName();
        });
        return this;
    }

    public VelocityMetricReport setServer(RegisteredServer server) {
        serverName = server.getServerInfo().getName();
        return this;
    }

    public VelocityMetricReport setCommand(String command) {
        return setCommand(command, new String[]{});
    }

    public VelocityMetricReport setCommand(String command, String[] args) {
        return setCommand(command, args, true);
    }

    public VelocityMetricReport setCommand(String command, String[] args, boolean allowed) {
        commandName = command;
        commandArgs = args;
        commandAllowed = allowed;
        return this;
    }
}
