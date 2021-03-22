package fr.republicraft.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import fr.republicraft.common.api.jdbc.DBClient;
import fr.republicraft.common.api.managers.Managers;
import fr.republicraft.velocity.api.channels.VelocityPluginChannelDispatcher;
import fr.republicraft.velocity.api.metrics.VelocityMetricReporter;
import fr.republicraft.velocity.config.PluginConfig;
import fr.republicraft.velocity.managers.*;
import lombok.Getter;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;

import static fr.republicraft.common.api.helper.ConfigHelper.loadConfig;


@Plugin(id = "republicraft", name = "Republicraft World Plugin", version = "1.0.0-SNAPSHOT",
        description = "Republicraft Velocity Plugin", authors = {"khips"})
public class RepublicraftPlugin {

    @Inject
    @Getter
    ProxyServer proxy;

    @Getter
    PluginConfig config;

    @Inject
    @Getter
    Logger logger;

    @Inject
    @Getter
    @DataDirectory
    Path configPath;

    @Getter
    VelocityPluginChannelDispatcher channel;

    @Getter
    DBClient client;

    @Getter
    Managers managers;

    @Getter
    VelocityMetricReporter reporter;

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) throws IOException, InstantiationException, IllegalAccessException {
        managers = new Managers(getLogger());
        config = loadConfig(configPath, PluginConfig.class);
        reporter = VelocityMetricReporter.getReporter(this);
        reporter.create().setEventName(event.toString()).send();
        client = new DBClient(config.getConnection(), getLogger());
        channel = VelocityPluginChannelDispatcher.of(VelocityPluginChannelDispatcher.class, this, this.getLogger());

        getManagers()
                .add(new DiscordManager(this))
                .add(new ChatManager(this))
                .add(new EconomyManager(this))
                .add(new HomeManager(this))
                .add(new JailManager(this))
                .add(new PlayerManager(this))
                .add(new PortalManager(this))
                .add(new ProxyManager(this))
                .add(new TablistManager(this))
                .add(new VotesManager(this))
                .start();

        getLogger().info("Successfully enabled.");
    }


    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        reporter.create().setEventName(event.toString()).send();

        // TODO
//        channel.close();

        if (managers != null) {
            managers.stop();
        }
        if (client != null) {
            client.close();
        }
        if (reporter != null) {
            reporter.close();
        }
    }

}
