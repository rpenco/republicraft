package fr.republicraft.papermc.world.api.metrics;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import fr.republicraft.common.api.metrics.api.BasicReport;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.event.Level;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PaperMetricReport extends BasicReport {

    @SerializedName("log.level.name")
    String logLevelName = Level.INFO.name();

    @SerializedName("log.level.code")
    int logLevelCode = Level.INFO.toInt();

    @SerializedName("event.name")
    String eventType;

    @SerializedName("server.version")
    String serverVersion;

    @SerializedName("server.name")
    String serverName;

    @SerializedName("server.bukkit.version")
    String serverBukkitVersion;

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

    public PaperMetricReport setPlayerUsername(String username){
        this.playerUsername = username;
        return this;
    }

    public PaperMetricReport setPlayerUniqueId(UUID uuid){
        this.playerUuid = uuid;
        return this;
    }

    public PaperMetricReport setEventName(String eventType) {
        this.eventType = eventType;
        return this;
    }

    public PaperMetricReport setServer(Server server) {
        serverVersion = server.getVersion();
        serverName = server.getName();
        serverBukkitVersion = server.getBukkitVersion();
        return this;
    }

    public PaperMetricReport setPlugin(JavaPlugin plugin) {
        pluginName = plugin.getName();
        return this;
    }

    public PaperMetricReport setPluginName(String name) {
        pluginName = name;
        return this;
    }

    public PaperMetricReport setLevel(Level level) {
        logLevelName = level.name();
        logLevelCode = level.toInt();
        return this;
    }

    public PaperMetricReport setPluginVersion(String name) {
        pluginVersion = name;
        return this;
    }

    public PaperMetricReport setProperty(String key, Double value) {
        properties.put(key, Double.toString(value));
        return this;
    }

    public PaperMetricReport setProperty(String key, String value) {
        properties.put(key, value);
        return this;
    }

    public PaperMetricReport setProperty(String key, Integer value) {
        properties.put(key, Integer.toString(value));
        return this;
    }

    public PaperMetricReport setProperty(String key, Long value) {
        properties.put(key, Long.toString(value));
        return this;
    }

    public PaperMetricReport setProperty(String key, UUID value) {
        properties.put(key, value.toString());
        return this;
    }

    public PaperMetricReport setProperty(String key, Boolean value) {
        properties.put(key, Boolean.toString(value));
        return this;
    }

    public PaperMetricReport setProperty(String key, LocalDateTime value) {
        properties.put(key, value.format(DateTimeFormatter.ISO_DATE_TIME));
        return this;
    }

    public PaperMetricReport setProperty(String key, Location value) {
        properties.put(key + ".x", String.valueOf(value.getX()));
        properties.put(key + ".y", String.valueOf(value.getY()));
        properties.put(key + ".z", String.valueOf(value.getZ()));
        properties.put(key + ".yaw", String.valueOf(value.getYaw()));
        properties.put(key + ".pitch", String.valueOf(value.getPitch()));
        properties.put(key + ".chunk", String.valueOf(value.getChunk()));
        properties.put(key + ".world", String.valueOf(value.getWorld()));
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

    public PaperMetricReport setPlayer(Player player) {
        playerUsername = player.getName();
        playerUuid = player.getUniqueId();
        playerServerName = player.getServer().getName();
        return this;
    }

    public PaperMetricReport setCommand(String command) {
        return setCommand(command, new String[]{});
    }

    public PaperMetricReport setCommand(String command, String[] args) {
        return setCommand(command, args, true);
    }

    public PaperMetricReport setCommand(String command, String[] args, boolean allowed) {
        commandName = command;
        commandArgs = args;
        commandAllowed = allowed;
        return this;
    }
}
