package fr.republicraft.papermc.world;


import fr.republicraft.common.api.jdbc.DBClient;
import fr.republicraft.common.api.managers.Managers;
import fr.republicraft.papermc.world.api.channels.PaperPluginChannelDispatcher;
import fr.republicraft.papermc.world.api.metrics.PaperMetricReporter;
import fr.republicraft.papermc.world.config.WorldConfig;
import fr.republicraft.papermc.world.managers.*;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import static fr.republicraft.common.api.helper.ConfigHelper.loadConfig;

public class RepublicraftPlugin extends JavaPlugin {

    @Getter
    private final Managers managers = new Managers(getSLF4JLogger());
    @Getter
    PaperMetricReporter reporter;
    @Getter
    private PaperPluginChannelDispatcher channel;
    @Getter
    private DBClient client;
    @Getter
    private WorldConfig worldConfig;
    @Getter
    private World world;

    @SneakyThrows
    @Override
    public void onEnable() {
        getLogger().info("enable plugin.");
        worldConfig = loadConfig(getDataFolder().toPath(), fr.republicraft.papermc.world.config.WorldConfig.class);
        if (worldConfig != null) {
            reporter = PaperMetricReporter.getReporter(this);
            reporter.create().setEventName("onEnable").send();

            client = new DBClient(worldConfig.getConnection(), getSLF4JLogger());
            world = getServer().getWorld(worldConfig.getWorld());
            channel = PaperPluginChannelDispatcher.of(PaperPluginChannelDispatcher.class, this, this.getSLF4JLogger());

            managers.add(new EconomyManager(this))
                    .add(new HomeManager(this))
                    .add(new TelePortalManager(this, worldConfig.getPortals()))
                    .add(new HoloManager(this, worldConfig.getHolograms()))
                    .add(new GriefPreventionManager(this))
                    .add(new LuckPermsManager(this))
                    .add(new PlayerManager(this))
                    .add(new NightManager(this))
                    .start();

            Bukkit.getServicesManager().register(RepublicraftPlugin.class, this, this, ServicePriority.Normal);
            getLogger().info("Successfully enabled.");
        } else {
            getLogger().severe("failed to load configuration. disable plugin.");
            this.getPluginLoader().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        getLogger().info("disabled plugin.");
        if (reporter != null) {
            reporter.create().setEventName("onDisable").send();
        }

        managers.stop();

        if (client != null) {
            client.close();
        }

        if (reporter != null) {
            reporter.close();
        }
    }

}
