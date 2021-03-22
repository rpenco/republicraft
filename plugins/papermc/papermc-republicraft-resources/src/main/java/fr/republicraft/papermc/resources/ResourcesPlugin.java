package fr.republicraft.papermc.resources;

import fr.republicraft.common.api.helper.ConfigHelper;
import fr.republicraft.papermc.resources.config.ResourcesConfig;
import fr.republicraft.papermc.resources.managers.AntiXRayManager;
import fr.republicraft.papermc.world.RepublicraftPlugin;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class ResourcesPlugin extends JavaPlugin {

    @Getter
    ResourcesConfig pluginConfig;

    @Getter
    RepublicraftPlugin republicraftPlugin;

    @Override
    public void onEnable() {
        getLogger().info("enable plugin.");
        pluginConfig = ConfigHelper.loadConfig(getDataFolder().toPath(), ResourcesConfig.class);


        RegisteredServiceProvider<RepublicraftPlugin> provider = Bukkit.getServicesManager().getRegistration(RepublicraftPlugin.class);
        if (provider == null) {
            getSLF4JLogger().info("failed to get Republicraft provider. Disable plugin.");
            this.setEnabled(false);
            return;
        } else {
            getSLF4JLogger().info("provider Republicraft plugin found.");
            republicraftPlugin = provider.getProvider();
            republicraftPlugin.getManagers()
                    .add(new AntiXRayManager(this, pluginConfig))
                    .start();
        }

        republicraftPlugin.getReporter().create(this)
                .setEventName("onEnable")
                .send();

        getLogger().info("Successfully enabled.");
    }


    @Override
    public void onDisable() {
        getLogger().info("onDisable plugin");
        republicraftPlugin.getReporter().create(this)
                .setEventName("onDisable")
                .send();
    }
}
